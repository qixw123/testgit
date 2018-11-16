package cn.redcdn.jec.device.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.annotation.Path;
import cn.redcdn.jec.common.dto.ResponseDto;
import cn.redcdn.jec.common.service.BaseService;
import cn.redcdn.jec.common.util.CheckUtil;

/**
 * 批量开启中控
 * 
 * @author zhang
 *
 */
@Path("/device/setcontrolbatch")
public class SetControlBatchService extends BaseService<String> {

	private static final Logger logger = LoggerFactory.getLogger(SetControlBatchService.class);

	private static final int OPEN_CONTROL = 2;

	@Autowired
	SetControlService setControlService;

	@Override
	public ResponseDto<String> process(JSONObject params, HttpServletRequest request, HttpServletResponse response) {
		CheckUtil.checkEmpty(params, "token", "ids");

		JSONArray idArray = JSON.parseArray(params.getString("ids"));
		for (int i = 0; i < idArray.size(); i++) {
			JSONObject paramObj = new JSONObject();
			paramObj.put("token", params.getString("token"));
			paramObj.put("id", idArray.get(i));
			paramObj.put("controlFlg", OPEN_CONTROL);
			try {
				setControlService.process(paramObj, request, response);
			} catch (Exception e) {
				logger.info(idArray.get(i) + "开启中控失败。");
			}
		}

		return new ResponseDto<String>();
	}

}
