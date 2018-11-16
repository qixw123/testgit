package cn.redcdn.jec.common.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.service.IBaseService;
import cn.redcdn.jec.common.util.CheckUtil;
import cn.redcdn.jec.common.util.MessageUtil;
import cn.redcdn.jec.common.util.PathFactoryUtil;
import cn.redcdn.jec.common.util.StringUtil;

@Controller
public class ExternalController{
	
	private static Logger logger = LoggerFactory.getLogger(ExternalController.class);

	/**
	 * 后台管理接口入口。
	 * 
	 * @param service 调用接口服务名
	 * @param params 调用接口参数名
	 * @param request request对象
	 * @return 调用结果
	 * @throws IOException 
	 */
	@SuppressWarnings({ "rawtypes" })
	@RequestMapping(value = {"/{part:^(?!errordata$).*$}/**"})
	@ResponseBody
	@CrossOrigin(allowCredentials="false")
	public String executeExternalService(@RequestBody(required = false) String params,
			HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		
		long timeStart = System.currentTimeMillis();
		String uuid = StringUtil.getUUIDString();
		logger.info("request({}):path={}, params={}", new Object[] { uuid, path, params});
		
		ResponseDto rspDto = new ResponseDto();
		MessageInfoDto messageInfo = null;
		try
		{
			JSONObject paramsObj = CheckUtil.parseObject(params);
			IBaseService baseService = (IBaseService) PathFactoryUtil.getBean(path);
			// 路径不正确
			if (baseService == null) {
				messageInfo = MessageUtil.getMessageInfoByKey("path.invalid.wrong");
				rspDto.setMessageInfo(messageInfo);
				return rspDto.toString();
			}
			rspDto = baseService.process(paramsObj, request, response);
			if (rspDto.getCode() == 0) {
				messageInfo = MessageUtil.getMessageInfoSuccess();
				rspDto.setMessageInfo(messageInfo);
			}
		}
		catch(RecoverableDataAccessException e){
			messageInfo = MessageUtil.getMessageInfoByKey("db.error");
			rspDto.setMessageInfo(messageInfo);
			logger.error("db.error:", e);
		}
		catch(DataAccessResourceFailureException e){
			messageInfo = MessageUtil.getMessageInfoByKey("db.error");
			rspDto.setMessageInfo(messageInfo);
			logger.error("db.error:", e);
		}
		catch (ExternalServiceException e)
		{
			rspDto.setMessageInfo(e.getMessageInfo());
		}
		catch (Throwable e)
		{
			messageInfo = MessageUtil.getMessageInfoByKey("system.error");
			rspDto.setMessageInfo(messageInfo);
			logger.error("exception:", e);
		}
		finally
		{
			long timeEnd = System.currentTimeMillis();
			logger.info("totalExeTime:{} response({}):{}", timeEnd - timeStart, uuid, rspDto.toString());
		}
		return rspDto.toString();
	}
}
