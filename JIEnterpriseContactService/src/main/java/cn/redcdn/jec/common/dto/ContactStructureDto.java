package cn.redcdn.jec.common.dto;

import java.util.List;

/**
 * 
 * @author zhang
 *
 */
public class ContactStructureDto {
	
	private String id;

	private String name;

	private List<ContactStructureDto> groups;

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

	public List<ContactStructureDto> getGroups() {
		return groups;
	}

	public void setGroups(List<ContactStructureDto> groups) {
		this.groups = groups;
	}
	
	@Override
	public boolean equals(Object obj)  
	{   ContactStructureDto contact = (ContactStructureDto) obj;
	    return this.getId().equals(contact.getId());  
	}
}