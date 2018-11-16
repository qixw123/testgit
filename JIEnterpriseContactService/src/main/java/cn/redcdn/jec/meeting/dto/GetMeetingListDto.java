package cn.redcdn.jec.meeting.dto;

import java.util.List;

/**
 * Created by wulei on 2018/7/19.
 */
public class GetMeetingListDto {

    private int totalSize;
    List<MeetingEntDto> meetingList;

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public List<MeetingEntDto> getMeetingList() {
        return meetingList;
    }

    public void setMeetingList(List<MeetingEntDto> meetingList) {
        this.meetingList = meetingList;
    }
}
