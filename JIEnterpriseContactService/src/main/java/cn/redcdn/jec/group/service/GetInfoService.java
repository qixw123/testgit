package cn.redcdn.jec.group.service;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dao.GroupInfoDao;
import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.GroupInfo;
import cn.redcdn.jec.common.entity.SystemAdmin;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.BaseInfoUtil;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.common.util.MessageUtil;
import cn.redcdn.jec.group.dto.GroupInfoDto;
import cn.redcdn.jec.user.dao.AdminLoginDao;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * 获取分组信息
 *
 * @author zhang
 */
@Path("/group/info")
public class GetInfoService extends BaseService<GroupInfoDto> {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    GroupInfoDao groupDao;

    @Autowired
    AdminLoginDao adminLoginDao;

    @Autowired
    BaseInfoUtil baseInfoUtil;

    @Override
    public ResponseDto<GroupInfoDto> process(JSONObject params, HttpServletRequest request,
                                             HttpServletResponse response) {
        CheckUtil.checkEmpty(params, "token");
        // token校验
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);

        String importer = baseInfoUtil.getImporterByToken(token);
        String tokenType = baseInfoUtil.getTokenTypeByToekn(token);

        String id = params.getString("id");
        String name = "";
        if (StringUtils.isNotBlank(id)) {
            GroupInfo group = groupDao.queryByKey(id);
            if (group == null || !importer.equals(group.getImporter())) {
                MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("groupid.notexist");
                throw new ExternalServiceException(messageInfo);
            }
            name = group.getName();
        } else {

            if (Objects.equals(tokenType, Constants.ACCOUNT_ADMIN)) {
                SystemAdmin adminInfo = adminLoginDao.getSystemAdminInfoByNubeOrAccount(null, importer);
                if (adminInfo != null) {
                    name = adminInfo.getName();
                }
            } else if (Objects.equals(tokenType, Constants.ORG_ADMIN)) {

            }

        }

        ResponseDto<GroupInfoDto> rspDto = new ResponseDto<>();
        GroupInfoDto dto = new GroupInfoDto();
        dto.setName(name);
        rspDto.setData(dto);
        return rspDto;
    }

}
