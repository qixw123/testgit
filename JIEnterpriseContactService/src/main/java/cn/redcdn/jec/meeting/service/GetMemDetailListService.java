package cn.redcdn.jec.meeting.service;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.MeetingApiService;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.meeting.dto.GetUserInviterRecordDto;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wulei on 2018/8/13.
 */
@Path(value = "/meetingCount/memDetailList")
public class GetMemDetailListService extends BaseService<GetUserInviterRecordDto>{

    @Autowired
    private MeetingApiService meetingApiService;
    @Autowired
    private TokenApiService tokenApiService;

    @Override
    public ResponseDto<GetUserInviterRecordDto> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "token", "meetingId","nube","pageNo", "pageSize");

        tokenApiService.checkSystemLoginToken(params.getString("token"));
        JSONObject obj = meetingApiService.getMemDetailList(params.getString("meetingId"),params.getString("nube"),params.getInteger("pageSize"),params.getInteger("pageNo"));

        GetUserInviterRecordDto dto = JSONObject.parseObject(obj.getString("response"),GetUserInviterRecordDto.class);
        ResponseDto<GetUserInviterRecordDto> rspDto = new ResponseDto<>();
        rspDto.setData(dto);
        return rspDto;
    }
}
