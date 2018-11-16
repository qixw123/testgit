package cn.redcdn.jec.group.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.GroupApiService;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dao.GroupInfoDao;
import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.GroupInfo;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.BaseInfoUtil;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.common.util.MessageUtil;
import cn.redcdn.jec.common.util.StringUtil;
import cn.redcdn.jec.device.dao.DeviceExdDao;
import cn.redcdn.jec.group.dao.GroupExtendDao;
import cn.redcdn.jec.org.dao.OrganizeAccountExtDao;
import cn.redcdn.jec.user.dao.AdminLoginDao;

/**
 * 更新组信息
 * 更改的组名不能和同一父级组下同级组名相同
 *
 * @author zhang
 */
@Path("/group/update")
public class UpdateGroupService extends BaseService<String> {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    GroupInfoDao groupDao;

    @Autowired
    GroupExtendDao groupExtendDao;

    @Autowired
    GroupApiService groupApiService;

    @Autowired
    DeviceExdDao deviceExdDao;

    @Autowired
    OrganizeAccountExtDao organizeAccountExtDao;

    @Autowired
    AdminLoginDao adminLoginDao;

    @Autowired
    BaseInfoUtil baseInfoUtil;

    @Override
    public ResponseDto<String> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {
        CheckUtil.checkEmpty(params, "token", "name");
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);
        String importer = baseInfoUtil.getImporterByToken(token);

        String tokenType = baseInfoUtil.getTokenTypeByToekn(token);
        String id = params.getString("id");
        String name = params.getString("name");

        if (StringUtils.isNotBlank(id)) {
            GroupInfo group = groupDao.queryByKey(id);
            if (group != null && StringUtil.isNotBlank(group.getName())
                    && importer.equals(group.getImporter())) {
                if (!group.getName().equals(name)) {
                    List<String> nameList = groupExtendDao.querySiblingName(id, group.getParentId(), importer);
                    if (nameList.contains(name)) {
                        MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("group.name.repeat");
                        throw new ExternalServiceException(messageInfo);
                    }
                    group.setName(name);
                    groupDao.updateByKey(group);

                    // 修改设备表里的组名
                    List<String> groupAndChildrenIds = new ArrayList<String>();
                    groupAndChildrenIds.add(id);
                    if (group.getLevel() == Constants.FIRST_LEVEL
                            || group.getLevel() == Constants.SECOND_LEVEL) {
                        // 查询 所有子级组，包含子级，子级的子级
                        List<GroupInfo> children = groupExtendDao.queryAllPosterity(id);
                        for (GroupInfo dto : children) {
                            groupAndChildrenIds.add(dto.getId());
                        }
                    }
                    deviceExdDao.updateByGroupIdBatch(groupAndChildrenIds, group.getLevel(), group.getName());

                    if (Objects.equals(tokenType, Constants.ORG_ADMIN)) {
                        organizeAccountExtDao.updateByGroupIdBatch(groupAndChildrenIds, group.getLevel(), group.getName());
                    }
                }
            } else {
                MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("groupid.notexist");
                throw new ExternalServiceException(messageInfo);
            }
        } else {
            adminLoginDao.updateByAccount(importer, name);
        }

        return new ResponseDto<>();
    }

}
