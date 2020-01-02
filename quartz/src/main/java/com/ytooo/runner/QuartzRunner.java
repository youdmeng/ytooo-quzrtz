package com.ytooo.runner;

import com.ytooo.context.SpringContext;
import com.ytooo.quartz.service.QuartzService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created by Youdmeng on 2019/6/28 0028.
 */
@Component
public class QuartzRunner implements ApplicationRunner {

    @Autowired
    QuartzService quartzService;

    private static final Logger logger = LoggerFactory.getLogger(QuartzRunner.class);
    @Override
    public void run(ApplicationArguments var1) throws Exception{
        logger.info("批处理加载开始");
        try {
            // 取spring容器中的任务调度服务bean
            if (null == quartzService) {
                quartzService = SpringContext.getBean("quartzServiceImpl");
            }
            // 调用service初始化批处理
            quartzService.initScheduler();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("批处理加载失败，请手动加载！" + e);
        }
        logger.info("批处理加载结束");
    }

}