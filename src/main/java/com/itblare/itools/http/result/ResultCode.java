package com.itblare.itools.http.result;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.http.result
 * ClassName:   ResultCode
 * Author:   Blare
 * Date:     Created in 2021/4/15 17:26
 * Description:    结果枚举
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/15 17:26    1.0.0             结果枚举
 */

/**
 * 结果枚举
 *
 * @author Blare
 * @create 2021/4/15 17:26
 * @since 1.0.0
 */
public enum ResultCode implements IMessage {

    SUCCESS("200", "SUCCESS"),
    FAIL("301", "FAIL"),
    ERROR_PARAMETER("400", "请求参数错误"),
    DATA_NOT_EXIST("404", "数据不存在");

    private final String code;

    private final String msg;

    ResultCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMeg() {
        return msg;
    }
    }
