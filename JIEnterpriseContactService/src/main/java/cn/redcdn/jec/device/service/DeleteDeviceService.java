package cn.redcdn.jec.device.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.redcdn.jec.common.util.BaseInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.device.dao.DeviceExdDao;

/**
 * 删除设备，只能删除自己导入/添加的设备
 *
 * @author zhang
 */
@Path("/device/delete")
public class DeleteDeviceService extends BaseService<String> {

    @Autowired
    TokenApiService tokenApiService;

    @Autowired
    DeviceExdDao deviceExdDao;

    @Autowired
    BaseInfoUtil baseInfoUtil;

    @Override
    public ResponseDto<String> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {
        CheckUtil.checkEmpty(params, "token", "ids");
        String token = params.getString("token");
        tokenApiService.checkSystemLoginToken(token);


        String importer = baseInfoUtil.getImporterByToken(token);
        JSONArray ids = params.getJSONArray("ids");

        if (ids.size() > 0) {
            deviceExdDao.deleteBatch(ids, importer);
        }

        return new ResponseDto<>();
    }
}
