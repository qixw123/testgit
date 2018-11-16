package cn.redcdn.jec.meeting.dto;

import java.util.List;

/**
 * Created by wulei on 2018/8/9.
 */

public class GetMeetingUserListDto {
    private int totalSize;
    private int totalAttendNum;
    private List<UserMeetingInfoDto> userList;

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getTotalAttendNum() {
		return totalAttendNum;
	}

	public void setTotalAttendNum(int totalAttendNum) {
		this.totalAttendNum = totalAttendNum;
	}

	public List<UserMeetingInfoDto> getUserList() {
        return userList;
    }

    public void setUserList(List<UserMeetingInfoDto> userList) {
        this.userList = userList;
    }
}
