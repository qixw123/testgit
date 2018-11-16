/**
 * 北京红云融通技术有限公司
 * 日期：$$Date: 2018-04-18 18:45:10 +0800 (周三, 18 四月 2018) $$
 * 作者：$$Author: zhoulin $$
 * 版本：$$Rev: 1800 $$
 * 版权：All rights reserved.
 */
package cn.redcdn.jec.common.entity;

import java.util.Date;

import cn.redcdn.jec.common.annotation.Now;

import cn.redcdn.jec.common.annotation.UUID;

/**
 *
 * @ClassName: Device
 * @Description: 数据库表device对应的entity
 */
public class Device
{
    /**
     * 表ID
     */
    @UUID
    private String id;
    
    /**
     * groupId
     */
    private String groupId;

    /**
     * 组名
     */
    private String name;

    /**
     * 账号：序列号/手机号/邮箱
     */
    private String account;

    /**
     * 账号类型
     */
    private Integer accountType;

    /**
     * 视讯号
     */
    private String nube;

    /**
     * 一级组id
     */
    private String firstGroup;

    /**
     * 二级组id
     */
    private String secondGroup;

    /**
     * 三级组id
     */
    private String thirdGroup;

    /**
     * 中控标识
     */
    private Integer controlFlg;

    /**
     * 中控视讯号
     */
    private String controlNube;

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

    public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
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
     * 获取账号：序列号/手机号/邮箱
     * @return account 账号：序列号/手机号/邮箱
     */
    public String getAccount()
    {
         return account;
    }

    /**
     * 设置账号：序列号/手机号/邮箱
     * @param account 账号：序列号/手机号/邮箱
     */
    public void setAccount(String account)
    {
         this.account = account;
    }

    /**
     * 获取账号类型
     * @return accountType 账号类型
     */
    public Integer getAccountType()
    {
         return accountType;
    }

    /**
     * 设置账号类型
     * @param accountType 账号类型
     */
    public void setAccountType(Integer accountType)
    {
         this.accountType = accountType;
    }

    /**
     * 获取视讯号
     * @return nube 视讯号
     */
    public String getNube()
    {
         return nube;
    }

    /**
     * 设置视讯号
     * @param nube 视讯号
     */
    public void setNube(String nube)
    {
         this.nube = nube;
    }

    /**
     * 获取一级组id
     * @return firstGroup 一级组id
     */
    public String getFirstGroup()
    {
         return firstGroup;
    }

    /**
     * 设置一级组id
     * @param firstGroup 一级组id
     */
    public void setFirstGroup(String firstGroup)
    {
         this.firstGroup = firstGroup;
    }

    /**
     * 获取二级组id
     * @return secondGroup 二级组id
     */
    public String getSecondGroup()
    {
         return secondGroup;
    }

    /**
     * 设置二级组id
     * @param secondGroup 二级组id
     */
    public void setSecondGroup(String secondGroup)
    {
         this.secondGroup = secondGroup;
    }

    /**
     * 获取三级组id
     * @return thirdGroup 三级组id
     */
    public String getThirdGroup()
    {
         return thirdGroup;
    }

    /**
     * 设置三级组id
     * @param thirdGroup 三级组id
     */
    public void setThirdGroup(String thirdGroup)
    {
         this.thirdGroup = thirdGroup;
    }

    /**
     * 获取中控标识
     * @return controlFlg 中控标识
     */
    public Integer getControlFlg()
    {
         return controlFlg;
    }

    /**
     * 设置中控标识
     * @param controlFlg 中控标识
     */
    public void setControlFlg(Integer controlFlg)
    {
         this.controlFlg = controlFlg;
    }

    /**
     * 获取中控视讯号
     * @return controlNube 中控视讯号
     */
    public String getControlNube()
    {
         return controlNube;
    }

    /**
     * 设置中控视讯号
     * @param controlNube 中控视讯号
     */
    public void setControlNube(String controlNube)
    {
         this.controlNube = controlNube;
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