package com.itblare.itools.captcha;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.captcha
 * ClassName:   ChineseGifCaptcha
 * Author:   Blare
 * Date:     Created in 2021/4/24 18:38
 * Description:    中文动态验证码
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/24 18:38    1.0.0         中文动态验证码
 */

import com.itblare.itools.image.GifEncoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import static com.itblare.itools.captcha.ImageCapHelper.graphicsChineseGifImage;

/**
 * 一句话功能简述：中文动态验证码
 *
 * @author Blare
 * @create 2021/4/24 18:38
 * @since 1.0.0
 */
public class ChineseGifCaptcha extends AbstractChineseCaptcha {

    public ChineseGifCaptcha() {
        setFont(new Font("宋体", Font.BOLD, 24));
    }

    public ChineseGifCaptcha(int width, int height) {
        setWidth(width);
        setHeight(height);
        setFont(new Font("宋体", Font.BOLD, 24));
    }

    public ChineseGifCaptcha(int width, int height, int len) {
        this(width, height);
        setLength(len);
        setFont(new Font("宋体", Font.BOLD, 24));
    }

    public ChineseGifCaptcha(int width, int height, int len, Font font) {
        this(width, height, len);
        setFont(font);
    }

    public ChineseGifCaptcha(int width, int height, int length, Integer interLine, Integer interOval, Integer besselLine, boolean randomLocation, Color backColor, Font font, Color fontColor, Color lineColor, Color ovalColor, Color besselLineColor, float noiseRate) {
        this(width, height, length, font);
        setInterLine(interLine);
        setInterOval(interOval);
        setBesselLine(besselLine);
        setRandomLocation(randomLocation);
        setBackColor(backColor);
        setLineColor(lineColor);
        setOvalColor(ovalColor);
        setBesselLineColor(besselLineColor);
        setNoiseRate(noiseRate);
    }

    @Override
    public boolean out(OutputStream os) {
        try {
            char[] stars = textChar();  // 获取验证码数组
            int[][] besselXY = new int[4][];
            GifEncoder gifEncoder = createGif(os, besselXY);
            for (int i = 0; i < length; i++) {
                BufferedImage frame = graphicsImage(stars, i, besselXY);
                gifEncoder.addFrame(frame);
                frame.flush();
            }
            gifEncoder.finish();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public String toBase64() {
        return toBase64("data:image/gif;base64,");
    }

    /**
     * 功能描述: 画随机码图
     *
     * @param charArr  字符数组
     * @param flag     透明度
     * @param besselXY 干扰线参数
     * @return {@link BufferedImage}
     * @method graphicsImage
     * @date 2021/4/24 18:40
     */
    private BufferedImage graphicsImage(char[] charArr, int flag, int[][] besselXY) {
        return graphicsChineseGifImage(
            charArr,
            width,
            height,
            getInterLine(),
            getInterOval(),
            null,
            isRandomLocation(),
            getBackColor(),
            getFont(),
            getFontColor(),
            getLineColor(),
            getOvalColor(),
            null,
            getNoiseRate(),
            true,
            flag,
            besselXY
        );
    }

    /**
     * 功能描述: 获取透明度,从0到1,自动计算步长
     *
     * @return {@link float}
     * @method getAlpha
     * @date 2021/4/24 18:40
     */
    private float getAlpha(int i, int j) {
        int num = i + j;
        float r = (float) 1 / (length - 1);
        float s = length * r;
        return num >= length ? (num * r - s) : num * r;
    }

    public static void main(String[] args) {
        final ChineseGifCaptcha chineseCaptcha = new ChineseGifCaptcha(
            80,
            30,
            5,
            3,
            3,
            3,
            true,
            Color.WHITE,
            new Font("宋体", Font.BOLD, 24),
            null,
            null,
            null,
            null,
            0.1f
        );
        final char[] alphas = chineseCaptcha.alphas();
        System.out.println(String.valueOf(alphas));
        final String s = chineseCaptcha.toBase64();
        System.out.println(s);
    }
}