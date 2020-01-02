package com.ytooo.quartz.util;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 *
 * Created by Youdmeng on 2019/6/28 0028.
 */
public class ExecutorUtil {
	
	/**
	 * 线程最大空闲时间, 单位毫秒
	 */
	public final static int  DEFAULT_KEEP_ALIVE_SECONDS = 60000;
	
	/**
	 * 生成线程池对象
	 * @param maxcount
	 * @param queueSize
	 * @return
	 */
	public static ThreadPoolTaskExecutor createPoolTaskExecutor(int maxcount, int queueSize) {
		ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
		// 线程池所使用的缓冲队列
		poolTaskExecutor.setQueueCapacity(queueSize);
		
		//线程池维护线程的最大数量,如果线程数量小于1则设置其为1
		if(maxcount < 1 ){
			maxcount = 1;
		}
		
		// 线程池维护线程的数量
		poolTaskExecutor.setCorePoolSize(maxcount);
		
		poolTaskExecutor.setMaxPoolSize(maxcount);
		
		/*
		 * 线程池维护线程所允许的空闲时间,,当线程池中的线程数量大于 corePoolSize时，
		 * 如果某线程空闲时间超过keepAliveTime，线程将被终止。这样，线程池可以动态的调整池中的线程数
		 */
		poolTaskExecutor.setKeepAliveSeconds(DEFAULT_KEEP_ALIVE_SECONDS);
		
		poolTaskExecutor.initialize();
		return poolTaskExecutor;
	}
}
