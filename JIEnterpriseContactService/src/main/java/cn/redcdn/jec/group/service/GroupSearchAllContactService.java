package cn.redcdn.jec.group.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dao.DeviceDao;
import cn.redcdn.jec.common.dto.IdAndNameDto;
import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.Device;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.MessageUtil;
import cn.redcdn.jec.contact.dto.DeviceSearchDto;
import cn.redcdn.jec.contact.dto.GroupSearchDto;
import cn.redcdn.jec.group.dao.SearchAllContactDao;

/**
 * 搜索全部企业通讯录
 * 
 * @author zhang
 *
 */
@Path("/group/searchallcontact")
public class GroupSearchAllContactService extends BaseService<List<GroupSearchDto>> {

	@Autowired
	TokenApiService tokenApiService;

	@Autowired
	DeviceDao deviceDao;
	
	@Autowired
	SearchAllContactDao searchAllContactDao;

	@Override
	public ResponseDto<List<GroupSearchDto>> process(JSONObject params, HttpServletRequest request,
			HttpServletResponse response) {
		// 必须check
		CheckUtil.checkEmpty(params, "token");

		tokenApiService.checkUserLoginToken(params.getString("token"));
		// 获取中控nube，进而获取管理员账号
		String nube = tokenApiService.getCreatorByToken(params.getString("token"));
		List<Device> deviceByNubeList = deviceDao.queryByField("nube", nube);
		if (deviceByNubeList.size() > 0) {
			// 管理员
			String importer = deviceByNubeList.get(0).getImporter();
			
			List<GroupSearchDto> contactRspList = new ArrayList<GroupSearchDto>();
			String content = params.getString("content");
	        // 分组查询
	        List<GroupSearchDto> contactList = searchAllContactDao.listGroupByContent(content, importer);
	        contactRspList.addAll(contactList);
	        // 成员查询
	        List<DeviceSearchDto> deviceList = searchAllContactDao.listDeviceByContent(content, importer);
	        
	        if (deviceList != null) {
	        	String lastGroupId = "";
	        	GroupSearchDto groupSearchDto = null;
	        	List<IdAndNameDto> childDeviceList = null;
	        	IdAndNameDto childDto = null;
		        for (DeviceSearchDto dto : deviceList) {
		        	childDto = new IdAndNameDto();
	            	childDto.setId(dto.getCid());
	            	childDto.setName(dto.getCname());
	            	// 分组ID不同、分组成员需要归总
		            if (!lastGroupId.equals(dto.getGid())) {
		            	groupSearchDto = new GroupSearchDto();
		            	groupSearchDto.setGroupId(dto.getGid());
		            	groupSearchDto.setGroupName(dto.getGname());
		            	childDeviceList = new ArrayList<IdAndNameDto>();
		            	groupSearchDto.setContacts(childDeviceList);
		            	contactRspList.add(groupSearchDto);
		            } 
	            	// 添加成员
	            	childDeviceList.add(childDto);
	            	lastGroupId = dto.getGid();
		        }
	        }	        
	        ResponseDto<List<GroupSearchDto>> rspDto = new ResponseDto<List<GroupSearchDto>>();
	        rspDto.setData(contactRspList);
	        
	        return rspDto;
		} else {
			MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("device.not.exist");
			throw new ExternalServiceException(messageInfo);
		}		
	}
}
