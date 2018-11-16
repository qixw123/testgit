package cn.redcdn.jec.meeting.service;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.MeetingApiService;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by qixw on 2018/9/27.
 */
@SuppressWarnings("rawtypes")
@Path(value = "/appointmentMeeting/edit")
public class EditAppointmentMeetingService extends BaseService {

    @Autowired
    private TokenApiService tokenApiService;

    @Autowired
    private MeetingApiService meetingApiService;

    @Override
    public ResponseDto process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "token", "meetingId");

        tokenApiService.checkSystemLoginToken(params.getString("token"));

         meetingApiService.editDetailMeeting(params.getInteger("meetingId"), params.getString("initNube"), 
        		 params.getString("topic"), params.getString("beginTime"), params.getString("endTime"), 
        		 params.getString("meetingPwd"));
   
        return new ResponseDto();
    }
}
