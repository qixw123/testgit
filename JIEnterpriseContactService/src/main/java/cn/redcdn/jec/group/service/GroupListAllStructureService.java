package cn.redcdn.jec.group.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dao.DeviceDao;
import cn.redcdn.jec.common.dto.ContactStructureDto;
import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.Device;
import cn.redcdn.jec.common.entity.GroupInfo;
import cn.redcdn.jec.common.entity.SystemAdmin;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.common.util.MessageUtil;
import cn.redcdn.jec.contact.dto.ContactAllStructureDto;
import cn.redcdn.jec.contact.dto.DeviceDetailDto;
import cn.redcdn.jec.device.dao.DeviceExdDao;
import cn.redcdn.jec.group.dao.GroupExtendDao;
import cn.redcdn.jec.user.dao.AdminLoginDao;

/**
 * 获取所有组织含成员
 * 
 * @author zhang
 *
 */
@Path("/group/listallstructure")
public class GroupListAllStructureService extends BaseService<List<ContactAllStructureDto>> {

	@Autowired
	TokenApiService tokenApiService;

	@Autowired
	DeviceDao deviceDao;

	@Autowired
	GroupExtendDao groupExtendDao;

	@Autowired
	AdminLoginDao adminLoginDao;
	
	@Autowired
	DeviceExdDao deviceExdDao;

	@Override
	public ResponseDto<List<ContactAllStructureDto>> process(JSONObject params, HttpServletRequest request,
			HttpServletResponse response) {
		// 必须check
		CheckUtil.checkEmpty(params, "token");

		tokenApiService.checkUserLoginToken(params.getString("token"));
		// 获取中控nube，进而获取管理员账号
		String nube = tokenApiService.getCreatorByToken(params.getString("token"));
		List<Device> deviceList = deviceDao.queryByField("nube", nube);
		if (deviceList.size() > 0) {
			// 管理员
			String importer = deviceList.get(0).getImporter();
			// 根据管理员查询所有设备列表
			List<Device> allDeviceList = deviceExdDao.queryAllByImporter(importer);
			// 所有通讯录结构列表
			List<ContactStructureDto> csList = getAllContactList(importer);
			ResponseDto<List<ContactAllStructureDto>> rspDto = new ResponseDto<List<ContactAllStructureDto>>();
			List<ContactAllStructureDto> allList = new ArrayList<ContactAllStructureDto>();
			ContactAllStructureDto csAllDto = null;
			if (csList != null) {
				for (ContactStructureDto csDto : csList) {
					csAllDto = new ContactAllStructureDto();
					setControllUser(csAllDto, csDto, allDeviceList);
					allList.add(csAllDto);
				}
			}

			rspDto.setData(allList);

			return rspDto;

		} else {
			MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("device.not.exist");
			throw new ExternalServiceException(messageInfo);
		}
	}

	private List<ContactStructureDto> getAllContactList(String importer) {
		List<GroupInfo> groupList = groupExtendDao.listAllGroup(importer);
		List<ContactStructureDto> rspList = new ArrayList<ContactStructureDto>();
		if (groupList != null) {
			for (GroupInfo groupDto : groupList) {
				if (groupDto.getLevel() == Constants.GROUP_FIRST) {
					// 取分组
					ContactStructureDto csDto = new ContactStructureDto();
					csDto.setId(groupDto.getId());
					csDto.setName(groupDto.getName());
					csDto.setGroups(new ArrayList<ContactStructureDto>());
					setChildGroups(csDto, groupList);
					rspList.add(csDto);
				}
			}
		}

		List<ContactStructureDto> allList = new ArrayList<ContactStructureDto>();
		ContactStructureDto allDto = new ContactStructureDto();
		allDto.setGroups(rspList);
		SystemAdmin adminInfo = adminLoginDao.getSystemAdminInfoByNubeOrAccount(null, importer);
		if (adminInfo != null) {
			allDto.setName(adminInfo.getName());
		} else {
			allDto.setName("通讯录");
		}
		allList.add(allDto);

		return allList;
	}

	private void setControllUser(ContactAllStructureDto cslAllDto, ContactStructureDto csDto, List<Device> deviceList) {
		List<DeviceDetailDto> contacts = new ArrayList<DeviceDetailDto>();
		List<ContactAllStructureDto> groups = new ArrayList<ContactAllStructureDto>();

		cslAllDto.setId(csDto.getId());
		cslAllDto.setName(csDto.getName());
		cslAllDto.setContacts(contacts);
		cslAllDto.setGroups(groups);

		DeviceDetailDto detailDto = null;
		boolean isSame = false;
		if (deviceList != null) {
			for (Device device : deviceList) {
				if (device.getGroupId().equals(cslAllDto.getId())) {
					detailDto = new DeviceDetailDto();
					detailDto.setId(device.getId());
					detailDto.setName(device.getName());
					detailDto.setNube(device.getNube());
					contacts.add(detailDto);
					isSame = true;
				} else {
					if (isSame) {
						break;
					}
				}
			}
		}
		if (csDto.getGroups() != null && !csDto.getGroups().isEmpty()) {
			if (csDto.getGroups() != null) {
				ContactAllStructureDto groupDto = null;
				for (ContactStructureDto csDetailDto : csDto.getGroups()) {
					groupDto = new ContactAllStructureDto();
					BeanUtils.copyProperties(csDetailDto, groupDto);
					groups.add(groupDto);
					setControllUser(groupDto, csDetailDto, deviceList);
				}
			}
		}
	}

	private void setChildGroups(ContactStructureDto csDto, List<GroupInfo> contactList) {
		List<ContactStructureDto> groups = new ArrayList<ContactStructureDto>();
		csDto.setGroups(groups);
		if (contactList != null) {
			for (GroupInfo childDto : contactList) {
				if (csDto.getId().equals(childDto.getParentId())) {
					ContactStructureDto childCsDto = new ContactStructureDto();
					childCsDto.setId(childDto.getId());
					childCsDto.setName(childDto.getName());
					groups.add(childCsDto);
					childCsDto.setGroups(new ArrayList<ContactStructureDto>());
					if (childDto.getLevel() != Constants.GROUP_THIRD) {
						setChildGroups(childCsDto, contactList);
					}
				}
			}
		}

	}
}
