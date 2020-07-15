package com.ytooo.quartz.job;

import com.ytooo.context.SpringContext;
import com.ytooo.quartz.enums.QuartzEnum;
import com.ytooo.quartz.util.ReflectionUtil;
import org.quartz.DisallowConcurrentExecution;
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
@DisallowConcurrentExecution //禁止同一个 JobDetail 中的多个实例并发执行
public class QuartzJob extends QuartzJobBean {

	private static final  Logger logger = LoggerFactory.getLogger(QuartzJob.class);



	/**
	 * 调用jobmap中的配置的bean.method
	 */
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		String beanName = (String) context.getMergedJobDataMap().get(QuartzEnum.OBJECT_NAME);
		String methodName = (String) context.getMergedJobDataMap().get(QuartzEnum.OBJECT_METHOD);
		try {
			ReflectionUtil.invokeMethod(
                    SpringContext.getBean(beanName),
					methodName, null, null);
		} catch (Exception e) {
			 logger.warn("批处理调用失败: " + beanName +"." + methodName + "\n" , e);
		}
	}

}
