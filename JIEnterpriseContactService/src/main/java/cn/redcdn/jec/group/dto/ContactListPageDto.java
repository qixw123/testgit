package cn.redcdn.jec.group.dto;

import java.util.List;

import cn.redcdn.jec.common.dto.PageResultDto;

public class ContactListPageDto extends PageResultDto {
	
	private List<ContactInfoDto> contacts;

	public List<ContactInfoDto> getContacts() {
		return contacts;
	}

	public void setContacts(List<ContactInfoDto> contacts) {
		this.contacts = contacts;
	}

}
