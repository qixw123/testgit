package cn.redcdn.jec.org.dao;

import cn.redcdn.jec.common.entity.GroupInfo;
import cn.redcdn.jec.common.entity.OrganizeAccount;
import cn.redcdn.jec.org.dto.AdminListDto;
import cn.redcdn.jec.org.dto.GroupLevelDto;
import cn.redcdn.jec.org.dto.OrgAdminInfoDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Project: JIEnterpriseContactService
 * Package: cn.redcdn.jec.org.dao
 * Author: mason
 * Time: 10:57
 * Date: 2018-08-27
 * Created with IntelliJ IDEA
 */
public interface OrganizeAccountExtDao {

    /**
     * 根据用户查找群组信息
     *
     * @param account
     * @return
     */
    GroupInfo selectGroupInfoByAccount(@Param("account") String account);

    /**
     * 根据账户名查找机构信息
     *
     * @param account
     * @return
     */
    OrganizeAccount selectInfoByAccount(@Param("account") String account);

    /**
     * 根据一组id查找
     */
    List<AdminListDto> selectInfoByAccountList(@Param("ids") List<String> ids, @Param("account") String account);

    /**
     * 根据账户查找机构管理员信息
     *
     * @param account
     * @return
     */
    OrgAdminInfoDto selectOrgAdminBaseInfoByAccount(@Param("account") String account);

    /**
     * 根据当前用户id，查找同级及其子级用户
     *
     * @param id
     * @return
     */
    List<AdminListDto> selectAdminListByCurrentUserId(@Param("ids") List<String> ids, @Param("account") String account, @Param("org") String org);

    /**
     * 根据账户查找管理员信息
     *
     * @param account
     * @return
     */
    OrgAdminInfoDto selectAdminBaseInfoByAccount(@Param("account") String account);

    /**
     * 批量删除账号
     *
     * @param accounts
     * @return
     */
    int deleteByAccountsLits(@Param("accounts") List<String> accounts);

    /**
     * 根据id获取群组层级名称
     *
     * @param id
     * @return
     */
    GroupLevelDto getGroupLevelNameById(@Param("id") String id);

    List<GroupInfo> getGroupInfo(@Param("id") String parentId, @Param("importer") String importer, @Param("level") String level);

    void updateByGroupIdBatch(@Param("list") List<String> groupAndChildrenIds,
                              @Param("level") Integer level, @Param("name") String name);

    List<OrganizeAccount> selectCurrentOrgAdmin(@Param("groupId") String groupId);
}
