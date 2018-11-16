package cn.redcdn.jec.meeting.dto;

/**
 * Created by wulei on 2018/8/13.
 */
public class UserInviterDto {
    private long startTime;
    private long endTime;
    private long timeSpan;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public long getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(long timeSpan) {
        this.timeSpan = timeSpan;
    }
}
