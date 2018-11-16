package cn.redcdn.jec.record.dto;

import java.util.ArrayList;
import java.util.List;

public class SummaryDto
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
     * detailList
     */
    private List<SummaryDetailDto> detailList = new ArrayList<SummaryDetailDto>();

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
	 * @return the detailList
	 */
	public List<SummaryDetailDto> getDetailList() {
		return detailList;
	}

	/**
	 * @param detailList the detailList to set
	 */
	public void setDetailList(List<SummaryDetailDto> detailList) {
		this.detailList = detailList;
	}
}
