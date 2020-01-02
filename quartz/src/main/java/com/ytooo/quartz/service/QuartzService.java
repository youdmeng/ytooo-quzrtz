package com.ytooo.quartz.service;

import org.quartz.JobKey;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 批处理调度service
 * Created by Youdmeng on 2019/6/28 0028.
 */
@Service
public interface QuartzService {

    /**
     * 初始化批处理，从配置中加载批处理项
     *
     * @throws Exception
     */
    void initScheduler() throws Exception;

    /**
     * 重载任务列表
     *
     * @throws Exception
     */
    void reScheduler() throws Exception;

    void cancelJob(JobKey key) throws Exception;

    /**
     * 取批处理任务
     *
     * @return
     */
    Set<JobKey> getJobList() throws Exception;

    ;

}
