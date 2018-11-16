package cn.redcdn.jec.record.dto;

public class SummaryDetailDto
{
	/**
     * fileName
     */
    private String fileName;
    
	/**
     * fileAdress
     */
    private String fileAdress;
    
	/**
     * fileAdress
     */
    private long createTime;

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileAdress
	 */
	public String getFileAdress() {
		return fileAdress;
	}

	/**
	 * @param fileAdress the fileAdress to set
	 */
	public void setFileAdress(String fileAdress) {
		this.fileAdress = fileAdress;
	}

	/**
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
