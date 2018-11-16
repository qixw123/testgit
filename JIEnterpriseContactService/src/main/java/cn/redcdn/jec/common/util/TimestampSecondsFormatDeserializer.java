package cn.redcdn.jec.common.util;

import java.lang.reflect.Type;
import java.sql.Timestamp;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.TimestampDeserializer;

public final class TimestampSecondsFormatDeserializer extends TimestampDeserializer {

	/**
	 * 日期型字段转换为秒后输出。
	 * 
	 * @param serializer serializer
	 * @param object 对象值
	 * @param obj1 obj1对象
	 * @param type type对象
	 */
    @SuppressWarnings("unchecked")
	@Override
	protected <T> T cast(DefaultJSONParser parser, Type clazz, Object fieldName, Object val) {
		if (val == null)
		{
			return null;
		}
		else 
		{
			String strVal = (String) val;
			if (strVal.length() == 0) {
				return null;
			}
            return (T) new Timestamp(Long.parseLong(strVal)*1000);
		}
	}
}