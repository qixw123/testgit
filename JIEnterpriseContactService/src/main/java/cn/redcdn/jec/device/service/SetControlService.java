package cn.redcdn.jec.device.service;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.api.UserCenterApiService;
import cn.redcdn.jec.common.dao.DeviceDao;
import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.Device;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.*;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Path("/device/setcontrol")
public class SetControlService extends BaseService<String> {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    DeviceDao deviceDao;

    @Autowired
    UserCenterApiService userCenterApiService;

    @Autowired
    BaseInfoUtil baseInfoUtil;

    private static final String APP_KEY = PropertiesUtil.getProperty("app_key");

    private static final String PASSWORD = PropertiesUtil.getProperty("password");

    @Override
    public ResponseDto<String> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {
        CheckUtil.checkEmpty(params, "token", "id", "controlFlg");
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);
        String importer = baseInfoUtil.getImporterByToken(token);
        // 中控开启标识
        Integer controlFlg = params.getInteger("controlFlg");

        Device device = deviceDao.queryByKey(params.getString("id"));
        if (device == null || device.getImporter() == null || !importer.equals(device.getImporter())) {
            MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("device.not.exist");
            throw new ExternalServiceException(messageInfo);
        }
        // 中控设备
        if (device.getAccountType() != null && device.getAccountType() == Constants.ACCOUNT_TYPE_TP) {
            if (device.getControlFlg() == null || device.getControlFlg() != controlFlg) {
                // 开启中控 且无中控视讯号 需要去用户中心开户
                if (device.getControlFlg() != null && device.getControlFlg() == Constants.DISABLED
                        && controlFlg == Constants.ENABLED && StringUtil.isBlank(device.getControlNube())) {
                    // 没有中控视讯号，去用户中心开户后设置
                    JSONObject controlUser = userCenterApiService.createPcAccount(APP_KEY, PASSWORD)
                            .getJSONObject("userInfo");
                    String controlNube = controlUser.getString("nubeNumber");
                    device.setControlNube(controlNube);
                }
                device.setControlFlg(controlFlg);
                deviceDao.updateByKey(device);
            }
        }

        return new ResponseDto<>();
    }

}
