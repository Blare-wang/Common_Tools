package com.itblare.itools.captcha;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.tools.captcha
 * ClassName:   ImageCaptcha
 * Author:   Blare
 * Date:     Created in 2021/4/13 16:53
 * Description:    图片验证码
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/13 16:53    1.0.0             图片验证码
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;

/**
 * 一句话功能简述：图片验证码
 *
 * @author Blare
 * @create 2021/4/13 16:53
 * @since 1.0.0
 */
public class ImageCaptcha {

    private static final Logger logger = LoggerFactory.getLogger(ImageCaptcha.class);

    /**
     * 验证码类型为仅数字,即0~9
     */
    public static final int TYPE_NUM_ONLY = 0;

    /**
     * 验证码类型为仅字母,即大小写字母混合
     */
    public static final int TYPE_LETTER_ONLY = 1;

    /**
     * 验证码类型为数字和大小写字母混合
     */
    public static final int TYPE_ALL_MIXED = 2;

    /**
     * 验证码类型为数字和大写字母混合
     */
    public static final int TYPE_NUM_UPPER = 3;

    /**
     * 验证码类型为数字和小写字母混合
     */
    public static final int TYPE_NUM_LOWER = 4;

    /**
     * 验证码类型为仅大写字母
     */
    public static final int TYPE_UPPER_ONLY = 5;

    /**
     * 验证码类型为仅小写字母
     */
    public static final int TYPE_LOWER_ONLY = 6;

    /**
     * 噪点率
     */
    public static final float NOISE_RATE = 0.02F;

    /**
     * 字体
     */
    private static final String[] FONT_NAMES = {"Default", "宋体", "华文楷体", "黑体", "微软雅黑", "楷体_GB2312"};

    /**
     * 安全随机数
     */
    private static final SecureRandom random = new SecureRandom();

    /**
     * 功能描述: 生成随机颜色
     *
     * @return {@link Color}
     * @method generateRandomColor
     * @author Blare
     * @date 2020/10/31 15:29
     * @updator Blare
     */
    public static Color generateRandomColor() {
        /*
            任何颜色都是由三原色组成（RGB）,
            JAVA中支持224为彩色，
            即红绿蓝分量取值介于0-255之间（8位表示）
         */
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    /**
     * 功能描述: 生成随机字体
     *
     * @param size 字体大小
     * @return {@link Font}
     * @method randomFont
     * @author Blare
     * @date 2020/10/31 17:09
     * @updator Blare
     */
    private static Font generateRandomFont(int size) {
        //获取随机的字体
        int index = random.nextInt(FONT_NAMES.length);
        String fontName = FONT_NAMES[index];
        //随机获取字体的大小
        int minSize = (int) (size * 0.8);
        size = random.nextInt(size - minSize + 1) + minSize;
        //返回一个随机的字体
        Font font;
        //随机获取字体的样式，0是无样式，1是加粗，2是斜体，3是加粗加斜体
        int style = random.nextInt(4);
        switch (style) {
            case Font.PLAIN:
                font = new Font(fontName, Font.PLAIN, size);
                break;
            case Font.BOLD:
                font = new Font(fontName, Font.BOLD, size);
                break;
            case Font.ITALIC:
                font = new Font(fontName, Font.ITALIC, size);
                break;
            default:
                font = new Font(fontName, Font.BOLD | Font.ITALIC, size);
                break;
        }
        return font;
    }

    /**
     * 功能描述: 绘制干扰线
     *
     * @param graphics  图形上下文Graphics对象
     * @param interLine 图片中干扰线的条数
     * @param lineColor 干扰线颜色,若为null则表示采用随机颜色
     * @param width     图片宽度(注意此宽度若过小,容易造成验证码文本显示不全,如4个字符的文本可使用85到90的宽度)
     * @param height    图片高度
     * @method drawLine
     * @author Blare
     * @date 2020/10/31 20:24
     * @updator Blare
     */
    private static void drawLine(Graphics graphics, int interLine, Color lineColor, int width, int height) {
        int x = 0, y, y1;
        for (int i = 0; i < interLine; i++) {
            graphics.setColor(null == lineColor ? generateRandomColor() : lineColor);
            y = random.nextInt(height);
            y1 = random.nextInt(height);
            graphics.drawLine(x, y, width, y1);
        }
    }

    /**
     * 功能描述: 绘制噪点
     *
     * @param bufferedImage 内存图像BufferedImage
     * @param width         图片宽度
     * @param height        图片高度
     * @param noiseRate     噪率
     * @method drawNoise
     * @author Blare
     * @date 2020/10/31 20:28
     * @updator Blare
     */
    private static void drawNoise(BufferedImage bufferedImage, int width, int height, float noiseRate) {
        int area = (int) (noiseRate * width * height);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            Color rgb = generateRandomColor();
            bufferedImage.setRGB(x, y, rgb.getRGB());
        }
    }

    /**
     * 功能描述: 使图片扭曲
     *
     * @param graphics  图形上下文Graphics对象
     * @param width     图片宽度
     * @param height    图片高度
     * @param color     颜色
     * @param borderGap 边界绘制
     * @method shear
     * @author Blare
     * @date 2020/10/31 20:47
     * @updator Blare
     */
    @SuppressWarnings("SameParameterValue")
    private static void shear(Graphics graphics, int width, int height, Color color, boolean borderGap) {
        shearX(graphics, width, height, color, borderGap);
        shearY(graphics, width, height, color, borderGap);
    }

    /**
     * 功能描述: X轴方向扭曲
     *
     * @param graphics 图形上下文Graphics对象
     * @param width    图片宽度
     * @param height   图片高度
     * @param color    颜色
     * @method shearX
     * @author Blare
     * @date 2020/10/31 20:48
     * @updator Blare
     */
    private static void shearX(Graphics graphics, int width, int height, Color color, boolean borderGap) {

        int period = random.nextInt(2);

        int frames = 1;
        int phase = random.nextInt(2);

        for (int i = 0; i < height; i++) {
            double d = (double) (period >> 1)
                * Math.sin((double) i / (double) period
                + (6.2831853071795862D * (double) phase)
                / (double) frames);
            graphics.copyArea(0, i, width, 1, (int) d, 0);
            if (borderGap) {
                graphics.setColor(color);
                graphics.drawLine((int) d, i, 0, i);
                graphics.drawLine((int) d + width, i, width, i);
            }
        }

    }

    /**
     * 功能描述: Y轴方向扭曲
     *
     * @param graphics 图形上下文Graphics对象
     * @param width    图片宽度
     * @param height   图片高度
     * @param color    颜色
     * @method shearY
     * @author Blare
     * @date 2020/10/31 20:32
     * @updator Blare
     */
    private static void shearY(Graphics graphics, int width, int height, Color color, boolean borderGap) {

        int period = random.nextInt(40) + 10; // 50;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < width; i++) {
            double d = (double) (period >> 1)
                * Math.sin((double) i / (double) period
                + (6.2831853071795862D * (double) phase)
                / (double) frames);
            graphics.copyArea(i, 0, 1, height, 0, (int) d);
            if (borderGap) {
                graphics.setColor(color);
                graphics.drawLine(i, (int) d, i, 0);
                graphics.drawLine(i, (int) d + height, i, height);
            }

        }
    }

    /**
     * 功能描述: 生成验证码字符串（ASCII码：1-10=>48-57，A-Z=>65-90，a-z=>97-122）
     *
     * @param type          验证码类型,参见本类的静态属性
     * @param length        验证码长度,要求大于0的整数
     * @param excludeString 需排除的特殊字符（无需排除则为null）
     * @return {@link String}
     * @method generateTextCode
     * @author Blare
     * @date 2020/10/31 15:31
     * @updator Blare
     */
    public static String generateTextCode(int type, int length, String excludeString) {

        if (length <= 0) {
            return "";
        }
        StringBuilder verifyCode = new StringBuilder();
        int i = 0;
        switch (type) {
            case TYPE_NUM_ONLY:
                while (i < length) {
                    int t = random.nextInt(10);
                    //排除特殊字符
                    if (null == excludeString || !excludeString.contains(t + "")) {
                        verifyCode.append(t);
                        i++;
                    }
                }
                break;
            case TYPE_LETTER_ONLY:
                while (i < length) {
                    int t = random.nextInt(123);
                    if ((t >= 97 || (t >= 65 && t <= 90)) && (null == excludeString || excludeString.indexOf((char) t) < 0)) {
                        verifyCode.append((char) t);
                        i++;
                    }
                }
                break;
            case TYPE_ALL_MIXED:
                while (i < length) {
                    int t = random.nextInt(123);
                    if ((t >= 97 || (t >= 65 && t <= 90) || (t >= 48 && t <= 57)) && (null == excludeString || excludeString.indexOf((char) t) < 0)) {
                        verifyCode.append((char) t);
                        i++;
                    }
                }
                break;
            case TYPE_NUM_UPPER:
                while (i < length) {
                    int t = random.nextInt(91);
                    if ((t >= 65 || (t >= 48 && t <= 57)) && (null == excludeString || excludeString.indexOf((char) t) < 0)) {
                        verifyCode.append((char) t);
                        i++;
                    }
                }
                break;
            case TYPE_NUM_LOWER:
                while (i < length) {
                    int t = random.nextInt(123);
                    if ((t >= 97 || (t >= 48 && t <= 57)) && (null == excludeString || excludeString.indexOf((char) t) < 0)) {
                        verifyCode.append((char) t);
                        i++;
                    }
                }
                break;
            case TYPE_UPPER_ONLY:
                while (i < length) {
                    int t = random.nextInt(91);
                    if ((t >= 65) && (null == excludeString || excludeString.indexOf((char) t) < 0)) {
                        verifyCode.append((char) t);
                        i++;
                    }
                }
                break;
            case TYPE_LOWER_ONLY:
                while (i < length) {
                    int t = random.nextInt(123);
                    if ((t >= 97) && (null == excludeString || excludeString.indexOf((char) t) < 0)) {
                        verifyCode.append((char) t);
                        i++;
                    }
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("已生成验证码：{}", verifyCode.toString());
        }
        return verifyCode.toString();
    }

    /**
     * 功能描述: 已有验证码,生成验证码图片的缓存对象
     *
     * @param textCode       文本验证码
     * @param width          图片宽度(注意此宽度若过小,容易造成验证码文本显示不全,如4个字符的文本可使用85到90的宽度)
     * @param height         图片高度
     * @param interLine      图片中干扰线的条数
     * @param randomLocation 每个字符的高低位置是否随机
     * @param backColor      图片颜色,若为null则表示采用随机颜色
     * @param foreColor      字体颜色,若为null则表示采用随机颜色
     * @param font           字体,若为null则表示采用随机字体
     * @param lineColor      干扰线颜色,若为null则表示采用随机颜色
     * @param distortion     是否图形变形
     * @return {@link BufferedImage}
     * @method generateImageCode
     * @author Blare
     * @date 2020/10/31 17:11
     * @updator Blare
     */
    public static BufferedImage generateImageCode(String textCode, int width, int height, int interLine, boolean randomLocation, Color backColor, Font font, Color foreColor, Color lineColor, float noiseRate, boolean distortion) {
        /*
            创建内存图像
            BufferedImage是抽象类Image的一个实现，是一个带缓冲区图像类，主要作用是将一幅图片加载到内存中。
                BufferedImage提供获得绘图对象、图像缩放、选择图像平滑度等功能，通常用来做图片大小变换、图片变灰、设置透明不透明等。
         */
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        /*
            获取图形上下文
            Graphics提供基本绘图和显示格式化文字的方法，画图用的坐标系原点在左上角，纵轴向下。
                主要有画线段、矩形、圆、椭圆、圆弧、多边形等各种颜色的图形、线条。
         */
        Graphics graphics = bufferedImage.getGraphics();
        // 画背景图(设置前景色)
        graphics.setColor(null == backColor ? generateRandomColor() : backColor);
        // 填充指定的矩形
        graphics.fillRect(0, 0, width, height);

        //画干扰线
        if (interLine > 0) {
            drawLine(graphics, interLine, lineColor, width, height);
        }

        // 添加噪点
        if (noiseRate > 0) {
            drawNoise(bufferedImage, width, height, noiseRate);
        }

        // 使图片扭曲
        if (distortion) {
            shear(graphics, width, height, generateRandomColor(), true);
        }

        //字体大小为图片高度的80%
        int fsize = (int) (height * 0.8);
        int fx = height - fsize;
        int fy = fsize;
        //设定字体
        graphics.setFont(null == font ? generateRandomFont(fsize) : font);
        //写验证码字符
        for (int i = 0; i < textCode.length(); i++) {
            fy = randomLocation ? (int) ((Math.random() * 0.3 + 0.6) * height) : fy;
            graphics.setColor(null == foreColor ? generateRandomColor() : foreColor);
            //将验证码字符显示到图象中
            graphics.drawString(textCode.charAt(i) + "", fx, fy);
            fx += fsize * 0.9;
        }
        // 绘制结束
        graphics.dispose();
        if (logger.isDebugEnabled()) {
            logger.debug("已完成验证码图片绘制");
        }
        return bufferedImage;
    }

    /**
     * 功能描述: 生成图片验证码的缓存对象
     *
     * @param type           验证码类型,参见本类的静态属性
     * @param length         验证码字符长度,要求大于0的整数
     * @param excludeString  需排除的特殊字符
     * @param width          图片宽度(注意此宽度若过小,容易造成验证码文本显示不全,如4个字符的文本可使用85到90的宽度)
     * @param height         图片高度
     * @param interLine      图片中干扰线的条数
     * @param randomLocation 每个字符的高低位置是否随机
     * @param backColor      图片颜色,若为null则表示采用随机颜色
     * @param foreColor      字体颜色,若为null则表示采用随机颜色
     * @param lineColor      干扰线颜色,若为null则表示采用随机颜色
     * @return {@link BufferedImage}
     * @method generateImageCode
     * @author Blare
     * @date 2020/10/31 15:29
     * @updator Blare
     */
    public static BufferedImage generateImageCode(int type, int length, String excludeString, int width, int height, int interLine, boolean randomLocation, Color backColor, Color foreColor, Color lineColor, float noiseRate, boolean distortion) {
        // 生成验证码字符串
        String textCode = generateTextCode(type, length, excludeString);
        // 生成验证码图片
        return generateImageCode(textCode, width, height, interLine, randomLocation, backColor, null, foreColor, lineColor, noiseRate, distortion);
    }
}
