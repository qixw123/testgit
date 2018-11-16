package cn.redcdn.jec.device.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.Device;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.MessageUtil;
import cn.redcdn.jec.device.dao.DeviceExtendDao;

@Path("/device/getcontrolnube")
public class GetControlNubeService extends BaseService<String> {

    /**
     * 设备DAO
     */
    @Autowired
    private DeviceExtendDao deviceExtendDao;

    /**
     * process 处理POST请求返回结果，包括返回码和返回码描述
     *
     * @param params   业务参数
     * @param request  request对象
     * @param response response对象
     * @return 返回码及描述
     */
    @Override
    public ResponseDto<String> process(JSONObject params,
                                              HttpServletRequest request, HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "nube");
        String nube = params.getString("nube");
        
        Device device = deviceExtendDao.queryByNube(nube);
        ResponseDto<String> rspDto = new ResponseDto<String>();
        if (device != null) {
        	rspDto.setData(device.getControlNube());
        } else {
        	MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("not.controller.user");
        	throw new ExternalServiceException(messageInfo);
        }
        
        return rspDto;
    }
}
