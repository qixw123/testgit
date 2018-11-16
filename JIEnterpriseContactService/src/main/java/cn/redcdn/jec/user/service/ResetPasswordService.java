package cn.redcdn.jec.user.service;

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

/**
 * Project: JIEnterpriseContactService
 * Package: cn.redcdn.jec.user.service
 * Author: mason
 * Time: 11:27
 * Date: 2018-08-24
 * Created with IntelliJ IDEA
 */
@SuppressWarnings("rawtypes")
@Path("/password/reset")
public class ResetPasswordService extends BaseService {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    OrganizeAccountDao organizeAccountDao;


    @Override
    public ResponseDto process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {


        CheckUtil.checkEmpty(params, "token", "id");
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);

        String id = params.getString("id");
        OrganizeAccount organizeAccount = organizeAccountDao.queryByKey(id);

        if (organizeAccount == null) return new ResponseDto();

        organizeAccount.setPassword(MD5Util.getMd5(PropertiesUtil.getProperty("default.account.password")));
        organizeAccountDao.updateByKey(organizeAccount);


        ThreadExecutor.excute(() -> MailUtil.sendCreateOrgAccountSuccessMessage(
                organizeAccount.getAccount(), PropertiesUtil.getProperty("default.account.password"), PropertiesUtil.getProperty("smtp.reset.subject"),Constants.RESET_ACCOUNT));
        return new ResponseDto();
    }

}
