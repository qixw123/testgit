package cn.redcdn.jec.org.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.redcdn.jec.common.util.BaseInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dao.OrganizeAccountDao;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.OrganizeAccount;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.org.dao.OrganizeAccountExtDao;
import cn.redcdn.jec.org.dto.GroupLevelDto;

/**
 * Project: JIEnterpriseContactService
 * Package: cn.redcdn.jec.org.service
 * Author: mason
 * Time: 10:32
 * Date: 2018-09-03
 * Created with IntelliJ IDEA
 */
@SuppressWarnings("rawtypes")
@Path("/org/updateadmininfo")
public class UpdateAdminInfo extends BaseService {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    OrganizeAccountDao organizeAccountDao;

    @Autowired
    OrganizeAccountExtDao organizeAccountExtDao;

    @Autowired
    BaseInfoUtil baseInfoUtil;

    @Override
    public ResponseDto process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {

        CheckUtil.checkEmpty(params, "token", "id", "group");
        String token = params.getString("token");
        String id = params.getString("id");
        String group = params.getString("group");
        String tokenType = baseInfoUtil.getTokenTypeByToekn(token);

        tokenApiService.checkSystemLoginToken(token);

        OrganizeAccount organizeAccount = new OrganizeAccount();
        organizeAccount.setId(id);
        organizeAccount.setContactGroupId(group);

        GroupLevelDto groupLevelName = organizeAccountExtDao.getGroupLevelNameById(group);
        if (groupLevelName != null) {

            if (groupLevelName.getFirst() == null && groupLevelName.getSecond() == null) {
                organizeAccount.setFirstGroup(groupLevelName.getThird());
                organizeAccount.setSecondGroup("");
                organizeAccount.setThirdGroup("");
            } else if (groupLevelName.getFirst() == null) {
                organizeAccount.setFirstGroup(groupLevelName.getSecond());
                organizeAccount.setSecondGroup(groupLevelName.getThird());
                organizeAccount.setThirdGroup("");
            } else {
                organizeAccount.setFirstGroup(groupLevelName.getFirst());
                organizeAccount.setSecondGroup(groupLevelName.getSecond());
                organizeAccount.setThirdGroup(groupLevelName.getThird());
            }
        }

        organizeAccountDao.updateByKey(organizeAccount);

        String anoToken = tokenApiService.getCreatorByToken("id_" + id);
        tokenApiService.setTokenInvalid(anoToken);

        return new ResponseDto();
    }
}
