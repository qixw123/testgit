/**
 * 南京青牛通讯技术有限公司
 * 日期：$Date: 2017年3月6日 下午2:26:00$
 * 作者：$Author: zhangmy $
 * 版本：$Revision: 1788 $
 * 版权：All rights reserved.
 */
package cn.redcdn.jec.common.api;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.redcdn.jec.common.dao.CacheDao;
import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.common.util.MessageUtil;

/**
 * @author liuyu
 *
 */
@Service
public class LoginApiService {
	@Autowired 
	CacheDao cacheDao;
	
	/**
	 * 登录成功后调用，取消redis的错误信息
	 * @param account
	 * @param accountType
	 */
	public void loginSuccess(String accountType,String account){
		cacheDao.delete(accountType + account);
		
	}
	
	/**
	 * check密码前检查已经错误的次数
	 * @param accountType
	 * @param account
	 * @return
	 */
	public MessageInfoDto getLoginErrorCount(String accountType,String account){
		MessageInfoDto messageInfo = new MessageInfoDto();
		String errCount = cacheDao.get(accountType + account);
		String errMsg = "";
		if(StringUtils.isNotBlank(errCount)){
			int errCountInt = Integer.parseInt(errCount);
			if(errCountInt >= Constants.MAX_ERROR_COUNT){
				//获取剩余时间
				Long time = cacheDao.ttl(accountType + account);
				messageInfo = MessageUtil.getMessageInfoByKey("password.error.max");
				errMsg = messageInfo.getMsg();
				errMsg = MessageFormat.format(errMsg, errCountInt,time/60 + 1);
				messageInfo.setMsg(errMsg);
			}
		}
		return messageInfo;
	}
	
	/**
	 * 登录失败密码错误之后调用
	 * @return
	 */
	public MessageInfoDto loginPassWordError(String accountType,String account){
		MessageInfoDto messageInfo = new MessageInfoDto();
		String errCount = cacheDao.get(accountType + account);
		String errMsg = "";
		if(StringUtils.isBlank(errCount)){
			//第一次输入错误
			cacheDao.set(accountType + account, "1", 15*60);
			messageInfo = MessageUtil.getMessageInfoByKey("password.error");
			errMsg = messageInfo.getMsg();
			errMsg = MessageFormat.format(errMsg, Constants.MAX_ERROR_COUNT - 1);
			messageInfo.setMsg(errMsg);
		}
		else{
			//非第一次出错
			Integer errCountInt = Integer.parseInt(errCount);
			if(errCountInt < Constants.MAX_ERROR_COUNT){
				errCountInt++;
			}

			if(errCountInt < Constants.MAX_ERROR_COUNT){
				messageInfo = MessageUtil.getMessageInfoByKey("password.error");
				errMsg = messageInfo.getMsg();
				errMsg = MessageFormat.format(errMsg, Constants.MAX_ERROR_COUNT - errCountInt);
				messageInfo.setMsg(errMsg);
			}
			else{
				messageInfo = MessageUtil.getMessageInfoByKey("password.error.max");
				errMsg = messageInfo.getMsg();
				errMsg = MessageFormat.format(errMsg, errCountInt,15);
				messageInfo.setMsg(errMsg);
			}
			cacheDao.set(accountType + account, String.valueOf(errCountInt), 15*60);
		}
		return messageInfo;
	}
}
