package cn.redcdn.jec.record.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.MeetingApiService;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.entity.Device;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.device.dao.DeviceExtendDao;
import cn.redcdn.jec.record.dto.RecordDto;

@Path("/record/list")
public class RecordListService extends BaseService<List<RecordDto>> {

    /**
     * 缓存
     */
    @Autowired
    private TokenApiService tokenApiService;

    @Autowired
    private MeetingApiService meetingApiService;

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
    public ResponseDto<List<RecordDto>> process(JSONObject params,
                                              HttpServletRequest request, HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "token", "manageNube");
        
        tokenApiService.checkSystemLoginToken(params.getString("token"));

        // 视讯号
        String manageNube = params.getString("manageNube");
        String startTime = params.getString("startTime");
        String endTime = params.getString("endTime");
        
        ResponseDto<List<RecordDto>> rspDto = new ResponseDto<List<RecordDto>>();
        JSONObject returnObj  = meetingApiService.getMeetingRecords(manageNube, startTime, endTime);
        String records = returnObj.getString("response");
        List<RecordDto> recordList = JSONArray.parseArray(records, RecordDto.class);
        Device device = deviceExtendDao.queryByNube(manageNube);
        if (recordList != null) {
	        for (RecordDto dto : recordList) {
	        	dto.setNube(manageNube);
	        	if (device != null) {
	        		dto.setRecoder(device.getName());
	        	}
	        }
        }
        rspDto.setData(recordList);
        
        return rspDto;
    }
}
