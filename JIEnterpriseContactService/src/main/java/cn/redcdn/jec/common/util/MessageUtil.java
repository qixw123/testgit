package cn.redcdn.jec.common.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import cn.redcdn.jec.common.dto.MessageInfoDto;

public class MessageUtil {
    
    private static MessageInfoDto successDto = getMessageInfoByKey("success");

    /**
     * 成功的返回码和返回码描述。
     * 
     * @param key 检索key
     * @return 返回码和返回码描述
     */
    public static MessageInfoDto getMessageInfoSuccess()
    {       
        return successDto;
    }
    
    /**
     * 根据key检索message文件得到返回码和返回码描述。
     * 
     * @param key 检索key
     * @return 返回码和返回码描述
     */
    public static MessageInfoDto getMessageInfoByKey(String key)
    {
        int state = Integer.parseInt(getMessageRcByKey(key));
        String message = getMessageRdByKey(key);
        
        return new MessageInfoDto(state, message);
    }
    
    /**
     * 根据key检索message文件得到返回码。
     * 
     * @param key 检索key
     * @return 返回码
     */
    public static String getMessageRcByKey(String key)
    {
        return MessagePropertiesConfigurer.getMessagePropertyByKey(key + ".code");
    }
    
    /**
     * 根据key检索message文件得到返回码描述。
     * 
     * @param key 检索key
     * @return 返回码描述
     */
    public static String getMessageRdByKey(String key)
    {
        return MessagePropertiesConfigurer.getMessagePropertyByKey(key + ".message");        
    }
    
    private static class MessagePropertiesConfigurer
    {
    
    	private static Logger logger = LoggerFactory.getLogger(MessagePropertiesConfigurer.class);
    	
        /**
         * messageProperies配置取得的变量
         */
        private static Properties messageProperties = null;
        
        static {
            try {
                Resource resource = new ClassPathResource("message.properties");
                messageProperties = PropertiesLoaderUtils.loadProperties(resource);
            } catch (IOException e) {
            	logger.error("MessageUtil.static", e);
            }
        }
        protected static String getMessagePropertyByKey(String key)
        {
            return messageProperties.getProperty(key);
        }
    }
}