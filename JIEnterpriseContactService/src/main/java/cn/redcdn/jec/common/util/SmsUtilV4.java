package cn.redcdn.jec.common.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.exception.ExternalServiceException;

public class SmsUtilV4 {
	private static Logger logger = LoggerFactory.getLogger(SmsUtilV4.class);

	private static String baseUrl = PropertiesUtil.getProperty("sms.url.addr");
	private static String type = PropertiesUtil.getProperty("sms.url.type");
	private static String password = PropertiesUtil.getProperty("sms.url.password");

	/**
	 * 发送短信
	 * 
	 * @param receiverNumber
	 * @param smsText
	 * @return 1为成功，0为失败
	 */
	public static int sendSms(String receiverNumber, String smsText) {

		try {
			String[] urls = baseUrl.split(",");
			for (int i = 0; i < urls.length; i++) {
				String url = urls[i] + "?type=" + type + "&password=" + password + "&mobile=" + receiverNumber
						+ "&message=" + URLEncoder.encode(smsText, "utf-8");
				logger.info("SMS request url:" + url);
				String strReturn = HttpUtil.sendGet(url);
				JSONObject jsonObject = JSON.parseObject(strReturn);
				if (jsonObject == null) {
					if (i != urls.length - 1) {
						continue;
					}
				}
				checkHttpResult(strReturn);
				break;
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			return 0;
		}
		return 1;
	}

	private static void checkHttpResult(String result) {
		JSONObject jsonObject = null;

		if (StringUtils.isEmpty(result)) {
			MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("sms.error.failed");
			throw new ExternalServiceException(messageInfo);
		}
		try {
			jsonObject = JSON.parseObject(result);
		} catch (Exception e) {
			logger.error(result, e);
			MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("sms.error.failed");
			throw new ExternalServiceException(messageInfo);
		}
		if (jsonObject == null || !jsonObject.containsKey("code")) {
			logger.error("result={}", result);
			MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("sms.error.failed");
			throw new ExternalServiceException(messageInfo);
		}

		if (!"1".equals(jsonObject.getString("code"))) {
			logger.error("result={}", result);
			if ("-104".equals(jsonObject.getString("code"))) {
				MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("sms.error.failed");
				throw new ExternalServiceException(messageInfo);
			}
			MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("sms.error.failed");
			throw new ExternalServiceException(messageInfo);
		}
	}

}
