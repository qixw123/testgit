/**
 * 北京红云融通技术有限公司
 * 日期：$$Date$$
 * 作者：$$Author$$
 * 版本：$$Rev$$
 * 版权：All rights reserved.
 */
package cn.redcdn.jec.common.entity;

import java.util.Date;

import cn.redcdn.jec.common.annotation.Now;

import cn.redcdn.jec.common.annotation.UUID;

/**
 *
 * @ClassName: OrganizeAccount
 * @Description: 数据库表organize_account对应的entity
 */
public class OrganizeAccount
{
    /**
     * UUID
     */
    @UUID
    private String id;

    /**
     * 机构帐号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 所属组织id
     */
    private String contactGroupId;

    /**
     * 一级组织名称
     */
    private String firstGroup;

    /**
     * 二级组织名称
     */
    private String secondGroup;

    /**
     * 三级组织名称
     */
    private String thirdGroup;

    /**
     * 管理员帐号
     */
    private String importer;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    @Now
    private Date createTime;

    /**
     * 修改时间
     */
    @Now
    private Date updateTime;

    /**
     * 获取UUID
     * @return id UUID
     */
    public String getId()
    {
         return id;
    }

    /**
     * 设置UUID
     * @param id UUID
     */
    public void setId(String id)
    {
         this.id = id;
    }

    /**
     * 获取机构帐号
     * @return account 机构帐号
     */
    public String getAccount()
    {
         return account;
    }

    /**
     * 设置机构帐号
     * @param account 机构帐号
     */
    public void setAccount(String account)
    {
         this.account = account;
    }

    /**
     * 获取密码
     * @return password 密码
     */
    public String getPassword()
    {
         return password;
    }

    /**
     * 设置密码
     * @param password 密码
     */
    public void setPassword(String password)
    {
         this.password = password;
    }

    /**
     * 获取所属组织id
     * @return contactGroupId 所属组织id
     */
    public String getContactGroupId()
    {
         return contactGroupId;
    }

    /**
     * 设置所属组织id
     * @param contactGroupId 所属组织id
     */
    public void setContactGroupId(String contactGroupId)
    {
         this.contactGroupId = contactGroupId;
    }

    /**
     * 获取一级组织名称
     * @return firstGroup 一级组织名称
     */
    public String getFirstGroup()
    {
         return firstGroup;
    }

    /**
     * 设置一级组织名称
     * @param firstGroup 一级组织名称
     */
    public void setFirstGroup(String firstGroup)
    {
         this.firstGroup = firstGroup;
    }

    /**
     * 获取二级组织名称
     * @return secondGroup 二级组织名称
     */
    public String getSecondGroup()
    {
         return secondGroup;
    }

    /**
     * 设置二级组织名称
     * @param secondGroup 二级组织名称
     */
    public void setSecondGroup(String secondGroup)
    {
         this.secondGroup = secondGroup;
    }

    /**
     * 获取三级组织名称
     * @return thirdGroup 三级组织名称
     */
    public String getThirdGroup()
    {
         return thirdGroup;
    }

    /**
     * 设置三级组织名称
     * @param thirdGroup 三级组织名称
     */
    public void setThirdGroup(String thirdGroup)
    {
         this.thirdGroup = thirdGroup;
    }

    /**
     * 获取管理员帐号
     * @return importer 管理员帐号
     */
    public String getImporter()
    {
         return importer;
    }

    /**
     * 设置管理员帐号
     * @param importer 管理员帐号
     */
    public void setImporter(String importer)
    {
         this.importer = importer;
    }

    /**
     * 获取创建者
     * @return creator 创建者
     */
    public String getCreator()
    {
         return creator;
    }

    /**
     * 设置创建者
     * @param creator 创建者
     */
    public void setCreator(String creator)
    {
         this.creator = creator;
    }

    /**
     * 获取创建时间
     * @return createTime 创建时间
     */
    public Date getCreateTime()
    {
         return createTime;
    }

    /**
     * 设置创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime)
    {
         this.createTime = createTime;
    }

    /**
     * 获取修改时间
     * @return updateTime 修改时间
     */
    public Date getUpdateTime()
    {
         return updateTime;
    }

    /**
     * 设置修改时间
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime)
    {
         this.updateTime = updateTime;
    }
}