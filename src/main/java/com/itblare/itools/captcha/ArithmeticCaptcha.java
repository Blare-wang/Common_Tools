package com.itblare.itools.captcha;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.captcha
 * ClassName:   ArithmeticCaptcha
 * Author:   Blare
 * Date:     Created in 2021/4/24 17:56
 * Description:    算术验证码
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/24 17:56    1.0.0         算术验证码
 */

import javax.imageio.ImageIO;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import static com.itblare.itools.captcha.ImageCapHelper.*;
import static com.itblare.itools.randoms.RandomsProcessor.number;

/**
 * 一句话功能简述：算术验证码
 *
 * @author Blare
 * @create 2021/4/24 17:56
 * @since 1.0.0
 */
public class ArithmeticCaptcha extends AbstractCaptcha {

    private String arithmeticString;  // 计算公式

    public ArithmeticCaptcha() {
    }

    public ArithmeticCaptcha(int width, int height) {
        this();
        setWidth(width);
        setHeight(height);
    }

    public ArithmeticCaptcha(int width, int height, int len) {
        this(width, height);
        setLength(len);
    }

    public ArithmeticCaptcha(int width, int height, int len, Font font) {
        this(width, height, len);
        setFont(font);
    }

    @Override
    protected char[] alphas() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(number(10));
            if (i < length - 1) {
                int type = number(1, 4);
                if (type == 1) {
                    sb.append("+");
                } else if (type == 2) {
                    sb.append("-");
                } else if (type == 3) {
                    sb.append("x");
                }
            }
        }
        // ScriptEngineManager作为java脚本引擎，可编译、执行python、js等多种语言脚本，让脚本开发人员不再受限于某一种语言
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        try {
            // 执行算术脚本
            chars = String.valueOf(engine.eval(sb.toString().replaceAll("x", "*")));
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        sb.append("=?");
        arithmeticString = sb.toString();
        return chars.toCharArray();
    }

    @Override
    protected char[] alphas(int len) {
        return alphas();
    }

    @Override
    public boolean out(OutputStream os) {
        checkAlpha();
        return graphicsImage(getArithmeticString().toCharArray(), os);
    }

    @Override
    public String toBase64() {
        return toBase64("data:image/png;base64,");
    }

    public String getArithmeticString() {
        checkAlpha();
        return arithmeticString;
    }

    public void setArithmeticString(String arithmeticString) {
        this.arithmeticString = arithmeticString;
    }

    /**
     * 功能描述: 生成验证码图片
     *
     * @param strs ？？
     * @param out  验证码输出流
     * @return {@link boolean}
     * @method graphicsImage
     * @author Blare
     * @date 2021/4/24 18:06
     * @updator Blare
     */
    private boolean graphicsImage(char[] strs, OutputStream out) {
        try {
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = (Graphics2D) bi.getGraphics();
            // 填充背景
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, width, height);
            // 抗锯齿
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // 画干扰圆
            drawBesselLine(g2d, 2, getRandomColor(), width, height);
            // 画字符串
            g2d.setFont(getFont());
            FontMetrics fontMetrics = g2d.getFontMetrics();
            int fW = width / strs.length;  // 每一个字符所占的宽度
            int fSp = (fW - (int) fontMetrics.getStringBounds("8", g2d).getWidth()) / 2;  // 字符的左右边距
            for (int i = 0; i < strs.length; i++) {
                g2d.setColor(getNormalFontColor());
                int fY = height - ((height - (int) fontMetrics.getStringBounds(String.valueOf(strs[i]), g2d).getHeight()) >> 1);  // 文字的纵坐标
                g2d.drawString(String.valueOf(strs[i]), i * fW + fSp + 3, fY - 3);
            }
            g2d.dispose();
            ImageIO.write(bi, "png", out);
            out.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void main(String[] args) {
        final ArithmeticCaptcha arithmeticCaptcha = new ArithmeticCaptcha();
        // 生产算术式，并返回式子结果
        final char[] alphas = arithmeticCaptcha.alphas();
        System.out.println(String.valueOf(alphas));
        // 生产验证码base64
        final String toBase64 = arithmeticCaptcha.toBase64();
        System.out.println(toBase64);
    }
}
