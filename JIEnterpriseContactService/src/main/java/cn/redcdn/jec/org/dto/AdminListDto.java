package cn.redcdn.jec.org.dto;

import java.util.Date;

/**
 * Project: JIEnterpriseContactService
 * Package: cn.redcdn.jec.org.dto
 * Author: mason
 * Time: 10:40
 * Date: 2018-08-28
 * Created with IntelliJ IDEA
 */
public class AdminListDto {

    private String id;
    private String account;
    private String org;
    private String contact;
    private String creator;
    private Date time;
    private Integer level;
    private TypeDto type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public TypeDto getType() {
        return type;
    }

    public void setType(TypeDto type) {
        this.type = type;
    }
}
