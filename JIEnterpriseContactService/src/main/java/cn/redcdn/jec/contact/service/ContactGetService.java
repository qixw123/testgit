package cn.redcdn.jec.contact.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.BaseInfoUtil;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.contact.dao.ContactExtendDao;
import cn.redcdn.jec.contact.dto.ContactAllStructureNumDto;
import cn.redcdn.jec.contact.dto.ContactGroupNumDto;

@Path("/contact/get")
public class ContactGetService extends BaseService<List<ContactAllStructureNumDto>> {

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

    @Autowired
    private BaseInfoUtil baseInfoUtil;

    /**
     * process 处理POST请求返回结果，包括返回码和返回码描述
     *
     * @param params   业务参数
     * @param request  request对象
     * @param response response对象
     * @return 返回码及描述
     */
    @Override
    public ResponseDto<List<ContactAllStructureNumDto>> process(JSONObject params,
                                                                HttpServletRequest request, HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "token", "id");
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);

        String account = baseInfoUtil.getImporterByToken(token);
        String id = params.getString("id");

        String tokenType = baseInfoUtil.getTokenTypeByToekn(token);


        int count = contactExtendDao.countContactsById(id);

        ContactAllStructureNumDto csDto = null;
        List<ContactAllStructureNumDto> rspList = new ArrayList<>();
        List<ContactGroupNumDto> contactList = null;

        if (Objects.equals(tokenType, Constants.ORG_ADMIN)) {
            String groupId = tokenApiService.getCreatorByToken(token + "_group");
            contactList = count > 0 ? contactExtendDao.listGroupWithOrg(groupId, id)
                    : contactExtendDao.listGroupDefaultWithOrg(groupId, id);
            if(contactList!=null){
                for (ContactGroupNumDto groupDto : contactList) {
                    if (Objects.equals(groupDto.getId(), groupId)) {
                        // 取分组
                        csDto = new ContactAllStructureNumDto();
                        csDto.setId(groupDto.getId());
                        csDto.setName(groupDto.getName());
                        csDto.setNum(groupDto.getNum());
                        csDto.setCheckFlg(groupDto.getCheckFlg());
                        csDto.setGroups(new ArrayList<>());
                        setChildGroups(csDto, contactList);
                        rspList.add(csDto);
                    }
                }
            }

        } else if (Objects.equals(tokenType, Constants.ACCOUNT_ADMIN)) {
            contactList = count > 0 ? contactExtendDao.listGroup(account, id)
                    : contactExtendDao.listGroupDefault(account, id);

            if (contactList != null) {
                for (ContactGroupNumDto groupDto : contactList) {
                    if (groupDto.getLevel() == Constants.GROUP_FIRST) {
                        // 取分组
                        csDto = new ContactAllStructureNumDto();
                        csDto.setId(groupDto.getId());
                        csDto.setName(groupDto.getName());
                        csDto.setNum(groupDto.getNum());
                        csDto.setCheckFlg(groupDto.getCheckFlg());
                        csDto.setGroups(new ArrayList<>());
                        setChildGroups(csDto, contactList);
                        rspList.add(csDto);
                    }
                }
            }
        }

        ResponseDto<List<ContactAllStructureNumDto>> rspDto = new ResponseDto<>();
        rspDto.setData(rspList);

        return rspDto;
    }

    private void setChildGroups(ContactAllStructureNumDto csDto, List<ContactGroupNumDto> contactList) {
        List<ContactAllStructureNumDto> groups = new ArrayList<>();
        csDto.setGroups(groups);
        if (contactList != null) {
            for (ContactGroupNumDto childDto : contactList) {
                if (csDto.getId().equals(childDto.getParentId())) {
                    ContactAllStructureNumDto childCsDto = new ContactAllStructureNumDto();
                    childCsDto.setId(childDto.getId());
                    childCsDto.setName(childDto.getName());
                    childCsDto.setNum(childDto.getNum());
                    childCsDto.setCheckFlg(childDto.getCheckFlg());
                    groups.add(childCsDto);
                    childCsDto.setGroups(new ArrayList<>());
                    if (childDto.getLevel() != Constants.GROUP_THIRD) {
                        setChildGroups(childCsDto, contactList);
                    }
                }
            }
        }

    }

}
