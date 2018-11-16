package cn.redcdn.jec.org.service;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dao.GroupInfoDao;
import cn.redcdn.jec.common.dao.OrganizeAccountDao;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.OrganizeAccount;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.org.dao.OrganizeAccountExtDao;
import cn.redcdn.jec.org.dto.AdminListDto;
import cn.redcdn.jec.org.dto.TypeDto;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Project: JIEnterpriseContactService
 * Package: cn.redcdn.jec.org.service
 * Author: mason
 * Time: 10:39
 * Date: 2018-08-28
 * Created with IntelliJ IDEA
 */
@Path("/org/adminlist")
public class AdminListService extends BaseService<List<AdminListDto>> {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    GroupInfoDao groupInfoDao;

    @Autowired
    OrganizeAccountExtDao organizeAccountExtDao;

    @Autowired
    OrganizeAccountDao organizeAccountDao;

    @Override
    public ResponseDto<List<AdminListDto>> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {

        CheckUtil.checkEmpty(params, "token");
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);

        String id = "";

        String type = "";
        if (params.containsKey("type")) {
            type = params.getString("type");
        }

        String content = "";
        if (params.containsKey("content")) {
            content = params.getString("content");
        }

        List<String> userId = new ArrayList<>();
        String tokenType = tokenApiService.getCreatorByToken(token + "_type");

        if (Objects.equals(tokenType, Constants.ACCOUNT_ADMIN)) {

            List<OrganizeAccount> organizeAccounts = organizeAccountDao.queryByField("importer", tokenApiService.getCreatorByToken(token));
            if (organizeAccounts == null || organizeAccounts.size() < 1) return new ResponseDto<>();
            for (OrganizeAccount organizeAccount : organizeAccounts) {
                userId.add(organizeAccount.getId());
            }

        } else if (Objects.equals(tokenType, Constants.ORG_ADMIN)) {
            id = tokenApiService.getCreatorByToken(token + "_id");
            userId.add(id);

        } else {
            return new ResponseDto<>();
        }

        List<AdminListDto> adminListDtos = new ArrayList<>();
        if (Objects.equals(type, "") && Objects.equals(content, "")) {

            adminListDtos = organizeAccountExtDao.selectAdminListByCurrentUserId(userId, null, null);

        } else if (!Objects.equals(type, "")) {

            if (Objects.equals(type, Constants.SEARCH_BY_ACCOUNT)) {

                adminListDtos = organizeAccountExtDao.selectAdminListByCurrentUserId(userId, content, null);

            } else if (Objects.equals(type, Constants.SEARCH_BY_ORG)) {
                adminListDtos = organizeAccountExtDao.selectAdminListByCurrentUserId(userId, null, content);
            }
        }

        if (adminListDtos == null || adminListDtos.size() < 1) return new ResponseDto<>();
        for (AdminListDto cell : adminListDtos) {
            TypeDto typeDto = new TypeDto();
            cell.setType(typeDto);
            cell.setOrg(joinGroup(cell.getOrg()));
        }

        int level = 0;
        for (AdminListDto cell : adminListDtos) {

            if (Objects.equals(tokenType, Constants.ORG_ADMIN) && Objects.equals(cell.getId(), id)) {
                cell.getType().setDel("2");
                cell.getType().setReset("2");
                level = cell.getLevel();
                break;
            }

        }

        for (AdminListDto cell : adminListDtos) {
            if (Objects.equals(tokenType, Constants.ORG_ADMIN) && cell.getLevel() == level && !Objects.equals(cell.getId(), id)) {
                cell.getType().setDel("2");
                cell.getType().setEditor("2");
                cell.getType().setReset("2");
            }
        }

        ResponseDto<List<AdminListDto>> rsp = new ResponseDto<>();
        rsp.setData(adminListDtos);

        return rsp;
    }

    private String joinGroup(String org) {
        String[] str = org.split("-");
        return String.join("-", str);
    }

}
