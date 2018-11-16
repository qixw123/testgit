package cn.redcdn.jec.common.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.redcdn.jec.common.dao.CacheDao;
import cn.redcdn.jec.common.entity.GroupInfo;
import cn.redcdn.jec.common.exception.ExternalException;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.common.util.StringUtil;
import cn.redcdn.jec.group.dao.GroupExtendDao;

@Service
public class GroupApiService extends BaseApiService {

	@Autowired
	CacheDao cacheDao;

	@Autowired
	GroupExtendDao groupExtendDao;

	// key:group_name_list 
	// value:所有组名的列表 一级组名直接是groupName 二级组名是parentName_groupName
	private static final String GROUP_LIST_KEY = "group_name_list";
	
	private static final int EXPIRE = 10 * 60;

	public void initGroupCache(String importer) {
		List<GroupInfo> dbGroupList = groupExtendDao.queryAll(importer);
		// 一级组名不能重复
		List<String> dbgroupNameList = new ArrayList<String>();
		
		for (GroupInfo dbGroup : dbGroupList) {
			// key:"name_"+id value:name
			cacheDao.set("name_" + dbGroup.getId(), dbGroup.getName(), EXPIRE);
			// key:importer_groupname_level_parent value:id 根据组名，父级组id，级别获取唯一的id
			cacheDao.set(importer + "_" + dbGroup.getName() + "_" + dbGroup.getLevel()
					+ "_" + dbGroup.getParentId(), dbGroup.getId(), EXPIRE);
			if (dbGroup.getLevel() == Constants.FIRST_LEVEL) {
				dbgroupNameList.add(dbGroup.getName());				
			}
			if (dbGroup.getLevel() == Constants.SECOND_LEVEL) {
				String parentName = cacheDao.get("name_" + dbGroup.getParentId());
				dbgroupNameList.add(parentName + Constants.STR_CONCAT_UNDERLINE + dbGroup.getName());
				cacheDao.set("parent_" + dbGroup.getId(), dbGroup.getParentId(), EXPIRE);
			}
			if (dbGroup.getLevel() == Constants.THIRD_LEVEL) {
				String grandParentId = cacheDao.get("parent_" + dbGroup.getParentId());
				String grandParentName = cacheDao.get("name_" + grandParentId);
				String parentName = cacheDao.get("name_" + dbGroup.getParentId());
				dbgroupNameList.add(grandParentName + "_" + parentName + "_" + dbGroup.getName());
				cacheDao.set("parent_" + dbGroup.getId(), dbGroup.getParentId(), EXPIRE);
			}
			// 设置某一父级下子集最大的sort
			if (cacheDao.isExist("maxsort_" + importer + "_" + dbGroup.getParentId())) {
				if (Integer.parseInt(cacheDao.get("maxsort_" + importer + "_" + dbGroup.getParentId())) < dbGroup.getSort()) {
					cacheDao.set("maxsort_" + importer + "_" + dbGroup.getParentId(), dbGroup.getSort(), EXPIRE);
				}
			} else {
				cacheDao.set("maxsort_" + importer + "_" + dbGroup.getParentId(), dbGroup.getSort(), EXPIRE);
			}

		}
		cacheDao.set(importer + "_" + GROUP_LIST_KEY , dbgroupNameList, EXPIRE);
	}

	@SuppressWarnings("unchecked")
	public void insertLevelGroup(List<String> groupNameList, int level, String importer) {
		List<GroupInfo> insGroupList = new ArrayList<GroupInfo>();
		
		List<String> dbGroupNameList = cacheDao.get(importer + "_" + GROUP_LIST_KEY , ArrayList.class);
		int sort = 0;
		String lastParentId = "";
		if (level == Constants.FIRST_LEVEL) {
			if (cacheDao.isExist("maxsort_" + importer + "_")) {
				sort = Integer.parseInt(cacheDao.get("maxsort_" + importer + "_"));
			} else {
				sort = 0;
			}
		}
		if (groupNameList.size() > 0) {
			for (int i = 0; i < groupNameList.size(); i++) {
				String name = groupNameList.get(i);
				if (dbGroupNameList.contains(name)) {
					// 去掉数据库已经存在的组名
					continue;
				}
				GroupInfo info = new GroupInfo();
				info.setImporter(importer);
				info.setCreator(importer);
				info.setLevel(level);
				String[] nameArr = name.split("_");
				info.setName(nameArr[nameArr.length - 1]);				
				switch (level) {
				case Constants.FIRST_LEVEL:
					info.setParentId("");
					break;
				case Constants.SECOND_LEVEL:
					info.setParentId(cacheDao.get(importer + "_" + nameArr[0] + Constants.STR_CONCAT_UNDERLINE
							+ Constants.FIRST_LEVEL + Constants.STR_CONCAT_UNDERLINE));										
					break;
				case Constants.THIRD_LEVEL:
					// 获取第一级组名和id
					String firstName = nameArr[0];
					String firstId = cacheDao.get(importer + "_" + firstName + Constants.STR_CONCAT_UNDERLINE + Constants.FIRST_LEVEL
							+ Constants.STR_CONCAT_UNDERLINE);
					// 获取第二级组名和id
					String secondName = nameArr[1];
					String secondId = cacheDao.get(importer + "_" + secondName + Constants.STR_CONCAT_UNDERLINE + Constants.SECOND_LEVEL
							+ Constants.STR_CONCAT_UNDERLINE + firstId);
					info.setParentId(secondId);
					break;
				default:
					break;
				}
				if (StringUtil.isNotBlank(info.getParentId())
						&& !info.getParentId().equals(lastParentId)) {
					if (cacheDao.isExist("maxsort_" + importer + "_" + info.getParentId())) {
						sort = Integer.parseInt(cacheDao.get("maxsort_" + importer + "_" + info.getParentId()));
					} else {
						sort = 0;
					}					
					lastParentId = info.getParentId();
				}
				info.setSort(++sort);
				insGroupList.add(info);
				dbGroupNameList.add(name);
			}
			if (insGroupList.size() > 0) {
				groupExtendDao.insertBatch(insGroupList);
			}
			for (GroupInfo info : insGroupList) {
				cacheDao.set(importer + "_" + info.getName() + Constants.STR_CONCAT_UNDERLINE + info.getLevel()
						+ Constants.STR_CONCAT_UNDERLINE + info.getParentId(), info.getId(), EXPIRE);
				if (cacheDao.isExist("maxsort_" + importer + "_" + info.getParentId())) {
					if (Integer.parseInt(cacheDao.get("maxsort_" + importer + "_" + info.getParentId())) < info.getSort()) {
						cacheDao.set("maxsort_" + importer + "_" + info.getParentId(), info.getSort(), EXPIRE);
					}
				} else {
					cacheDao.set("maxsort_" + importer + "_" + info.getParentId(), info.getSort(), EXPIRE);
				}
			}
			cacheDao.set(importer + "_" + GROUP_LIST_KEY , dbGroupNameList, EXPIRE);
		}
	}

	/**
	 * 根据组名、级别、父组获取groupId
	 * @param thirdGroupName 
	 * 
	 * @return
	 */
	public String getGroupIdByNameLevelParent(String importer, String groupName, int groupLevel, String parentId) {
		String groupId = cacheDao.get(importer + "_" + 
				groupName + "_" + groupLevel + "_" + parentId);
		if (StringUtil.isBlank(groupId)) {
			List<String> list = groupExtendDao.queryByNameLevelParent(groupName, groupLevel, parentId, importer);
			if (list.size() > 0) {
				groupId = list.get(0);
			} else {
				throw new ExternalException();
			}
		}
		return groupId;
	}
}
