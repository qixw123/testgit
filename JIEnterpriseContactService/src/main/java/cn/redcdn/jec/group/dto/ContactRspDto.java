package cn.redcdn.jec.group.dto;

import java.util.List;

public class ContactRspDto
{

	/**
     * contacts
     */
    private List<ContactDto> contacts;
    
	/**
     * childGroups
     */
    private List<ChildContactDto> childGroups;

	public List<ContactDto> getContacts() {
		return contacts;
	}

	public void setContacts(List<ContactDto> contacts) {
		this.contacts = contacts;
	}

	public List<ChildContactDto> getChildGroups() {
		return childGroups;
	}

	public void setChildGroups(List<ChildContactDto> childGroups) {
		this.childGroups = childGroups;
	}
}
