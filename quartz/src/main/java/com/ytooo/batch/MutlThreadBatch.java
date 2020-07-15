package com.ytooo.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 多线程执行测试方法
 * Created by Youdmeng on 2020/1/3 0003.
 */
@Service
public class MutlThreadBatch {

    private static final Logger logger = LoggerFactory.getLogger(MutlThreadBatch.class);

    public void excuteData() {
        logger.info("执行：excuteData");
    }

    public void excuteFinal() {
        logger.info("执行：excuteFinal");
    }
}
