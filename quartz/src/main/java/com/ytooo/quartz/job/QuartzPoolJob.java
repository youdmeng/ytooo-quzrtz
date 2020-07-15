package com.ytooo.quartz.job;

import com.ytooo.context.SpringContext;
import com.ytooo.quartz.entity.ThreadExecutorBean;
import com.ytooo.quartz.enums.QuartzEnum;
import com.ytooo.quartz.util.AysnTask;
import com.ytooo.quartz.util.ExecutorUtil;
import com.ytooo.quartz.util.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

/**
 * 以多线程的方式执行任务
 *
 */
public class QuartzPoolJob extends QuartzJobBean {

	private static final Logger logger = LoggerFactory.getLogger(QuartzPoolJob.class);

	/**
	 * 调用jobmap中的配置的bean.method
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

		String beanName = (String) context.getMergedJobDataMap().get(QuartzEnum.OBJECT_NAME);
		String methodName = (String) context.getMergedJobDataMap().get(QuartzEnum.OBJECT_METHOD);
		String listMethodName = methodName + "Data";
		String finalMethodName = methodName + "Final";
				
		//线程池
		ThreadPoolTaskExecutor threadPool = (ThreadPoolTaskExecutor)
		    context.getJobDetail().getJobDataMap().get(QuartzEnum.THREAD_POOL + QuartzEnum.OBJECT_ID);
		
		/**
		 * 如果线程池为空则初始换线程池，并放到context中
		 */
		if (null == threadPool) {
			int poolSize = Integer.parseInt((String) context.getMergedJobDataMap().get(QuartzEnum.THREAD_POOL_SIZE));
			if (poolSize < 1) {
				poolSize = 1;
			}
			
			//线程等待队列长度
			int queueSize = 0;
			String queueSz = (String) context.getMergedJobDataMap().get(QuartzEnum.QUEUE_CAPACITY);
			if(StringUtils.isNotBlank(queueSz)){
				queueSize = Integer.parseInt(queueSz);
			}
			if (queueSize < 1) {
				queueSize = 500;
			}

			threadPool = ExecutorUtil.createPoolTaskExecutor(poolSize,queueSize);
			context.getJobDetail().getJobDataMap().put(QuartzEnum.THREAD_POOL + QuartzEnum.OBJECT_ID, threadPool);
			logger.info(beanName + ".(" + listMethodName + "," + methodName + ");线程池创建完成");
		}else{
			logger.info(beanName + ".(" + listMethodName + "," + methodName + ");线程池从context中取得");
		}
		
		logger.info(beanName + ".(" + listMethodName + "," + methodName + ");多线程任务执行开始");
		try {
			// 任务列表
			List list = (List) ReflectionUtil.invokeMethod(SpringContext.getBean(beanName), listMethodName, null, null);

			//线程池分配并执行方法, 任务分配应该交给线程池管理。循环处理任务列表
			if (null != list && list.size() > 0) {
				for (int i=0; i<list.size(); i++) {
					logger.info("处理任务ID：" + i);
					ThreadExecutorBean exeDto = new ThreadExecutorBean();
					exeDto.setBeanName(beanName);
					exeDto.setMethod(methodName);
					exeDto.setTask(list.get(i));
					threadPool.execute(new AysnTask(exeDto));
					
					//如果任务数量超过线程池的任务队列长度则超过部分不在本次处理
					//if(i > ExecutorUtil.DEFAULT_QUEUE_CAPACITY){
					//	break;
					//}
				}
			}

			try {
				// 暂停0.5秒, 防止ActiveCount数值不准确问题。当任务量少时可能发生ActiveCount是0,而实际任务线程组中已经有任务情况
				Thread.sleep(500L);
				
				// 查看所有任务是否执行完成				
				while (threadPool.getActiveCount() > 0) {
					// 暂停0.5秒
					Thread.sleep(500L);
				}
			} catch (InterruptedException e) {
				logger.warn("任务执行异常: \n", e);
			}

		} catch (Exception e) {
			logger.warn("批处理调用失败: " + beanName + "." + methodName + "\n", e);
		} finally {
			try {
				// 结束事件
				ReflectionUtil.invokeMethod(SpringContext.getBean(beanName), finalMethodName, null, null);
			} catch (Exception e) {
				logger.warn("批处理Final调用失败: " + beanName + "." + finalMethodName + "\n", e);
			}
		}
		logger.info(beanName + ".(" + listMethodName + "," + methodName + ");多线程任务执行结束");
	}
	
}
