/**
 * 北京红云融通技术有限公司
 * 日期：$$Date: 2018-04-24 16:50:19 +0800 (周二, 24 四月 2018) $$
 * 作者：$$Author: zhoulin $$
 * 版本：$$Rev: 1871 $$
 * 版权：All rights reserved.
 */
package cn.redcdn.jec.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.redcdn.jec.common.dto.ContactDetailDto;

public interface ContactCommonDao
{
    /**
     * 根据查找数据库中组织信息
     * 
     * @return Device 符合条件的数据信息
     */
    Integer countContactsByNube(String nube);
    
    /**
     * 根据groupId查找数据库中信息
     * 
     * @param String groupId 查询的数据信息条件
     * @return Device 符合条件的数据信息
     */
    List<ContactDetailDto> listGroupByNube(@Param("nube") String nube);
    
    /**
     * 根据groupId查找数据库中信息
     * 
     * @param String groupId 查询的数据信息条件
     * @return Device 符合条件的数据信息
     */
    List<ContactDetailDto> listGroupDefaultByNube(@Param("nube") String nube);
}