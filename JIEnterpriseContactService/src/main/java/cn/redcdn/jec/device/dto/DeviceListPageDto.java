package cn.redcdn.jec.device.dto;

import java.util.List;

import cn.redcdn.jec.common.dto.PageResultDto;

public class DeviceListPageDto extends PageResultDto {
	
	/**
	 * 联系人 设备/用户
	 */
	private List<DeviceListDto> deviceList;

	public List<DeviceListDto> getDeviceList() {
		return deviceList;
	}

	public void setDeviceList(List<DeviceListDto> deviceList) {
		this.deviceList = deviceList;
	}

}
