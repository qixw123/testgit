package cn.redcdn.jec.contact.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.ContactApiService;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dao.ContactCommonDao;
import cn.redcdn.jec.common.dto.ContactStructureDto;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.Device;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.contact.dto.ContactAllStructureDto;
import cn.redcdn.jec.contact.dto.DeviceDetailDto;
import cn.redcdn.jec.device.dao.DeviceExtendDao;

@Path("/contact/listallstructure")
public class ContactListAllstructureService extends BaseService<List<ContactAllStructureDto>> {

    /**
     * 缓存
     */
    @Autowired
    private TokenApiService tokenApiService;    

    /**
     * ContantApiService
     */
    @Autowired
    private ContactApiService contactApiService;

    /**
     * DeviceExtendDao
     */
    @Autowired
    private DeviceExtendDao deviceExtendDao;

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
    public ResponseDto<List<ContactAllStructureDto>> process(JSONObject params,
                                              HttpServletRequest request, HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "token");
        tokenApiService.checkUserLoginToken(params.getString("token"));
        
        String nube = tokenApiService.getCreatorByToken(params.getString("token"));
        List<ContactStructureDto> csList = contactApiService.getContactStructure(nube);
        
        int count  = contactCommonDao.countContactsByNube(nube);
        List<Device> deviceList = null;
        if (count > 0) {
            deviceList = deviceExtendDao.queryAllByNube(nube);
        } else {
            deviceList = deviceExtendDao.queryDeaultByNube(nube);
        }
        ResponseDto<List<ContactAllStructureDto>> rspDto = new ResponseDto<List<ContactAllStructureDto>>();
        List<ContactAllStructureDto> allList = new ArrayList<ContactAllStructureDto>();
        ContactAllStructureDto csAllDto = null;
        if (csList != null) {
	        for (ContactStructureDto csDto : csList) {
	        	csAllDto = new ContactAllStructureDto();
	        	setControllUser(csAllDto, csDto, deviceList);
	        	allList.add(csAllDto);
	        }
        }
        
        rspDto.setData(allList);
        
        return rspDto;
    }
    
    private void setControllUser(ContactAllStructureDto cslAllDto, 
    		ContactStructureDto csDto, List<Device> deviceList) {
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
}
