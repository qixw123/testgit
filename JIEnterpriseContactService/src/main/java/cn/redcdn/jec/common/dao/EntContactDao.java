/**
 * 北京红云融通技术有限公司
 * 日期：$$Date: 2018-04-18 18:45:10 +0800 (周三, 18 四月 2018) $$
 * 作者：$$Author: zhoulin $$
 * 版本：$$Rev: 1800 $$
 * 版权：All rights reserved.
 */
package cn.redcdn.jec.common.dao;

import cn.redcdn.jec.common.entity.EntContact;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface EntContactDao
{
    /**
     * 将数据插入数据库表中
     * 
     * @param entContact 插入的数据信息
     */
    void insert(EntContact entContact);

    /**
     * 根据key查找数据库中信息
     * 
     * @param String id 查询的数据信息条件
     * @return EntContact 符合条件的数据信息
     */
    EntContact queryByKey(String id);

    /**
     * 根据key更新数据库中信息
     * 
     * @param entContact 更新的数据信息
     * @return 更新数据的行数
     */
    int updateByKey(EntContact entContact);

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
     * @return List<EntContact> 符合条件的数据信息
     */
    List<EntContact> queryByField(@Param("fieldName") String fieldName, @Param("fieldValue") String fieldValue);

}