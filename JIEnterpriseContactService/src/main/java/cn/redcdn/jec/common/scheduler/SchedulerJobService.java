/**
 * 北京红云融通技术有限公司
 * 日期：$$Date: 2018-05-02 17:55:00 +0800 (周三, 02 五月 2018) $$
 * 作者：$$Author: zhangmy $$
 * 版本：$$Rev: 1963 $$
 * 版权：All rights reserved.
 */
package cn.redcdn.jec.common.scheduler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.api.UserCenterApiService;
import cn.redcdn.jec.common.dao.CacheDao;
import cn.redcdn.jec.common.entity.Device;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.util.StringUtil;
import cn.redcdn.jec.device.dao.DeviceExdDao;

/**  
 * 定时任务执行
 *  
 */
@EnableScheduling
@Service
@PropertySource("classpath:config.properties")
public class SchedulerJobService {
	private static Logger logger = LoggerFactory.getLogger(SchedulerJobService.class);
	
	@Autowired
	UserCenterApiService userCenterApiService;
	
	@Autowired
	DeviceExdDao deviceExdDao;
	
    @Autowired 
    CacheDao cacheDao;
    
    public static final int TASK_EXPIRE = 60 * 60;
    
    public static final String SYNC_USER_KEY = "sync_user_data";

    @Scheduled(cron = "${user_data_sync}")
	public void syncUserData() 
	{
    	while (true) {
			if (StringUtil.isNotBlank(cacheDao.get(SYNC_USER_KEY))) {
				break;
			}
			if (checkTaskStatus(SYNC_USER_KEY)) {
				logger.info("定时更新参会名称......");
				List<Device> deviceList = deviceExdDao.queryAll();
				// key:nube value:name
				Map<String, String> nameMap = new HashMap<String, String>();
				List<String> nubeList = new ArrayList<String>();
				for (Device device: deviceList) {
					nubeList.add(device.getNube());
					nameMap.put(device.getNube(), device.getName());
				}
				// 需要更新的设备列表
				List<Device> updDeviceList = new ArrayList<Device>();
				JSONObject obj = userCenterApiService.searchAccount(nubeList);
				if (obj != null && obj.getInteger("status") == 0) {
					JSONArray dataArray = obj.getJSONArray("users");
			    	if (dataArray != null && dataArray.size() > 0) {
			    		for (int i = 0; i < dataArray.size(); i++) {
			    			JSONObject userObj = dataArray.getJSONObject(i);
			    			String nickName = "";
			    			if (userObj.containsKey("nickName")) {
			    				nickName = userObj.getString("nickName");
			    			}
			    			if (!nickName.equals(nameMap.get(userObj.getString("nubeNumber")))) {
			    				Device updDevice = new Device();
			    				updDevice.setNube(userObj.getString("nubeNumber"));
			    				updDevice.setName(nickName);
			    				updDeviceList.add(updDevice);
			    				cacheDao.set("name_" + userObj.getString("nubeNumber"), nickName);
			    			}
			    		}
			    		if (updDeviceList.size() > 0) {
			    			deviceExdDao.updateByNubeBatch(updDeviceList);
			    		}			    		
			    	}
				}
				
			    break;	
			}
    	}
	}
    
    public boolean checkTaskStatus(String task){    	
		boolean flg = false;
		if(cacheDao.setnx(task, "true")==1) {
			cacheDao.setExpiredTime(task, TASK_EXPIRE);
			flg= true; 
		}else{
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw new ExternalServiceException();
			}
		}
		
		return flg;
    }
    
}