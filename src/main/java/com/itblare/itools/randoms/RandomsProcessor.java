package com.itblare.itools.randoms;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.randoms
 * ClassName:   RandomsProcessor
 * Author:   Blare
 * Date:     Created in 2021/4/24 17:10
 * Description:    随机字符生成器
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/24 17:10    1.0.0         随机字符生成器
 */

import java.security.SecureRandom;

/**
 * 一句话功能简述：随机字符生成器
 *
 * @author Blare
 * @create 2021/4/24 17:10
 * @since 1.0.0
 */
public class RandomsProcessor {

    /**
     * 真随机数，相对伪随机数Random
     * 注：SecureRandom的安全性是通过操作系统提供的安全的随机种子来生成随机数。这个种子是通过CPU的热噪声、读写磁盘的字节、网络流量等各种随机事件产生的“熵”。
     */
    protected static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 定义验证码字符.去除了0、O、I、L等容易混淆的字母
     */
    public static final char[] ALPHA = {'2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'M', 'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    };

    /**
     * 自定义字符长度
     */
    protected static int alphaLength = ALPHA.length;

    /**
     * 数字的最大索引
     */
    public static final int numMaxIndex = 7;

    /**
     * 字符的最小索引
     */
    public static final int charMinIndex = numMaxIndex + 1;

    /**
     * 字符的最大索引
     */
    public static final int charMaxIndex = alphaLength - 1;

    /**
     * 大写字符最小索引
     */
    public static final int upperMinIndex = charMinIndex;

    /**
     * 大写字符最大索引
     */
    public static final int upperMaxIndex = upperMinIndex + 23;

    /**
     * 小写字母最小索引
     */
    public static final int lowerMinIndex = upperMaxIndex + 1;

    /**
     * 小写字母最大索引
     */
    public static final int lowerMaxIndex = charMaxIndex;

    /**
     * 功能描述: 产生两个数之间的随机数
     *
     * @param min 最小值
     * @param max 最大值
     * @return {@link int}
     * @method number
     * @date 2021/4/24 17:19
     */
    public static int number(int min, int max) {
        return min + RANDOM.nextInt(max - min);
    }

    /**
     * 功能描述: 产生0-num的随机数,不包括num
     *
     * @param num 最大值
     * @return {@link int}
     * @method number
     * @date 2021/4/24 17:20
     */
    public static int number(int num) {
        return RANDOM.nextInt(num);
    }

    /**
     * 功能描述: 获取ALPHA中的随机字符
     *
     * @return {@link char}
     * @method alpha
     * @date 2021/4/24 17:23
     */
    public static char alpha() {
        return ALPHA[number(alphaLength)];
    }

    /**
     * 功能描述: 获取ALPHA中第0位到第num位的随机字符
     *
     * @param number 到第几位结束
     * @return {@link char}
     * @method
     * @date 2021/4/24 17:23
     */
    public static char alpha(int number) {
        return ALPHA[number(number)];
    }

    /**
     * 功能描述: 获取ALPHA中第min位到第max位的随机字符
     *
     * @param min 从第几位开始
     * @param max 到第几位结束
     * @return {@link char}
     * @method alpha
     * @date 2021/4/24 17:24
     */
    public static char alpha(int min, int max) {
        return ALPHA[number(min, max)];
    }
}
