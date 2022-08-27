package com.itblare.itools.captcha;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.captcha
 * ClassName:   ChineseCaptcha
 * Author:   Blare
 * Date:     Created in 2021/4/24 18:26
 * Description:    中文验证码
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/24 18:26    1.0.0         中文验证码
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import static com.itblare.itools.captcha.ImageCapHelper.getRandomColor;
import static com.itblare.itools.captcha.ImageCapHelper.graphicsChineseImage;

/**
 * 中文验证码
 *
 * @author Blare
 * @create 2021/4/24 18:26
 * @since 1.0.0
 */
public class ChineseCaptcha extends AbstractChineseCaptcha {

    public ChineseCaptcha() {
        super();
        setFont(new Font("宋体", Font.BOLD, 24));
    }

    public ChineseCaptcha(int width, int height) {
        this();
        setFont(new Font("宋体", Font.BOLD, 24));
        setWidth(width);
        setHeight(height);
    }

    public ChineseCaptcha(int width, int height, int len) {
        this(width, height);
        setFont(new Font("宋体", Font.BOLD, 24));
        setLength(len);
    }

    public ChineseCaptcha(int width, int height, int len, Font font) {
        this(width, height, len);
        setFont(font);
    }

    public ChineseCaptcha(int width, int height, int length, Integer interLine, Integer interOval, Integer besselLine, boolean randomLocation, Color backColor, Font font, Color fontColor, Color lineColor, Color ovalColor, Color besselLineColor, float noiseRate, boolean distortion) {
        this(width, height, length, font);
        setInterLine(interLine);
        setInterOval(interOval);
        setBesselLine(besselLine);
        setRandomLocation(randomLocation);
        setFontColor(fontColor);
        setBackColor(backColor);
        setLineColor(lineColor);
        setOvalColor(ovalColor);
        setBesselLineColor(besselLineColor);
        setNoiseRate(noiseRate);
        setDistortion(distortion);
    }

    @Override
    public boolean out(OutputStream out) {
        return graphicsImage(textChar(), out);
    }

    @Override
    public String toBase64() {
        return toBase64("data:image/png;base64,");
    }

    /**
     * 生成验证码图形
     *
     * @param charArr 验证码的字符数组
     * @param out     输出流
     * @return {@link boolean}
     * @method graphicsImage
     * @date 2021/4/24 18:34
     */
    private boolean graphicsImage(char[] charArr, OutputStream out) {
        try {
            BufferedImage image = graphicsChineseImage(
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
                isDistortion(),
                true
            );

            ImageIO.write(image, "png", out);
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
        final ChineseCaptcha chineseCaptcha = new ChineseCaptcha(
            80,
            30,
            5,
            3,
            3,
            3,
            true,
            Color.WHITE,
            new Font("宋体", Font.BOLD, 24),
            getRandomColor(),
            null,
            null,
            null,
            0.1f,
            false
        );
        final char[] alphas = chineseCaptcha.alphas();
        System.out.println(String.valueOf(alphas));
        final String s = chineseCaptcha.toBase64();
        System.out.println(s);
    }
}