/**
 * 南京青牛通讯技术有限公司
 * 日期：$Date: 2017年3月6日 下午2:26:00$
 * 作者：$Author: qixw $
 * 版本：$Revision: 2942 $
 * 版权：All rights reserved.
 */
package cn.redcdn.jec.common.api;

import cn.redcdn.jec.common.util.DateUtil;
import cn.redcdn.jec.common.util.PropertiesUtil;
import cn.redcdn.jec.common.util.StringUtil;
import cn.redcdn.jec.meeting.dto.InvotedUserPhoneIdDto;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuyu
 *
 */
@Service
public class MeetingApiService extends BaseApiService {
	
	private final String MEETING_URL = PropertiesUtil.getProperty("meeting_url");
	
	public JSONObject getMeetingRecords(String manageNube, String startTime, String endTime) {
		JSONObject paramsObj = new JSONObject();
		paramsObj.put("manageNube", manageNube);
		if (StringUtil.isBlank(startTime)) {
			startTime = "0";
		}
		paramsObj.put("startTime", startTime);
		
		if (StringUtil.isBlank(endTime)) {
			endTime = String.valueOf(DateUtil.getSystemTimeStamp());
		}
		paramsObj.put("endTime", endTime);
		JSONObject param = new JSONObject();
		param.put("service", "GetMeetingRecords");
		param.put("params", paramsObj.toJSONString());
		
		return postMultiFormWithRC(MEETING_URL, "/callService", param); 
	}

	public JSONObject getMeetingList(List<String> nubes, String meetingType, int pageSize,int pageNo,String searchType,
									 String content,String state,String startTime, String endTime) {
		JSONObject paramsObj = new JSONObject();
		paramsObj.put("pageSize", pageSize);
		paramsObj.put("pageNo", pageNo);
		if (!StringUtil.isBlank(meetingType)) {
			paramsObj.put("meetingType", meetingType);
		}
		if (!StringUtil.isBlank(startTime)) {
			paramsObj.put("startTime", startTime);
		}
		if (!StringUtil.isBlank(endTime)) {
			paramsObj.put("endTime", endTime);
		}
		if (!StringUtil.isBlank(searchType)){
			paramsObj.put("searchType", searchType);
		}
		if (!StringUtil.isBlank(content)){
			paramsObj.put("content", content);
		}
		if (!StringUtil.isBlank(state)) {
			paramsObj.put("state", state);
		}
		JSONArray array = new JSONArray();
		if(nubes != null){
			nubes.stream().forEach(e->array.add(e));
			paramsObj.put("nubes",array);
		}
		JSONObject param = new JSONObject();
		param.put("service", "GetMeetingList");
		param.put("params", paramsObj.toJSONString());

		return postMultiFormWithRC(MEETING_URL, "/callService", param);
	}

	public JSONObject getRecordList(String meetingId,int pageSize,int pageNo,String startTime, String endTime) {
		JSONObject paramsObj = new JSONObject();
		paramsObj.put("pageSize", pageSize);
		paramsObj.put("pageNo", pageNo);
		paramsObj.put("meetingId", meetingId);
		if (!StringUtil.isBlank(startTime)) {
			paramsObj.put("startTime", startTime);
		}
		if (!StringUtil.isBlank(endTime)) {
			paramsObj.put("endTime", endTime);
		}
		JSONObject param = new JSONObject();
		param.put("service", "GetRecordList");
		param.put("params", paramsObj.toJSONString());

		return postMultiFormWithRC(MEETING_URL, "/callService", param);
	}

	public JSONObject GetMeetingSummary(String meetingId) {
		JSONObject paramsObj = new JSONObject();
		paramsObj.put("meetingId", meetingId);
		JSONObject param = new JSONObject();
		param.put("service", "GetMeetingSummary");
		param.put("params", paramsObj.toJSONString());

		return postMultiFormWithRC(MEETING_URL, "/callService", param);
	}

	public  JSONObject getMemDetailList(String meetingId, String nube, int pageSize,int pageNo){
		JSONObject paramsObj = new JSONObject();
		paramsObj.put("pageSize", pageSize);
		paramsObj.put("pageNo", pageNo);
		paramsObj.put("meetingId", meetingId);
		paramsObj.put("nube", nube);
		JSONObject param = new JSONObject();
		param.put("service", "GetUserInviterRecord");
		param.put("params", paramsObj.toJSONString());

		return postMultiFormWithRC(MEETING_URL, "/callService", param);
	}

	public  JSONObject getMemList(String meetingId,String searchType,String content,int pageSize,int pageNo,String sortField,String sortDirect ){
		JSONObject paramsObj = new JSONObject();
		paramsObj.put("pageSize", pageSize);
		paramsObj.put("pageNo", pageNo);
		paramsObj.put("meetingId", meetingId);
		if (!StringUtil.isBlank(searchType)){
			paramsObj.put("searchType", searchType);
		}
		if (!StringUtil.isBlank(content)){
			paramsObj.put("content", content);
		}
		paramsObj.put("sortField", sortField);
		paramsObj.put("sortDirect", sortDirect);
		JSONObject param = new JSONObject();
		param.put("service", "GetUserMeetingInfo");
		param.put("params", paramsObj.toJSONString());

		return postMultiFormWithRC(MEETING_URL, "/callService", param);
	}
	public  JSONObject createWebMeeting(int meetingType,
			String initNube, String beginDateTime, String endDateTime,List<InvotedUserPhoneIdDto> invotedUsers,
			String topic,String app,String meetingPwd){
		JSONObject paramsObj = new JSONObject();
		paramsObj.put("meetingType", meetingType);
		paramsObj.put("initNube", initNube);
		paramsObj.put("beginDateTime", beginDateTime);
		paramsObj.put("endDateTime", endDateTime);
		paramsObj.put("invotedUsers", invotedUsers);
		paramsObj.put("topic", topic);
		paramsObj.put("app", app);
		paramsObj.put("meetingPwd", meetingPwd);
		JSONObject param = new JSONObject();
		param.put("service", "CreateWebMeeting");
		param.put("params", paramsObj.toJSONString());

		return postMultiFormWithRC(MEETING_URL, "/callService", param);
	}
	public  JSONObject getMeetingBriefList(List<String> nubes,
			String searchType, String content,int pageNo,int pageSize){
		JSONObject paramsObj = new JSONObject();
		paramsObj.put("nubes", nubes);
		paramsObj.put("searchType", searchType);
		paramsObj.put("content", content);
		paramsObj.put("pageNo", pageNo);
		paramsObj.put("pageSize",pageSize);
		JSONObject param = new JSONObject();
		param.put("service", "GetMeetingBriefList");
		param.put("params", paramsObj.toJSONString());

		return postMultiFormWithRC(MEETING_URL, "/callService", param);
	}
	public  JSONObject editDetailMeeting(int meetingId,
			String initNube, String topic, String beginTime,String endTime,
			String meetingPwd){
		JSONObject paramsObj = new JSONObject();
		paramsObj.put("meetingId", meetingId);
		paramsObj.put("initNube", initNube);
		paramsObj.put("topic", topic);
		paramsObj.put("beginTime", beginTime);
		paramsObj.put("endTime", endTime);
		paramsObj.put("meetingPwd", meetingPwd);
		JSONObject param = new JSONObject();
		param.put("service", "EditDetailMeeting");
		param.put("params", paramsObj.toJSONString());

		return postMultiFormWithRC(MEETING_URL, "/callService", param);
	}
	public  JSONObject modifyMeetingManageNube(int meetingId,
			String manageNube){
		JSONObject paramsObj = new JSONObject();
		paramsObj.put("meetingId", meetingId);
		paramsObj.put("manageNube", manageNube);
		JSONObject param = new JSONObject();
		param.put("service", "ModifyMeetingManageNube");
		param.put("params", paramsObj.toJSONString());

		return postMultiFormWithRC(MEETING_URL, "/callService", param);
	}
}
