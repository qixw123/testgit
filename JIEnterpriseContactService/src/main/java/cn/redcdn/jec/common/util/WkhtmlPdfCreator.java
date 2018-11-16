package cn.redcdn.jec.common.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteWatchdog;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import cn.redcdn.jec.common.exception.ExternalException;

/**
 * @author jillukowicz
 */
@Service
public class WkhtmlPdfCreator {

  private static final Logger logger = LoggerFactory.getLogger(WkhtmlPdfCreator.class);

  
  public void create(WkhtmlRequest request, HttpServletResponse response) {
    List<String> pdfCommand = Arrays.asList(
        "wkhtmltopdf",
        "--disable-smart-shrinking",
        request.getIn(),
        "/home/medical/tomcat8/apps/rcs/webapps/rcs/pdf/test111.pdf"
    );

    ProcessBuilder pb = new ProcessBuilder(pdfCommand);
    Process pdfProcess;

    try {
      pdfProcess = pb.start();

      try (InputStream in = pdfProcess.getInputStream()) {
        pdfToResponse(in, response);
        waitForProcessInCurrentThread(pdfProcess);
        requireSuccessfulExitStatus(pdfProcess);
        setResponseHeaders(response, request);

        logger.info("Wrote PDF file to the response from request: {}", request);
      } catch (Exception ex) {
    	  writeErrorMessageToLog(ex, pdfProcess);
        throw new RuntimeException("PDF generation failed");
      } finally {
        pdfProcess.destroy();
      }
    } catch (IOException ex) {
      logger.error("Could not create a PDF file because of an error occurred: ", ex);
      throw new RuntimeException("PDF generation failed");
    }
  }

  private void pdfToResponse(InputStream in, HttpServletResponse response) throws IOException {
    logger.debug("Writing created PDF file to HTTP response");

    OutputStream out = response.getOutputStream();
    IOUtils.copy(in, out);
    out.flush();
  }

  private void waitForProcessInCurrentThread(Process process) {
    try {
      process.waitFor(5, TimeUnit.SECONDS);
    } catch (InterruptedException ex) {
      Thread.currentThread().interrupt();
    }

    logger.debug("Wkhtmltopdf ended");
  }

  private void requireSuccessfulExitStatus(Process process) {
    if (process.exitValue() != 0) {
      throw new RuntimeException("PDF generation failed");
    }
  }

  private void setResponseHeaders(HttpServletResponse response, WkhtmlRequest request) {
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=\"" + request.getOut() + "\"");
  }

  private void writeErrorMessageToLog(Exception ex, Process pdfProcess) throws IOException {
    logger.error("Could not create PDF because an exception was thrown: ", ex);
    logger.error("The exit value of PDF process is: {}", pdfProcess.exitValue());

    String errorMessage = getErrorMessageFromProcess(pdfProcess);
    logger.error("PDF process ended with error message: {}", errorMessage);
  }

  private String getErrorMessageFromProcess(Process pdfProcess) {
    try {
      BufferedReader reader = new BufferedReader(new InputStreamReader(pdfProcess.getErrorStream()));
      StringWriter writer = new StringWriter();

      String line;
      while ((line = reader.readLine()) != null) {
        writer.append(line);
      }

      return writer.toString();
    } catch (IOException ex) {
      logger.error("Could not extract error message from process because an exception was thrown", ex);
      return "";
    }
  }
  
  public byte[] getPDFByURL(String url) throws IOException, InterruptedException {

      CommandLine cmdLine = new CommandLine("wkhtmltopdf");
//      cmdLine.addArgument("--disable-smart-shrinking");
      cmdLine.addArgument("--margin-right");
      cmdLine.addArgument("0cm");
      cmdLine.addArgument("--margin-left");
      cmdLine.addArgument("0cm");
//      cmdLine.addArgument("--page-width");
//      cmdLine.addArgument("300mm");
      cmdLine.addArgument(url);
      cmdLine.addArgument("--print-media-type");
      cmdLine.addArgument("-");
      return getDocument(cmdLine);
	
  }

  private byte[] getDocument(CommandLine cmdLine) throws IOException {
      DefaultExecutor executor = new DefaultExecutor();
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
      PumpStreamHandler pumpStreamHandler = new PumpStreamHandler(outputStream, errorStream);
      ExecuteWatchdog watchdog = new ExecuteWatchdog(300 * 1000);
      executor.setWatchdog(watchdog);
      executor.setExitValues(null);
      executor.setStreamHandler(pumpStreamHandler);
      executor.execute(cmdLine);
      return outputStream.toByteArray();
  }
  
  /**
   * 保持PDF文件到path中
   *
   * @param path
   * @param document
   * @return
   * @throws IOException
   */
  public File saveAs(String path, byte[] document) throws IOException {
      File file = new File(path);
      //判断目标文件所在的目录是否存在  
      if(!file.getParentFile().exists()) {  
          //如果目标文件所在的目录不存在，则创建父目录  
    	  logger.info("目标文件所在目录不存在，准备创建它！");  
          if(!file.getParentFile().mkdirs()) {  
        	  logger.error("创建目标文件所在目录失败！");  
              throw new ExternalException();  
          }  
      }  
      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
      bufferedOutputStream.write(document);
      bufferedOutputStream.flush();
      bufferedOutputStream.close();
      return file;
  }

}
