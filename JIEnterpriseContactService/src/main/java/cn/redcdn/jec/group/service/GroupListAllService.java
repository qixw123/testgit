package cn.redcdn.jec.group.service;

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
import cn.redcdn.jec.common.util.BaseInfoUtil;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.common.util.MessageUtil;
import cn.redcdn.jec.contact.dto.ContactAllStructureNumDto;
import cn.redcdn.jec.group.dao.GroupExtendDao;
import cn.redcdn.jec.org.service.GetOrgListService;
import cn.redcdn.jec.user.dao.AdminLoginDao;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Path("/group/listall")
public class GroupListAllService extends BaseService<List<ContactStructureDto>> {

    /**
     * 缓存
     */
    @Autowired
    private TokenApiService tokenApiService;

    /**
     * ContactExtendDao
     */
    @Autowired
    private GroupExtendDao groupExtendDao;

    /**
     * AdminLoginDao
     */
    @Autowired
    private AdminLoginDao adminLoginDao;

    @Autowired
    DeviceDao deviceDao;

    @Autowired
    GetOrgListService getOrgListService;

    @Autowired
    BaseInfoUtil baseInfoUtil;

    /**
     * process 处理POST请求返回结果，包括返回码和返回码描述
     *
     * @param params   业务参数
     * @param request  request对象
     * @param response response对象
     * @return 返回码及描述
     */
    @Override
    public ResponseDto<List<ContactStructureDto>> process(JSONObject params,
                                                          HttpServletRequest request, HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "token");

        String token = params.getString("token");
        String tokenType = baseInfoUtil.getTokenTypeByToekn(token);
        if (Objects.equals(tokenType, Constants.ORG_ADMIN)) {
            ResponseDto<List<ContactStructureDto>> rsp = new ResponseDto<>();
            rsp.setData(getOrgGroup(token));
            return rsp;
        }

        String account = "";
        try {
            tokenApiService.checkSystemLoginToken(params.getString("token"));
            account = tokenApiService.getCreatorByToken(params.getString("token"));
        } catch (ExternalServiceException e) {
            // 既支持管理员token，也支持终端token
            tokenApiService.checkUserLoginToken(params.getString("token"));
            // 获取中控nube，进而获取管理员账号
            String nube = tokenApiService.getCreatorByToken(params.getString("token"));
            List<Device> deviceList = deviceDao.queryByField("nube", nube);
            if (deviceList.size() > 0) {
                account = deviceList.get(0).getImporter();
            } else {
                MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("device.not.exist");
                throw new ExternalServiceException(messageInfo);
            }
        }


        ContactStructureDto csDto = null;
        List<GroupInfo> contactList = groupExtendDao.listAllGroup(account);
        List<ContactStructureDto> rspList = new ArrayList<ContactStructureDto>();
        ResponseDto<List<ContactStructureDto>> rspDto = new ResponseDto<List<ContactStructureDto>>();
        if (contactList != null) {
            for (GroupInfo groupDto : contactList) {
                if (groupDto.getLevel() == Constants.GROUP_FIRST) {
                    // 取分组
                    csDto = new ContactStructureDto();
                    csDto.setId(groupDto.getId());
                    csDto.setName(groupDto.getName());
                    csDto.setGroups(new ArrayList<ContactStructureDto>());
                    setChildGroups(csDto, contactList);
                    rspList.add(csDto);
                }
            }
        }

        List<ContactStructureDto> allList = new ArrayList<ContactStructureDto>();
        ContactStructureDto allDto = new ContactStructureDto();
        allDto.setGroups(rspList);
        SystemAdmin adminInfo = adminLoginDao.getSystemAdminInfoByNubeOrAccount(null, account);
        if (adminInfo != null) {
            allDto.setName(adminInfo.getName());
        } else {
            allDto.setName("通讯录");
        }
        allList.add(allDto);
        rspDto.setData(allList);

        return rspDto;
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

    private List<ContactStructureDto> getOrgGroup(String token) {

        String importer = baseInfoUtil.getImporterByToken(token);
        String id = tokenApiService.getCreatorByToken(token + "_group");
        List<ContactAllStructureNumDto> groupData = getOrgListService.getGroupInfoById(id, importer, null);

        List<ContactStructureDto> result = new ArrayList<>();
        for (ContactAllStructureNumDto temp : groupData) {
            ContactStructureDto cell = new ContactStructureDto();
            copyData(cell, temp);
            result.add(cell);
        }

        return result;
    }

    private void copyData(ContactStructureDto target, ContactAllStructureNumDto source) {
        if (source == null) return;

        target.setId(source.getId());
        target.setName(source.getName());
        if (source.getGroups() != null && source.getGroups().size() > 0) {
            List<ContactStructureDto> temp = new ArrayList<>();
            for (ContactAllStructureNumDto contactAllStructureNumDto : source.getGroups()) {
                ContactStructureDto cell = new ContactStructureDto();
                copyData(cell, contactAllStructureNumDto);
                temp.add(cell);
            }
            target.setGroups(temp);
        }
    }

}
