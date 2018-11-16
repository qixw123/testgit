package cn.redcdn.jec.device.dto;

/**
 * GetDeviceList接口返回的dto
 * @author zhang
 *
 */
public class DeviceListDto {
	
	private String id;
	
	private String name;
	
	private String account;
	
	private String nube;
	
	private String firstGroup;
	
	private String secondGroup;
	
	private String thirdGroup;
	
	private String controlFlg;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

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

	public String getControlFlg() {
		return controlFlg;
	}

	public void setControlFlg(String controlFlg) {
		this.controlFlg = controlFlg;
	}
}
