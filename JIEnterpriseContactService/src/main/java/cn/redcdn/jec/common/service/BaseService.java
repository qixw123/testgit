/**
 * 南京青牛通讯技术有限公司
 * 日期：$$Date: 2018-04-17 18:12:51 +0800 (周二, 17 四月 2018) $$
 * 作者：$$Author: zhangmy $$
 * 版本：$$Rev: 1788 $$
 * 版权：All rights reserved.
 */
package cn.redcdn.jec.common.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.dto.ResponseDto;

public abstract class BaseService<T> implements IBaseService<T>
{

	/**
     * process 处理POST请求返回结果，包括返回码和返回码描述
     * 
     * @param params 业务参数
     * @param request request对象
	 * @param response response对象
     * @return 返回码及描述
     */
	@Override
    public ResponseDto<T> process(JSONObject params, 
        HttpServletRequest request, HttpServletResponse response) {
    	return null;
    }    
}
