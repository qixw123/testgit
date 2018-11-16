package cn.redcdn.jec.common.dto;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

import cn.redcdn.jec.common.util.DateUtil;


public class ResponseDto<T> extends MessageInfoDto
{

    /**
	 * 有效数据
	 */
    private T data;
    
	/**
	 * getter method
	 * @return Returns the data.
	 */
	public T getData() {
		return data;
	}

	/**
	 * setter method
	 * @param data The data to set.
	 */
	public void setData(T data) {
		this.data = data;
	}


	@Override
    public String toString()
    {
		return JSONObject.toJSONString(this, DateUtil.getSecondsFormatConfig(), 
				SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullNumberAsZero, 
				SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty);
    }
}
