package cn.redcdn.jec.contact.dto;

import java.util.List;

/**
 * 
 * @author zhang
 *
 */
public class ContactAllStructureDto {
	
	private String id;

	private String name;

	private List<DeviceDetailDto> contacts;

	private List<ContactAllStructureDto> groups;

	
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


	public List<DeviceDetailDto> getContacts() {
		return contacts;
	}


	public void setContacts(List<DeviceDetailDto> contacts) {
		this.contacts = contacts;
	}


	public List<ContactAllStructureDto> getGroups() {
		return groups;
	}


	public void setGroups(List<ContactAllStructureDto> groups) {
		this.groups = groups;
	}


	@Override
	public boolean equals(Object obj)  
	{   ContactAllStructureDto contact = (ContactAllStructureDto) obj;
	    return this.getId().equals(contact.getId());  
	}
}