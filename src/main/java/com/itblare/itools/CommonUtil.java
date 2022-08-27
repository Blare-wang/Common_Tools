package com.itblare.itools;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools
 * Author:   Created by Blare
 * Date:     Created in 2021/5/1 18:40
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/5/1 18:40      1.0.0              高频工具方法
 */

import com.qiniu.common.Constants;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 高频工具方法
 *
 * @author Blare
 * @create 2021/5/1 18:40
 * @since 1.0.0
 */
public class CommonUtil {

    /**
     * 获取指定位数的随机数
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     * @date 2020/11/22
     */
    public static String getRandomString(int length) {
        String base = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789./";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 讲空格和特殊字符进行替代
     *
     * @param src 待处理字符串
     * @date 2020/11/22
     */
    public static String replaceSpaceAndSpecialCharacters(String src) {
        if (null == src || "".equals(src)) {
            return "";
        }
        String newStr = src.replace(" ", "");
        return removeSpecialCharacter(newStr, "");
    }

    /**
     * 特殊字符统一替换
     *
     * @param character   待处理字符串
     * @param replacement 替代字符
     * @date 2020/11/22
     */
    public static String removeSpecialCharacter(String character, String replacement) {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(character);
        return m.replaceAll(replacement).trim();
    }

    /**
     * 将特殊字符 替换为 '-'
     *
     * @method filterUnsafeUrlCharts
     * @param str 待处理字符串
     * @return {@link String}
     * @date 2021/4/17 23:37
     */
    public static String filterUnsafeUrlCharts(String str) {
        if (Objects.isNull(str) || "".equals(str)) {
            return str;
        }
        String regEx = "[<>\"#%{}^\\[\\]`\\s\\\\]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("-").trim();
    }

    /**
     * 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）并拼接成字符
     *
     * @method sortMap
     * @param paramsMap 待处理集合
     * @return {@link String}
     * @date 2021/5/1 18:33
     */
    public static String sortMap(Map<String, Object> paramsMap) {
        List<Map.Entry<String, Object>> infoIds = new ArrayList<>(paramsMap.entrySet());
        infoIds.sort(Map.Entry.comparingByKey());
        // 构造URL 键值对的格式
        StringBuilder buf = new StringBuilder();
        for (Map.Entry<String, Object> item : infoIds) {
            if ((Objects.nonNull(item.getKey()) && !"".equals(item.getKey())) && item.getValue() != null) {
                String key = item.getKey();
                String val = item.getValue().toString();
                buf.append(key).append("=").append(val);
                buf.append("&");
            }
        }
        String buff = buf.toString();
        if (!buff.isEmpty()) {
            buff = buff.substring(0, buff.length() - 1);
        }
        return buff;
    }

    public static byte[] utf8Bytes(String data) {
        return data.getBytes(Constants.UTF_8);
    }

    public static String utf8String(byte[] data) {
        return new String(data, Constants.UTF_8);
    }
}