package cn.redcdn.jec.common.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.dto.ResponseDto;

public interface IBaseService<T> {
	
    /**
     * process 处理POST请求返回结果，包括返回码和返回码描述
     * 
     * @param params 业务参数
     * @param request request对象
	 * @param response response对象
     * @return 返回码及描述
     */
    ResponseDto<T> process(JSONObject params, 
        HttpServletRequest request, HttpServletResponse response);
   
}
