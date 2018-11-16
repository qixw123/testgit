package cn.redcdn.jec.user.dao;

import org.apache.ibatis.annotations.Param;

import cn.redcdn.jec.common.entity.OrganizeAccount;

public interface OrgAdminLoginDao {

    /**
     * 查询用户
     *
     * @param String account 账号
     * @param String password 密码
     * @return 信息
     */
    OrganizeAccount getOrgAdminInfo(@Param("account") String account, @Param("password") String password);

    /**
     * 批量重置密码
     *
     * @param accounts
     * @param password
     * @return
     */
    int updateOrgAdminPassword(@Param("account") String account, @Param("password") String password);
}
