package com.ytooo.quartz.enums;

/**
 * Created by Youdmeng on 2019/6/28 0028.
 */
public class QuartzEnum {

    /**
     * key 对象标识
     */
    public static final String OBJECT_NAME = "beanName";

    /**
     * key beanId
     */
    public static final String OBJECT_ID = "beanId";

    /**
     * key 对象方法
     */
    public static final String OBJECT_METHOD = "methodName";

    /**
     * 执行方法
     */
    public static final String BATCH_EXE_TYPE = "batchExeType";


    //scheduler
    public static final String SCHEDULER = "scheduler";

    //job
    public static final String JOB = "job";

    //trigger
    public static final String TRIGGER = "trigger";

    //service
    public static final String SERVICE = "service";

    //批处理不启动
    public static final String BATCH_NOT_RUN = "0";

    //启动前置批处理
    public static final String BATCH_RUN_FRONT = "01";

    //启动后置批处理
    public static final String BATCH_RUN_BACK = "02";

    /**
     * key 线程池大小
     */
    public static final String THREAD_POOL_SIZE = "threadPoolSize";

    /**
     * key线程池
     */
    public static final String THREAD_POOL = "threadPool";

    /**
     * key 分段大小
     */
    public static final String SECTION_SIZE = "sectionSize";

    /**
     * key 任务开始下标
     */
    public static final String INDEX_START = "indexStart";

    /**
     * key 任务开始下标
     */
    public static final String INDEX_END = "indexEnd";


    /**
     * 批处理执行方式 单线程执行
     */
    public static final String SINGLE_THREAD = "singleThread";

    /**
     * 批处理执行方式 多线程不分段执行
     */
    public static final String MULT_THREAD_DEFUALT = "mutlThread";

    /**
     * 批处理执行方式 多线程分段执行
     */
    public static final String MULT_THREAD_SECTION = "mutlThreadSection";

    /**
     * key  最大任务队列长度
     */
    public static final String QUEUE_CAPACITY = "queueCapacity";

}
