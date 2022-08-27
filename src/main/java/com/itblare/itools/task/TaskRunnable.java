package com.itblare.itools.task;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.task
 * ClassName:   TaskRunnable
 * Author:   Blare
 * Date:     Created in 2021/5/6 9:32
 * Description:    任务线程
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/5/6 9:32    1.0.0         任务线程
 */

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * 任务线程
 *
 * @author Blare
 * @create 2021/5/6 9:32
 * @since 1.0.0
 */
public class TaskRunnable implements Runnable {

    /**
     * 子线程倒计数锁
     */
    private final CountDownLatch childLatch;

    /**
     * 运行的任务
     */
    private final Runnable task;

    // 子线程异常

    public TaskRunnable(CountDownLatch childLatch, Runnable task) {
        this.childLatch = childLatch;
        this.task = task;
    }

    @Override
    public void run() {
        try {
            if (Objects.nonNull(task)) {
                task.run();
            }
        }finally {
            if (Objects.nonNull(childLatch)) {
                childLatch.countDown();
            }
        }
    }
}
