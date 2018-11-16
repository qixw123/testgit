package cn.redcdn.jec.device.service;

import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dao.DeviceDao;
import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.Device;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.common.util.MessageUtil;

/**
 * 添加设备
 *
 * @author zhang
 */
@Path("/device/add")
public class AddDeviceService extends BaseService<String> {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    DeviceDao deviceDao;

    @Override
    public ResponseDto<String> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {
        CheckUtil.checkEmpty(params, "token", "nube", "accountType", "groupId", "firstGroup");
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);

        String tokenType = tokenApiService.getCreatorByToken(token + "_type");
        String creator = tokenApiService.getCreatorByToken(token);

        List<Device> deviceList = deviceDao.queryByField("nube", params.getString("nube"));
        if (deviceList.size() > 0) {
            MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("device.nube.exist");
            throw new ExternalServiceException(messageInfo);
        }

        Device insDevice = JSON.toJavaObject(params, Device.class);
        if (insDevice.getAccountType() == Constants.ACCOUNT_TYPE_TP) {
            insDevice.setControlFlg(Constants.DISABLED);
        }

        insDevice.setCreator(creator);
        if (Objects.equals(tokenType, Constants.ACCOUNT_ADMIN)) {
            insDevice.setImporter(creator);
        } else if (Objects.equals(tokenType, Constants.ORG_ADMIN)) {
            insDevice.setImporter(tokenApiService.getCreatorByToken(token + "_belong"));
        }
        deviceDao.insert(insDevice);

        return new ResponseDto<>();
    }

}
