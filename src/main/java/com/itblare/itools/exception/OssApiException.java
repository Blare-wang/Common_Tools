package com.itblare.itools.exception;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.tools.exception
 * ClassName:   OssApiException
 * Author:   Blare
 * Date:     Created in 2021/4/12 10:39
 * Description:    对象存储异常
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/12 10:39    1.0.0             对象存储异常
 */

/**
 * 对象存储异常
 *
 * @author Blare
 * @create 2021/4/12 10:39
 * @since 1.0.0
 */
public class OssApiException extends BaseException {

    public OssApiException() {
        super();
    }

    public OssApiException(String message) {
        super(message);
    }

    public OssApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
