package cn.redcdn.jec.common.api;

import java.util.List;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.util.PropertiesUtil;

/**
 * <dl>
 * <dt>UserApiService.java</dt>
 * <dd>Description: 医疗用户中心接口封装</dd>
 * <dd>Copyright: Copyright (C) 2017</dd>
 * <dd>Company: 北京红云融通技术有限公司</dd>
 * <dd>CreateDate: 2017-2-22</dd>
 * </dl>
 * 
 * @author Qbian
 */
@Service
public class UserCenterApiService extends BaseApiService {
	
	private final String USER_SAAS_URL = PropertiesUtil.getProperty("user_saas_url");
			
	public JSONObject checkUserToken(String token) {
		return getMultiWithNotException(USER_SAAS_URL, "/BaikuUserCenterV2/passportService?service=verifyToken&accessToken=" + token);
	}
	
	public void setAccountAttr(String token, String headUrl, String nickname) {
		JSONObject paramsObj = new JSONObject();
		paramsObj.put("headUrl", headUrl);
		paramsObj.put("nickname", nickname);
		JSONObject param = new JSONObject();
		param.put("service", "setAccountAttr");
		param.put("accessToken", token);
		param.put("params", paramsObj.toJSONString());
		
		postMultiForm(USER_SAAS_URL, "/BaikuUserCenterV2/passportService", param);
	}
	
	public JSONObject modifyAccount(String nube, String appKey) {
		JSONObject paramsObj = new JSONObject();
		paramsObj.put("nubeNumber", nube);
		paramsObj.put("appKey", appKey);
		JSONObject param = new JSONObject();
		param.put("service", "modifyAccount");
		param.put("params", paramsObj.toJSONString());
		return postMultiForm(USER_SAAS_URL, "/BaikuUserCenterV2/internalService", param); 
	}
	
	public JSONObject getUserInfo(String token) {
		return getMultiWithNotException(USER_SAAS_URL, "/BaikuUserCenterV2/auth?service=getUserInfo&accessToken=" + token);
	}
	
	public JSONObject searchAccountInternal(String account) {
		JSONObject paramsObj = new JSONObject();
		paramsObj.put("account", account);
		JSONObject param = new JSONObject();
		param.put("service", "searchAccountInternal");
		param.put("params", paramsObj.toJSONString());
		JSONObject resultObj = postMultiFormNotError(USER_SAAS_URL, "/BaikuUserCenterV2/internalService", param);
		return resultObj;
	}
	
	public JSONObject getNubeNumberInternal(JSONObject paramsObj) {
		JSONObject param = new JSONObject();
		param.put("service", "getNubeNumberInternal");
		param.put("params", paramsObj.toJSONString());
//		param.put("appId", "vchannel");
		if (paramsObj.containsKey("prettyNumberList")) {
			return postMultiForm(USER_SAAS_URL, "/BaikuUserCenterV2/internalService", param); 
		} else {
			JSONObject resultObj = postMultiFormNotError(USER_SAAS_URL, "/BaikuUserCenterV2/internalService", param); 
			if (resultObj.getInteger("status") != 0 && resultObj.getInteger("status") != -12)
			{
				MessageInfoDto messageInfo = new MessageInfoDto();
				messageInfo.setCode(resultObj.getInteger("status"));
				messageInfo.setMsg(resultObj.getString("message"));
				throw new ExternalServiceException(messageInfo);
			}
			return resultObj;
		}
	}
	
	public JSONObject createAccount(JSONObject paramsObj) {
		JSONObject param = new JSONObject();
		param.put("service", "createAccountInternal");
		param.put("params", paramsObj.toJSONString());
		JSONObject resultObj = postMultiForm(USER_SAAS_URL, "/BaikuUserCenterV2/internalService", param);
		if (resultObj.getInteger("status") != 0)
		{
			MessageInfoDto messageInfo = new MessageInfoDto();
			messageInfo.setCode(resultObj.getInteger("status"));
			messageInfo.setMsg(resultObj.getString("message"));
			throw new ExternalServiceException(messageInfo);
		}
		return resultObj;
	}
	
	public JSONObject createPcAccount(String appKey, String password) {
		
		JSONObject paramsObj = new JSONObject();
		paramsObj.put("appKey", appKey);
		paramsObj.put("serialNumber", "pcNumber");
		paramsObj.put("type", 3);
		paramsObj.put("userType", 6);
		paramsObj.put("num", 1);
		JSONObject result = getNubeNumberInternal(paramsObj);
		
		// 判定是不是号码己用光，如果己用光，采用靓号规则再取号
		if (result.getInteger("status") == -12) {
			JSONArray array = new JSONArray();
			array.add("AAA");
			array.add("ABC");
			array.add("CBA");
			array.add("AABB");
			array.add("ABAB");
			array.add("ABBA");
			array.add("H4");
			paramsObj.put("prettyNumberList", array);
			result = getNubeNumberInternal(paramsObj);
		}
		String nube = result.getJSONArray("nubeNumList").getString(0);
	
	
		paramsObj.clear();
		paramsObj.put("nubeNumber", nube);
		paramsObj.put("password", password);
		paramsObj.put("appType", "pc");
		paramsObj.put("appKey", appKey);
		return createAccount(paramsObj);
	}

	public JSONObject searchAccount(List<String> nubeList) {
		JSONObject paramsObj = new JSONObject();
		paramsObj.put("nubeNumbers", nubeList.toArray());
		JSONObject param = new JSONObject();
		param.put("service", "searchAccount");
		param.put("params", paramsObj.toJSONString());
		JSONObject resultObj = postMultiFormNotError(USER_SAAS_URL, "/BaikuUserCenterV2/passportService", param);
		return resultObj;
	}
}
