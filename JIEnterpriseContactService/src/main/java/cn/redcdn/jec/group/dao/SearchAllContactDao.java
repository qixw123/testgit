package cn.redcdn.jec.group.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.redcdn.jec.contact.dto.DeviceSearchDto;
import cn.redcdn.jec.contact.dto.GroupSearchDto;

public interface SearchAllContactDao {

	List<GroupSearchDto> listGroupByContent(@Param("content") String content, @Param("importer") String importer);

	List<DeviceSearchDto> listDeviceByContent(@Param("content") String content, @Param("importer") String importer);

}
