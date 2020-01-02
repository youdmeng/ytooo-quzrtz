package com.ytooo.rest;

import com.ytooo.http.Response;
import com.ytooo.http.Result;
import com.ytooo.quartz.service.QuartzService;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 定时任务手动控制
 * Created by Youdmeng on 2019/7/1 0001.
 */
@RestController
@RequestMapping("/quartzController")
public class QuartzController {

    private static final Logger logger = LoggerFactory.getLogger(QuartzController.class);

    @Autowired
    private QuartzService quartzService;

    /**
     * 重新加载任务
     *
     * @return
     */
    @GetMapping("/reLoadBatch")
    public Response reLoadBatch() {
        try {
            quartzService.reScheduler();
        } catch (Exception e) {
            logger.error("[重新加载任务] [失败] " + e.getMessage(), e);
            return Result.error(e.getMessage());
        }
        return Result.success();
    }

    /**
     * 获取任务列表
     *
     * @return
     */
    @GetMapping("/getJobList")
    public Response getJobList() {
        try {
            return Result.success(quartzService.getJobList());
        } catch (Exception e) {
            logger.error("[获取任务列表] [失败] " + e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    /**
     * 取消指定任务
     *
     * @return
     */
    @GetMapping("/cancelJob")
    public Response cancelJob(String jobName, String group) {
        logger.info("进入取消指定任务，参数：任务名:" + jobName + " 任务组:" + group);
        try {
            JobKey key = new JobKey(jobName, group);
            quartzService.cancelJob(key);
        } catch (Exception e) {
            logger.error("[取消指定任务] [失败] " + e.getMessage(), e);
            return Result.error();
        }
        return Result.success();
    }
}
