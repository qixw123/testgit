package cn.redcdn.jec.user.dao;

import cn.redcdn.jec.common.entity.SystemAdmin;
import org.apache.ibatis.annotations.Param;

public interface AdminLoginDao {

    /**
     * 查询用户
     *
     * @param String account 账号
     * @param String password 密码
     * @return 信息
     */
    SystemAdmin getSystemAdminInfo(@Param("account") String account, @Param("password") String password);

    /**
     * 查询用户
     *
     * @param String account 账号
     * @param String password 密码
     * @return 信息
     */
    SystemAdmin getSystemAdminInfoByNubeOrAccount(@Param("nube") String nube, @Param("account") String account);

    /**
     * /*
     * 更新
     *
     * @param String account 账号
     * @param String password 密码
     * @return 信息
     */
    int updateByAccount(@Param("account") String account, @Param("name") String name);
    int updatePasswordByAccount(@Param("account") String account, @Param("pwd") String pwd);
}
