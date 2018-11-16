/**
 * 南京青牛通讯技术有限公司
 * 日期：$Date: 2018-04-17 18:12:51 +0800 (周二, 17 四月 2018) $
 * 作者：$Author: zhangmy $
 * 版本：$Rev: 1788 $
 * 版权：All rights reserved.
 */
package cn.redcdn.jec.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import cn.redcdn.jec.common.exception.ExternalException;

public class FileUtil
{ 
	
	private final static int BUFFER = 1024 * 8;	
	
	/** 
	 * 数据解压缩 
	 *  
	 * @param is 输入流
     * @param os 输出流
	 */  
	public static void write(InputStream is, OutputStream os) 
	{  
	    BufferedInputStream bis = null;
	    try
	    {
	        BufferedOutputStream bos = new BufferedOutputStream(os);
	        bis = new BufferedInputStream(is);  
		    int count = 0;  
		    byte data[] = new byte[BUFFER];  
		    while ((count = bis.read(data, 0, BUFFER)) != -1) 
		    {  
		        bos.write(data, 0, count);  
		    }  
		    bos.flush();		    
	    }
		catch (Exception e)
		{
			throw new ExternalException();
		} 
		finally
		{
			if (bis != null)
			{
			    try 
			    {
			    	bis.close();
				} 
			    catch (IOException e) 
				{
					throw new ExternalException();
				} 
			}
		}
	}
	
	/** 
	 * 生成文件夹 
	 *  
	 * @param is 输入流
     * @param os 输出流
	 */  
	public static void mkdirs(String destPath) 
	{
		File file = new File(destPath);
		file.mkdirs();
	}
	
	/**
	 * 下载图片到本地
	 * @param urlString 远程图片地址
	 * @param localLogoPath 保存的本地位置
	 * @throws Exception
	 */
	public static boolean download(String urlString, String localLogoPath) {  
		OutputStream os = null;
		InputStream is = null;
		try {
			URL url = new URL(urlString);
			URLConnection con = url.openConnection();
			con.setConnectTimeout(5 * 1000);
			is = con.getInputStream();
			byte[] bs = new byte[1024];
			int len;
			File sf = new File(localLogoPath);
			if (!sf.getParentFile().exists()) {
				sf.getParentFile().mkdirs();
			}
			os = new FileOutputStream(localLogoPath);
			while ((len = is.read(bs)) != -1) {
				os.write(bs, 0, len);
			}
		} catch (Exception e) {
			return false;
		} finally {
			try {
				if (os != null) {
					os.close();
				}if (is != null) {
					is.close();
				}
			} catch (Exception e) {
				return false;
			} 
		}
		
		return true;
    }   
	
	/**
	 * 删除本地图片
	 * @param destPath 文件路径及其名
	 */
	public static void delFile(String destPath) {
		File file = new File(destPath);
		if(file.exists()) {
			file.delete();
		}
	}
	
	/**
	 * 根据文件路径读取输入流
	 * @param path 文件路径
	 * @return 输入流
	 */
	public static FileInputStream readFile(String path) {
		File file = new File(path);
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fin;
	}
	
	/**
	 * 根据输入流读取字节数组
	 * @param inStream 输入流
	 * @return 字节数组
	 */
	public static byte[] readStream(InputStream inStream) {
		ByteArrayOutputStream outStream = null;
		try {
			outStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while((len = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				outStream.close();
				inStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return outStream.toByteArray();
	}
	
}
