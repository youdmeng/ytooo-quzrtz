package com.ytooo.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Youdmeng on 2019/6/28 0028.
 */
@Service
public class TestBatch {
    private static int aaa = 1;
    private static final Logger logger = LoggerFactory.getLogger(TestBatch.class);
    public void excute(){
        if (aaa < 500) {
            logger.info(aaa++ + "");
        }else if (aaa == 500) {
            logger.info(aaa++ + "");
            logger.info("[测试batch] [执行完毕]");
        }
    }
}
