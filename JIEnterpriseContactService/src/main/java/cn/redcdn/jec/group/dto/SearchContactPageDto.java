package cn.redcdn.jec.group.dto;

import cn.redcdn.jec.common.dto.PageInfoDto;

public class SearchContactPageDto extends PageInfoDto {
	
	/**
	 * 搜索内容
	 */
	private String content;
	
	/**
	 * 直属组织
	 */
	private String groupId;
	
	/**
	 * 导入者
	 */
	private String importer;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getImporter() {
		return importer;
	}

	public void setImporter(String importer) {
		this.importer = importer;
	}

}
