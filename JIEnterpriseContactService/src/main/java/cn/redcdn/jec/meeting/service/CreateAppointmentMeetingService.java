package cn.redcdn.jec.meeting.service;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.MeetingApiService;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.meeting.dto.AppointmentMeetingIdDto;
import cn.redcdn.jec.meeting.dto.InvotedUserPhoneIdDto;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by qixw on 2018/9/27.
 */
@Path(value = "/appointmentMeeting/create")
public class CreateAppointmentMeetingService extends BaseService<AppointmentMeetingIdDto> {

    @Autowired
    private TokenApiService tokenApiService;

    @Autowired
    private MeetingApiService meetingApiService;

    @Override
    public ResponseDto<AppointmentMeetingIdDto> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "token", "meetingType", "initNube");

        tokenApiService.checkSystemLoginToken(params.getString("token"));
        
        List<InvotedUserPhoneIdDto> invotedUsers = JSONObject.parseArray(params.getString("nubes"),InvotedUserPhoneIdDto.class);
        JSONObject obj = meetingApiService.createWebMeeting(params.getInteger("meetingType"), 
        		params.getString("initNube"), params.getString("beginDateTime"), params.getString("endDateTime"), invotedUsers, 
        		params.getString("topic"), params.getString("app"), params.getString("meetingPwd"));

        AppointmentMeetingIdDto dto = JSONObject.parseObject(obj.getString("response"),AppointmentMeetingIdDto.class);
        ResponseDto<AppointmentMeetingIdDto> rspDto = new ResponseDto<>();
        rspDto.setData(dto);
        return rspDto;
    }
}
