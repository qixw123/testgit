package cn.redcdn.jec.meeting.service;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.MeetingApiService;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.meeting.dto.GetMeetingUserListDto;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wulei on 2018/8/13.
 */
@Path(value = "/meetingCount/memList")
public class GetMemListService extends BaseService<GetMeetingUserListDto> {
    @Autowired
    private MeetingApiService meetingApiService;
    @Autowired
    private TokenApiService tokenApiService;
    @Override
    public ResponseDto<GetMeetingUserListDto> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "token", "meetingId","pageNo", "pageSize","sortField","sortDirect");

        tokenApiService.checkSystemLoginToken(params.getString("token"));
        JSONObject obj = meetingApiService.getMemList(params.getString("meetingId"),
                params.getString("searchType"),
                params.getString("content"),
                params.getInteger("pageSize"),
                params.getInteger("pageNo"),
                params.getString("sortField"),
                params.getString("sortDirect"));

        GetMeetingUserListDto dto = JSONObject.parseObject(obj.getString("response"),GetMeetingUserListDto.class);
        ResponseDto<GetMeetingUserListDto> rspDto = new ResponseDto<>();
        rspDto.setData(dto);
        return rspDto;
    }
}
