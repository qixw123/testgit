/**
 * 北京红云融通技术有限公司
 * 日期：$$Date: 2018-04-19 16:51:58 +0800 (周四, 19 四月 2018) $$
 * 作者：$$Author: zhoulin $$
 * 版本：$$Rev: 1819 $$
 * 版权：All rights reserved.
 */
package cn.redcdn.jec.common.dto;

/**
 * 
 * @author zhang
 *
 */
public class ContactDetailDto {
	
	private String fid;

	private String fname;
	
	private String sid;

	private String sname;
	
	private String tid;

	private String tname;

	private Integer tlevel;

	public String getFid() {
		return fid;
	}

	public void setFid(String fid) {
		this.fid = fid;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getTname() {
		return tname;
	}

	public void setTname(String tname) {
		this.tname = tname;
	}

	public Integer getTlevel() {
		return tlevel;
	}

	public void setTlevel(Integer tlevel) {
		this.tlevel = tlevel;
	}

}