package com.itblare.itools.captcha;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.captcha
 * ClassName:   AbstractCaptcha
 * Author:   Blare
 * Date:     Created in 2021/4/24 17:05
 * Description:    验证码抽象类
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/24 17:05    1.0.0         验证码抽象类
 */

import com.itblare.itools.image.GifEncoder;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Base64;

import static com.itblare.itools.captcha.ImageCapHelper.getCapCode;
import static com.itblare.itools.captcha.ImageCapHelper.getRandomFont;
import static com.itblare.itools.randoms.RandomsProcessor.number;

/**
 * 验证码抽象类
 *
 * @author Blare
 * @create 2021/4/24 17:05
 * @since 1.0.0
 */
public abstract class AbstractCaptcha {

    // 内置字体
    private Font font = null; // 验证码的字体

    // 验证码描述
    protected int width = 130; // 验证码显示宽度
    protected int height = 48; // 验证码显示高度
    protected Integer interLine = 0; // 验证码干扰线的条数
    protected Integer interOval = 0; // 验证码干扰圆的个数
    protected Integer besselLine = 0; // 验证码干扰圆的个数
    protected Color lineColor = null; // 干扰线颜色
    protected Color ovalColor = null; // 干扰圆颜色
    protected Color backColor = Color.WHITE; // 图片颜色
    protected Color besselLineColor = null; // 贝塞尔曲线颜色
    protected Color fontColor = null; // 字体颜色
    protected float noiseRate = 0.02F; // 噪点率
    protected boolean distortion = false; // 是否图形变形
    protected boolean randomLocation = false; // 验证码每个字符的高低位置是否随机

    protected int length = 5; // 验证码随机字符长度
    protected int charType = 0;  // 验证码类型
    protected String chars = null; // 当前验证码

    /**
     * 生成随机验证码
     *
     * @return {@link char[]}
     * @method alphas
     * @date 2021/4/24 18:02
     */
    protected char[] alphas() {
        return alphas(-1);
    }

    /**
     * 生成随机验证码
     *
     * @param len 验证码长度
     * @return {@link char[]}
     * @method alphas
     * @date 2021/4/24 18:01
     */
    protected char[] alphas(int len) {
        if (len < 1) {
            len = length;
        }
        String capCode = getCapCode(charType, len);
        this.chars = capCode;
        return capCode.toCharArray();
    }

    /**
     * 验证码流输出
     *
     * @param os 输出流
     * @return {@link boolean}
     * @method out
     * @date 2021/4/24 17:40
     */
    public abstract boolean out(OutputStream os);

    /**
     * 验证码base64输出
     *
     * @return {@link String}
     * @method toBase64
     * @date 2021/4/24 17:41
     */
    public abstract String toBase64();

    /**
     * 输出base64编码
     *
     * @param type 编码头，eg：data:image/png;base64,
     * @return {@link String}
     * @method toBase64
     * @date 2021/4/24 17:42
     */
    public String toBase64(String type) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        out(outputStream);
        return type + Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    /**
     * 获取当前的验证码
     *
     * @return {@link String}
     * @method text
     * @date 2021/4/24 17:45
     */
    public String text() {
        checkAlpha();
        return chars;
    }

    /**
     * 获取当前验证码的字符数组
     *
     * @return {@link char[]}
     * @method textChar
     * @date 2021/4/24 17:45
     */
    public char[] textChar() {
        checkAlpha();
        return chars.toCharArray();
    }

    /**
     * 检查验证码是否生成，没有则立即生成
     *
     * @method checkAlpha
     * @date 2021/4/24 17:45
     */
    public void checkAlpha() {
        if (chars == null) {
            alphas(-1); // 生成验证码
        }
    }

    /**
     * 检查验证码是否生成，没有则立即生成
     *
     * @param length 指定长度
     * @method checkAlpha
     * @date 2021/4/24 17:55
     */
    public void checkAlpha(int length) {
        this.length = length;
        if (chars == null) {
            alphas(length); // 生成验证码
        }
    }

    /**
     * GIF 生成
     *
     * @param os       输出流
     * @param besselXY 绘制立方曲线坐标
     * @return {@link GifEncoder}
     * @method createGif
     * @date 2021/4/26 1:40
     */
    public GifEncoder createGif(OutputStream os, int[][] besselXY) {
        // 随机生成贝塞尔曲线参数
        int x1 = 5, y1 = number(5, height / 2);
        int x2 = width - 5, y2 = number(height / 2, height - 5);
        if (number(2) == 0) {
            int ty = y1;
            y1 = y2;
            y2 = ty;
        }
        int ctrlx = number(width / 4, width / 4 * 3), ctrly = number(5, height - 5);
        int ctrlx1 = number(width / 4, width / 4 * 3), ctrly1 = number(5, height - 5);
        besselXY[0] = new int[] {x1, y1};
        besselXY[1] = new int[] {ctrlx, ctrly};
        besselXY[2] = new int[] {ctrlx1, ctrly1};
        besselXY[3] = new int[] {x2, y2};
        // 开始画gif每一帧
        GifEncoder gifEncoder = new GifEncoder();
        gifEncoder.setQuality(180);
        gifEncoder.setDelay(500);
        gifEncoder.setRepeat(0);
        gifEncoder.start(os);
        return gifEncoder;
    }

    public Font getFont() {
        if (font == null) {
            try {
                setFont(getRandomFont(24));
            } catch (Exception e) {
                setFont(new Font("Arial", Font.BOLD, 32));
            }
        }
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Integer getInterLine() {
        return interLine;
    }

    public void setInterLine(Integer interLine) {
        this.interLine = interLine;
    }

    public Integer getInterOval() {
        return interOval;
    }

    public void setInterOval(Integer interOval) {
        this.interOval = interOval;
    }

    public Integer getBesselLine() {
        return besselLine;
    }

    public void setBesselLine(Integer besselLine) {
        this.besselLine = besselLine;
    }

    public Color getLineColor() {
        return lineColor;
    }

    public void setLineColor(Color lineColor) {
        this.lineColor = lineColor;
    }

    public Color getOvalColor() {
        return ovalColor;
    }

    public void setOvalColor(Color ovalColor) {
        this.ovalColor = ovalColor;
    }

    public Color getBackColor() {
        return backColor;
    }

    public void setBackColor(Color backColor) {
        this.backColor = backColor;
    }

    public Color getBesselLineColor() {
        return besselLineColor;
    }

    public void setBesselLineColor(Color besselLineColor) {
        this.besselLineColor = besselLineColor;
    }

    public Color getFontColor() {
        return fontColor;
    }

    public void setFontColor(Color fontColor) {
        this.fontColor = fontColor;
    }

    public float getNoiseRate() {
        return noiseRate;
    }

    public void setNoiseRate(float noiseRate) {
        this.noiseRate = noiseRate;
    }

    public boolean isDistortion() {
        return distortion;
    }

    public void setDistortion(boolean distortion) {
        this.distortion = distortion;
    }

    public boolean isRandomLocation() {
        return randomLocation;
    }

    public void setRandomLocation(boolean randomLocation) {
        this.randomLocation = randomLocation;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getCharType() {
        return charType;
    }

    public void setCharType(int charType) {
        this.charType = charType;
    }

    public String getChars() {
        return chars;
    }

    public void setChars(String chars) {
        this.chars = chars;
    }
}