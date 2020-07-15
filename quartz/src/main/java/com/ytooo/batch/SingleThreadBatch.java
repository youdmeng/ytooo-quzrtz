package com.ytooo.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 单线程测试任务
 * Created by Youdmeng on 2019/6/28 0028.
 */
@Service
public class SingleThreadBatch {
    private static final Logger logger = LoggerFactory.getLogger(SingleThreadBatch.class);
    public void excute(){
        logger.info("[单线程测试任务] [执行开始]");
        int aaa = 1;
        while (aaa <= 10) {
            logger.info(aaa++ + "");
        }
        logger.info("[单线程测试任务] [执行完毕]");
    }
}
