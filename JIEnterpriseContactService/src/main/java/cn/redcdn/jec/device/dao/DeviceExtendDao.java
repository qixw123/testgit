/**
 * 北京红云融通技术有限公司
 * 日期：$$Date: 2018-05-07 15:28:09 +0800 (周一, 07 五月 2018) $$
 * 作者：$$Author: zhoulin $$
 * 版本：$$Rev: 2065 $$
 * 版权：All rights reserved.
 */
package cn.redcdn.jec.device.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.redcdn.jec.common.entity.Device;
import cn.redcdn.jec.group.dto.ChildContactDto;
import cn.redcdn.jec.group.dto.ContactDto;

public interface DeviceExtendDao
{

    /**
     * 根据nube查找数据库中信息
     * 
     * @param String nube 查询的数据信息条件
     * @return Device 符合条件的数据信息
     */
    List<Device> queryAllByNube(String nube);

    /**
     * 根据nube查找数据库中信息
     * 
     * @param String nube 查询的数据信息条件
     * @return Device 符合条件的数据信息
     */
    List<Device> queryDeaultByNube(String nube);

    /**
     * 根据nube查找数据库中信息
     * 
     * @param String nube 查询的数据信息条件
     * @return Device 符合条件的数据信息
     */
    Device queryByNube(String nube);

    /**
     * 根据groupId查找数据库中信息
     * 
     * @param String groupId 查询的数据信息条件
     * @return Device 符合条件的数据信息
     */
    List<ContactDto> queryByNubeAndGroupId(@Param("nube") String nube, @Param("groupId")String groupId);
    
    /**
     * 根据groupId查找数据库中信息
     * 
     * @param String groupId 查询的数据信息条件
     * @return Device 符合条件的数据信息
     */
    List<ChildContactDto> getFirstSumByNubeAndGroupId(@Param("nube") String nube, @Param("groupId")String groupId);
    
    /**
     * 根据groupId查找数据库中信息
     * 
     * @param String groupId 查询的数据信息条件
     * @return Device 符合条件的数据信息
     */
    List<ChildContactDto> getSecondSumByNubeAndGroupId(@Param("nube") String nube, @Param("groupId")String groupId);
}