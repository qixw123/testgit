/**
 * 北京红云融通技术有限公司
 * 日期：$$Date: 2018-04-17 18:12:51 +0800 (周二, 17 四月 2018) $$
 * 作者：$$Author: zhangmy $$
 * 版本：$$Rev: 1788 $$
 * 版权：All rights reserved.
 */
package cn.redcdn.jec.common.entity;

/**
 *
 * @ClassName: SystemAdmin
 * @Description: 数据库表system_admin对应的entity
 */
public class SystemAdmin
{
    /**
     * 表ID
     */
    private String id;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 姓名
     */
    private String name;

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
     * 获取账号
     * @return account 账号
     */
    public String getAccount()
    {
         return account;
    }

    /**
     * 设置账号
     * @param account 账号
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
     * 获取姓名
     * @return name 姓名
     */
    public String getName()
    {
         return name;
    }

    /**
     * 设置姓名
     * @param name 姓名
     */
    public void setName(String name)
    {
         this.name = name;
    }
}