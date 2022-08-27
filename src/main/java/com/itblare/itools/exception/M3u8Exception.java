package com.itblare.itools.exception;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.tools.exception
 * ClassName:   M3u8Exception
 * Author:   Blare
 * Date:     Created in 2021/4/12 10:38
 * Description:    M3U8异常
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/12 10:38    1.0.0             M3U8异常
 */

/**
 * M3U8异常
 *
 * @author Blare
 * @create 2021/4/12 10:38
 * @since 1.0.0
 */
public class M3u8Exception extends BaseException {

    public M3u8Exception() {
        super();
    }

    public M3u8Exception(String message) {
        super(message);
    }

    public M3u8Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
