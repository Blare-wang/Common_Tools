package com.itblare.itools.captcha;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.captcha
 * ClassName:   AbstractChineseCaptcha
 * Author:   Blare
 * Date:     Created in 2021/4/24 18:27
 * Description:    中文验证码抽象类
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/24 18:27    1.0.0         中文验证码抽象类
 */

import java.awt.*;

import static com.itblare.itools.captcha.ImageCapHelper.alphaHan;

/**
 * 中文验证码抽象类
 *
 * @author Blare
 * @create 2021/4/24 18:27
 * @since 1.0.0
 */
public abstract class AbstractChineseCaptcha extends AbstractCaptcha {

    /**
     * 初始化
     */
    public AbstractChineseCaptcha() {
        setFont(new Font("楷体", Font.PLAIN, 28));
        setLength(4);
    }

    @Override
    protected char[] alphas() {
        return alphas(-1);
    }

    @Override
    protected char[] alphas(int len) {
        if (0 > len)
            len = this.length;
        char[] cs = new char[len];
        for (int i = 0; i < len; i++) {
            cs[i] = alphaHan();
        }
        chars = new String(cs);
        return cs;
    }
}