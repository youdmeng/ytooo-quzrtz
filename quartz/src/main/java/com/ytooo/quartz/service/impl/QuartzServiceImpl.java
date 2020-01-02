package com.ytooo.quartz.service.impl;

import com.ytooo.quartz.entity.QuartzJobBean;
import com.ytooo.quartz.enums.QuartzEnum;
import com.ytooo.quartz.job.QuartzJob;
import com.ytooo.quartz.job.QuartzPoolJob;
import com.ytooo.quartz.job.QuartzSectionPoolJob;
import com.ytooo.quartz.service.QuartzService;
import com.ytooo.quartz.util.QuartzUtil;
import com.ytooo.repository.QuartzInfoEO;
import com.ytooo.repository.QuartzInfoEOPage;
import com.ytooo.repository.dao.QuartzInfoEODao;
import org.apache.commons.lang3.StringUtils;
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
    private QuartzInfoEODao quartzInfoEODao;

    @Value("${batch.run.key}")
    private String runKey;

    public void initScheduler() throws Exception {

        QuartzInfoEOPage page = new QuartzInfoEOPage();
        List<QuartzInfoEO> batchList = quartzInfoEODao.queryByList(page);
        if (batchList.size() <= 0) {
            logger.info("[未发现批处理],获取批处理列表大小为0");
        }
        //逐笔设置任务信息到quartz,并调度任务
        if (batchList.size() > 0) {
            for (QuartzInfoEO batchInfo : batchList) {
                QuartzJobBean job = new QuartzJobBean();
                job.setCronExpression(batchInfo.getExecuteTime());
                //按照分号和等号拆分ExecuteParameter，然后将拆分后的参数放入到JobDataMap中
                String exeParams = batchInfo.getExecuteParameter();
                if (StringUtils.isNotBlank(exeParams)) {
                    //分号拆分ExecuteParameter
                    String[] params = exeParams.split(";");
                    if (params.length > 0) {
                        for (String s : params) {
                            if (StringUtils.isNotBlank(s)) {
                                //等号拆分字符串s
                                String[] keyM = s.split("=");
                                try {
                                    job.getJobDataMap().put(keyM[0], keyM[1]);
                                    logger.info("执行参数key:" + keyM[0] + ",value:" + keyM[1]);
                                } catch (Exception e) {
                                    logger.warn("批处理参数配置错误", e);
                                }
                            }
                        }
                    }
                }

                job.getJobDataMap().put(QuartzJob.OBJECT_NAME, batchInfo.getObjectName());
                job.getJobDataMap().put(QuartzJob.OBJECT_METHOD, batchInfo.getObjectMethod());

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
                job.getJobDataMap().put(QuartzJob.OBJECT_ID, batchInfo.getId());
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
