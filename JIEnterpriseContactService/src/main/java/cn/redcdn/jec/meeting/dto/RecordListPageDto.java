package cn.redcdn.jec.meeting.dto;

import java.util.List;

/**
 * Created by wulei on 2018/7/16.
 */
public class RecordListPageDto {
    private int totalSize;
    private List<MeetingRecordDto> recordList;

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public List<MeetingRecordDto> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<MeetingRecordDto> recordList) {
        this.recordList = recordList;
    }
}
