package cn.redcdn.jec.org.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.OrganizeAccount;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.org.dao.OrganizeAccountExtDao;
import cn.redcdn.jec.org.dto.GetAdminListDto;

/**
 * Project: JIEnterpriseContactService
 * Package: cn.redcdn.jec.org.service
 * Author: mason
 * Time: 11:12
 * Date: 2018-08-27
 * Created with IntelliJ IDEA
 */
@Path("/org/orgadminlist")
public class GetAdminListService extends BaseService<List<GetAdminListDto>> {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    OrganizeAccountExtDao organizeAccountExtDao;

    @Autowired
    GetOrgListService getOrgListService;

    @Override
    public ResponseDto<List<GetAdminListDto>> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {

        CheckUtil.checkEmpty(params, "token");
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);

        String id = params.getString("id");
        if (id == null || Objects.equals(id, "")) return new ResponseDto<>();
        List<OrganizeAccount> organizeAccount = organizeAccountExtDao.selectCurrentOrgAdmin(id);

        List<GetAdminListDto> adminListDtos = new ArrayList<>();
        for (OrganizeAccount o : organizeAccount) {
            GetAdminListDto adminListDto = new GetAdminListDto();
            BeanUtils.copyProperties(o, adminListDto);
            adminListDto.setContact(getOrgListService.getGroupInfoById(o.getContactGroupId(), null, null));
            adminListDtos.add(adminListDto);
        }

        ResponseDto<List<GetAdminListDto>> rsp = new ResponseDto<>();
        rsp.setData(adminListDtos);
        return rsp;
    }


}
