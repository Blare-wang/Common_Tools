package com.itblare.itools.security;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.security
 * ClassName:   MD5
 * Author:   Blare
 * Date:     Created in 2021/4/16 10:36
 * Description:    MD5 加密
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/16 10:36    1.0.0             MD5 加密
 */

import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * 一句话功能简述：MD5 加密
 *
 * @author Blare
 * @create 2021/4/16 10:36
 * @since 1.0.0
 */
public class MD5Processor {

    /**
     * 功能描述: MD5-32位签名算法
     *
     * @param encryptedStr 待加密字符串
     * @param charset      字符集【UTF8、UTF-8、GBK、ISO-8859-1等】
     * @return {@link String}
     * @method md5Encode
     * @author Blare
     * @date 2021/4/16 11:02
     * @updator Blare
     */
    public static String md5Encode(String encryptedStr, String charset) {
        MessageDigest md5;
        try {
            //MessageDigest jdk 1.8数字签名
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        if (Objects.isNull(charset)) {
            charset = "UTF-8";
        }
        // 获取字节数组
        final byte[] bytes = encryptedStr.getBytes(Charset.forName(charset));
        // 字节数组编码，结果返回的byte[]长度始终为16位
        final byte[] digest = md5.digest(bytes);
        // byte[]数组 转换String
        StringBuilder hexValue = new StringBuilder();
        for (byte md5Byte : digest) {
            // md5Byte & 0xff -> 保正生成的Md5字符串为固定长度32
            final int val = md5Byte & 0xff;
            String hv = Integer.toHexString(val);
            if (hv.length() < 2) {
                hexValue.append("0");
            }
            hexValue.append(hv);
        }
        return hexValue.toString();
    }

    public static void main(String[] args) {
        final String encode = md5Encode("123456", "UTF-8");
        System.out.println(encode);
    }
}
