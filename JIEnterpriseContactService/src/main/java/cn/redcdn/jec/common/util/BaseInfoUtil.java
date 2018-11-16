package cn.redcdn.jec.common.util;

import cn.redcdn.jec.common.api.TokenApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * Project: JIEnterpriseContactService
 * Package: cn.redcdn.jec.common.util
 * Author: mason
 * Time: 14:24
 * Date: 2018-09-04
 * Created with IntelliJ IDEA
 */
@Service
public class BaseInfoUtil {

    @Autowired
    TokenApiService tokenApiService;

    public String getImporterByToken(String token) {
        String tokenType = tokenApiService.getCreatorByToken(token + "_type");
        if (Objects.equals(tokenType, Constants.ACCOUNT_ADMIN)) {
            return tokenApiService.getCreatorByToken(token);
        } else if (Objects.equals(tokenType, Constants.ORG_ADMIN)) {
            return tokenApiService.getCreatorByToken(token + "_belong");
        }
        return "";
    }

    public String getTokenTypeByToekn(String token) {
        return tokenApiService.getCreatorByToken(token + "_type");
    }

}
