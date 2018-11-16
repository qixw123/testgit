package cn.redcdn.jec.org.dto;

import java.util.Date;

/**
 * Project: JIEnterpriseContactService
 * Package: cn.redcdn.jec.org.dto
 * Author: mason
 * Time: 10:16
 * Date: 2018-08-29
 * Created with IntelliJ IDEA
 */
public class OrgAdminInfoDto {

    private String account;
    private String org;
    private String creator;
    private Date time;
    private GroupDto first;
    private GroupDto second;
    private GroupDto third;

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

    public GroupDto getFirst() {
        return first;
    }

    public void setFirst(GroupDto first) {
        this.first = first;
    }

    public GroupDto getSecond() {
        return second;
    }

    public void setSecond(GroupDto second) {
        this.second = second;
    }

    public GroupDto getThird() {
        return third;
    }

    public void setThird(GroupDto third) {
        this.third = third;
    }
}
