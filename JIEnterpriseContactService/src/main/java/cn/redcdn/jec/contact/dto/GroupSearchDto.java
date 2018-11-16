package cn.redcdn.jec.contact.dto;

import java.util.List;

import cn.redcdn.jec.common.dto.IdAndNameDto;

public class GroupSearchDto
{
	/**
     * groupId
     */
    private String groupId;

    /**
     * groupName
     */
    private String groupName;

    /**
     * contacts
     */
    private List<IdAndNameDto> contacts;

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<IdAndNameDto> getContacts() {
		return contacts;
	}

	public void setContacts(List<IdAndNameDto> contacts) {
		this.contacts = contacts;
	}
}
