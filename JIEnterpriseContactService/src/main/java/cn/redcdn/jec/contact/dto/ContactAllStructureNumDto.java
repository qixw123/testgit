package cn.redcdn.jec.contact.dto;

import java.util.List;

/**
 * @author zhang
 */
public class ContactAllStructureNumDto {

    private String id;

    private String name;

    private String parentId;

    private Integer checkFlg;

    private Integer num;

    private List<ContactAllStructureNumDto> groups;

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

    public Integer getNum() {
        return num;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public List<ContactAllStructureNumDto> getGroups() {
        return groups;
    }

    public void setGroups(List<ContactAllStructureNumDto> groups) {
        this.groups = groups;
    }


    public Integer getCheckFlg() {
        return checkFlg;
    }

    public void setCheckFlg(Integer checkFlg) {
        this.checkFlg = checkFlg;
    }

    @Override
    public boolean equals(Object obj) {
        ContactAllStructureDto contact = (ContactAllStructureDto) obj;
        return this.getId().equals(contact.getId());
    }
}