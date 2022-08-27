package com.itblare.itools.task;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.task
 * ClassName:   TaskParallelHandler
 * Author:   Blare
 * Date:     Created in 2021/5/6 9:35
 * Description:    任务并行处理
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/5/6 9:35    1.0.0         任务并行处理
 */

import java.util.concurrent.CountDownLatch;

/**
 * 任务并行处理
 *
 * @author Blare
 * @create 2021/5/6 9:35
 * @since 1.0.0
 */
public class TaskParallelHandler extends AbstractMultiTaskHandler {

    public TaskParallelHandler() {
        super();
    }

    @Override
    public void run() {
        if (1 == taskList.size()) {
            runInThisThread();
        } else {
            runInNewThread();
        }
    }

    /**
     * 新建线程运行
     *
     * @method runInNewThread
     * @date 2021/5/6 0:44
     */
    private void runInNewThread() {
        childLatch = new CountDownLatch(taskList.size());
        for (Runnable task : taskList) {
            invoke(new TaskRunnable(childLatch, task));
        }
        taskList.clear();
        try {
            childLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void invoke(Runnable task) {
        if (task.getClass().isAssignableFrom(Thread.class)) {
            ((Thread) task).start();
        } else {
            new Thread(task).start();
        }
    }

    /**
     * 当前线程运行
     *
     * @method runInThisThread
     * @date 2021/5/6 0:44
     */
    private void runInThisThread() {
        taskList.get(0).run();
    }
}
