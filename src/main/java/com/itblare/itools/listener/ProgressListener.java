package com.itblare.itools.listener;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.tools.listener
 * ClassName:   ProgressListener
 * Author:   Blare
 * Date:     Created in 2021/4/13 10:00
 * Description:    进度监听
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/13 10:00    1.0.0             进度监听
 */

/**
 * 进度监听
 *
 * @author Blare
 * @create 2021/4/13 10:00
 * @since 1.0.0
 */
public interface ProgressListener {

    /**
     * 开始
     *
     * @param msg 开始消息
     * @method start
     * @date 2021/4/13 10:02
     */
    void start(String msg);

    /**
     * 处理中
     *
     * @param finished 目前进度
     * @param sum      总进度
     * @method process
     * @date 2021/4/13 10:02
     */
    void process(int finished, int sum);

    /**
     * 结束
     *
     * @param result 结果
     * @method end
     * @date 2021/4/13 10:02
     */
    void end(String result);
}
