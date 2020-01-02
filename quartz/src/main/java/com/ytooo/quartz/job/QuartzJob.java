package com.ytooo.quartz.job;

import com.ytooo.context.SpringContext;
import com.ytooo.quartz.util.ReflectionUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 工具类：调用具体批处理业务类,串行执行
 * Created by Youdmeng on 2019/6/28 0028.
 */
public class QuartzJob extends QuartzJobBean implements StatefulJob {

	private static final  Logger logger = LoggerFactory.getLogger(QuartzJob.class);

	/**
	 * key 对象标识
	 */
	public static final String OBJECT_NAME = "beanName";

	/**
	 * key beanId
	 */
	public static final String OBJECT_ID = "beanId";

	/**
	 * key 对象方法
	 */
	public static final String OBJECT_METHOD = "methodName";

	/**
	 * 调用jobmap中的配置的bean.method
	 */
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		String beanName = (String) context.getMergedJobDataMap().get(OBJECT_NAME);
		String methodName = (String) context.getMergedJobDataMap().get(OBJECT_METHOD);
		try {
			ReflectionUtil.invokeMethod(
                    SpringContext.getBean("testBatch"),
					methodName, null, null);
		} catch (Exception e) {
			 logger.warn("批处理调用失败: " + beanName +"." + methodName + "\n" , e);
		}
	}

}
