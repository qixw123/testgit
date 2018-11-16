/**
 * 北京红云融通技术有限公司
 * 日期：$$Date: 2018-09-05 18:00:04 +0800 (周三, 05 九月 2018) $$
 * 作者：$$Author: mengshen $$
 * 版本：$$Rev: 2777 $$
 * 版权：All rights reserved.
 */
package cn.redcdn.jec.contact.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.redcdn.jec.common.entity.EntContact;
import cn.redcdn.jec.contact.dto.ContactGroupNumDto;
import cn.redcdn.jec.contact.dto.DeviceSearchDto;
import cn.redcdn.jec.contact.dto.GroupSearchDto;

public interface ContactExtendDao {
    /**
     * 根据groupId查找数据库中信息
     *
     * @param String groupId 查询的数据信息条件
     * @return Device 符合条件的数据信息
     */
    List<GroupSearchDto> listGroupByNubeAndContent(@Param("nube") String nube, @Param("content") String content);

    /**
     * 根据groupId查找数据库中信息
     *
     * @param String groupId 查询的数据信息条件
     * @return Device 符合条件的数据信息
     */
    List<DeviceSearchDto> listDeviceByNubeAndContent(@Param("nube") String nube, @Param("content") String content);

    /**
     * 根据查找数据库中组织信息
     *
     * @return Device 符合条件的数据信息
     */
    Integer countContactsById(String id);

    /**
     * 根据查找数据库中组织信息
     *
     * @return Device 符合条件的数据信息
     */
    List<ContactGroupNumDto> listGroup(@Param("account") String nube, @Param("id") String id);

    List<ContactGroupNumDto> listGroupWithOrg(@Param("groupId") String groupId, @Param("id") String id);

    /**
     * 根据查找数据库中组织信息
     *
     * @return Device 符合条件的数据信息
     */
    List<ContactGroupNumDto> listGroupDefault(@Param("account") String nube, @Param("id") String id);

    List<ContactGroupNumDto> listGroupDefaultWithOrg(@Param("groupId") String groupId, @Param("id") String id);


    int deleteById(String id);


    void insertBatch(List<EntContact> list);

    /**
     * 根据groupId查找数据库中信息
     *
     * @param String groupId 查询的数据信息条件
     * @return Device 符合条件的数据信息
     */
    List<GroupSearchDto> listDeafultGroupByNubeAndContent(@Param("nube") String nube, @Param("content") String content);

    /**
     * 根据groupId查找数据库中信息
     *
     * @param String groupId 查询的数据信息条件
     * @return Device 符合条件的数据信息
     */
    List<DeviceSearchDto> listDeafulteByNubeAndContent(@Param("nube") String nube, @Param("content") String content);

}