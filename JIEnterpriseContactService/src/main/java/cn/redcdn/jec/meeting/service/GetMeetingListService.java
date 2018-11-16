package cn.redcdn.jec.meeting.service;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.MeetingApiService;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.meeting.dto.GetMeetingListDto;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by wulei on 2018/7/27.
 */
@Path(value = "/meetingCount/list")
public class GetMeetingListService extends BaseService<GetMeetingListDto>{

    @Autowired
    private TokenApiService tokenApiService;

    @Autowired
    private MeetingApiService meetingApiService;

    @Override
    public ResponseDto<GetMeetingListDto> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {

        // 必须check
        CheckUtil.checkEmpty(params, "token", "pageNo", "pageSize");

        tokenApiService.checkSystemLoginToken(params.getString("token"));
        List<String> nubes = JSONObject.parseArray(params.getString("nubes"),String.class);
        JSONObject obj = meetingApiService.getMeetingList(nubes,params.getString("meetingType"),params.getInteger("pageSize"),params.getInteger("pageNo")
                ,params.getString("searchType"),params.getString("content"),params.getString("state")
                ,params.getString("startTime"),params.getString("endTime"));

        GetMeetingListDto dto = JSONObject.parseObject(obj.getString("response"),GetMeetingListDto.class);
        ResponseDto<GetMeetingListDto> rspDto = new ResponseDto<>();
        rspDto.setData(dto);
        return rspDto;
    }
}
