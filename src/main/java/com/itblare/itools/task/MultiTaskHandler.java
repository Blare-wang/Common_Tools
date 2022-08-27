package com.itblare.itools.task;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.task
 * ClassName:   MultiTaskHandler
 * Author:   Blare
 * Date:     Created in 2021/5/6 9:30
 * Description:    多任务处理
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/5/6 9:30    1.0.0         多任务处理
 */

/**
 * 多任务处理
 *
 * @author Blare
 * @create 2021/5/6 9:30
 * @since 1.0.0
 */
public interface MultiTaskHandler {

    /**
     * 添加任务
     *
     * @param task 任务
     * @method addTask
     * @date 2021/5/6 0:25
     */
    void addTask(Runnable... task);

    /**
     * 执行任务
     *
     * @method run
     * @date 2021/5/6 0:26
     */
    void run();
}
