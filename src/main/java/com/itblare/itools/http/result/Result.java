package com.itblare.itools.http.result;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.http.result
 * ClassName:   Result
 * Author:   Blare
 * Date:     Created in 2021/4/15 17:30
 * Description:    响应结果
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/15 17:30    1.0.0             响应结果
 */

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Objects;

/**
 * 一句话功能简述：响应结果
 *
 * @author Blare
 * @create 2021/4/15 17:30
 * @since 1.0.0
 */
public class Result<T> implements Serializable {

    private static final String ERROR_UNKNOWN = "ERROR_UNKNOWN";

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 当前时间戳
     */
    private String timestamp;

    /**
     * 返回对象
     */
    private T data;

    public Result(String code, String msg) {
        if (Objects.nonNull(code) && !"".equals(code)) {
            code = ERROR_UNKNOWN;
        }
        this.code = code;
        this.msg = msg;
    }

    public Result(T data, String code, String msg) {
        if (Objects.nonNull(code) && !"".equals(code)) {
            code = ERROR_UNKNOWN;
        }
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    public Result(RuntimeException ex) {
        this.code = "403";
        this.msg = ex.getLocalizedMessage();
    }

    public Result(T data, IMessage msgDefined) {
        if (data != null) {
            this.data = data;
        }
        this.code = msgDefined.getCode();
        this.msg = msgDefined.getMeg();
    }

    public Result(IMessage msgDefined) {
        this(msgDefined, null);
    }

    public Result(IMessage msgDefined, String info) {
        this.code = msgDefined.getCode();
        this.msg = MessageFormat.format(msgDefined.getMeg(), info);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
