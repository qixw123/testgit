/**
 * 北京红云融通技术有限公司
 * 日期：$$Date: 2018-04-25 14:31:08 +0800 (周三, 25 四月 2018) $$
 * 作者：$$Author: zhangmy $$
 * 版本：$$Rev: 1883 $$
 * 版权：All rights reserved.
 */
package cn.redcdn.jec.common.entity;

import java.util.Date;

import cn.redcdn.jec.common.annotation.Now;
import cn.redcdn.jec.common.annotation.UUID;

/**
 *
 * @ClassName: GroupInfo
 * @Description: 数据库表group_info对应的entity
 */
public class GroupInfo
{
    /**
     * 表ID
     */
    @UUID
    private String id;

    /**
     * 组名
     */
    private String name;

    /**
     * 组的级别
     */
    private Integer level;

    /**
     * 上级组id
     */
    private String parentId;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 导入者
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
     * 更新时间
     */
    @Now(type="U")
    private Date updateTime;

    /**
     * 获取表ID
     * @return id 表ID
     */
    public String getId()
    {
         return id;
    }

    /**
     * 设置表ID
     * @param id 表ID
     */
    public void setId(String id)
    {
         this.id = id;
    }

    /**
     * 获取组名
     * @return name 组名
     */
    public String getName()
    {
         return name;
    }

    /**
     * 设置组名
     * @param name 组名
     */
    public void setName(String name)
    {
         this.name = name;
    }

    /**
     * 获取组的级别
     * @return level 组的级别
     */
    public Integer getLevel()
    {
         return level;
    }

    /**
     * 设置组的级别
     * @param level 组的级别
     */
    public void setLevel(Integer level)
    {
         this.level = level;
    }

    /**
     * 获取上级组id
     * @return parentId 上级组id
     */
    public String getParentId()
    {
         return parentId;
    }

    /**
     * 设置上级组id
     * @param parentId 上级组id
     */
    public void setParentId(String parentId)
    {
         this.parentId = parentId;
    }

    /**
     * 获取序号
     * @return sort 序号
     */
    public Integer getSort()
    {
         return sort;
    }

    /**
     * 设置序号
     * @param sort 序号
     */
    public void setSort(Integer sort)
    {
         this.sort = sort;
    }

    /**
     * 获取导入者
     * @return importer 导入者
     */
    public String getImporter()
    {
         return importer;
    }

    /**
     * 设置导入者
     * @param importer 导入者
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