package com.ytooo.quartz.util;

import com.ytooo.quartz.entity.QuartzJobBean;
import com.ytooo.quartz.job.QuartzJob;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 *
 * Created by Youdmeng on 2019/6/28 0028.
 **/
@Component
public class QuartzUtil {

	@Autowired
	private Scheduler scheduler;

	public void scheduleCronJob(QuartzJobBean quartzJobDTO) throws Exception {
    	// 创建一个job
		JobDetail jobDetail = createJobDetail(quartzJobDTO);
		CronTriggerFactoryBean triggerFactory = new CronTriggerFactoryBean();
		triggerFactory.setCronExpression(quartzJobDTO.getCronExpression());
		triggerFactory.setName(quartzJobDTO.getJobName());
		triggerFactory.setJobDetail(jobDetail);
		triggerFactory.setCronExpression(quartzJobDTO.getCronExpression());
		triggerFactory.afterPropertiesSet();
		scheduler.scheduleJob(jobDetail,triggerFactory.getObject());
	}

	public void cancelJob(JobKey jobKey) throws Exception {
		scheduler.deleteJob(jobKey);
	}

	public Set<JobKey>  getJobKeys() throws Exception {
		return scheduler.getJobKeys(GroupMatcher.jobGroupEquals(Scheduler.DEFAULT_GROUP));
	}

	/**
	 * 新建一个JobDetail
	 * @param quartzJobDTO
	 * @return
	 */
	private JobDetail createJobDetail(QuartzJobBean quartzJobDTO) throws NoSuchMethodException, ClassNotFoundException {
		//创建一个job
		JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean ();
		jobDetailFactory.setName(quartzJobDTO.getJobName());                   //job类命名
		jobDetailFactory.setJobDataAsMap(quartzJobDTO.getJobDataMap());
		jobDetailFactory.setJobClass(QuartzJob.class);      //JobClass，job执行类
		jobDetailFactory.afterPropertiesSet();
		return  jobDetailFactory.getObject();
	}
}
