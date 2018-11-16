package cn.redcdn.jec.user.service;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.DeviceApiService;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.api.UserCenterApiService;
import cn.redcdn.jec.common.dao.CacheDao;
import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.StringUtil;
import cn.redcdn.jec.user.dto.UserSearchDto;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 查询用户信息 根据视讯号
 *
 * @author zhang
 */
@Path("/user/search")
public class SearchUserService extends BaseService<UserSearchDto> {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    UserCenterApiService userCenterApiService;

    @Autowired
    CacheDao cacheDao;

    @Autowired
    DeviceApiService deviceApiService;

    @Override
    public ResponseDto<UserSearchDto> process(JSONObject params, HttpServletRequest request,
                                              HttpServletResponse response) {
        // 校验参数的必须性
        CheckUtil.checkEmpty(params, "nube", "token");
        // 校验token的有效性
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);

        String nube = params.getString("nube");
        if (StringUtil.isBlank(cacheDao.get("account_" + nube))) {
            // 去用户中心查询账号信息
            JSONObject resultObj = userCenterApiService.searchAccountInternal(nube);
            if (resultObj.getInteger("status") != 0) {
                MessageInfoDto messageInfo = new MessageInfoDto();
                messageInfo.setCode(resultObj.getInteger("status"));
                messageInfo.setMsg(resultObj.getString("message"));
                throw new ExternalServiceException(messageInfo);
            }
            JSONObject userInfo = resultObj.getJSONObject("userInfo");
            // 缓存用户信息
            deviceApiService.cacheInfo(userInfo);
        }

        UserSearchDto dto = new UserSearchDto();
        dto.setName(cacheDao.get("name_" + nube));
        dto.setNube(nube);
        dto.setAccount(cacheDao.get("account_" + nube));
        dto.setAccountType(Integer.parseInt(cacheDao.get("account_type_" + nube)));

        ResponseDto<UserSearchDto> rspDto = new ResponseDto<UserSearchDto>();
        rspDto.setData(dto);

        return rspDto;
    }

}
