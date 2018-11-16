package cn.redcdn.jec.common.api;

import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.util.HttpUtil;
import cn.redcdn.jec.common.util.MessageUtil;

/**
 * <dl>
 * <dt>BaseApiService.java</dt>
 * <dd>Description:接口访问基础类</dd>
 * <dd>Company: 北京红云融通技术有限公司</dd>
 * <dd>CreateDate: 2016-2-26</dd>
 * </dl>
 * 
 * @author yusy
 */
@Service
public class BaseApiService {
	
	private Logger logger = LoggerFactory.getLogger(BaseApiService.class);
	
	/**
	 * 发送http请求
	* Description:
	*
	* @param method
	* @param url
	* @param params
	* @return
	 */
	public JSONObject post(String url, JSONObject paramsObj) {
		logger.info("http request url:{}, params:{}", url, paramsObj.toJSONString());
		String result = HttpUtil.sendPost(url, paramsObj.toJSONString());
		logger.info("http response：" + result);
		JSONObject jsonObject = JSON.parseObject(result);
		if(jsonObject == null) 
		{
			MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("http.post.failure");
			throw new ExternalServiceException(messageInfo);
		}		
		if (jsonObject.getInteger("code") == -907) {
		    MessageInfoDto messageInfo = new MessageInfoDto();
		    messageInfo.setCode(jsonObject.getInteger("code"));
		    messageInfo.setMsg(jsonObject.getString("msg"));
		    throw new ExternalServiceException(messageInfo);

		} else if (jsonObject.getInteger("code") != 0)
		{
			MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("http.business.failure");
			throw new ExternalServiceException(messageInfo);
		}
			
		return jsonObject;
	}
	/**
	 * 发送http请求
	* Description:
	*
	* @param method
	* @param url
	* @param params
	* @return
	 */
	public JSONObject postMulti(String urlstrs, String service, JSONObject paramsObj) {
		String[] urls = urlstrs.split(",");
		for (int i = 0; i < urls.length; i++) {
			String url = urls[i] + service;
			logger.info("http request url:{}, params:{}", url, paramsObj.toJSONString());
			String result = HttpUtil.sendPost(url, paramsObj.toJSONString());
			logger.info("http response：" + result);
			JSONObject jsonObject = JSON.parseObject(result);
			if(jsonObject == null) 
			{
				if (i != urls.length - 1) {
					continue;
				} else {
					MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("http.post.failure");
					throw new ExternalServiceException(messageInfo);
				}
			}		
			if (jsonObject.getInteger("code") == -907) {
			    MessageInfoDto messageInfo = new MessageInfoDto();
			    messageInfo.setCode(jsonObject.getInteger("code"));
			    messageInfo.setMsg(jsonObject.getString("msg"));
			    throw new ExternalServiceException(messageInfo);
	
			} else if (jsonObject.getInteger("code") != 0)
			{
				MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("http.business.failure");
				throw new ExternalServiceException(messageInfo);
			}
				
			return jsonObject;
		}
		return null;
	}
	
	/**
	 * 发送http请求
	* Description:
	*
	* @param method
	* @param url
	* @param params
	* @return
	 */
	public JSONObject postForm(String url, JSONObject paramsObj) {
		logger.info("http request url:{}, params:{}", url, paramsObj.toJSONString());
		StringBuffer sb = new StringBuffer();
		for (Entry<String, Object> entry : paramsObj.entrySet())
		{
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
			sb.append("&");
		}
		if (sb.length() > 0)
		{
			sb.delete(sb.length() - 1, sb.length());
		}
		String result = HttpUtil.sendPostForm(url, sb.toString());
		logger.info("http response：" + result);
		JSONObject jsonObject = JSON.parseObject(result);
		if(jsonObject == null) 
		{
			MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("http.post.failure");
			throw new ExternalServiceException(messageInfo);
		}		
		if (jsonObject.getInteger("status") != 0)
		{
			MessageInfoDto messageInfo = new MessageInfoDto();
			messageInfo.setCode(jsonObject.getInteger("status"));
			messageInfo.setMsg(jsonObject.getString("message"));
			throw new ExternalServiceException(messageInfo);
		}
			
		return jsonObject;
	}
	
	/**
	 * 发送http请求
	* Description:
	*
	* @param method
	* @param url
	* @param params
	* @return
	 */
	public JSONObject postMultiForm(String urlstrs, String service, JSONObject paramsObj) {
		String[] urls = urlstrs.split(",");
		for (int i = 0; i < urls.length; i++) {
			String url = urls[i] + service;
		    logger.info("http request url:{}, params:{}", url, paramsObj.toJSONString());
			StringBuffer sb = new StringBuffer();
			for (Entry<String, Object> entry : paramsObj.entrySet())
			{
				sb.append(entry.getKey());
				sb.append("=");
				sb.append(entry.getValue());
				sb.append("&");
			}
			if (sb.length() > 0)
			{
				sb.delete(sb.length() - 1, sb.length());
			}
			String result = HttpUtil.sendPostForm(url, sb.toString());
			logger.info("http response：" + result);
			JSONObject jsonObject = JSON.parseObject(result);
			if(jsonObject == null) 
			{
				if (i != urls.length - 1) {
					continue;
				} else {
					MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("http.post.failure");
					throw new ExternalServiceException(messageInfo);
				}
			}		
			if (jsonObject.getInteger("status") != 0)
			{
				MessageInfoDto messageInfo = new MessageInfoDto();
				messageInfo.setCode(jsonObject.getInteger("status"));
				messageInfo.setMsg(jsonObject.getString("message"));
				throw new ExternalServiceException(messageInfo);
			}
				
			return jsonObject;
		}
		return null;
	}
	
	/**
	 * 发送http请求
	* Description:
	*
	* @param method
	* @param url
	* @param params
	* @return
	 */
	public JSONObject postOnlyForm(String urlstrs, String service, JSONObject paramsObj) {
		String[] urls = urlstrs.split(",");
		String url = urls[0] + service;
	    logger.info("http request url:{}, params:{}", url, paramsObj.toJSONString());
		StringBuffer sb = new StringBuffer();
		for (Entry<String, Object> entry : paramsObj.entrySet())
		{
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
			sb.append("&");
		}
		if (sb.length() > 0)
		{
			sb.delete(sb.length() - 1, sb.length());
		}
		String result = HttpUtil.sendPostForm(url, sb.toString());
		logger.info("http response：" + result);
		JSONObject jsonObject = JSON.parseObject(result);
		if(jsonObject == null) 
		{
			MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("http.post.failure");
			throw new ExternalServiceException(messageInfo);
		}		
		if (jsonObject.getInteger("status") != 0)
		{
			MessageInfoDto messageInfo = new MessageInfoDto();
			messageInfo.setCode(jsonObject.getInteger("status"));
			messageInfo.setMsg(jsonObject.getString("message"));
			throw new ExternalServiceException(messageInfo);
		}
			
		return jsonObject;
	}
	
	/**
	 * 发送http请求
	* Description:
	*
	* @param method
	* @param url
	* @param params
	* @return
	 */
	public JSONObject postFormWithRC(String url, JSONObject paramsObj) {
		logger.info("http request url:{}, params:{}", url, paramsObj.toJSONString());
		StringBuffer sb = new StringBuffer();
		for (Entry<String, Object> entry : paramsObj.entrySet())
		{
			sb.append(entry.getKey());
			sb.append("=");
			sb.append(entry.getValue());
			sb.append("&");
		}
		if (sb.length() > 0)
		{
			sb.delete(sb.length() - 1, sb.length());
		}
		String result = HttpUtil.sendPostForm(url, sb.toString());
		logger.info("http response：" + result);
		JSONObject jsonObject = JSON.parseObject(result);
		if(jsonObject == null
			|| !jsonObject.containsKey("result")) 
		{
			MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("http.post.failure");
			throw new ExternalServiceException(messageInfo);
		}		
		if (jsonObject.getJSONObject("result").getInteger("rc") != 0)
		{
			MessageInfoDto messageInfo = new MessageInfoDto();
			messageInfo.setCode(jsonObject.getJSONObject("result").getInteger("rc"));
			messageInfo.setMsg(jsonObject.getJSONObject("result").getString("rd"));
			throw new ExternalServiceException(messageInfo);
		}
			
		return jsonObject;
	}
	

	
	/**
	 * 发送http请求
	* Description:
	*
	* @param method
	* @param url
	* @param params
	* @return
	 */
	public JSONObject postMultiFormWithRC(String urlstrs, String service, JSONObject paramsObj) {
		String[] urls = urlstrs.split(",");
		for (int i = 0; i < urls.length; i++) {
			String url = urls[i] + service;
			logger.info("http request url:{}, params:{}", url, paramsObj.toJSONString());
			StringBuffer sb = new StringBuffer();
			for (Entry<String, Object> entry : paramsObj.entrySet())
			{
				sb.append(entry.getKey());
				sb.append("=");
				sb.append(entry.getValue());
				sb.append("&");
			}
			if (sb.length() > 0)
			{
				sb.delete(sb.length() - 1, sb.length());
			}
			String result = HttpUtil.sendPostForm(url, sb.toString());
			logger.info("http response：" + result);
			JSONObject jsonObject = JSON.parseObject(result);
			if(jsonObject == null
				|| !jsonObject.containsKey("result")) 
			{
				if (i != urls.length - 1) {
					continue;
				} else {
					MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("http.post.failure");
					throw new ExternalServiceException(messageInfo);
				}
			}		
			if (jsonObject.getJSONObject("result").getInteger("rc") != 0)
			{
				MessageInfoDto messageInfo = new MessageInfoDto();
				messageInfo.setCode(jsonObject.getJSONObject("result").getInteger("rc"));
				messageInfo.setMsg(jsonObject.getJSONObject("result").getString("rd"));
				throw new ExternalServiceException(messageInfo);
			}
				
			return jsonObject;
		}
		return null;
	}
	
	/**
	 * 发送http请求
	* Description:
	*
	* @param method
	* @param url
	* @param params
	* @return
	 */
	public JSONObject getWithNotException(String url) {
		logger.info("http request url:{}", url);
		String result = HttpUtil.sendGet(url);
		logger.info("http response：" + result);
		JSONObject jsonObject = JSON.parseObject(result);
		if(jsonObject == null) 
		{
			MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("http.post.failure");
			throw new ExternalServiceException(messageInfo);
		}
			
		return jsonObject;
	}
	
	/**
	 * 发送http请求
	* Description:
	*
	* @param method
	* @param url
	* @param params
	* @return
	 */
	public JSONObject getMultiWithNotException(String urlstrs, String service) {
		String[] urls = urlstrs.split(",");
		for (int i = 0; i < urls.length; i++) {
			String url = urls[i] + service;
			logger.info("http request url:{}", url);
			String result = HttpUtil.sendGet(url);
			logger.info("http response：" + result);
			JSONObject jsonObject = JSON.parseObject(result);
			if(jsonObject == null) 
			{
				if (i != urls.length - 1) {
					continue;
				} else {
					MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("http.post.failure");
					throw new ExternalServiceException(messageInfo);
				}
			}
				
			return jsonObject;
		}
		return null;
	}
	
	public JSONObject postMultiFormNotError(String urlstrs, String service, JSONObject paramsObj) {
		String[] urls = urlstrs.split(",");
		for (int i = 0; i < urls.length; i++) {
			String url = urls[i] + service;
		    logger.info("http request url:{}, params:{}", url, paramsObj.toJSONString());
			StringBuffer sb = new StringBuffer();
			for (Entry<String, Object> entry : paramsObj.entrySet())
			{
				sb.append(entry.getKey());
				sb.append("=");
				sb.append(entry.getValue());
				sb.append("&");
			}
			if (sb.length() > 0)
			{
				sb.delete(sb.length() - 1, sb.length());
			}
			String result = HttpUtil.sendPostForm(url, sb.toString());
			logger.info("http response：" + result);
			JSONObject jsonObject = JSON.parseObject(result);
			if(jsonObject == null) 
			{
				if (i != urls.length - 1) {
					continue;
				} else {
					MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("http.post.failure");
					throw new ExternalServiceException(messageInfo);
				}
			}		
				
			return jsonObject;
		}
		return null;
	}
	
}
