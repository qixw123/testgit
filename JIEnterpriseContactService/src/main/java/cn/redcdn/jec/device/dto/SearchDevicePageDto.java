package cn.redcdn.jec.device.dto;

import cn.redcdn.jec.common.dto.PageInfoDto;

public class SearchDevicePageDto extends PageInfoDto {

    private String groupId;

    private String importer;
    /**
     * 搜索类型
     */
    private String searchType;

    /**
     * 搜索内容
     */
    private String content;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImporter() {
        return importer;
    }

    public void setImporter(String importer) {
        this.importer = importer;
    }

}
