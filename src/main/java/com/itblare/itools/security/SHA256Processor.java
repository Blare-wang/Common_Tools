package com.itblare.itools.security;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.security
 * ClassName:   SHA256Processor
 * Author:   Blare
 * Date:     Created in 2021/4/17 23:45
 * Description:    SHA256加密
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/17 23:45    1.0.0             SHA256加密
 */

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 一句话功能简述：SHA256加密
 *
 * @author Blare
 * @create 2021/4/17 23:45
 * @since 1.0.0
 */
public class SHA256Processor {

    private static final String ALGORITHMS_NAME = "SHA-256";

    /**
     * 功能描述: 计算文件hash
     *
     * @param filePath 文件路径
     * @return {@link String}
     * @method hash
     * @date 2021/4/17 23:46
     */
    public static String hash(String filePath) {
        long start = System.currentTimeMillis();
        File file = new File(filePath);
        String strDes;
        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
            MessageDigest digest = MessageDigest.getInstance(ALGORITHMS_NAME);
            // 每次读取16M
            int bufferSize = 16 * 1024;
            byte[] buffer = new byte[bufferSize];
            int sizeRead;
            while ((sizeRead = in.read(buffer)) != -1) {
                digest.update(buffer, 0, sizeRead);
            }
            strDes = bytes2Hex(digest.digest());
            in.close();
        } catch (Exception e) {
            throw new RuntimeException("Unable to compute hash while signing request: " + e.getMessage(), e);
        }
        System.out.println("calculate complete take time: " + (System.currentTimeMillis() - start) / 1000);
        return strDes;
    }

    /**
     * 功能描述: 计算一个字节数组的 hash
     *
     * @param src 字节数组
     * @return {@link String}
     * @method hash
     * @date 2021/4/17 23:46
     */
    public static String hash(byte[] src) {
        String strDes;
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHMS_NAME);
            md.update(src);
            strDes = bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unable to compute hash while signing request: " + e.getMessage(), e);
        }
        return strDes;
    }

    /**
     * 功能描述: 字节转HEX字符串
     *
     * @param bts 待处理字节数组
     * @return {@link String}
     * @method bytes2Hex
     * @date 2021/4/17 23:46
     */
    private static String bytes2Hex(byte[] bts) {
        StringBuilder des = new StringBuilder();
        String tmp;
        for (byte bt : bts) {
            tmp = (Integer.toHexString(bt & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString().toUpperCase();
    }
}
