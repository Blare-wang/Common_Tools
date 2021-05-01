package com.itblare.itools.http.result;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.http.result
 * ClassName:   IMessage
 * Author:   Blare
 * Date:     Created in 2021/4/15 17:25
 * Description:    消息接口
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/15 17:25    1.0.0             消息接口
 */

/**
 * 一句话功能简述：消息接口
 *
 * @author Blare
 * @create 2021/4/15 17:25
 * @since 1.0.0
 */
public interface IMessage {

    /**
     * 功能描述: 得到常量key或值
     *
     * @return {@link String}
     * @method getCode
     * @date 2021/4/16 10:32
     */
    String getCode();

    /**
     * 功能描述: 得到常量的定义或描述（如果国际化,在这里实现）
     *
     * @return {@link String}
     * @method getMeg
     * @date 2021/4/16 10:33
     */
    String getMeg();
}
