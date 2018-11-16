package cn.redcdn.jec.common.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.redcdn.jec.common.dao.CacheDao;
import cn.redcdn.jec.common.util.Constants;
import cn.redcdn.jec.common.util.StringUtil;

@Service
public class DeviceApiService extends BaseApiService {

	@Autowired
	CacheDao cacheDao;

	/**
	 * 缓存设备/用户信息
	 * 
	 * @param userInfo
	 */
	public void cacheInfo(JSONObject userInfo) {
		String nube = userInfo.getString("nubeNumber");
		String nickname = userInfo.getString("nickname");
		String account = "";
		Integer accountType = null;
		// 设置账号及账号类型
		if (StringUtil.isNotBlank(userInfo.getString("imeiPreBind"))) {
			account = userInfo.getString("imeiPreBind");
			accountType = Constants.ACCOUNT_TYPE_TP;
		} else if (StringUtil.isNotBlank(userInfo.getString("mobile"))) {
			account = userInfo.getString("mobile");
			accountType = Constants.ACCOUNT_TYPE_MOBILE;
		} else if (StringUtil.isNotBlank(userInfo.getString("mail"))) {
			account = userInfo.getString("mail");
			accountType = Constants.ACCOUNT_TYPE_MAIL;
		} else {
			account = "";
			accountType = Constants.ACCOUNT_TYPE_OTHER;
		}
		// 缓存用户信息
		cacheDao.set("name_" + nube, nickname);
		cacheDao.set("account_" + nube, account);
		cacheDao.set("account_type_" + nube, accountType);

	}

}
