package cn.redcdn.jec.meeting.dto;

import java.util.List;

/**
 * Created by wulei on 2018/8/13.
 */
public class GetUserInviterRecordDto {
    private int totalSize;
    private List<UserInviterDto> recordList;

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public List<UserInviterDto> getRecordList() {
        return recordList;
    }

    public void setRecordList(List<UserInviterDto> recordList) {
        this.recordList = recordList;
    }
}
