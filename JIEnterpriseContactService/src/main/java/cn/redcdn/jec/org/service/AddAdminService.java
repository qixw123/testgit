package cn.redcdn.jec.org.service;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.async.ThreadExecutor;
import cn.redcdn.jec.common.dao.OrganizeAccountDao;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.OrganizeAccount;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.common.util.MD5Util;
import cn.redcdn.jec.common.util.MailUtil;
import cn.redcdn.jec.common.util.PropertiesUtil;
import cn.redcdn.jec.org.dao.OrganizeAccountExtDao;
import cn.redcdn.jec.org.dto.GroupLevelDto;

/**
 * Project: JIEnterpriseContactService
 * Package: cn.redcdn.jec.org.service
 * Author: mason
 * Time: 16:52
 * Date: 2018-08-27
 * Created with IntelliJ IDEA
 */
@SuppressWarnings("rawtypes")
@Path("/org/addacount")
public class AddAdminService extends BaseService {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    OrganizeAccountDao organizeAccountDao;

    @Autowired
    OrganizeAccountExtDao organizeAccountExtDao;

    @Override
    public ResponseDto process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {

        CheckUtil.checkEmpty(params, "token", "account", "group");
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);

        String tokenType = tokenApiService.getCreatorByToken(token + "_type");

        String account = params.getString("account");
        String group = params.getString("group");
        GroupLevelDto groupLevelName = organizeAccountExtDao.getGroupLevelNameById(group);

        String importer = "";
        String creator = "";

        if (Objects.equals(tokenType, Constants.ORG_ADMIN)) {

            creator = tokenApiService.getCreatorByToken(token);
            importer = tokenApiService.getCreatorByToken(token + "_belong");


        } else if (Objects.equals(tokenType, Constants.ACCOUNT_ADMIN)) {
            importer = tokenApiService.getCreatorByToken(token);
            creator = importer;
        }

        OrganizeAccount organizeAccount = new OrganizeAccount();
        organizeAccount.setAccount(account);
        organizeAccount.setPassword(MD5Util.getMd5(
                PropertiesUtil.getProperty("default.account.password")));
        organizeAccount.setContactGroupId(group);
        organizeAccount.setCreator(creator);
        organizeAccount.setImporter(importer);

        if (groupLevelName != null) {
            if (groupLevelName.getFirst() == null && groupLevelName.getSecond() == null) {
                organizeAccount.setFirstGroup(groupLevelName.getThird());
            } else if (groupLevelName.getFirst() == null) {
                organizeAccount.setFirstGroup(groupLevelName.getSecond());
                organizeAccount.setSecondGroup(groupLevelName.getThird());
            } else {
                organizeAccount.setFirstGroup(groupLevelName.getFirst());
                organizeAccount.setSecondGroup(groupLevelName.getSecond());
                organizeAccount.setThirdGroup(groupLevelName.getThird());
            }
        }

        organizeAccountDao.insert(organizeAccount);

        // 异步处理
        ThreadExecutor.excute(() -> MailUtil.sendCreateOrgAccountSuccessMessage(
                account, PropertiesUtil.getProperty("default.account.password"), PropertiesUtil.getProperty("smtp.create.subject"), Constants.CREATE_ACCOUNT) );

        return new ResponseDto();
    }

}
