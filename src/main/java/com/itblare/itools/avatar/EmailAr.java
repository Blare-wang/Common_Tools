package com.itblare.itools.avatar;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.avatar
 * ClassName:   EmailAr
 * Author:   Blare
 * Date:     Created in 2021/4/19 17:53
 * Description:    邮箱头像
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/19 17:53    1.0.0         邮箱头像
 */

import java.security.MessageDigest;

/**
 * 一句话功能简述：邮箱头像
 *
 * @author Blare
 * @create 2021/4/19 17:53
 * @since 1.0.0
 */
public class EmailAr {

    /**
     * 功能描述: 邮箱MD5
     *
     * @param message 邮箱地址
     * @return {@link String}
     * @method emailToMd5
     * @author Blare
     * @date 2021/4/19 17:53
     * @updator Blare
     */
    public static String emailToMd5(String message) {
        String temp = "";
        try {
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            byte[] encodeMd5Digest = md5Digest.digest(message.getBytes());
            temp = convertByteToHexString(encodeMd5Digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * 功能描述: 字节转HexString
     *
     * @param bytes 字节数组
     * @return {@link String}
     * @method convertByteToHexString
     * @author Blare
     * @date 2021/4/19 17:54
     * @updator Blare
     */
    public static String convertByteToHexString(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            int temp = aByte & 0xff;
            String tempHex = Integer.toHexString(temp);
            if (tempHex.length() < 2) {
                result.append("0").append(tempHex);
            } else {
                result.append(tempHex);
            }
        }
        return result.toString();
    }

    /**
     * 功能描述: 根据email获取gravatar头像
     *
     * @param email 邮箱地址
     * @return {@link String}
     * @method getGravatar
     * @author Blare
     * @date 2021/4/19 17:54
     * @updator Blare
     */
    public static String getGravatar(String email) {
        String emailMd5 = emailToMd5(email);
        //设置图片大小32px
        return "https://s.gravatar.com/avatar/" + emailMd5 + "?s=32";
    }

    public static void main(String[] args) {
        String gravatarImg = getGravatar("admin@199604.com");
        System.out.println(gravatarImg);
        //输出https://s.gravatar.com/avatar/162d8d081d380691c5279df2bbef4152?s=32
    }
}
