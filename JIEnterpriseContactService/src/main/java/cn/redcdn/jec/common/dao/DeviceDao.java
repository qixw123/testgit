/**
 * 北京红云融通技术有限公司
 * 日期：$$Date: 2018-04-17 18:12:51 +0800 (周二, 17 四月 2018) $$
 * 作者：$$Author: zhangmy $$
 * 版本：$$Rev: 1788 $$
 * 版权：All rights reserved.
 */
package cn.redcdn.jec.common.dao;

import cn.redcdn.jec.common.entity.Device;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface DeviceDao
{
    /**
     * 将数据插入数据库表中
     * 
     * @param device 插入的数据信息
     */
    void insert(Device device);

    /**
     * 根据key查找数据库中信息
     * 
     * @param String id 查询的数据信息条件
     * @return Device 符合条件的数据信息
     */
    Device queryByKey(String id);

    /**
     * 根据key更新数据库中信息
     * 
     * @param device 更新的数据信息
     * @return 更新数据的行数
     */
    int updateByKey(Device device);

    /**
     * 根据key删除数据库中信息
     * 
     * @param String id 删除的数据信息
     * @return 删除数据的行数
     */
    int deleteByKey(String id);
    /**
     * 根据field查询据库中信息
     * 
     * @param String fieldName 查询的字段名信息
     * @param String fieldValue 查询的字段的数据信息
     * @return List<Device> 符合条件的数据信息
     */
    List<Device> queryByField(@Param("fieldName") String fieldName, @Param("fieldValue") String fieldValue);

}