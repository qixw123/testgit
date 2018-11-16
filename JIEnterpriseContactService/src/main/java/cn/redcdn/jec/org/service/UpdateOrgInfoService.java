package cn.redcdn.jec.org.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dao.GroupInfoDao;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.GroupInfo;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.org.dao.OrganizeAccountExtDao;

/**
 * Project: JIEnterpriseContactService
 * Package: cn.redcdn.jec.org
 * Author: mason
 * Time: 10:28
 * Date: 2018-08-27
 * Created with IntelliJ IDEA
 */
@SuppressWarnings("rawtypes")
@Path("/org/updateinfo")
public class UpdateOrgInfoService extends BaseService {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    OrganizeAccountExtDao organizeAccountExtDao;

    @Autowired
    GroupInfoDao groupInfoDao;

    @Override
    public ResponseDto process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {

        CheckUtil.checkEmpty(params, "token", "id", "name");
        String name = params.getString("name");
        String id = params.getString("id");
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);

        GroupInfo groupInfo = new GroupInfo();
        groupInfo.setId(id);
        groupInfo.setName(name);
        groupInfoDao.updateByKey(groupInfo);

        return new ResponseDto();
    }
}
