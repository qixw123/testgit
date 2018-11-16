package cn.redcdn.jec.meeting.service;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.MeetingApiService;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.meeting.dto.GetAppointmentMeetingListDto;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by qixw on 2018/9/27.
 */
@Path(value = "/appointmentMeeting/list")
public class GetAppointmentMeetingListService extends BaseService<GetAppointmentMeetingListDto> {

    @Autowired
    private TokenApiService tokenApiService;

    @Autowired
    private MeetingApiService meetingApiService;

    @Override
    public ResponseDto<GetAppointmentMeetingListDto> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "token", "pageNo", "pageSize");

        tokenApiService.checkSystemLoginToken(params.getString("token"));

        List<String> nubes = JSONObject.parseArray(params.getString("nubes"),String.class);
        JSONObject obj = meetingApiService.getMeetingBriefList(nubes, params.getString("searchType"),
        		params.getString("content"), params.getInteger("pageNo"), params.getInteger("pageSize"));

        GetAppointmentMeetingListDto dto = JSONObject.parseObject(obj.getString("response"),GetAppointmentMeetingListDto.class);
        ResponseDto<GetAppointmentMeetingListDto> rspDto = new ResponseDto<>();
        rspDto.setData(dto);
        return rspDto;
    }
}
