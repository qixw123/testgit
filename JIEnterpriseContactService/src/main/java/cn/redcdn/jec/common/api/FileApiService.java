package cn.redcdn.jec.common.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.redcdn.jec.common.dto.MessageInfoDto;
import cn.redcdn.jec.common.exception.ExternalServiceException;
import cn.redcdn.jec.common.util.MessageUtil;

@Service
public class FileApiService extends BaseApiService {
	
	private static Logger logger = LoggerFactory.getLogger(FileApiService.class);

	/**
	 * 将数据写入指定TXT文件里
	 * @param filePath
	 * @param data
	 */
	public void writeTXT(String filePath, String data) {
		try {
            File writeFile = new File(filePath);
            //判断目标文件所在的目录是否存在
            if (!writeFile.getParentFile().exists()) {
                writeFile.getParentFile().mkdirs();
            }
            BufferedWriter out = new BufferedWriter(new FileWriter(writeFile));
            out.write(data);
            out.flush(); // 把缓存区内容压入文件
            out.close(); // 最后记得关闭文件
        } catch (Exception e) {
            logger.error("写入TXT文件出现问题");
            MessageInfoDto messageInfo = MessageUtil.getMessageInfoByKey("system.error");
            throw new ExternalServiceException(messageInfo);
        }
	}

	public void downloadFile(HttpServletResponse response, String filePath, String fileName) {
		response.setContentType("multipart/form-data");
		response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
		try {
			InputStream inputStream = new FileInputStream(filePath);
			byte[] tempBytes = new byte[2048];
			while (inputStream.read(tempBytes) != -1) {
				response.getOutputStream().write(tempBytes);
			}
			inputStream.close();
		} catch (IOException e) {
			throw new ExternalServiceException();
		} finally {
			try {
				OutputStream outputStream = response.getOutputStream();
				outputStream.close();
				outputStream.flush();
			} catch (IOException e) {
				throw new ExternalServiceException();
			}
		}
		
	}

}
