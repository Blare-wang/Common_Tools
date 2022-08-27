package com.itblare.itools.http.result;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.http.result
 * ClassName:   XmlResponse
 * Author:   Blare
 * Date:     Created in 2021/4/16 10:29
 * Description:    XML 信息响应
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/16 10:29    1.0.0             XML 信息响应
 */

/**
 * XML 信息响应
 *
 * @author Blare
 * @create 2021/4/16 10:29
 * @since 1.0.0
 */
public class XmlResponse {

    public XmlInfo xmlInfo;

    public XmlResponse(XmlInfo xmlInfo) {
        this.xmlInfo = xmlInfo;
    }
}