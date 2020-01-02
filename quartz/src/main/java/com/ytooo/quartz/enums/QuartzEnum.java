package com.ytooo.quartz.enums;

/**
 * Created by Youdmeng on 2019/6/28 0028.
 */
public enum QuartzEnum {

    //scheduler
    SCHEDULER("scheduler"),

    //job
    JOB("job"),

    //trigger
    TRIGGER("trigger"),

    //service
    SERVICE("service"),

    //批处理不启动
    BATCH_NOT_RUN("0"),

    //启动前置批处理
    BATCH_RUN_FRONT("01"),

    //启动后置批处理
    BATCH_RUN_BACK("02"),

    /**
     * key 线程池大小
     */
    OBJECT_OBJECT_POOL_SIZE("threadPoolSize"),

    /**
     * key线程池
     */
    THREAD_POOL("threadPool"),

    /**
     * key 分段大小
     */
    SECTION_SIZE("sectionSize"),

    /**
     * key 任务开始下标
     */
    INDEX_START("indexStart"),

    /**
     * key 任务开始下标
     */
    INDEX_END("indexEnd"),

    /**
     * key 批处理执行方式
     */
    BATCH_EXE_TYPE("batchExeType"),

    /**
     * 批处理执行方式 单线程执行
     */
    SINGLE_THREAD("singleThread"),

    /**
     * 批处理执行方式 多线程不分段执行
     */
    MULT_THREAD_DEFUALT("mutlThread"),

    /**
     * 批处理执行方式 多线程分段执行
     */
    MULT_THREAD_SECTION("mutlThreadSection"),

    /**
     * key  最大任务队列长度
     */
    QUEUE_CAPACITY("queueCapacity");

    private String key;

    QuartzEnum(String key) {
        this.key = key;
    }

    /**
     * 属性get
     */
    public String getKey() {
        return key;
    }

    /**
     * 属性set
     */
    public void setKey(String key) {
        this.key = key;
    }
}
