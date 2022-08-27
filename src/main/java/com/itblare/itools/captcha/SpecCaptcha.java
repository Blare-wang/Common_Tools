package com.itblare.itools.captcha;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.captcha
 * ClassName:   SpecCaptcha
 * Author:   Blare
 * Date:     Created in 2021/4/24 18:58
 * Description:    图片验证码
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/24 18:58    1.0.0         图片验证码
 */

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import static com.itblare.itools.captcha.ImageCapHelper.generateImageCode;

/**
 * 图片验证码
 *
 * @author Blare
 * @create 2021/4/24 18:58
 * @since 1.0.0
 */
public class SpecCaptcha extends AbstractCaptcha {

    public SpecCaptcha() {
    }

    public SpecCaptcha(int width, int height) {
        this();
        setWidth(width);
        setHeight(height);
    }

    public SpecCaptcha(int width, int height, int length) {
        this(width, height);
        setLength(length);
    }

    public SpecCaptcha(int width, int height, int length, Font font) {
        this(width, height, length);
        setFont(font);
    }

    public SpecCaptcha(int width, int height, int length, int interLine, boolean randomLocation, Color backColor, Font font, Color fontColor, Color lineColor, float noiseRate, boolean distortion) {
        this(width, height, length, font);
        setInterLine(interLine);
        setRandomLocation(randomLocation);
        setBackColor(backColor);
        setLineColor(lineColor);
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
     * @param charArr 验证码
     * @param out     输出流
     * @return {@link boolean}
     * @method graphicsImage
     * @date 2021/4/24 19:00
     */
    private boolean graphicsImage(char[] charArr, OutputStream out) {
        try {
            BufferedImage image = generateImageCode(
                charArr,
                width,
                height,
                getInterLine(),
                null,
                isRandomLocation(),
                getBackColor(),
                getFont(),
                getFontColor(),
                getLineColor(),
                null,
                getNoiseRate(),
                isDistortion(),
                true);
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
        final SpecCaptcha specCaptcha = new SpecCaptcha(
            80,
            30,
            5,
            3,
            true,
            Color.GRAY,
            null,
            null,
            null,
            0.01f,
            false
        );
        final char[] alphas = specCaptcha.alphas();
        System.out.println(String.valueOf(alphas));
        final String s = specCaptcha.toBase64();
        System.out.println(s);
    }
}