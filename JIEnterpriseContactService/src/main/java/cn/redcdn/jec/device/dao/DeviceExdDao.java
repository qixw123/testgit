package cn.redcdn.jec.device.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.alibaba.fastjson.JSONArray;

import cn.redcdn.jec.common.entity.Device;
import cn.redcdn.jec.device.dto.DeviceListDto;
import cn.redcdn.jec.device.dto.SearchDevicePageDto;
import cn.redcdn.jec.group.dto.ContactInfoDto;
import cn.redcdn.jec.group.dto.SearchContactPageDto;

/**
 * 
 * @author zhang
 *
 */
public interface DeviceExdDao {
	List<String> queryAllNube();

	int insertBatch(@Param("list")List<Device> insDeviceList);

	List<DeviceListDto> getDeviceListWithPage(SearchDevicePageDto pageDto);
	List<DeviceListDto> getDeviceListWitOrgWithPage(SearchDevicePageDto pageDto);

	void deleteBatch(@Param("list")JSONArray ids, @Param("importer") String creator);

	List<DeviceListDto> getControlListWithPage(SearchDevicePageDto pageDto);
	List<DeviceListDto> getControlListWithOrgWithPage(SearchDevicePageDto pageDto);

	List<Device> queryByGroupIds(@Param("list")List<String> groupAndChildrenIds);

	List<ContactInfoDto> getContactListWithPage(SearchContactPageDto pageDto);

	List<Device> queryAll();

	void updateByNubeBatch(List<Device> updDeviceList);

	void updateByGroupIdBatch(@Param("list") List<String> groupAndChildrenIds, 
			@Param("level") Integer level, @Param("name") String name);

	void updateUpgradeBatch(@Param("list") List<String> groupAndChildrenIds, 
			@Param("category") Integer category);

	List<Device> queryAllByImporter(String importer);
}
