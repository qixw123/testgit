package cn.redcdn.jec.common.exception;

import cn.redcdn.jec.common.dto.MessageInfoDto;

public class ExternalServiceException extends RuntimeException
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2665640036436939863L;
	
	private MessageInfoDto messageInfo;

	public ExternalServiceException()
	{
		super();
	}

	public ExternalServiceException(MessageInfoDto messageInfo)
	{
		super(messageInfo.getMsg());
		this.messageInfo = messageInfo;
	}

	/**
	 * getter method
	 * @return Returns the messageInfo.
	 */
	public MessageInfoDto getMessageInfo() {
		return messageInfo;
	}

	/**
	 * setter method
	 * @param messageInfo The messageInfo to set.
	 */
	public void setMessageInfo(MessageInfoDto messageInfo) {
		this.messageInfo = messageInfo;
	}
}
