/**
 * 北京红云融通技术有限公司
 * 日期：$$Date: 2018-04-20 17:25:04 +0800 (周五, 20 四月 2018) $$
 * 作者：$$Author: zhoulin $$
 * 版本：$$Rev: 1836 $$
 * 版权：All rights reserved.
 */
package cn.redcdn.jec.common.entity;

import java.util.Date;

import cn.redcdn.jec.common.annotation.Now;

import cn.redcdn.jec.common.annotation.UUID;

/**
 *
 * @ClassName: EntContact
 * @Description: 数据库表ent_contact对应的entity
 */
public class EntContact
{
    /**
     * ID
     */
    @UUID
    private String id;

    /**
     * 中控用户ID
     */
    private String deviceId;

    /**
     * 中控用户通讯录组ID
     */
    private String contactGroupId;

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
     * 更新时间
     */
    @Now(type="U")
    private Date updateTime;

    /**
     * 获取ID
     * @return id ID
     */
    public String getId()
    {
         return id;
    }

    /**
     * 设置ID
     * @param id ID
     */
    public void setId(String id)
    {
         this.id = id;
    }

    /**
     * 获取中控用户ID
     * @return deviceId 中控用户ID
     */
    public String getDeviceId()
    {
         return deviceId;
    }

    /**
     * 设置中控用户ID
     * @param deviceId 中控用户ID
     */
    public void setDeviceId(String deviceId)
    {
         this.deviceId = deviceId;
    }

    /**
     * 获取中控用户通讯录组ID
     * @return contactGroupId 中控用户通讯录组ID
     */
    public String getContactGroupId()
    {
         return contactGroupId;
    }

    /**
     * 设置中控用户通讯录组ID
     * @param contactGroupId 中控用户通讯录组ID
     */
    public void setContactGroupId(String contactGroupId)
    {
         this.contactGroupId = contactGroupId;
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
     * 获取更新时间
     * @return updateTime 更新时间
     */
    public Date getUpdateTime()
    {
         return updateTime;
    }

    /**
     * 设置更新时间
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime)
    {
         this.updateTime = updateTime;
    }
}