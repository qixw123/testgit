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
import cn.redcdn.jec.common.dao.GroupInfoDao;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.GroupInfo;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.contact.dto.ContactAllStructureNumDto;
import cn.redcdn.jec.org.dao.OrganizeAccountExtDao;

/**
 * Project: JIEnterpriseContactService
 * Package: cn.redcdn.jec.org
 * Author: mason
 * Time: 15:10
 * Date: 2018-08-26
 * Created with IntelliJ IDEA
 */
@Path("/org/orglist")
public class GetOrgListService extends BaseService<List<ContactAllStructureNumDto>> {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    OrganizeAccountExtDao organizeAccountExtDao;

    @Autowired
    GroupInfoDao groupInfoDao;

    @Override
    public ResponseDto<List<ContactAllStructureNumDto>> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {

        CheckUtil.checkEmpty(params, "token");
        String token = params.getString("token");
        String id = params.getString("id");
        tokenApiService.checkSystemLoginToken(token);

        String tokenType = tokenApiService.getCreatorByToken(token + "_type");

        List<ContactAllStructureNumDto> result = new ArrayList<>();
        if (Objects.equals(tokenType, Constants.ORG_ADMIN)) {
            String importer = tokenApiService.getCreatorByToken(token + "_belong");
            result = getGroupInfoById(id, importer, null);

        } else if (Objects.equals(tokenType, Constants.ACCOUNT_ADMIN)) {
            String importer = tokenApiService.getCreatorByToken(token);
            result = getGroupInfoById(null, importer, "1");
        }

        ResponseDto<List<ContactAllStructureNumDto>> rsp = new ResponseDto<>();
        rsp.setData(result);

        return rsp;
    }

    public List<ContactAllStructureNumDto> getGroupInfoById(String id, String importer, String level) {

        List<ContactAllStructureNumDto> result = new ArrayList<>();

        if (id == null ) {
            List<GroupInfo> groupInfos = organizeAccountExtDao.getGroupInfo(null, importer, level);
            for (GroupInfo groupInfo : groupInfos) {
                ContactAllStructureNumDto temp = new ContactAllStructureNumDto();
                BeanUtils.copyProperties(groupInfo, temp);
                setChild(temp, importer);
                result.add(temp);
            }
        } else {

            GroupInfo root = groupInfoDao.queryByKey(id);

            if (root == null) return new ArrayList<>();

            ContactAllStructureNumDto contactAllStructureNumDto = new ContactAllStructureNumDto();
            BeanUtils.copyProperties(root, contactAllStructureNumDto);
            setChild(contactAllStructureNumDto, importer);
            result.add(contactAllStructureNumDto);
        }

        return result;
    }

    private void setChild(ContactAllStructureNumDto parent, String importer) {

        if (parent == null) return;

        List<GroupInfo> groupInfos = organizeAccountExtDao.getGroupInfo(parent.getId(), importer,null);

        if (groupInfos == null || groupInfos.size() == 0) {
            return;
        }

        List<ContactAllStructureNumDto> level = new ArrayList<>();
        for (GroupInfo g : groupInfos) {
            ContactAllStructureNumDto contactAllStructureNumDto = new ContactAllStructureNumDto();
            BeanUtils.copyProperties(g, contactAllStructureNumDto);
            level.add(contactAllStructureNumDto);
            setChild(contactAllStructureNumDto, importer);
        }
        parent.setGroups(level);
    }

}
