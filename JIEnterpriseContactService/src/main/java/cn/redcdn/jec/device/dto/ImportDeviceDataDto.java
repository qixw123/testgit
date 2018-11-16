package cn.redcdn.jec.device.dto;

import com.alibaba.fastjson.JSON;

/**
 * 导入的设备数据
 * @author zhang
 *
 */
public class ImportDeviceDataDto {
	/**
	 * 视讯号
	 */
	private String nube;
	
	/**
	 * 一级组名
	 */
	private String firstGroup;
	
	/**
	 * 二级组名
	 */
	private String secondGroup;
	
	/**
	 * 三级组名
	 */
	private String thirdGroup;
	
	/**
	 * 错误原因
	 */
	private String reason;

	public String getNube() {
		return nube;
	}

	public void setNube(String nube) {
		this.nube = nube;
	}

	public String getFirstGroup() {
		return firstGroup;
	}

	public void setFirstGroup(String firstGroup) {
		this.firstGroup = firstGroup;
	}

	public String getSecondGroup() {
		return secondGroup;
	}

	public void setSecondGroup(String secondGroup) {
		this.secondGroup = secondGroup;
	}

	public String getThirdGroup() {
		return thirdGroup;
	}

	public void setThirdGroup(String thirdGroup) {
		this.thirdGroup = thirdGroup;
	}
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
