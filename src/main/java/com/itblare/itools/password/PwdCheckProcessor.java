package com.itblare.itools.password;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.password
 * ClassName:   PwdCheckProcessor
 */

import java.util.Objects;

/**
 * 弱密码检测
 *
 * @author Blare
 * @version 1.0.0
 * @since 2021/10/12 14:58
 */
public class PwdCheckProcessor {

    public static String[] KEYBOARD_SLOPE_ARR = {
            "!qaz", "1qaz", "@wsx", "2wsx", "#edc", "3edc", "$rfv", "4rfv", "%tgb", "5tgb",
            "^yhn", "6yhn", "&ujm", "7ujm", "*ik,", "8ik,", "(ol.", "9ol.", ")p;/", "0p;/",
            "+[;.", "=[;.", "_pl,", "-pl,", ")okm", "0okm", "(ijn", "9ijn", "*uhb", "8uhb",
            "&ygv", "7ygv", "^tfc", "6tfc", "%rdx", "5rdx", "$esz", "4esz"
    };
    public static String[] KEYBOARD_HORIZONTAL_ARR = {
            "01234567890-=",
            "!@#$%^&*()_+",
            "qwertyuiop[]",
            "QWERTYUIOP{}",
            "asdfghjkl;'",
            "ASDFGHJKL:",
            "zxcvbnm,./",
            "ZXCVBNM<>?",
    };

    public static String DEFAULT_SPECIAL_CHAR = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
    public static String SPECIAL_CHAR = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";

    /**
     * 检测密码中字符长度 ，符合长度要求则返回true
     *
     * @param password 密码字符串
     * @param minNum   最小长度
     * @param maxNum   最大长度
     * @return {@link boolean}
     * @author Blare
     */
    public static boolean checkPasswordLength(String password, Integer minNum, Integer maxNum) {
        boolean flag = false;
        if (Objects.isNull(maxNum)) {
            minNum = Objects.isNull(minNum) ? 0 : minNum;
            if (password.length() >= minNum) {
                flag = true;
            }
        } else {
            minNum = Objects.isNull(minNum) ? 0 : minNum;
            if (password.length() >= minNum && password.length() <= maxNum) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 检测密码中是否包含数字，包含数字则返回true
     *
     * @param password 密码字符串
     * @return {@link boolean}
     * @author Blare
     */
    public static boolean checkContainDigit(String password) {
        for (char pass : password.toCharArray()) {
            if (Character.isDigit(pass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测密码中是否包含字母（不区分大小写）,包含则返回true
     *
     * @param password 密码字符串
     * @return {@link boolean}
     * @author Blare
     */
    public static boolean checkContainCase(String password) {
        for (char pass : password.toCharArray()) {
            if (Character.isLetter(pass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测密码中是否包含小写字母，包含则返回tru
     *
     * @param password 密码字符串
     * @return {@link boolean}
     * @author Blare
     */
    public static boolean checkContainLowerCase(String password) {
        for (char pass : password.toCharArray()) {
            if (Character.isLowerCase(pass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测密码中是否包含大写字母，包含则返回true
     *
     * @param password 密码字符串
     * @return {@link boolean}
     * @author Blare
     */
    public static boolean checkContainUpperCase(String password) {
        for (char pass : password.toCharArray()) {
            if (Character.isUpperCase(pass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检测密码中是否包含特殊符号，包含则返回true
     *
     * @param password 密码字符串
     * @return {@link boolean}
     * @author Blare
     */
    public static boolean checkContainSpecialChar(String password) {
        for (char pass : password.toCharArray()) {
            if (SPECIAL_CHAR.indexOf(pass) != -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * 键盘规则匹配器 横向连续检测
     * 检测密码中是否含有横向连续字符串，含有则返回true
     *
     * @param password    密码字符串
     * @param repetitions 连续字符数
     * @param isLower     是否区分大小写 true:区分大小写， false:不区分大小写
     * @return {@link boolean}
     * @author Blare
     */
    public static boolean checkLateralKeyboardSite(String password, int repetitions, boolean isLower) {
        String t_password = password;
        //将所有输入字符转为小写
        t_password = t_password.toLowerCase();
        int n = t_password.length();
        /*
         * 键盘横向规则检测
         */
        int arrLen = KEYBOARD_HORIZONTAL_ARR.length;
        return check(password, repetitions, isLower, t_password, n, KEYBOARD_HORIZONTAL_ARR);
    }

    /**
     * 物理键盘，斜向连接校验， 如1qaz,4rfv, !qaz,@WDC,zaq1 返回true
     *
     * @param password    字符串
     * @param repetitions 重复次数
     * @param isLower     是否区分大小写 true:区分大小写， false:不区分大小写
     * @return {@link boolean}
     * @author Blare
     */
    public static boolean checkKeyboardSlantSite(String password, int repetitions, boolean isLower) {
        String t_password = password;
        t_password = t_password.toLowerCase();
        int n = t_password.length();
        /*
         * 键盘斜线方向规则检测
         */
        int arrLen = KEYBOARD_SLOPE_ARR.length;
        return check(password, repetitions, isLower, t_password, n, KEYBOARD_SLOPE_ARR);
    }

    private static boolean check(String password, int repetitions, boolean isLower, String t_password, int n, String[] keyboardSlopeArr) {
        for (int i = 0; i + repetitions <= n; i++) {
            String str = t_password.substring(i, i + repetitions);
            String distinguishStr = password.substring(i, i + repetitions);
            for (String configStr : keyboardSlopeArr) {
                String revOrderStr = new StringBuffer(configStr).reverse().toString();
                //检测包含字母(区分大小写)
                if (isLower) {
                    //考虑 大写键盘匹配的情况
                    String UpperStr = configStr.toUpperCase();
                    if ((configStr.contains(distinguishStr)) || (UpperStr.contains(distinguishStr))) {
                        return true;
                    }
                    //考虑逆序输入情况下 连续输入
                    String revUpperStr = new StringBuffer(UpperStr).reverse().toString();
                    if ((revOrderStr.contains(distinguishStr)) || (revUpperStr.contains(distinguishStr))) {
                        return true;
                    }
                } else {
                    if (configStr.contains(str)) {
                        return true;
                    }
                    //考虑逆序输入情况下 连续输入
                    if (revOrderStr.contains(str)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 评估a-z,z-a这样的连续字符,含有a-z,z-a连续字符串 返回true
     *
     * @param password    字符串
     * @param repetitions 连续个数
     * @param isLower     是否区分大小写 true:区分大小写， false:不区分大小写
     * @return {@link boolean}
     * @author Blare
     */
    public static boolean checkSequentialChars(String password, int repetitions, boolean isLower) {
        String t_password = password;
        boolean flag = false;
        int normal_count;
        int reversed_count;
        //检测包含字母(区分大小写)
        if (!isLower) {
            t_password = t_password.toLowerCase();
        }
        int n = t_password.length();
        char[] pwdCharArr = t_password.toCharArray();

        for (int i = 0; i + repetitions <= n; i++) {
            normal_count = 0;
            reversed_count = 0;
            for (int j = 0; j < repetitions - 1; j++) {
                if (pwdCharArr[i + j + 1] - pwdCharArr[i + j] == 1) {
                    normal_count++;
                    if (normal_count == repetitions - 1) {
                        return true;
                    }
                }

                if (pwdCharArr[i + j] - pwdCharArr[i + j + 1] == 1) {
                    reversed_count++;
                    if (reversed_count == repetitions - 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 验证键盘上是否存在多个连续重复的字符， 如！！！！, qqqq, 1111, ====, AAAA返回true
     *
     * @param password    字符串
     * @param repetitions 重复次数
     * @return {@link boolean}
     * @author Blare
     */
    public static boolean checkSequentialSameChars(String password, int repetitions) {
        int n = password.length();
        char[] pwdCharArr = password.toCharArray();
        boolean flag = false;
        int count;
        for (int i = 0; i + repetitions <= n; i++) {
            count = 0;
            for (int j = 0; j < repetitions - 1; j++) {
                if (pwdCharArr[i + j] == pwdCharArr[i + j + 1]) {
                    count++;
                    if (count == repetitions - 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        //System.out.print(checkSequentialSameChars("AAAAA", 5));
        //System.out.print(checkSequentialChars("Abcks", 3, true));
        //System.out.print(checkKeyboardSlantSite("Qaz", 3, false));
        //System.out.print(checkLateralKeyboardSite("qwer", 3, false));
    }
}