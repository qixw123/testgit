package cn.redcdn.jec.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import cn.redcdn.jec.common.annotation.Path;

@Component
public final class PathFactoryUtil implements ApplicationContextAware 
{
    private static Map<String, Object> pathMap = new HashMap<String, Object>(); 
    
    public void setApplicationContext(ApplicationContext context) throws BeansException 
    {
    	Map<String, Object> serviceMap =  context.getBeansWithAnnotation(Path.class);
    	for (Entry<String, Object> entry: serviceMap.entrySet()) {
    		Path path = entry.getValue().getClass().getAnnotation(Path.class);
    		pathMap.put(path.value(), entry.getValue());
    	}
    }

    /**
     * 根据一个bean的id获取配置文件中相应的bean
     * 
     * @param className className
     * @return Object
     * @throws BeansException BeansException
     */
    public static Object getBean(String path) throws BeansException
    {
        return pathMap.get(path);
    }
}