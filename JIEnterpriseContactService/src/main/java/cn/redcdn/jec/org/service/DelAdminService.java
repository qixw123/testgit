package cn.redcdn.jec.org.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.org.dao.OrganizeAccountExtDao;

/**
 * Project: JIEnterpriseContactService
 * Package: cn.redcdn.jec.org.service
 * Author: mason
 * Time: 10:23
 * Date: 2018-08-28
 * Created with IntelliJ IDEA
 */
@SuppressWarnings("rawtypes")
@Path("/org/deladmin")
public class DelAdminService extends BaseService {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    OrganizeAccountExtDao organizeAccountExtDao;

    @Override
    public ResponseDto process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {

        CheckUtil.checkEmpty(params, "token", "accounts");
        tokenApiService.checkSystemLoginToken(params.getString("token"));

        List<String> accountArray = JSONObject.parseArray(params.getString("accounts"), String.class);

        if (accountArray.size() > 0) {
            organizeAccountExtDao.deleteByAccountsLits(accountArray);

            for (String temp : accountArray) {

                String anoToken = tokenApiService.getCreatorByToken("account_" +temp);
                tokenApiService.setTokenInvalid(anoToken);

            }
        }


        return new ResponseDto();
    }
}
