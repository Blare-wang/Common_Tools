package com.itblare.itools.http.result;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.http.result
 * ClassName:   XmlInfo
 * Author:   Blare
 * Date:     Created in 2021/4/16 10:31
 * Description:    XML 信息接口
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/16 10:31    1.0.0             XML 信息接口
 */

/**
 * 一句话功能简述：XML 信息接口
 *
 * @author Blare
 * @create 2021/4/16 10:31
 * @since 1.0.0
 */
public interface XmlInfo {

    /**
     * 功能描述: 代码
     *
     * @method getCode
     * @return {@link String}
     * @date 2021/4/16 10:31
     */
    String getCode();

    /**
     * 功能描述: 信息
     *
     * @method getMessage
     * @return {@link String}
     * @date 2021/4/16 10:32
     */
    String getMessage();
}
