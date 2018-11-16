package cn.redcdn.jec.group.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.redcdn.jec.common.entity.GroupInfo;

public interface GroupExtendDao {

	List<GroupInfo> queryAll(String importer);

	void insertBatch(@Param("list") List<GroupInfo> groupList);

	List<String> queryByNameLevelParent(@Param("name") String groupName, @Param("level") int groupLevel,
			@Param("parentId") String parentId, @Param("importer")String importer);

	List<GroupInfo> queryByLevelParent(@Param("level") Integer level, @Param("parentId") String parentId, 
			@Param("importer")String importer);

	List<String> querySiblingName(@Param("id")String id, @Param("parentId")String parentId, 
			@Param("importer")String importer);

	List<GroupInfo> queryChildren(@Param("parentId")String parentId);
	
	void deleteBatch(@Param("list")List<String> groupAndChildrenIds);

	GroupInfo getGrandParent(String id);

	void updateBatch(List<GroupInfo> groupAndChildren);

	List<GroupInfo> queryAllPosterity(String groupId);

	List<GroupInfo> queryByContactDeviceId(String id);
    
    List<GroupInfo> listAllGroup(String account);
    
    List<GroupInfo> queryFirstChildren(@Param("importer")String importer);
}
