package cn.redcdn.jec.meeting.dto;

/**
 * Created by wulei on 2018/7/16.
 */
public class MeetingRecordDto {

    private String name;
    private String nube;
    private Long recordTimeSpan;
    private String endRecordTime;
    private String startRecordTime;
    private String recordFile;

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNube() {
        return nube;
    }

    public void setNube(String nube) {
        this.nube = nube;
    }

    public long getRecordTimeSpan() {
        return recordTimeSpan;
    }

    public void setRecordTimeSpan(Long recordTimeSpan) {
        this.recordTimeSpan = recordTimeSpan;
    }

    public String getEndRecordTime() {
        return endRecordTime;
    }

    public void setEndRecordTime(String endRecordTime) {
        this.endRecordTime = endRecordTime;
    }

    public String getStartRecordTime() {
        return startRecordTime;
    }

    public void setStartRecordTime(String startRecordTime) {
        this.startRecordTime = startRecordTime;
    }

    public String getRecordFile() {
        return recordFile;
    }

    public void setRecordFile(String recordFile) {
        this.recordFile = recordFile;
    }
}
