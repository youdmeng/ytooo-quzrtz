package com.ytooo.quartz.job;

import com.ytooo.context.SpringContext;
import com.ytooo.quartz.entity.ThreadExecutorBean;
import com.ytooo.quartz.enums.QuartzEnum;
import com.ytooo.quartz.util.AysnTask;
import com.ytooo.quartz.util.ExecutorUtil;
import com.ytooo.quartz.util.ReflectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.Map;

/**
 * 多线程任务列表分段提取模式
 * Created by Youdmeng on 2019/6/28 0028.
 */
public class QuartzSectionPoolJob extends QuartzJob {

    private static final Logger logger = LoggerFactory.getLogger(QuartzSectionPoolJob.class);

    /**
     * 调用jobmap中的配置的bean.method
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {

        String beanName = (String) context.getMergedJobDataMap().get(OBJECT_NAME);
        String methodName = (String) context.getMergedJobDataMap().get(OBJECT_METHOD);
        String listMethodName = methodName + "Data";
        String finalMethodName = methodName + "Final";
        String initMethodName = methodName + "Init";
        logger.info(beanName + ".(" + listMethodName + "," + methodName + ");多线程任务分段执行开始");

        //分段大小
        int sectionSize = Integer.parseInt((String) context.getMergedJobDataMap().get(QuartzEnum.SECTION_SIZE));
        if (sectionSize < 1) {
            sectionSize = 1;
        }

        //分段起始下标识
        int start = 1;

        //分段结束下标识
        int end = sectionSize;

        //任务开始下标
        String indexStart = (String) context.getMergedJobDataMap().get(QuartzEnum.INDEX_START);
        if (StringUtils.isNotBlank(indexStart)) {
            start = Integer.parseInt(indexStart);
            end = Integer.parseInt(indexStart) + sectionSize;
        }

        int indexEnd = -1;
        if (StringUtils.isNotBlank((String) context.getMergedJobDataMap().get(QuartzEnum.INDEX_END))) {
            indexEnd = Integer.parseInt((String) context.getMergedJobDataMap().get(QuartzEnum.INDEX_END));
        }

        if (start < 1) {
            start = 1;
        }

        //线程池
        ThreadPoolTaskExecutor threadPool = (ThreadPoolTaskExecutor)
                context.getJobDetail().getJobDataMap().get(QuartzEnum.THREAD_POOL + OBJECT_ID);

        /**
         * 如果线程池为空则初始换线程池，并放到context中
         */
        if (null == threadPool) {
            int poolSize = Integer.parseInt((String) context.getMergedJobDataMap().get(QuartzEnum.OBJECT_OBJECT_POOL_SIZE));
            if (poolSize < 1) {
                poolSize = 1;
            }

            //线程等待队列长度
            int queueSize = 0;
            String queueSz = (String) context.getMergedJobDataMap().get(QuartzEnum.QUEUE_CAPACITY);
            if (StringUtils.isNotBlank(queueSz)) {
                queueSize = Integer.parseInt(queueSz);
            }
            if (queueSize < 1) {
                queueSize = sectionSize;
            }

            threadPool = ExecutorUtil.createPoolTaskExecutor(poolSize, queueSize);
            context.getJobDetail().getJobDataMap().put(QuartzEnum.THREAD_POOL + OBJECT_ID, threadPool);
            logger.info(beanName + ".(" + listMethodName + "," + methodName + ");线程池创建完成");
        } else {
            logger.info(beanName + ".(" + listMethodName + "," + methodName + ");线程池从context中取得");
        }

        try {
            Class<?>[] initParameterTypes = { Map.class };
            Object[] initParameters = { context.getJobDetail().getJobDataMap() };

            //调用初始化方法
            ReflectionUtil.invokeMethod(SpringContext.getBean(beanName), initMethodName, initParameterTypes, initParameters);

            //如果结束任务下标超过配置的结束下标则
            if (end > indexEnd && indexEnd != -1) {
                end = indexEnd;
            }
            Class<?>[] parameterTypes = { Integer.class, Integer.class };
            Object[] parameters = { start, end };

            // 任务列表
            List list = (List) ReflectionUtil.invokeMethod(SpringContext.getBean(beanName), listMethodName, parameterTypes, parameters);

            while (list != null && !list.isEmpty()) {
                // 线程池分配并执行方法, 任务分配应该交给线程池管理。循环处理任务列表
                if (null != list && list.size() > 0) {
                    for (int i = 0; i < list.size(); i++) {
                        ThreadExecutorBean exeDto = new ThreadExecutorBean();
                        exeDto.setBeanName(beanName);
                        exeDto.setMethod(methodName);
                        exeDto.setTask(list.get(i));
                        threadPool.execute(new AysnTask(exeDto));
                    }
                }

                try {
                    // 暂停0.5秒,
                    // 防止ActiveCount数值不准确问题。当任务量少时可能发生ActiveCount是0,而实际任务线程组中已经有任务情况
                    Thread.sleep(500L);

                    // 查看所有任务是否执行完成
                    while (threadPool.getActiveCount() > 0) {
                        // 暂停0.5秒
                        Thread.sleep(500L);
                    }
                } catch (InterruptedException e) {
                    logger.warn("任务执行异常: \n", e);
                }

                //查询下一段任务
                start = start + sectionSize;
                end = end + sectionSize;

                //如果结束任务下标超过配置的结束下标则
                if (end > indexEnd && indexEnd != -1) {
                    end = indexEnd;
                }
                parameters[0] = start;
                parameters[1] = end;
                list = (List) ReflectionUtil.invokeMethod(SpringContext.getBean(beanName), listMethodName, parameterTypes, parameters);
            }

        } catch (Exception e) {
            logger.warn("批处理调用失败: " + beanName + "." + methodName + "\n", e);
        } finally {
            try {
                // 结束事件
                ReflectionUtil.invokeMethod(SpringContext.getBean(beanName), finalMethodName, null, null);
            } catch (Exception e) {
                logger.warn("批处理Final调用失败: " + beanName + "." + finalMethodName + "\n", e);
            }
        }
        logger.info(beanName + ".(" + listMethodName + "," + methodName + ");多线程任务执行结束");
    }

}
