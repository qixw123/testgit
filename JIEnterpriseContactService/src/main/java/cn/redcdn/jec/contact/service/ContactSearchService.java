package cn.redcdn.jec.contact.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dao.ContactCommonDao;
import cn.redcdn.jec.common.dto.IdAndNameDto;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.contact.dao.ContactExtendDao;
import cn.redcdn.jec.contact.dto.DeviceSearchDto;
import cn.redcdn.jec.contact.dto.GroupSearchDto;

@Path("/contact/search")
public class ContactSearchService extends BaseService<List<GroupSearchDto>> {

    /**
     * 缓存
     */
    @Autowired
    private TokenApiService tokenApiService;    

    /**
     * ContactExtendDao
     */
    @Autowired
    private ContactExtendDao contactExtendDao;

    /**
     * ContactExtendDao
     */
    @Autowired
    private ContactCommonDao contactCommonDao;

    /**
     * process 处理POST请求返回结果，包括返回码和返回码描述
     *
     * @param params   业务参数
     * @param request  request对象
     * @param response response对象
     * @return 返回码及描述
     */
    @Override
    public ResponseDto<List<GroupSearchDto>> process(JSONObject params,
                                              HttpServletRequest request, HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "token", "content");
        tokenApiService.checkUserLoginToken(params.getString("token"));
        String nube = tokenApiService.getCreatorByToken(params.getString("token"));
        
        List<GroupSearchDto> contactRspList = new ArrayList<GroupSearchDto>();
        List<DeviceSearchDto> deviceList = null;
        List<GroupSearchDto> contactList = null;
        int count  = contactCommonDao.countContactsByNube(nube);
        if (count > 0) {
	        // 分组查询
	        contactList = contactExtendDao.listGroupByNubeAndContent(nube, params.getString("content"));
	        // 成员查询
	        deviceList = contactExtendDao.listDeviceByNubeAndContent(nube, params.getString("content"));
        } else {
        	// 分组查询
	        contactList = contactExtendDao.listDeafultGroupByNubeAndContent(nube, params.getString("content"));
	        // 成员查询
	        deviceList = contactExtendDao.listDeafulteByNubeAndContent(nube, params.getString("content"));
        }
        
        contactRspList.addAll(contactList);
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
    }
}
