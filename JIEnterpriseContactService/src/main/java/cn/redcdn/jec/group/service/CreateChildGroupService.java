package cn.redcdn.jec.group.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.redcdn.jec.common.util.BaseInfoUtil;
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
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.common.util.MessageUtil;
import cn.redcdn.jec.group.dao.GroupExtendDao;

/**
 * 创建子分组
 *
 * @author zhang
 */
@Path("/group/createchild")
public class CreateChildGroupService extends BaseService<String> {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    GroupInfoDao groupDao;

    @Autowired
    GroupExtendDao groupExtendDao;

    @Autowired
    GroupApiService groupApiService;

    @Autowired
    BaseInfoUtil baseInfoUtil;

    @Override
    public ResponseDto<String> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {
        CheckUtil.checkEmpty(params, "token", "childName");
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);

        String creator = tokenApiService.getCreatorByToken(token);
        String importer = baseInfoUtil.getImporterByToken(token);

        String id = params.getString("id");
        String childName = params.getString("childName");

        ResponseDto<String> rspDto = new ResponseDto<String>();
        int maxSort = 0;
        int level = 0;
        if (StringUtils.isNotBlank(id)) {
            GroupInfo group = groupDao.queryByKey(id);
            if (group != null && group.getLevel() != null && importer.equals(group.getImporter())) {
                if (group.getLevel() == Constants.FIRST_LEVEL
                        || group.getLevel() == Constants.SECOND_LEVEL) {
                    List<GroupInfo> children = groupExtendDao.queryChildren(id);
                    maxSort = 0;
                    level = group.getLevel();
                    for (GroupInfo dto : children) {
                        if (dto.getName().equals(childName)) {
                            MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("group.name.repeat");
                            throw new ExternalServiceException(messageInfo);
                        }
                        if (dto.getSort() > maxSort) {
                            maxSort = dto.getSort();
                        }
                    }
                } else {
                    MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("group.cannot.createchild");
                    throw new ExternalServiceException(messageInfo);
                }
            } else {
                MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("groupid.notexist");
                throw new ExternalServiceException(messageInfo);
            }
        } else {
            List<GroupInfo> children = groupExtendDao.queryFirstChildren(importer);
            maxSort = 0;
            for (GroupInfo dto : children) {
                if (dto.getName().equals(childName)) {
                    MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("group.name.repeat");
                    throw new ExternalServiceException(messageInfo);
                }
                if (dto.getSort() > maxSort) {
                    maxSort = dto.getSort();
                }
            }
        }

        GroupInfo child = new GroupInfo();
        child.setLevel(level + 1);
        child.setCreator(creator);
        child.setName(childName);
        child.setParentId(id);
        child.setSort(maxSort + 1);
        child.setImporter(importer);
        groupDao.insert(child);
        rspDto.setData(child.getId());

        return rspDto;
    }

}
