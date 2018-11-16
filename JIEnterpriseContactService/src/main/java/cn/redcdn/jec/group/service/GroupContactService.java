package cn.redcdn.jec.group.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
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
import cn.redcdn.jec.device.dao.DeviceExtendDao;
import cn.redcdn.jec.group.dto.ChildContactDto;
import cn.redcdn.jec.group.dto.ContactDto;
import cn.redcdn.jec.group.dto.ContactRspDto;

@Path("/group/contact")
public class GroupContactService extends BaseService<ContactRspDto> {

    /**
     * 缓存
     */
    @Autowired
    private TokenApiService tokenApiService;    

    /**
     * 设备DAO
     */
    @Autowired
    private DeviceExtendDao deviceExtendDao;

    /**
     * 分组DAO
     */
    @Autowired
    private GroupInfoDao groupInfoDao;

    /**
     * process 处理POST请求返回结果，包括返回码和返回码描述
     *
     * @param params   业务参数
     * @param request  request对象
     * @param response response对象
     * @return 返回码及描述
     */
    @Override
    public ResponseDto<ContactRspDto> process(JSONObject params,
                                              HttpServletRequest request, HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "token", "id");
        tokenApiService.checkUserLoginToken(params.getString("token"));
        
        String nube = tokenApiService.getCreatorByToken(params.getString("token"));
        String id = params.getString("id");

        // 直属用户查询
        List<ContactDto> contactList = deviceExtendDao.queryByNubeAndGroupId(nube, id);
        List<ChildContactDto> childContactList = null;
        GroupInfo info = groupInfoDao.queryByKey(id);
        if (info == null) {
        	MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("groupid.notexist");
        	throw new ExternalServiceException(messageInfo);
        } else if (info.getLevel() == Constants.GROUP_FIRST) {
	        childContactList = deviceExtendDao.getFirstSumByNubeAndGroupId(nube, id);
        } else if (info.getLevel() == Constants.GROUP_SECOND) {
	        childContactList = deviceExtendDao.getSecondSumByNubeAndGroupId(nube, id);
        }
        ResponseDto<ContactRspDto> rspDto = new ResponseDto<ContactRspDto>();
        ContactRspDto contactRspDto = new ContactRspDto();
        contactRspDto.setContacts(contactList);
        contactRspDto.setChildGroups(childContactList);
        rspDto.setData(contactRspDto);
        
        return rspDto;
    }
}
