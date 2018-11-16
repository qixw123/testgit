package cn.redcdn.jec.meeting.dto;

/**
 * Created by wulei on 2018/8/13.
 */
public class UserMeetingInfoDto {
    private String userName;
    private String nube;
    private long lastAttendTime;
    private int attendNum;
    private long totalTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNube() {
        return nube;
    }

    public void setNube(String nube) {
        this.nube = nube;
    }

    public long getLastAttendTime() {
        return lastAttendTime;
    }

    public void setLastAttendTime(long lastAttendTime) {
        this.lastAttendTime = lastAttendTime;
    }

    public int getAttendNum() {
        return attendNum;
    }

    public void setAttendNum(int attendNum) {
        this.attendNum = attendNum;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }
}
