package cn.redcdn.jec.record.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.MeetingApiService;
import cn.redcdn.jec.common.api.TokenApiService;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.record.dto.SummaryDto;

@Path("/record/summary")
public class RecordSummaryService extends BaseService<SummaryDto> {

    /**
     * 缓存
     */
    @Autowired
    private TokenApiService tokenApiService;

    @Autowired
    private MeetingApiService meetingApiService;

    /**
     * process 处理POST请求返回结果，包括返回码和返回码描述
     *
     * @param params   业务参数
     * @param request  request对象
     * @param response response对象
     * @return 返回码及描述
     */
    @Override
    public ResponseDto<SummaryDto> process(JSONObject params,
                                              HttpServletRequest request, HttpServletResponse response) {
        // 必须check
        CheckUtil.checkEmpty(params, "token", "meetingId");
        
        tokenApiService.checkSystemLoginToken(params.getString("token"));

        ResponseDto<SummaryDto> rspDto = new ResponseDto<SummaryDto>();
        JSONObject returnObj  = meetingApiService.GetMeetingSummary(params.getString("meetingId"));
        JSONObject responseObj = returnObj.getJSONObject("response");
        SummaryDto summaryDto = JSONObject.toJavaObject(responseObj, SummaryDto.class);
        rspDto.setData(summaryDto);
        return rspDto;
    }
}
