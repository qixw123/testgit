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
 * 修改会议主持人视讯号
 * Created by qixw on 2018/9/27.
 */
@SuppressWarnings("rawtypes")
@Path(value = "/appointmentMeeting/editNube")
public class EditAppointmentMeetingNubeService extends BaseService {

    @Autowired
    private TokenApiService tokenApiService;

    @Autowired
    private MeetingApiService meetingApiService;

    @Override
    public ResponseDto process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "token", "meetingId","manageNube");

        tokenApiService.checkSystemLoginToken(params.getString("token"));

         meetingApiService.modifyMeetingManageNube(params.getInteger("meetingId"),params.getString("manageNube"));
   
        return new ResponseDto();
    }
}
