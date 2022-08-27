package com.itblare.itools.exception;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.tools.exception
 * ClassName:   FileException
 * Author:   Blare
 * Date:     Created in 2021/4/12 10:35
 * Description:    文件异常
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/12 10:35    1.0.0             文件异常
 */

/**
 * 文件异常
 *
 * @author Blare
 * @create 2021/4/12 10:35
 * @since 1.0.0
 */
public class FileException  extends BaseException {


    public FileException() {
        super();
    }

    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }
}
