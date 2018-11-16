package cn.redcdn.jec.user.service;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.LoginApiService;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.OrganizeAccount;
import cn.redcdn.jec.common.entity.SystemAdmin;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.*;
import cn.redcdn.jec.user.dao.AdminLoginDao;
import cn.redcdn.jec.user.dao.OrgAdminLoginDao;
import cn.redcdn.jec.user.dto.AdminLoginDto;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Path("/admin/login")
public class AdminLoginService extends BaseService<AdminLoginDto> {

    /**
     * 系统管理员
     */
    @Autowired
    private AdminLoginDao adminLoginDao;

    /**
     * 机构管理员
     */
    @Autowired
    private OrgAdminLoginDao orgAdminLoginDao;

    /**
     * 缓存
     */
    @Autowired
    private TokenApiService tokenApiService;

    @Autowired
    private LoginApiService loginApiService;


    /**
     * process 处理POST请求返回结果，包括返回码和返回码描述
     *
     * @param params   业务参数
     * @param request  request对象
     * @param response response对象
     * @return 返回码及描述
     */
    @Override
    public ResponseDto<AdminLoginDto> process(JSONObject params,
                                              HttpServletRequest request, HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "account", "password");
        // 账号
        String account = params.getString("account");
        // 密码
        String password = MD5Util.getMd5(params.getString("password"));

        AdminLoginDto adminLoginDto;

        if (account.contains("@")) {
            adminLoginDto = orgAdminLogin(account, password);
        } else {
            adminLoginDto = adminLogin(account, password);
        }

        ResponseDto<AdminLoginDto> rspDto = new ResponseDto<>();
        rspDto.setData(adminLoginDto);

        return rspDto;
    }

    private AdminLoginDto adminLogin(String account, String password) {
        MessageInfoDto messageInfo = loginApiService.getLoginErrorCount(Constants.ACCOUNT_TYPE_SYSADMIN, account);
        if (messageInfo.getCode() < 0) {
            throw new ExternalServiceException(messageInfo);
        }
        SystemAdmin adminInfo = adminLoginDao.getSystemAdminInfo(account, password);
        if (adminInfo == null) {
            messageInfo = loginApiService.loginPassWordError(Constants.ACCOUNT_TYPE_SYSADMIN, account);
            throw new ExternalServiceException(messageInfo);
        }
        // 参数获取params
        AdminLoginDto adminLoginDto = new AdminLoginDto();

        String token = StringUtil.getUUIDString();
        // 放入缓存
        tokenApiService.setSystemToken(token);
        tokenApiService.setTokenCreator(token, adminInfo.getAccount());
        tokenApiService.setTokenCreator(token + "_id", adminInfo.getId());
        tokenApiService.setTokenCreator(token + "_type", Constants.ACCOUNT_ADMIN);
        loginApiService.loginSuccess(Constants.ACCOUNT_TYPE_SYSADMIN, account);
        adminLoginDto.setToken(token);
        adminLoginDto.setType(Constants.ACCOUNT_ADMIN);

        return adminLoginDto;

    }

    private AdminLoginDto orgAdminLogin(String account, String password) {
        MessageInfoDto messageInfo = loginApiService.getLoginErrorCount(Constants.ORG_TYPE_SYSADMIN, account);
        if (messageInfo.getCode() < 0) {
            throw new ExternalServiceException(messageInfo);
        }
        OrganizeAccount orgAdminInfo = orgAdminLoginDao.getOrgAdminInfo(account, password);
        if (orgAdminInfo == null) {
            messageInfo = loginApiService.loginPassWordError(Constants.ORG_TYPE_SYSADMIN, account);
            throw new ExternalServiceException(messageInfo);
        }
        // 参数获取params
        AdminLoginDto adminLoginDto = new AdminLoginDto();

        String token = StringUtil.getUUIDString();
        // 放入缓存
        tokenApiService.setSystemToken(token);
        tokenApiService.setTokenCreator(token, orgAdminInfo.getAccount());
        tokenApiService.setTokenCreator("account_" + orgAdminInfo.getAccount(), token);
        tokenApiService.setTokenCreator("id_" + orgAdminInfo.getId(), token);
        tokenApiService.setTokenCreator(token + "_id", orgAdminInfo.getId());
        tokenApiService.setTokenCreator(token + "_type", Constants.ORG_ADMIN);
        tokenApiService.setTokenCreator(token + "_group", orgAdminInfo.getContactGroupId());
        tokenApiService.setTokenCreator(token + "_belong", orgAdminInfo.getImporter());
        loginApiService.loginSuccess(Constants.ACCOUNT_TYPE_SYSADMIN, account);
        adminLoginDto.setToken(token);
        adminLoginDto.setType(Constants.ORG_ADMIN);

        return adminLoginDto;
    }
}
