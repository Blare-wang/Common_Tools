package com.itblare.itools.task;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.task
 * ClassName:   TaskParallelWithThreadPool
 * Author:   Blare
 * Date:     Created in 2021/5/6 9:46
 * Description:    线程池并行执行任务
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/5/6 9:46    1.0.0         线程池并行执行任务
 */

import java.util.concurrent.ExecutorService;

/**
 * 线程池并行执行任务
 *
 * @author Blare
 * @create 2021/5/6 9:46
 * @since 1.0.0
 */
public class TaskParallelWithThreadPool extends TaskParallelHandler {

    private ExecutorService service;

    public TaskParallelWithThreadPool() {
        super();
    }

    public TaskParallelWithThreadPool(ExecutorService service) {
        this.service = service;
    }

    public ExecutorService getService() {
        return service;
    }

    public void setService(ExecutorService service) {
        this.service = service;
    }

    @Override
    protected void invoke(Runnable task) {
        if (null != service) {
            service.execute(task);
        } else {
            super.invoke(task);
        }
    }
}
