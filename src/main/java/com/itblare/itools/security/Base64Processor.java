package com.itblare.itools.security;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.security
 * ClassName:   Base64
 * Author:   Blare
 * Date:     Created in 2021/4/17 23:22
 * Description:    Base64工具
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/17 23:22    1.0.0             Base64工具
 */

/**
 * 一句话功能简述：Base64工具
 *
 * @author Blare
 * @create 2021/4/17 23:22
 * @since 1.0.0
 */
public class Base64Processor {

    /**
     * 功能描述: base64 编码
     *
     * @param data 待加密数据字节数组
     * @return {@link String}
     * @method encode
     * @date 2021/4/17 23:24
     */
    public static String encode(byte[] data) {
        return new String(java.util.Base64.getEncoder().encode(data));
    }

    /**
     * 功能描述: base64 解码
     *
     * @param encodeStr 待解密字符串
     * @return {@link byte[]}
     * @method decode
     * @date 2021/4/17 23:24
     */
    public static byte[] decode(String encodeStr) {
        return java.util.Base64.getDecoder().decode(encodeStr.getBytes());
    }
}