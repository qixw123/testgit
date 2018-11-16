package cn.redcdn.jec.meeting.dto;

import java.util.List;

/**
 *  Created by qixw on 2018/9/27.
 */
public class GetAppointmentMeetingListDto {
	private int totalSize;
	private List<AppointmentMeetingEntDto > meetingList;
	public int getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
	public List<AppointmentMeetingEntDto> getMeetingList() {
		return meetingList;
	}
	public void setMeetingList(List<AppointmentMeetingEntDto> meetingList) {
		this.meetingList = meetingList;
	}
	
	


}
