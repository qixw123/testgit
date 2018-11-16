package cn.redcdn.jec.common.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <dl>
 * <dt>CacheOperateThreadPool.java</dt>
 * <dd>Description:内存数据处理线程池</dd>
 * <dd>Company: 安徽青牛信息技术有限公司</dd>
 * <dd>CreateDate: 2014-11-27</dd>
 * </dl>
 * 
 * @author yusy
 */
public class ThreadExecutor { 

	private static Logger logger = LoggerFactory.getLogger(ThreadExecutor.class);
	
	private static ExecutorService service = Executors.newCachedThreadPool();
	
	public static void excute(final CallBack callBack)
	{
		service.execute(new Runnable() {  
			 @Override  
             public void run() {  
				 try {
					 callBack.process();
				 } catch(Exception e) {
					 logger.error("ThreadExecutor error :", e);
				 }
			 }
		});
	}
}
