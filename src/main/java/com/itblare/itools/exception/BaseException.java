package com.itblare.itools.exception;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.tools.exception
 * ClassName:   BaseException
 * Author:   Blare
 * Date:     Created in 2021/4/12 10:31
 * Description:    自定义基础型运行时异常类
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/12 10:31    1.0.0             自定义基础型运行时异常类
 */

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 自定义基础型运行时异常类
 *
 * @author Blare
 * @create 2021/4/12 10:31
 * @since 1.0.0
 */
public class BaseException  extends RuntimeException{

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public BaseException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message
     *         the detail message. The detail message is saved for
     *         later retrieval by the {@link #getMessage()} method.
     */
    public BaseException(String message) {
        super(message);
    }

    /**
     * Constructs a new runtime exception with the specified detail message and
     * cause.  <p>Note that the detail message associated with
     * {@code cause} is <i>not</i> automatically incorporated in
     * this runtime exception's detail message.
     *
     * @param message
     *         the detail message (which is saved for later retrieval
     *         by the {@link #getMessage()} method).
     * @param cause
     *         the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A <tt>null</tt> value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since 1.4
     */
    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /** Constructs a new runtime exception with the specified cause and a
     * detail message of {@code (cause==null ? null : cause.toString())}
     * (which typically contains the class and detail message of
     * {@code cause}).  This constructor is useful for runtime exceptions
     * that are little more than wrappers for other throwables.
     *
     * @param  cause the cause (which is saved for later retrieval by the
     *         {@link #getCause()} method).  (A {@code null} value is
     *         permitted, and indicates that the cause is nonexistent or
     *         unknown.)
     * @since  1.4
     */
    public BaseException(Throwable cause) {
        super(cause);
    }

    public static String getTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }
}
