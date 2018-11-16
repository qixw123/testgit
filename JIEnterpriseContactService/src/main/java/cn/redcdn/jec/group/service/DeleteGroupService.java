package cn.redcdn.jec.group.service;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.redcdn.jec.common.util.BaseInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.GroupApiService;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dao.GroupInfoDao;
import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.Device;
import cn.redcdn.jec.common.entity.GroupInfo;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.common.util.MessageUtil;
import cn.redcdn.jec.device.dao.DeviceExdDao;
import cn.redcdn.jec.group.dao.GroupExtendDao;

/**
 * 判断当前组下是否有数据，有数据，则提示无法删除；
 *
 * @author zhang
 */
@Path("/group/delete")
public class DeleteGroupService extends BaseService<String> {

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
    BaseInfoUtil baseInfoUtil;

    @Override
    public ResponseDto<String> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {
        CheckUtil.checkEmpty(params, "token", "id");
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);
        String importer = baseInfoUtil.getImporterByToken(token);

        String groupId = params.getString("id");

        GroupInfo group = groupDao.queryByKey(groupId);
        if (group == null || group.getLevel() == null || !importer.equals(group.getImporter())) {
            MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("groupid.notexist");
            throw new ExternalServiceException(messageInfo);
        }

        List<String> groupAndChildrenIds = new ArrayList<String>();
        groupAndChildrenIds.add(groupId);
        if (group.getLevel() == Constants.FIRST_LEVEL
                || group.getLevel() == Constants.SECOND_LEVEL) {
            // 查询 所有子级组，包含子级，子级的子级
            List<GroupInfo> children = groupExtendDao.queryAllPosterity(groupId);
            for (GroupInfo dto : children) {
                groupAndChildrenIds.add(dto.getId());
            }
        }

        // 查找该组及其子级组是否有设备
        List<Device> list = deviceExdDao.queryByGroupIds(groupAndChildrenIds);
        if (list.size() > 0) {
            MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("group.has.data");
            throw new ExternalServiceException(messageInfo);
        }
        // 将该组及其子组都删除
        groupExtendDao.deleteBatch(groupAndChildrenIds);

        return new ResponseDto<>();
    }

}
