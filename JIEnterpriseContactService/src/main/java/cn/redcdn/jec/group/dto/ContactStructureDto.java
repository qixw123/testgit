package cn.redcdn.jec.group.dto;

import java.util.List;

public class ContactStructureDto {
	
	private String id;
	
	private String name;
	
	private List<ContactStructureDto> childList;

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

	public List<ContactStructureDto> getChildList() {
		return childList;
	}

	public void setChildList(List<ContactStructureDto> childList) {
		this.childList = childList;
	}

}
