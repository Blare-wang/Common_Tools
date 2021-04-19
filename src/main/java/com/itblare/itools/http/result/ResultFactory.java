package com.itblare.itools.http.result;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.http.result
 * ClassName:   ResultFactory
 * Author:   Blare
 * Date:     Created in 2021/4/15 17:28
 * Description:    结果处理工厂
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/15 17:28    1.0.0             结果处理工厂
 */


/**
 * 一句话功能简述：结果处理工厂
 *
 * @author Blare
 * @create 2021/4/15 17:28
 * @since 1.0.0
 */
public class ResultFactory {

    /**
     * 功能描述: 成功结果
     *
     * @return {@link Result<M>}
     * @method wrapper
     * @author Blare
     * @date 2021/4/15 17:29
     * @updator Blare
     */
    public static <M> Result<M> wrapper() {
        return new Result<M>(ResultCode.SUCCESS);
    }

    /**
     * 功能描述: 成功结果
     *
     * @return {@link Result<M>}
     * @method wrapper
     * @author Blare
     * @date 2021/4/15 17:33
     * @updator Blare
     */
    public static <M> Result<M> wrapper(M data) {
        return new Result<>(data, ResultCode.SUCCESS);
    }

    /**
     * 功能描述：响应结果
     *
     * @param data 结果数据
     * @param code 响应状态
     * @return {@link Result<M>}
     * @method wrapper
     * @author Blare
     * @date 2021/4/15 17:33
     * @updator Blare
     */
    public static <M> Result<M> wrapper(M data, ResultCode code) {
        return new Result<>(data, code);
    }

    /**
     * 功能描述：响应结果
     *
     * @param data 结果数据
     * @param code 响应状态
     * @param msg  响应描述
     * @return {@link Result<M>}
     * @method wrapper
     * @author Blare
     * @date 2021/4/15 17:34
     * @updator Blare
     */
    public static <M> Result<M> wrapper(M data, String code, String msg) {
        return new Result<>(data, code, msg);
    }

    /**
     * 功能描述: 响应结果
     *
     * @param msg 响应信息
     * @return {@link Result}
     * @method wrapper
     * @author Blare
     * @date 2021/4/15 17:36
     * @updator Blare
     */
    public static Result<?> wrapper(ResultCode msg) {
        return new Result<>(msg);
    }

    /**
     * 功能描述: 响应结果
     *
     * @param msg  响应信息
     * @param info 响应描述
     * @return {@link Result}
     * @method wrapper
     * @author Blare
     * @date 2021/4/15 17:36
     * @updator Blare
     */
    public static Result<?> wrapper(ResultCode msg, String info) {
        return new Result<>(msg, info);
    }

    /**
     * 功能描述: 响应结果
     *
     * @return {@link Result}
     * @method wrapper
     * @author Blare
     * @date 2021/4/15 17:36
     * @updator Blare
     */
    public static Result<?> wrapper(RuntimeException ex) {
        return new Result<>(ex);
    }
}