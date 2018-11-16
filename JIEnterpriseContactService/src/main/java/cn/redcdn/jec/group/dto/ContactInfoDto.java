package cn.redcdn.jec.group.dto;

import java.util.List;

import cn.redcdn.jec.common.dto.ContactStructureDto;

/**
 * 
 * @author zhang
 *
 */
public class ContactInfoDto {

	private String id;

	private String name;

	private String nube;

	private String account;
	
	private String controlFlg;

	private List<ContactStructureDto> structures;

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

	public String getNube() {
		return nube;
	}

	public void setNube(String nube) {
		this.nube = nube;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public List<ContactStructureDto> getStructures() {
		return structures;
	}

	public void setStructures(List<ContactStructureDto> structures) {
		this.structures = structures;
	}

	public String getControlFlg() {
		return controlFlg;
	}

	public void setControlFlg(String controlFlg) {
		this.controlFlg = controlFlg;
	}

}
