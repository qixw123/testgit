/**
 * 南京青牛通讯技术有限公司
 * 日期：$Date: 2018-04-17 18:12:51 +0800 (周二, 17 四月 2018) $
 * 作者：$Author: zhangmy $
 * 版本：$Rev: 1788 $
 * 版权：All rights reserved.
 */
package cn.redcdn.jec.common.util;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import cn.redcdn.jec.common.exception.ExternalException;

public class QRFileUtil
{ 	

	private static Logger logger = LoggerFactory.getLogger(QRFileUtil.class);
	
	private static final String CHARSET = "utf-8";  
    private static final String FORMAT_NAME = "png";  
    // 二维码尺寸  
    private static final int QRCODE_SIZE = 420;  
    // LOGO宽度  
    private static final int WIDTH = 120;  
    // LOGO高度  
    private static final int HEIGHT = 120;  
  
    private static BufferedImage createImage(String content, String imgPath, boolean needCompress)  {  
        Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();  
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);  
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);  
        hints.put(EncodeHintType.MARGIN, 1);  
        try 
        {
	        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,  
	                BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);  
	        
	        // 去白边
	        int[] rec = bitMatrix.getEnclosingRectangle();  
	        int resWidth = rec[2] + 1;  
	        int resHeight = rec[3] + 1;  
	        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);  
	        resMatrix.clear();  
	        for (int i = 0; i < resWidth; i++) {  
	            for (int j = 0; j < resHeight; j++) {  
	                if (bitMatrix.get(i + rec[0], j + rec[1])) { 
	                     resMatrix.set(i, j); 
	                } 
	            }  
	        }  
	        
	        int width = resMatrix.getWidth();  
	        int height = resMatrix.getHeight();  
	        BufferedImage image = new BufferedImage(width, height,  
	                BufferedImage.TYPE_INT_RGB);  
	        for (int x = 0; x < width; x++) {  
	            for (int y = 0; y < height; y++) {  
	                image.setRGB(x, y, resMatrix.get(x, y) ? 0xFF000000  
	                        : 0xFFFFFFFF);  
	            }  
	        }  
	        if (imgPath == null || "".equals(imgPath)) {  
	            return image;  
	        }  
	        // 插入图片  
	        insertImage(image, imgPath, needCompress);  
	        return image;  
        } catch(Exception e)
        {
        	logger.error("createImage", e);
        	throw new ExternalException();
        }
    }  
  
    /** 
     * 插入LOGO 
     *  
     * @param source 
     *            二维码图片 
     * @param imgPath 
     *            LOGO图片地址 
     * @param needCompress 
     *            是否压缩 
     * @throws Exception 
     */  
    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) 
    {  
    	File imgPathFile = new File(imgPath);
		// 不含路径的文件名
		File cutPathFile = new File(imgPathFile.getParent(), "cut" + imgPathFile.getName()); 
        cutPic(imgPath, cutPathFile.getPath());
        if (!cutPathFile.exists()) {  
            return;  
        }  
        try
        {
	        Image src = ImageIO.read(cutPathFile);  
	        int width = src.getWidth(null);  
	        int height = src.getHeight(null);  
	        if (needCompress) { // 压缩LOGO  
	            if (width > WIDTH) {  
	                width = WIDTH;  
	            }  
	            if (height > HEIGHT) {  
	                height = HEIGHT;  
	            }  
	            
	            Image image = src.getScaledInstance(width, height,  
	                    Image.SCALE_SMOOTH);  
	            BufferedImage tag = new BufferedImage(width, height,  
	                    BufferedImage.TYPE_INT_RGB);  
	            Graphics g = tag.getGraphics();  
	            g.drawImage(image, 0, 0, null); // 绘制缩小后的图  
	            g.dispose();  
	            src = image;  
	        }  
	        // 插入LOGO  
	        Graphics2D graph = source.createGraphics();  
	        int x = (source.getWidth() - width) / 2;  
	        int y = (source.getHeight() - height) / 2;  
	        graph.drawImage(src, x, y, width, height, null);  
	        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);  
	        graph.setStroke(new BasicStroke(3f));  
	        graph.draw(shape);  
	        graph.dispose();  
        } catch(Exception e)
        {
        	logger.error("insertImage", e);
        	throw new ExternalException();
        }
        // 裁剪图片删除
        cutPathFile.delete();
    }
    
    /** 
     * 生成二维码(内嵌LOGO) 
     *  
     * @param content 
     *            内容 
     * @param imgPath 
     *            LOGO地址 
     * @param destPath 
     *            存储地址 
     * @param needCompress 
     *            是否压缩LOGO 
     * @throws Exception 
     */  
    public static void encode(String content, String imgPath, String destPath, boolean needCompress)  
    {  
        try
        {
		    BufferedImage image = createImage(content, imgPath, needCompress);  
		    FileUtil.mkdirs(destPath);  
        	ImageIO.write(image, FORMAT_NAME, new File(destPath)); 

        } catch(Exception e)
        {
        	logger.error("encode", e);
        	throw new ExternalException();
        }
    }
  
    /** 
     * 生成二维码(内嵌LOGO) 
     *  
     * @param content 
     *            内容 
     * @param imgPath 
     *            LOGO地址 
     * @param destPath 
     *            存储地址 
     * @throws Exception 
     */  
    public static void encode(String content, String imgPath, String destPath)  
    {  
        encode(content, imgPath, destPath, false);  
    }  
  
    /** 
     * 生成二维码 
     *  
     * @param content 
     *            内容 
     * @param destPath 
     *            存储地址 
     * @param needCompress 
     *            是否压缩LOGO 
     * @throws Exception 
     */  
    public static void encode(String content, String destPath,  
            boolean needCompress) 
    {  
        encode(content, null, destPath, needCompress);  
    }  
  
    /** 
     * 生成二维码 
     *  
     * @param content 
     *            内容 
     * @param destPath 
     *            存储地址 
     * @throws Exception 
     */  
    public static void encode(String content, String destPath) 
    {  
        encode(content, null, destPath, false);  
    }  
  
    /** 
     * 生成二维码(内嵌LOGO) 
     *  
     * @param content 
     *            内容 
     * @param imgPath 
     *            LOGO地址 
     * @param output 
     *            输出流 
     * @param needCompress 
     *            是否压缩LOGO 
     * @throws Exception 
     */  
    public static void encode(String content, String imgPath,  
            OutputStream output, boolean needCompress)
    {  
    	try
    	{
	        BufferedImage image = createImage(content, imgPath,  
	                needCompress);  
	        ImageIO.write(image, FORMAT_NAME, output);  
    	} catch(Exception e)
        {
        	throw new ExternalException();
        }
    }  
  
    /** 
     * 生成二维码 
     *  
     * @param content 
     *            内容 
     * @param output 
     *            输出流 
     * @throws Exception 
     */  
    public static void encode(String content, OutputStream output)  
    {  
        encode(content, null, output, false);  
    }  
  
    /** 
     * 解析二维码 
     *  
     * @param file 
     *            二维码图片 
     * @return 
     * @throws Exception 
     */  
    public static String decode(File file) {  
    	try
    	{
	        BufferedImage image = ImageIO.read(file);  
	        if (image == null) {  
	            return null;  
	        }  
	        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(  
	                image);  
	        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));  
	        Result result;  
	        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>();  
	        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);  
	        result = new MultiFormatReader().decode(bitmap, hints);  
	        String resultStr = result.getText();  
	        return resultStr;  
    	} catch(Exception e)
        {
        	throw new ExternalException();
        }
    }
    
    /** 
     * @param srcFile源文件 
     * @param outFile输出文件 
     * @param x坐标 
     * @param y坐标 
     * @param width宽度 
     * @param height高度 
     * @return 
     * @作者 王建明 
     * @创建日期 2012-8-2 
     * @创建时间 下午02:05:03 
     * @描述 —— 裁剪图片 
     */  
    public static void cutPic(String srcFile, String outFile) {  
        FileInputStream is = null;  
        ImageInputStream iis = null;  
        try {  
            // 如果源图片不存在  
            if (!new File(srcFile).exists()) {  
                return;  
            }  
            // 读取图片文件  
            is = new FileInputStream(srcFile);
            // 获取文件格式  
            String ext = srcFile.substring(srcFile.lastIndexOf(".") + 1);  
            // ImageReader声称能够解码指定格式  
            Iterator<ImageReader> it = ImageIO.getImageReaders(new FileImageInputStream(new File(srcFile)));
            ImageReader reader = it.next();  
            // 获取图片流  
            iis = ImageIO.createImageInputStream(is);  
            // 输入源中的图像将只按顺序读取  
            reader.setInput(iis, true);  
            // 描述如何对流进行解码  
            ImageReadParam param = reader.getDefaultReadParam();  
            // 图片裁剪区域
            int orgWidth = reader.getWidth(0);
            int orgHeight = reader.getHeight(0);
            Rectangle rect = null;
            if (orgWidth > orgHeight)
            {
            	rect = new Rectangle((orgWidth - orgHeight)  / 2, 0,  orgHeight, orgHeight);  
            }
            else
            {
            	rect = new Rectangle(0, (orgHeight- orgWidth) / 2,  orgWidth, orgWidth);  
            }
            // 提供一个 BufferedImage，将其用作解码像素数据的目标  
            param.setSourceRegion(rect);  
            // 使用所提供的 ImageReadParam 读取通过索引 imageIndex 指定的对象  
            BufferedImage bi = reader.read(0, param);  
  
            // 保存新图片  
            File tempOutFile = new File(outFile);  
            if (!tempOutFile.exists()) {  
                tempOutFile.mkdirs();  
            }  
            ImageIO.write(bi, ext, new File(outFile));  
        } catch (Exception e) { 
        	logger.error("cutPic", e);
        	throw new ExternalException();
        } finally {  
            try {  
                if (is != null) {  
                    is.close();  
                }  
                if (iis != null) {  
                    iis.close();  
                }  
            } catch (IOException e) {  
            	logger.error("cutPic", e);
            	throw new ExternalException();
            }  
        }  
    }
}  
