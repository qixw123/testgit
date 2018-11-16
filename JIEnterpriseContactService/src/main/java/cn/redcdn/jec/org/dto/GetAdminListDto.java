package cn.redcdn.jec.org.dto;

import cn.redcdn.jec.contact.dto.ContactAllStructureNumDto;

import java.util.Date;
import java.util.List;

/**
 * Project: JIEnterpriseContactService
 * Package: cn.redcdn.jec.org.dto
 * Author: mason
 * Time: 11:23
 * Date: 2018-08-27
 * Created with IntelliJ IDEA
 */
public class GetAdminListDto {

    private String id;
    private String account;
    private String creator;
    private Date createTime;
    private List<ContactAllStructureNumDto> contact;

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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<ContactAllStructureNumDto> getContact() {
        return contact;
    }

    public void setContact(List<ContactAllStructureNumDto> contact) {
        this.contact = contact;
    }
}
