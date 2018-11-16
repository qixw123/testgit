package cn.redcdn.jec.group.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.redcdn.jec.common.util.BaseInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.GroupApiService;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dao.GroupInfoDao;
import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.GroupInfo;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.common.util.MessageUtil;
import cn.redcdn.jec.device.dao.DeviceExdDao;
import cn.redcdn.jec.group.dao.GroupExtendDao;

/**
 * 升级分组
 *
 * @author zhang
 */
@Path("/group/upgrade")
public class UpgradeGroupService extends BaseService<String> {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    GroupInfoDao groupDao;

    @Autowired
    GroupExtendDao groupExtendDao;

    @Autowired
    GroupApiService groupApiService;

    @Autowired
    DeviceExdDao deviceExdDao;

    @Autowired
    BaseInfoUtil baseInfoUtil;

    public static final int CATEGORY_TO_FIRST = 1;//第二级升第一级
    public static final int CATEGORY_TO_SECOND = 2;//第三级升第二级

    @Override
    public ResponseDto<String> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {
        CheckUtil.checkEmpty(params, "token", "id");
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);
        String importer = baseInfoUtil.getImporterByToken(token);

        String id = params.getString("id");

        GroupInfo group = groupDao.queryByKey(id);
        if (group != null && group.getLevel() != null && importer.equals(group.getImporter())) {
            if (group.getLevel() == Constants.FIRST_LEVEL) {
                MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("group.cannot.upgrade");
                throw new ExternalServiceException(messageInfo);
            }
            if (group.getLevel() == Constants.SECOND_LEVEL) {
                upgrade(group, Constants.FIRST_LEVEL, "", importer);

                List<GroupInfo> groupAndChildren = new ArrayList<GroupInfo>();
                groupAndChildren.add(group);
                List<String> groupAndChildrenIds = new ArrayList<String>();
                groupAndChildrenIds.add(id);
                List<GroupInfo> children = groupExtendDao.queryChildren(id);
                for (GroupInfo dto : children) {
                    GroupInfo child = new GroupInfo();
                    child.setId(dto.getId());
                    child.setName(dto.getName());
                    child.setLevel(Constants.SECOND_LEVEL);
                    groupAndChildren.add(child);
                    groupAndChildrenIds.add(dto.getId());
                }
                groupExtendDao.updateBatch(groupAndChildren);
                // 更新设备的组名
                deviceExdDao.updateUpgradeBatch(groupAndChildrenIds, CATEGORY_TO_FIRST);
            }
            if (group.getLevel() == Constants.THIRD_LEVEL) {
                upgrade(group, Constants.SECOND_LEVEL, groupExtendDao.getGrandParent(id).getId(), importer);
                groupDao.updateByKey(group);
                List<String> groupAndChildrenIds = new ArrayList<String>();
                groupAndChildrenIds.add(id);
                // 更新设备的组名
                deviceExdDao.updateUpgradeBatch(groupAndChildrenIds, CATEGORY_TO_SECOND);
            }
        } else {
            MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("groupid.notexist");
            throw new ExternalServiceException(messageInfo);
        }

        return new ResponseDto<>();
    }

    /**
     * @param group    需要升级的某组
     * @param level    需要升到的级别
     * @param parentId 升级后的父级id
     * @param importer
     */
    private void upgrade(GroupInfo group, int level, String parentId, String importer) {
        List<GroupInfo> groupList = groupExtendDao.queryByLevelParent(level, parentId, importer);
        int maxSort = 0;
        for (GroupInfo groupInfo : groupList) {
            if (groupInfo.getName().equals(group.getName())) {
                MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("group.name.repeat");
                throw new ExternalServiceException(messageInfo);
            }
            if (groupInfo.getSort() > maxSort) {
                maxSort = groupInfo.getSort();
            }
        }
        group.setSort(maxSort + 1);
        group.setLevel(level);
        group.setParentId(parentId);
    }

}
