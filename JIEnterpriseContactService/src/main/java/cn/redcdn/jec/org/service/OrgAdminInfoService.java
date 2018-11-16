package cn.redcdn.jec.org.service;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dao.GroupInfoDao;
import cn.redcdn.jec.common.dao.OrganizeAccountDao;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.GroupInfo;
import cn.redcdn.jec.common.entity.OrganizeAccount;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.org.dao.OrganizeAccountExtDao;
import cn.redcdn.jec.org.dto.GroupDto;
import cn.redcdn.jec.org.dto.OrgAdminInfoDto;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * Project: JIEnterpriseContactService
 * Package: cn.redcdn.jec.org
 * Author: mason
 * Time: 10:03
 * Date: 2018-08-29
 * Created with IntelliJ IDEA
 */
@Path("/org/admininfo")
public class OrgAdminInfoService extends BaseService<OrgAdminInfoDto> {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    OrganizeAccountExtDao organizeAccountExtDao;

    @Autowired
    OrganizeAccountDao organizeAccountDao;

    @Autowired
    GroupInfoDao groupInfoDao;

    @Override
    public ResponseDto<OrgAdminInfoDto> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {

        CheckUtil.checkEmpty(params, "token");
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);

        String account = tokenApiService.getCreatorByToken(token);
        String type = tokenApiService.getCreatorByToken(token + "_type");

        OrgAdminInfoDto orgAdminInfoDto = new OrgAdminInfoDto();

        if (Objects.equals(type, Constants.ACCOUNT_ADMIN)) {
            orgAdminInfoDto = organizeAccountExtDao.selectAdminBaseInfoByAccount(account);
        } else if (Objects.equals(type, Constants.ORG_ADMIN)) {
            // 获取基本信息
            orgAdminInfoDto = organizeAccountExtDao.selectOrgAdminBaseInfoByAccount(account);

            // 获取群组信息
            getOrgAdminLevel(orgAdminInfoDto, account);
        }

        ResponseDto<OrgAdminInfoDto> rsp = new ResponseDto<>();
        rsp.setData(orgAdminInfoDto);

        return rsp;
    }

    private void getOrgAdminLevel(OrgAdminInfoDto orgAdminInfoDto, String account) {
        OrganizeAccount organizeAccount = organizeAccountExtDao.selectInfoByAccount(account);
        if (organizeAccount == null
                || organizeAccount.getContactGroupId() == null
                || Objects.equals(organizeAccount.getContactGroupId(), "")) return;

        String groupId = organizeAccount.getContactGroupId();

        GroupInfo current = groupInfoDao.queryByKey(groupId);
        if (current == null) return;

        if (current.getLevel() == 3) {

            GroupDto level3 = new GroupDto();
            level3.setId(current.getId());
            level3.setName(current.getName());
            level3.setLevel(String.valueOf(current.getLevel()));
            orgAdminInfoDto.setThird(level3);

            GroupInfo level2Info = groupInfoDao.queryByKey(current.getParentId());
            GroupDto level2 = setLevel(level2Info);
            orgAdminInfoDto.setSecond(level2);

            GroupInfo level1Info = groupInfoDao.queryByKey(level2Info.getParentId());
            GroupDto level1 = setLevel(level1Info);
            orgAdminInfoDto.setFirst(level1);

        } else if (current.getLevel() == 2) {
            GroupDto level2 = new GroupDto();
            level2.setId(current.getId());
            level2.setName(current.getName());
            level2.setLevel(String.valueOf(current.getLevel()));
            orgAdminInfoDto.setSecond(level2);

            GroupInfo level1Info = groupInfoDao.queryByKey(current.getParentId());
            GroupDto level1 = setLevel(level1Info);
            orgAdminInfoDto.setFirst(level1);

        } else if (current.getLevel() == 1) {
            GroupDto level1 = new GroupDto();
            level1.setId(current.getId());
            level1.setName(current.getName());
            level1.setLevel(String.valueOf(current.getLevel()));
            orgAdminInfoDto.setFirst(level1);
        }

    }

    private GroupDto setLevel(GroupInfo groupInfo) {
        if (groupInfo == null) return null;

        GroupDto groupDto = new GroupDto();
        groupDto.setName(groupInfo.getName());
        groupDto.setId(groupInfo.getId());
        groupDto.setLevel(String.valueOf(groupInfo.getLevel()));

        return groupDto;
    }
}
