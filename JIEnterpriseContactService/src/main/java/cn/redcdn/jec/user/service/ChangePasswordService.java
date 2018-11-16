package cn.redcdn.jec.user.service;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.OrganizeAccount;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.common.util.MD5Util;
import cn.redcdn.jec.common.util.MessageUtil;
import cn.redcdn.jec.user.dao.AdminLoginDao;
import cn.redcdn.jec.user.dao.OrgAdminLoginDao;

/**
 * Project: JIEnterpriseContactService
 * Package: cn.redcdn.jec.user.service
 * Author: mason
 * Time: 11:27
 * Date: 2018-08-24
 * Created with IntelliJ IDEA
 */
@SuppressWarnings("rawtypes")
@Path("/password/change")
public class ChangePasswordService extends BaseService {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    OrgAdminLoginDao orgAdminLoginDao;

    @Autowired
    AdminLoginDao adminLoginDao;

    @Override
    public ResponseDto process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {

        CheckUtil.checkEmpty(params, "token", "newpw");

        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);

        String newpw = params.getString("newpw");

        String oldpw = "";
        if (params.containsKey("oldpw")) {
            oldpw = params.getString("oldpw");
        }

        String account = tokenApiService.getCreatorByToken(token);
        String type = tokenApiService.getCreatorByToken(token + "_type");

        if (Objects.equals(type, Constants.ORG_ADMIN)) {

            OrganizeAccount organizeAccount = orgAdminLoginDao.getOrgAdminInfo(account, MD5Util.getMd5(oldpw));

            if (organizeAccount != null) {
                orgAdminLoginDao.updateOrgAdminPassword(account, MD5Util.getMd5(newpw));

            } else {
                throw new ExternalServiceException(MessageUtil.getMessageInfoByKey("old.password.error"));
            }

        } else if (Objects.equals(type, Constants.ACCOUNT_ADMIN)) {

            adminLoginDao.updatePasswordByAccount(account, MD5Util.getMd5(newpw));

        }

        return new ResponseDto();
    }
}
