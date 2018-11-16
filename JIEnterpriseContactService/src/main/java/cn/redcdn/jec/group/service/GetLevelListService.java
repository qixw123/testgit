package cn.redcdn.jec.group.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
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
import cn.redcdn.jec.group.dao.GroupExtendDao;

/**
 * 获取各级组列表
 *
 * @author zhang
 */
@Path("/group/levellist")
public class GetLevelListService extends BaseService<List<GroupInfo>> {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    GroupExtendDao groupExtendDao;

    @Autowired
    BaseInfoUtil baseInfoUtil;

    @Override
    public ResponseDto<List<GroupInfo>> process(JSONObject params, HttpServletRequest request,
                                                HttpServletResponse response) {
        CheckUtil.checkEmpty(params, "token", "level");
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);

        String importer = baseInfoUtil.getImporterByToken(token);

        Integer level = params.getInteger("level");
        String parentId = params.getString("parentId");
        if (level != Constants.FIRST_LEVEL && StringUtil.isBlank(parentId)) {
            MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("parent.empty");
            throw new ExternalServiceException(messageInfo);
        }
        List<GroupInfo> list = groupExtendDao.queryByLevelParent(level, parentId, importer);

        ResponseDto<List<GroupInfo>> rspDto = new ResponseDto<>();
        rspDto.setData(list);
        return rspDto;
    }

}
