package cn.redcdn.jec.common.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.dao.CacheDao;
import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.util.MessageUtil;
import cn.redcdn.jec.common.util.PropertiesUtil;
import cn.redcdn.jec.common.util.StringUtil;

@Service
public class TokenApiService extends BaseApiService {

    private final Logger logger = LoggerFactory.getLogger(TokenApiService.class);

    private final String expire_time = PropertiesUtil.getProperty("token_expire_time");

    // 游客token的有效期，以月为单位
    private final String visitor_expire_time = PropertiesUtil.getProperty("visitor_token_expire_time");

    @Autowired
    UserCenterApiService userCenterApiService;

    @Autowired
    CacheDao cacheDao;

    /**
     * 设置用户token
     *
     * @param key
     * @param value
     * @return
     */
    public void setToken(String token, String id) {
        // 四十天有效
        cacheDao.set("app_id_" + token, id, 40 * 24 * 60 * 60);
        // 把以前的旧token清空
        String oldToken = cacheDao.get("token_uid_" + id);
        if (StringUtil.isNotBlank(oldToken)) {
            cacheDao.delete("exist_" + oldToken);
        }
        cacheDao.set("token_uid_" + id, token, 40 * 24 * 60 * 60);
        int time = Integer.parseInt(expire_time) * 60 * 60;
        cacheDao.set("exist_" + token, "valid", time);
    }

    /**
     * 设置管理员登录的token
     *
     * @param token
     * @param VchannelId
     */
    public void setSystemToken(String token) {
        // 四十天有效
        cacheDao.set("system_exist_" + token, "valid", 40 * 24 * 60 * 60);
    }

    /**
     * 设置token有效
     *
     * @param key
     * @param value
     * @return
     */
    public void setTokenValid(String token) {
        int time = Integer.parseInt(expire_time) * 60 * 60;
        cacheDao.set("exist_" + token, "valid", time);
    }

    /**
     * 设置token创建者
     *
     * @param key
     * @param value
     * @return
     */
    public void setTokenCreator(String token, String account) {
        cacheDao.set("creator_" + token, account, 40 * 24 * 60 * 60);
    }

    /**
     * 根据token获取创建者
     *
     * @param key
     * @param value
     * @return
     */
    public String getCreatorByToken(String token) {
        return cacheDao.get("creator_" + token);
    }

    /**
     * 根据token获取用户id
     *
     * @param token
     * @return
     */
    public String getIdByToken(String token) {
        return cacheDao.get("app_id_" + token);
    }

    /**
     * app 用户登录时使用
     *
     * @param token
     * @return
     */
    public void checkUserLoginToken(String token) {
        String existToken = cacheDao.get("exist_" + token);

        if ("valid".equals(existToken)) {
            return;
        } else if ("invalid".equals(existToken)) {
            MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("token.invalid");
            throw new ExternalServiceException(messageInfo);
        } else {
            // 2、查询用户中心
            JSONObject userInfoJson = userCenterApiService.getUserInfo(token);
            logger.info("校验用户token结果{}", new Object[]{userInfoJson});
            if ("0".equals(userInfoJson.getString("status"))) {
                String nube = userInfoJson.getJSONObject("userInfo").getString("nubeNumber");
                setTokenCreator(token, nube);
                setTokenValid(token);
                return;
            }
            MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("token.invalid");
            throw new ExternalServiceException(messageInfo);
        }
    }

    /**
     * 管理员token验证
     *
     * @param token
     */
    public void checkSystemLoginToken(String token) {
        String existToken = cacheDao.get("system_exist_" + token);
        if (!"valid".equals(existToken)) {
            MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("token.invalid");
            throw new ExternalServiceException(messageInfo);
        }
    }

    /**
     * 创建新的token
     *
     * @param pattern token模式，用以区分普通的token
     * @return 返回新的token
     */
    public String createToken(String pattern) {
        String token = StringUtil.getUUIDString();
        // 三十天有效
        int time = Integer.parseInt(visitor_expire_time) * 30 * 24 * 60 * 60;
        cacheDao.set("app_id_" + token, pattern, time);
        cacheDao.set("exist_" + token, "valid", time);

        return token;
    }

    public void setTokenInvalid(String token) {
        cacheDao.set("system_exist_" + token, "invalid");
    }
}
