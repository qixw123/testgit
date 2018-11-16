package cn.redcdn.jec.contact.dto;

public class DeviceSearchDto
{
	/**
     * gid
     */
    private String gid;

    /**
     * gname
     */
    private String gname;
    
	/**
     * cid
     */
    private String cid;

    /**
     * cname
     */
    private String cname;

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getGname() {
		return gname;
	}

	public void setGname(String gname) {
		this.gname = gname;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}
}
