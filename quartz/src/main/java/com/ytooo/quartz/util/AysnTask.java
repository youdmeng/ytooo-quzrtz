package com.ytooo.quartz.util;

import com.ytooo.context.SpringContext;
import com.ytooo.quartz.entity.ThreadExecutorBean;
import com.ytooo.quartz.job.QuartzSectionPoolJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Created by Youdmeng on 2019/6/28 0028.
 */
public class AysnTask implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(QuartzSectionPoolJob.class);

	private ThreadExecutorBean dto;

	public AysnTask(ThreadExecutorBean exeDto) {
		this.dto = exeDto;
	}

	@SuppressWarnings("unchecked")
	public void run() {
		String beanName = dto.getBeanName();
		String methodName = dto.getMethod();
		Object[] parameters = { dto.getTask() };
		Class[] clazz = { dto.getTask().getClass() };

		logger.info(Thread.currentThread() + ", " + beanName + "." + methodName + "开始");
		try {
			// 调用任务执行
			ReflectionUtil.invokeMethod(SpringContext.getBean(beanName), methodName, clazz, parameters);
		} catch (Exception e) {
			logger.warn("任务执行失败: " + beanName + "." + methodName + "\n", e);
		}

		logger.info(Thread.currentThread() + ", " + beanName + "." + methodName + "结束");
	}
}
