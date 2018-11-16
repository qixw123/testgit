/**
 * 北京红云融通技术有限公司
 * 日期：$$Date: 2018-04-22 11:24:46 +0800 (周日, 22 四月 2018) $$
 * 作者：$$Author: zhoulin $$
 * 版本：$$Rev: 1841 $$
 * 版权：All rights reserved.
 */
package cn.redcdn.jec.contact.dto;

/**
 * 
 * @author zhang
 *
 */
public class ContactGroupNumDto {
	
	private String id;

	private String name;

	private Integer num;

	private Integer level;

	private String parentId;

	private Integer checkFlg;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getCheckFlg() {
		return checkFlg;
	}

	public void setCheckFlg(Integer checkFlg) {
		this.checkFlg = checkFlg;
	}

}