package com.itblare.itools.file.download;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.file.download
 * ClassName:   Downloader
 * Author:   Blare
 * Date:     Created in 2021/4/26 23:45
 * Description:    下载器接口
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/26 23:45    1.0.0         下载器接口
 */

/**
 * 下载器接口
 *
 * @author Blare
 * @create 2021/4/26 23:45
 * @since 1.0.0
 */
public interface Downloader {

    /**
     * 资源下载
     *
     * @param url 下载链接
     * @param dir 资源存储目录
     * @method download
     * @date 2021/4/26 23:46
     */
    void download(String url, String dir);
}
