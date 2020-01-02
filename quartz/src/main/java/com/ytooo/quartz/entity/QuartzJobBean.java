package com.ytooo.quartz.entity;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Youdmeng on 2019/6/28 0028.
 */
@Data
public class QuartzJobBean {

	private String jobName;

	private Class jobClass;
	
	private Map<String, Object> jobDataMap = new HashMap<String, Object>();

	private String cronExpression;

	private int repeatCount;

	private long repeatInterval;

	private Date startTime;

	private long startDelay;

}
