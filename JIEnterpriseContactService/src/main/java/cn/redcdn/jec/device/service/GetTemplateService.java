package cn.redcdn.jec.device.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.api.FileApiService;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.service.BaseService;

/**
 * 下载导入设备模板
 * 
 * @author zhang
 *
 */
@Path("/device/template")
public class GetTemplateService extends BaseService<String> {

	@Autowired
	FileApiService fileApiService;

	private static final String TEMPLATE_NAME = "设备信息模板.xls";

	@Override
	public ResponseDto<String> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {
		String fileName = "";
		try {
			fileName = URLEncoder.encode(TEMPLATE_NAME, "UTF-8");
		} catch (UnsupportedEncodingException e1) {
			throw new ExternalServiceException();
		}
		String filePath = request.getSession().getServletContext().getRealPath("/") 
				+ "/template/" + TEMPLATE_NAME;

		fileApiService.downloadFile(response, filePath, fileName);

		return new ResponseDto<String>();
	}
}
