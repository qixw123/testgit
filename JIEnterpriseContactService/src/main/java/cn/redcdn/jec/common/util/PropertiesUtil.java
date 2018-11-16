package cn.redcdn.jec.common.util;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesUtil {

    /**
     * 根据key检索config文件得到对应值。
     * 
     * @param key 检索key
     * @return key对应值
     */
    public static String getProperty(String key)
    {
        return ConfigPropertiesConfigurer.getProperty(key);
    }
    
    /**
     * 根据key检索config文件得到对应值。
     * 
     * @param key 检索key
     * @return key对应值
     */
    public static int getPropertyInt(String key)
    {
        return Integer.parseInt(ConfigPropertiesConfigurer.getProperty(key));
    }
    
    private static class ConfigPropertiesConfigurer
    {
    
    	private static Logger logger = LoggerFactory.getLogger(ConfigPropertiesConfigurer.class);
    	
        /**
         * messageProperies配置取得的变量
         */
        private static Properties configProperties = null;
        
        static {
            try {
                Resource resource = new ClassPathResource("config.properties");
                configProperties = PropertiesLoaderUtils.loadProperties(resource);
            } catch (IOException e) {
            	logger.error("ConfigPropertiesConfigurer.static", e);
            }
        }
        
        protected static String getProperty(String key)
        {
            return configProperties.getProperty(key);
        }
    }
}