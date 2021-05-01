package com.itblare.itools.security;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.security
 * ClassName:   PBKDF2Processor
 * Author:   Blare
 * Date:     Created in 2021/4/19 17:18
 * Description:    PBKDF2加密
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/19 17:18    1.0.0             PBKDF2加密
 */

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 * 一句话功能简述：全新的、安全的加密算法:PBKDF2
 * PBKDF2算法通过多次hash来对密码进行加密。
 * 原理是通过password和salt进行hash，然后将结果作为salt在与password进行hash，多次重复此过程，生成最终的密文。
 * 此过程可能达到上千次
 *
 * @author Blare
 * @create 2021/4/19 17:18
 * @since 1.0.0
 */
public class PBKDF2Processor {

    /**
     * 盐的长度
     */
    private static final int SALT_SIZE = 16;
    /**
     * 生成密文的长度
     */
    private static final int HASH_SIZE = 32;
    /**
     * 迭代次数
     */
    private static final int PBKDF2_ITERATIONS = 1000;

    /**
     * 功能描述: 密码验证
     *
     * @param password 输入的密码
     * @param salt     盐
     * @param key      原密文
     * @return {@link boolean}
     * @method verify
     * @date 2021/4/19 17:22
     */
    public static boolean verify(String password, String salt, String key)
        throws NoSuchAlgorithmException, InvalidKeySpecException, DecoderException {
        // 用相同的盐值对用户输入的密码进行加密
        String result = getPBKDF2(password, salt);
        // 把加密后的密文和原密文进行比较，相同则验证成功，否则失败
        return result.equals(key);
    }

    /**
     * 功能描述: 生成密文
     *
     * @param password 密码
     * @param salt     盐
     * @return {@link String}
     * @method getPBKDF2
     * @date 2021/4/19 17:23
     */
    public static String getPBKDF2(String password, String salt) throws NoSuchAlgorithmException,
        InvalidKeySpecException, DecoderException {
        //将16进制字符串形式的salt转换成byte数组
        byte[] bytes = Hex.decodeHex(salt.toCharArray());
        KeySpec spec = new PBEKeySpec(password.toCharArray(), bytes, PBKDF2_ITERATIONS, HASH_SIZE * 4);
        SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = secretKeyFactory.generateSecret(spec).getEncoded();
        //将byte数组转换为16进制的字符串
        return Hex.encodeHexString(hash);
    }

    /**
     * 功能描述: 生成随机盐
     *
     * @return {@link String}
     * @method getSalt
     * @date 2021/4/19 17:23
     */
    public static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] bytes = new byte[SALT_SIZE / 2];
        sr.nextBytes(bytes);
        //将byte数组转换为16进制的字符串
        return Hex.encodeHexString(bytes);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, DecoderException {
        String pwd = "123456";
        System.out.println(String.format("密码：%s", pwd));
        //随机盐
        String salt = getSalt();
        System.out.println(String.format("生成的随机密钥：%s", salt));
        //加密
        final String pbkdf2 = getPBKDF2(pwd, salt);
        System.out.println(String.format("PBKDF2加密结果：%s", pbkdf2));
        // 验证
        final boolean verify = verify(pwd, salt, pbkdf2);
        System.out.println(String.format("密码：%s 对{密钥：%s, 密文：%s}的验证结果：%s", pwd, salt, pbkdf2, verify));
    }
}
