/**
 * 南京青牛通讯技术有限公司
 * 日期：$$Date: 2018-04-17 18:12:51 +0800 (周二, 17 四月 2018) $$
 * 作者：$$Author: zhangmy $$
 * 版本：$$Rev: 1788 $$
 * 版权：All rights reserved.
 */
package cn.redcdn.jec.common.util;

import java.text.MessageFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.exception.ExternalServiceException;

public class CheckUtil
{

	/**
	 * 校验是否为JSON格式，请求参数为空返回NULL
	 * 
	 * @param json
	 * @return
	 */
	public static JSONObject parseObject(String json)
	{
		// 参数为空的时候 默认检查成功
		if (StringUtil.isNotBlank(json))
		{
			try
			{
				return JSON.parseObject(json);
			}
			catch (Exception e)
			{
				MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("wrong.json");
				throw new ExternalServiceException(messageInfo);
			}
		}
		
		return new JSONObject();
	}
	/**
	 * 必须check
	 * 
	 * @param json
	 * @return
	 */
	public static void checkEmpty(JSONObject params, String... keys) {
		StringBuilder sb  = new StringBuilder();
		if(params == null){
			MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("params.empty");
			String message = messageInfo.getMsg();
			message = MessageFormat.format(message, keys[0]);
			messageInfo.setMsg(message);
			
			throw new ExternalServiceException(messageInfo);
		}
		for (String key : keys) {
			String value = params.getString(key);
			if (StringUtil.isBlank(value)) {
				sb.append(",");
				sb.append(key);
			}
		}

		if (sb.length() > 0) {
			MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("params.empty");
			String message = messageInfo.getMsg();
			sb.delete(0, 1);
			message = MessageFormat.format(message, sb.toString());
			messageInfo.setMsg(message);
			
			throw new ExternalServiceException(messageInfo);
		}
	}
	
	/**
	 * 必须check
	 * 
	 * @param json
	 * @return
	 */
	public static void checkEmptyExceptEmptyString(JSONObject params, String... keys) {
		StringBuilder sb  = new StringBuilder();
		for (String key : keys) {
			String value = params.getString(key);
			if (value == null) {
				sb.append(",");
				sb.append(key);
			}
		}

		if (sb.length() > 0) {
			MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("params.empty");
			String message = messageInfo.getMsg();
			sb.delete(0, 1);
			message = MessageFormat.format(message, sb.toString());
			messageInfo.setMsg(message);
			
			throw new ExternalServiceException(messageInfo);
		}
	}
	
	/**
	 * 检查列表是否为空
	 * @param params 
	 * @param visitListParam
	 */
	public static void checkListEmpty(JSONObject params, String... keys) {
		StringBuilder sb = new StringBuilder();
		for (String key : keys) {
			String value = params.getString(key);
			JSONArray jsonarray = JSON.parseArray(value);
			if (jsonarray.size() <= 0)
	        {
				sb.append(",");
				sb.append(key);
	        }
		}
		if (0 < sb.length()) {
			MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("param.list.empty");
			String message = messageInfo.getMsg();
			sb.delete(0, 1);
			message = MessageFormat.format(message, sb.toString());
			messageInfo.setMsg(message);
			
			throw new ExternalServiceException(messageInfo);
		}		
	}
	
	/**
	 * 判断是否是Phone
	 * 
	 * @author: chenzc
	 * @CreateTime: 2017-09-25
	 * @param mobile
	 * @return
	 * @Return: boolean
	 */
	public static boolean isMobile(String mobile)
	{
		boolean isMatched = false;
		if (StringUtils.isNotEmpty(mobile))
		{
			Pattern p = Pattern
					.compile("^((\\+86)|86|0086)?((((010|020|021|022|023|024|025|026|027|028|029)?\\d{8})|((0[3-9][1-9]{2})?\\d{7,8})))$");
			Matcher m = p.matcher(mobile);
			isMatched = m.matches();
		}
		return isMatched;
	}
	
	/**
	 * 判断是否是email
	 * 
	 * @author: chenzc
	 * @CreateTime: 2017-09-25
	 * @param mail
	 * @return
	 * @Return: boolean
	 */
	public static boolean isMail(String mail)
	{
		if (!StringUtils.isEmpty(mail))
		{
			Pattern p = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_-]*(\\.)?[a-zA-Z0-9]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)+$");// a.@a.com
			Matcher m = p.matcher(mail);
			return m.matches();
		}
		return false;
	}
}
