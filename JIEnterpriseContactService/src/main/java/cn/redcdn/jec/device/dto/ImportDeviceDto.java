package cn.redcdn.jec.device.dto;

public class ImportDeviceDto {
	/**
	 * 导入成功数
	 */
	private int success;
	
	/**
	 * 导入失败数
	 */
	private int failure;
	
	/**
	 * 失败数据存放地址
	 */
	private String url;

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public int getFailure() {
		return failure;
	}

	public void setFailure(int failure) {
		this.failure = failure;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
