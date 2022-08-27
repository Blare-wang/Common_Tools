package com.itblare.itools.task;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.task
 * ClassName:   AbstractMultiTaskHandler
 * Author:   Blare
 * Date:     Created in 2021/5/6 9:31
 * Description:    多任务处理抽象
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/5/6 9:31    1.0.0         多任务处理抽象
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

/**
 * 多任务处理抽象
 *
 * @author Blare
 * @create 2021/5/6 9:31
 * @since 1.0.0
 */
public abstract class AbstractMultiTaskHandler implements MultiTaskHandler {

    /**
     * 子线程倒计数锁
     */
    protected CountDownLatch childLatch;

    /**
     * 任务列表
     */
    protected List<Runnable> taskList;

    // 子线程异常

    public AbstractMultiTaskHandler() {
        this.taskList = new ArrayList<>();
    }

    public void setChildLatch(CountDownLatch childLatch) {
        this.childLatch = childLatch;
    }

    @Override
    public void addTask(Runnable... tasks) {
        if (Objects.isNull(tasks)) {
            taskList = new ArrayList<>();
        }
        Collections.addAll(taskList, tasks);
    }

    @Override
    public abstract void run();
}
