package cn.redcdn.jec.device.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dao.DeviceDao;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.Device;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;

/**
 * Project: JIEnterpriseContactService
 * Package: cn.redcdn.jec.device.service
 * Author: mason
 * Time: 15:51
 * Date: 2018-08-24
 * Created with IntelliJ IDEA
 */
@SuppressWarnings("rawtypes")
@Path("/device/changegroup")
public class ChangeGroupService extends BaseService {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    DeviceDao deviceDao;

    @Override
    public ResponseDto process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {

        CheckUtil.checkEmpty(params, "token", "id", "group");
        tokenApiService.checkSystemLoginToken(params.getString("token"));

        String id = params.getString("id");
        String group = params.getString("group");

        Device device = deviceDao.queryByKey(id);
        device.setGroupId(group);

        String firstGroup = params.getString("firstGroup");
        String secondGroup = params.getString("secondGroup");
        String thirdGroup = params.getString("thirdGroup");

        device.setFirstGroup(firstGroup);
        device.setSecondGroup(secondGroup);
        device.setThirdGroup(thirdGroup);

        deviceDao.updateByKey(device);

        return new ResponseDto();
    }
}
