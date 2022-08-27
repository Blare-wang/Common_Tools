package com.itblare.itools.file.download.printer;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.file.download
 * ClassName:   DownloadProgressPrinter
 * Author:   Blare
 * Date:     Created in 2021/4/26 23:54
 * Description:    下载进程描述接口
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/26 23:54    1.0.0         下载进程描述接口
 */

/**
 * 下载进程描述接口
 *
 * @author Blare
 * @create 2021/4/26 23:54
 * @since 1.0.0
 */
public interface DownloadProgressPrinter {


    /**
     * 任务下载进度描述
     *
     * @param task                  下载任务名
     * @param contentLength         文件总大小
     * @param alreadyDownloadLength 已经下载的大小
     * @param speed                 下载速度
     * @method printInfo
     * @date 2021/4/26 23:57
     */
    void printInfo(String task, long contentLength, long alreadyDownloadLength, long speed);

    /**
     * 获取下载内容大小
     *
     * @return {@link long}
     * @method getContentLength
     * @date 2021/4/27 0:02
     */
    long getContentLength();

    /**
     * 默认下载内容大小
     *
     * @param contentLength 长度
     * @return {@link long}
     * @method setContentLength
     * @date 2021/4/27 0:02
     */
    long setContentLength(long contentLength);

    /**
     * 获取已下载内容大小
     *
     * @return {@link long}
     * @method getAlreadyDownloadLength
     * @date 2021/4/27 0:03
     */
    long getAlreadyDownloadLength();
}