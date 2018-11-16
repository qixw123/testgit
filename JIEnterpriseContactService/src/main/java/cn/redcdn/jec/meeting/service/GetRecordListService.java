package cn.redcdn.jec.meeting.service;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.MeetingApiService;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.meeting.dto.RecordListPageDto;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by wulei on 2018/7/27.
 */
@Path(value = "/meetingCount/recordList")
public class GetRecordListService extends BaseService<RecordListPageDto> {

    @Autowired
    private TokenApiService tokenApiService;

    @Autowired
    private MeetingApiService meetingApiService;

    @Override
    public ResponseDto<RecordListPageDto> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "token", "meetingId", "pageNo", "pageSize");

        tokenApiService.checkSystemLoginToken(params.getString("token"));
        JSONObject obj = meetingApiService.getRecordList(params.getString("meetingId"),params.getInteger("pageSize"),params.getInteger("pageNo")
                ,params.getString("startTime"),params.getString("endTime"));

        RecordListPageDto dto = JSONObject.parseObject(obj.getString("response"),RecordListPageDto.class);
        ResponseDto<RecordListPageDto> rspDto = new ResponseDto<>();
        rspDto.setData(dto);
        return rspDto;
    }
}
