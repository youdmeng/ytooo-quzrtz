package com.ytooo.quartz.entity;

import lombok.Data;

/**
 * Created by Youdmeng on 2019/6/28 0028.
 */
@Data
public class ThreadExecutorBean {

	/**
	 * 当前行任务
	 */
	private Object task;
	
	/**
	 * 执行任务的bean名称
	 */
	private String beanName;
	
	/**
	 * bean的method
	 */
	private String method;

}
