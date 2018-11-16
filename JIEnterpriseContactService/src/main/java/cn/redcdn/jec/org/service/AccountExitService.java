package cn.redcdn.jec.org.service;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.OrganizeAccount;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.org.dao.OrganizeAccountExtDao;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Project: JIEnterpriseContactService
 * Package: cn.redcdn.jec.org.service
 * Author: mason
 * Time: 10:09
 * Date: 2018-08-28
 * Created with IntelliJ IDEA
 */
@Path("/org/accountexit")
public class AccountExitService extends BaseService<String> {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    OrganizeAccountExtDao organizeAccountExtDao;

    @Override
    public ResponseDto<String> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {

        CheckUtil.checkEmpty(params, "token", "account");
        tokenApiService.checkSystemLoginToken(params.getString("token"));

        String account = params.getString("account");
        OrganizeAccount organizeAccount = organizeAccountExtDao.selectInfoByAccount(account);

        String result = "";

        if (organizeAccount == null) {
            result = "0";
        } else {
            result = "1";
        }

        ResponseDto<String> rsp = new ResponseDto<>();
        rsp.setData(result);
        return rsp;
    }
}
