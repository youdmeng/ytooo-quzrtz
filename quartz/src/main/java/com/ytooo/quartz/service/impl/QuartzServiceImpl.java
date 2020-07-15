package com.ytooo.quartz.service.impl;

import com.ytooo.quartz.entity.QuartzJobBean;
import com.ytooo.quartz.enums.QuartzEnum;
import com.ytooo.quartz.job.QuartzJob;
import com.ytooo.quartz.job.QuartzPoolJob;
import com.ytooo.quartz.job.QuartzSectionPoolJob;
import com.ytooo.quartz.service.QuartzService;
import com.ytooo.quartz.util.QuartzUtil;
import com.ytooo.repository.QuartzInfo;
import com.ytooo.repository.QuartzInfoExample;
import com.ytooo.repository.dao.QuartzInfoDao;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by Youdmeng on 2019/6/28 0028.
 */
@Service
public class QuartzServiceImpl implements QuartzService {
    private static final Logger logger = LoggerFactory.getLogger(QuartzServiceImpl.class);

    @Autowired
    private QuartzUtil quartzUtil;

    @Autowired
    private QuartzInfoDao quartzInfoDao;

    @Value("${batch.run.key}")
    private String runKey;

    public void initScheduler() throws Exception {

//        QuartzInfoEOPage page = new QuartzInfoEOPage();
        QuartzInfoExample example = new QuartzInfoExample();
        example.createCriteria().andIsDeleteEqualTo(0);
        List<QuartzInfo> batchList = quartzInfoDao.selectByExample(example);
        if (batchList.size() <= 0) {
            logger.info("[未发现批处理],获取批处理列表大小为0");
        }
        //逐笔设置任务信息到quartz,并调度任务
        if (batchList.size() > 0) {
            for (QuartzInfo batchInfo : batchList) {
                QuartzJobBean job = new QuartzJobBean();
                job.setCronExpression(batchInfo.getExecuteTime());
                job.getJobDataMap().put(QuartzEnum.OBJECT_NAME, batchInfo.getObjectName());
                job.getJobDataMap().put(QuartzEnum.OBJECT_METHOD, batchInfo.getObjectMethod());
                job.getJobDataMap().put(QuartzEnum.BATCH_EXE_TYPE, batchInfo.getBatchExeType());
                job.getJobDataMap().put(QuartzEnum.THREAD_POOL_SIZE, batchInfo.getThreadPoolSize());
                job.getJobDataMap().put(QuartzEnum.QUEUE_CAPACITY, batchInfo.getQueueCapacity());
                job.getJobDataMap().put(QuartzEnum.SECTION_SIZE, batchInfo.getSectionSize());
                job.getJobDataMap().put(QuartzEnum.INDEX_START, batchInfo.getIndexStart());
                job.getJobDataMap().put(QuartzEnum.INDEX_END, batchInfo.getIndexEnd());
                //判断是否以多线程的方式执行任务
                if (QuartzEnum.MULT_THREAD_DEFUALT.equals(job.getJobDataMap().get(QuartzEnum.BATCH_EXE_TYPE))) {
                    //多线程的方式执行任务
                    logger.info("多线程不分段执行任务" + batchInfo.getObjectName() + "." + batchInfo.getObjectMethod());
                    job.setJobClass(QuartzPoolJob.class);
                } else if (QuartzEnum.MULT_THREAD_SECTION.equals(job.getJobDataMap().get(QuartzEnum.BATCH_EXE_TYPE))) {
                    //多线程分段的方式执行任务
                    logger.info("多线程分段执行任务" + batchInfo.getObjectName() + "." + batchInfo.getObjectMethod());
                    job.setJobClass(QuartzSectionPoolJob.class);
                } else {
                    //单线程方式执行任务
                    logger.info("单线程执行任务" + batchInfo.getObjectName() + "." + batchInfo.getObjectMethod());
                    job.setJobClass(QuartzJob.class);
                }

                job.setCronExpression(batchInfo.getExecuteTime());
                String jobName = batchInfo.getName() + "_" + batchInfo.getId();
                job.getJobDataMap().put(QuartzEnum.OBJECT_ID, batchInfo.getId());
                job.setJobName(jobName);
                logger.info("----开始部署任务:" + jobName + " [任务cron] " + "[" + batchInfo.getExecuteTime() + "]");
                quartzUtil.scheduleCronJob(job);
                logger.info("----成功部署任务:" + jobName + " [任务cron] " + "[" + batchInfo.getExecuteTime() + "]");

            }
            logger.info("批处理提取并调度完成");
        }

    }

    public void reScheduler() throws Exception {
        // 取消现有的任务
        Set<JobKey> jobKeys = quartzUtil.getJobKeys();
        if (null != jobKeys && jobKeys.size() > 0) {
            for (JobKey jobKey : jobKeys) {
                logger.info("----开始移除任务:" + jobKey.getName());
                quartzUtil.cancelJob(jobKey);
                logger.info("----成功移除任务:" + jobKey.getName());
            }
        }
        logger.info("现有任务已全部取消");

        this.initScheduler();
    }

    public void cancelJob(JobKey key) throws Exception {
        quartzUtil.cancelJob(key);
    }

    /**
     * 取批处理任务
     *
     * @return
     * @throws Exception
     */
    @Override
    public Set<JobKey> getJobList() throws Exception {
        return quartzUtil.getJobKeys();
    }

}
