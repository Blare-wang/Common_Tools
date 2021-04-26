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
import java.awt.geom.CubicCurve2D;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.util.Objects;

import static com.itblare.itools.randoms.RandomsProcessor.number;

/**
 * 一句话功能简述：图片验证码
 *
 * @author Blare
 * @create 2021/4/13 16:53
 * @since 1.0.0
 */
public class ImageCapHelper {

    private static final Logger logger = LoggerFactory.getLogger(ImageCapHelper.class);

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
     * 常用汉字
     */
    public static final String DELTA = "\u7684\u4e00\u4e86\u662f\u6211\u4e0d\u5728\u4eba\u4eec\u6709\u6765\u4ed6\u8fd9\u4e0a\u7740\u4e2a\u5730\u5230\u5927\u91cc\u8bf4\u5c31\u53bb\u5b50\u5f97\u4e5f\u548c\u90a3\u8981\u4e0b\u770b\u5929\u65f6\u8fc7\u51fa\u5c0f\u4e48\u8d77\u4f60\u90fd\u628a\u597d\u8fd8\u591a\u6ca1\u4e3a\u53c8\u53ef\u5bb6\u5b66\u53ea\u4ee5\u4e3b\u4f1a\u6837\u5e74\u60f3\u751f\u540c\u8001\u4e2d\u5341\u4ece\u81ea\u9762\u524d\u5934\u9053\u5b83\u540e\u7136\u8d70\u5f88\u50cf\u89c1\u4e24\u7528\u5979\u56fd\u52a8\u8fdb\u6210\u56de\u4ec0\u8fb9\u4f5c\u5bf9\u5f00\u800c\u5df1\u4e9b\u73b0\u5c71\u6c11\u5019\u7ecf\u53d1\u5de5\u5411\u4e8b\u547d\u7ed9\u957f\u6c34\u51e0\u4e49\u4e09\u58f0\u4e8e\u9ad8\u624b\u77e5\u7406\u773c\u5fd7\u70b9\u5fc3\u6218\u4e8c\u95ee\u4f46\u8eab\u65b9\u5b9e\u5403\u505a\u53eb\u5f53\u4f4f\u542c\u9769\u6253\u5462\u771f\u5168\u624d\u56db\u5df2\u6240\u654c\u4e4b\u6700\u5149\u4ea7\u60c5\u8def\u5206\u603b\u6761\u767d\u8bdd\u4e1c\u5e2d\u6b21\u4eb2\u5982\u88ab\u82b1\u53e3\u653e\u513f\u5e38\u6c14\u4e94\u7b2c\u4f7f\u5199\u519b\u5427\u6587\u8fd0\u518d\u679c\u600e\u5b9a\u8bb8\u5feb\u660e\u884c\u56e0\u522b\u98de\u5916\u6811\u7269\u6d3b\u90e8\u95e8\u65e0\u5f80\u8239\u671b\u65b0\u5e26\u961f\u5148\u529b\u5b8c\u5374\u7ad9\u4ee3\u5458\u673a\u66f4\u4e5d\u60a8\u6bcf\u98ce\u7ea7\u8ddf\u7b11\u554a\u5b69\u4e07\u5c11\u76f4\u610f\u591c\u6bd4\u9636\u8fde\u8f66\u91cd\u4fbf\u6597\u9a6c\u54ea\u5316\u592a\u6307\u53d8\u793e\u4f3c\u58eb\u8005\u5e72\u77f3\u6ee1\u65e5\u51b3\u767e\u539f\u62ff\u7fa4\u7a76\u5404\u516d\u672c\u601d\u89e3\u7acb\u6cb3\u6751\u516b\u96be\u65e9\u8bba\u5417\u6839\u5171\u8ba9\u76f8\u7814\u4eca\u5176\u4e66\u5750\u63a5\u5e94\u5173\u4fe1\u89c9\u6b65\u53cd\u5904\u8bb0\u5c06\u5343\u627e\u4e89\u9886\u6216\u5e08\u7ed3\u5757\u8dd1\u8c01\u8349\u8d8a\u5b57\u52a0\u811a\u7d27\u7231\u7b49\u4e60\u9635\u6015\u6708\u9752\u534a\u706b\u6cd5\u9898\u5efa\u8d76\u4f4d\u5531\u6d77\u4e03\u5973\u4efb\u4ef6\u611f\u51c6\u5f20\u56e2\u5c4b\u79bb\u8272\u8138\u7247\u79d1\u5012\u775b\u5229\u4e16\u521a\u4e14\u7531\u9001\u5207\u661f\u5bfc\u665a\u8868\u591f\u6574\u8ba4\u54cd\u96ea\u6d41\u672a\u573a\u8be5\u5e76\u5e95\u6df1\u523b\u5e73\u4f1f\u5fd9\u63d0\u786e\u8fd1\u4eae\u8f7b\u8bb2\u519c\u53e4\u9ed1\u544a\u754c\u62c9\u540d\u5440\u571f\u6e05\u9633\u7167\u529e\u53f2\u6539\u5386\u8f6c\u753b\u9020\u5634\u6b64\u6cbb\u5317\u5fc5\u670d\u96e8\u7a7f\u5185\u8bc6\u9a8c\u4f20\u4e1a\u83dc\u722c\u7761\u5174\u5f62\u91cf\u54b1\u89c2\u82e6\u4f53\u4f17\u901a\u51b2\u5408\u7834\u53cb\u5ea6\u672f\u996d\u516c\u65c1\u623f\u6781\u5357\u67aa\u8bfb\u6c99\u5c81\u7ebf\u91ce\u575a\u7a7a\u6536\u7b97\u81f3\u653f\u57ce\u52b3\u843d\u94b1\u7279\u56f4\u5f1f\u80dc\u6559\u70ed\u5c55\u5305\u6b4c\u7c7b\u6e10\u5f3a\u6570\u4e61\u547c\u6027\u97f3\u7b54\u54e5\u9645\u65e7\u795e\u5ea7\u7ae0\u5e2e\u5566\u53d7\u7cfb\u4ee4\u8df3\u975e\u4f55\u725b\u53d6\u5165\u5cb8\u6562\u6389\u5ffd\u79cd\u88c5\u9876\u6025\u6797\u505c\u606f\u53e5\u533a\u8863\u822c\u62a5\u53f6\u538b\u6162\u53d4\u80cc\u7ec6";

    /**
     * 常用颜色
     */
    public static final int[][] COLOR = {
        {0, 135, 255}, {51, 153, 51}, {255, 102, 102}, {255, 153, 0},
        {153, 102, 0}, {153, 102, 153}, {51, 153, 153}, {102, 102, 255},
        {0, 102, 204}, {204, 51, 51}, {0, 153, 204}, {0, 51, 102}
    };

    /**
     * 安全随机数拓展（0~5像素）
     */
    private static final int pixel = number(5);

    /**
     * 功能描述: 返回随机汉字
     *
     * @return {@link char}
     * @method alphaHan
     * @author Blare
     * @date 2021/4/24 18:28
     * @updator Blare
     */
    public static char alphaHan() {
        return DELTA.charAt(number(DELTA.length()));
    }

    /**
     * 功能描述: 获取随机常用颜色
     *
     * @return {@link Color}
     * @method getCommonlyFontColor
     * @author Blare
     * @date 2021/4/26 0:19
     * @updator Blare
     */
    public static Color getNormalFontColor() {
        int[] color = COLOR[number(COLOR.length)];
        return new Color(color[0], color[1], color[2]);
    }

    /**
     * 功能描述: 生成随机颜色
     *
     * @return {@link Color}
     * @method generateRandomColor
     * @author Blare
     * @date 2020/10/31 15:29
     * @updator Blare
     */
    public static Color getRandomColor() {
        /*
            任何颜色都是由三原色组成（RGB）,
            JAVA中支持224为彩色，
            即红绿蓝分量取值介于0-255之间（8位表示）
         */
        return new Color(number(255), number(255), number(255));
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
    public static Font getRandomFont(int size) {
        //获取随机的字体
        int index = number(FONT_NAMES.length);
        String fontName = FONT_NAMES[index];
        //随机获取字体的大小
        int minSize = (int) (size * 0.8);
        size = number(size - minSize + 1) + minSize;
        //返回一个随机的字体
        Font font;
        //随机获取字体的样式，0是无样式，1是加粗，2是斜体，3是加粗加斜体
        int style = number(4);
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
     * 功能描述: 随机画干扰线
     *
     * @param interLine 数量
     * @param graphics  图形上下文Graphics对象
     * @method drawLine
     * @author Blare
     * @date 2020/10/31 20:24
     * @updator Blare
     */
    public void drawLine(Graphics graphics, int interLine, int width, int height) {
        drawLine(graphics, interLine, null, width, height);
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
            graphics.setColor(null == lineColor ? getRandomColor() : lineColor);
            y = number(height);
            y1 = number(height);
            graphics.drawLine(x, y, width, y1);
        }
    }

    /**
     * 功能描述: 随机画干扰圆
     *
     * @param interOval 数量
     * @param graphics  图形上下文Graphics对象
     * @method drawOval
     * @author Blare
     * @date 2021/4/24 17:49
     * @updator Blare
     */
    public static void drawOval(Graphics graphics, int interOval, int width, int height) {
        drawOval(graphics, interOval, null, width, height);
    }

    /**
     * 功能描述: 随机画干扰圆
     *
     * @param interOval 数量
     * @param color     颜色
     * @param graphics  图形上下文Graphics对象
     * @method drawOval
     * @author Blare
     * @date 2021/4/24 17:49
     * @updator Blare
     */
    public static void drawOval(Graphics graphics, int interOval, Color color, int width, int height) {
        for (int i = 0; i < interOval; i++) {
            graphics.setColor(color == null ? getRandomColor() : color);
            int w = 5 + number(10);
            graphics.drawOval(number(width - 25), number(height - 15), w, w);
        }
    }

    /**
     * 功能描述: 随机画贝塞尔曲线
     *
     * @param num      数量
     * @param graphics 图形上下文Graphics对象
     * @method drawBesselLine
     * @author Blare
     * @date 2021/4/24 17:50
     * @updator Blare
     */
    public static void drawBesselLine(Graphics2D graphics, int num, int width, int height) {
        drawBesselLine(graphics, num, null, width, height);
    }

    /**
     * 功能描述: 随机画贝塞尔曲线
     *
     * @param num      数量
     * @param color    颜色
     * @param graphics 图形上下文Graphics对象
     * @method drawBesselLine
     * @author Blare
     * @date 2021/4/24 17:50
     * @updator Blare
     */
    public static void drawBesselLine(Graphics2D graphics, int num, Color color, int width, int height) {
        for (int i = 0; i < num; i++) {
            graphics.setColor(color == null ? getRandomColor() : color);
            int x1 = 5, y1 = number(5, height / 2);
            int x2 = width - 5, y2 = number(height / 2, height - 5);
            int ctrlx = number(width / 4, width / 4 * 3);
            int ctrly = number(5, height - 5);
            if (number(2) == 0) {
                int ty = y1;
                y1 = y2;
                y2 = ty;
            }
            if (number(2) == 0) {  // 二阶贝塞尔曲线
                QuadCurve2D shape = new QuadCurve2D.Double();
                shape.setCurve(x1, y1, ctrlx, ctrly, x2, y2);
                graphics.draw(shape);
            } else {  // 三阶贝塞尔曲线
                int ctrlx1 = number(width / 4, width / 4 * 3), ctrly1 = number(5, height - 5);
                CubicCurve2D shape = new CubicCurve2D.Double(x1, y1, ctrlx, ctrly, ctrlx1, ctrly1, x2, y2);
                graphics.draw(shape);
            }
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
            int x = number(width);
            int y = number(height);
            Color rgb = getRandomColor();
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

        int period = number(2);

        int frames = 1;
        int phase = number(2);

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

        int period = number(40) + 10; // 50;
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
     * 功能描述: 生成验证码字符串（ASCII码：0-9=>48-57，A-Z=>65-90，a-z=>97-122）
     *
     * @param type   验证码类型,参见本类的静态属性
     * @param length 验证码长度,要求大于0的整数
     * @return {@link String}
     * @method generateTextCode
     * @author Blare
     * @date 2020/10/31 15:31
     * @updator Blare
     */
    public static String getCapCode(int type, int length) {

        if (length < 0) {
            return "";
        }
        StringBuilder code = new StringBuilder();
        int i = 0;
        while (i < length) {
            int t;
            switch (type) {
                case TYPE_NUM_ONLY:
                    t = number(10);
                    code.append(t);
                    i++;
                    break;
                case TYPE_LETTER_ONLY:
                    t = number(65, 122);
                    if (((t >= 97 && t <= 122) || (t >= 65 && t <= 90))) {
                        code.append((char) t);
                        i++;
                    }
                    break;
                case TYPE_ALL_MIXED:
                    t = number(123);
                    if (t >= 0 && t <= 9) {
                        code.append(t);
                        i++;
                    } else if (((t >= 97 && t <= 122) || (t >= 65 && t <= 90) || (t >= 48 && t <= 57))) {
                        code.append((char) t);
                        i++;
                    }
                    break;
                case TYPE_NUM_UPPER:
                    t = number(90);
                    if (t >= 0 && t <= 9) {
                        code.append(t);
                        i++;
                    } else if ((t >= 65 && t <= 90) || (t >= 48 && t <= 57)) {
                        code.append((char) t);
                        i++;
                    }
                    break;
                case TYPE_NUM_LOWER:
                    t = number(122);
                    if (t >= 0 && t <= 9) {
                        code.append(t);
                        i++;
                    } else if ((t >= 97 && t <= 122) || (t >= 48 && t <= 57)) {
                        code.append((char) t);
                        i++;
                    }
                    break;
                case TYPE_UPPER_ONLY:
                    t = number(65, 90);
                    if (t >= 65 && t <= 90) {
                        code.append((char) t);
                        i++;
                    }
                    break;
                case TYPE_LOWER_ONLY:
                    t = number(97, 122);
                    if (t >= 97 && t <= 122) {
                        code.append((char) t);
                        i++;
                    }
                    break;
                default:
                    t = number(122);
                    code.append((char) t);
                    i++;
                    break;
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("已生成验证码：{}", code.toString());
        }
        return code.toString();
    }

    /**
     * 功能描述:
     *
     * @param charArr        字符数组
     * @param width          图片宽度(注意此宽度若过小,容易造成验证码文本显示不全,如4个字符的文本可使用85到90的宽度)
     * @param height         图片高度
     * @param interLine      图片中干扰线的条数
     * @param randomLocation 每个字符的高低位置是否随机
     * @param backColor      图片颜色,若为null则表示采用随机颜色
     * @param font           字体
     * @param fontColor      字体颜色,若为null则表示采用随机颜色
     * @param lineColor      干扰线颜色,若为null则表示采用随机颜色
     * @param noiseRate      噪点率
     * @param distortion     是否图形变形
     * @param resize         宽度不够，是否重置
     * @return {@link BufferedImage}
     * @method generateImageCode
     * @author Blare
     * @date 2021/4/26 0:10
     * @updator Blare
     */
    public static BufferedImage generateImageCode(char[] charArr,
                                                  int width,
                                                  int height,
                                                  int interLine,
                                                  boolean randomLocation,
                                                  Color backColor,
                                                  Font font,
                                                  Color fontColor,
                                                  Color lineColor,
                                                  float noiseRate,
                                                  boolean distortion,
                                                  boolean resize) {
        return generateImageCode(charArr, width, height, interLine, null, randomLocation, backColor, font, fontColor, lineColor, null, noiseRate, distortion, resize);
    }

    /**
     * 功能描述: 生成图片验证码的缓存对象
     *
     * @param type           验证码类型,参见本类的静态属性
     * @param length         验证码字符长度,要求大于0的整数
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
    public static BufferedImage generateImageCode(int type,
                                                  int length,
                                                  int width,
                                                  int height,
                                                  int interLine,
                                                  boolean randomLocation,
                                                  Color backColor,
                                                  Color foreColor,
                                                  Color lineColor,
                                                  float noiseRate,
                                                  boolean distortion) {
        // 生成验证码字符串
        String textCode = getCapCode(type, length);
        // 生成验证码图片
        return generateImageCode(textCode.toCharArray(), width, height, interLine, randomLocation, backColor, null, foreColor, lineColor, noiseRate, distortion, true);
    }

    private static BufferedImage createBufferedImage(int width, int height) {

        /*
         *  创建内存图像
         *  BufferedImage是抽象类Image的一个实现，是一个带缓冲区图像类，主要作用是将一幅图片加载到内存中。
         *  BufferedImage提供获得绘图对象、图像缩放、选择图像平滑度等功能，通常用来做图片大小变换、图片变灰、设置透明不透明等。
         */
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    private static Graphics2D graphics(BufferedImage bufferedImage,
                                       int width,
                                       int height,
                                       Integer interLine,
                                       Integer interOval,
                                       Integer besselLine,
                                       boolean randomLocation,
                                       Color backColor,
                                       Font font,
                                       Color fontColor,
                                       Color lineColor,
                                       Color ovalColor,
                                       Color BesselLineColor,
                                       Float noiseRate,
                                       boolean distortion) {
        /*
         *  获取图形上下文
         *  Graphics提供基本绘图和显示格式化文字的方法，画图用的坐标系原点在左上角，纵轴向下。
         *      主要有画线段、矩形、圆、椭圆、圆弧、多边形等各种颜色的图形、线条。
         */
        Graphics2D graphics = (Graphics2D) bufferedImage.getGraphics();
        // 画背景图(设置前景色)
        graphics.setColor(null == backColor ? getRandomColor() : backColor);
        // 填充指定的矩形
        graphics.fillRect(0, 0, width, height);
        // 抗锯齿
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //画干扰线
        if (Objects.nonNull(interLine) && interLine > 0) {
            drawLine(graphics, interLine, lineColor, width, height);
        }
        //画干扰圆
        if (Objects.nonNull(interOval) && interOval > 0) {
            drawOval(graphics, interOval, ovalColor, width, height);
        }
        // 画贝塞尔曲线
        if (Objects.nonNull(besselLine) && besselLine > 0) {
            drawBesselLine(graphics, besselLine, BesselLineColor, width, height);
        }
        // 使图片扭曲
        if (distortion) {
            shear(graphics, width, height, getRandomColor(), true);
        }
        // 添加噪点
        if (Objects.nonNull(noiseRate) && noiseRate > 0) {
            drawNoise(bufferedImage, width, height, noiseRate);
        }
        return graphics;
    }

    /**
     * 功能描述: 已有验证码,生成验证码图片的缓存对象
     *
     * @param charArr        字符数组
     * @param width          图片宽度(注意此宽度若过小,容易造成验证码文本显示不全,如4个字符的文本可使用85到90的宽度)
     * @param height         图片高度
     * @param interLine      图片中干扰线的条数
     * @param interOval      图片中干扰圆的个数
     * @param randomLocation 每个字符的高低位置是否随机
     * @param backColor      图片颜色,若为null则表示采用随机颜色
     * @param fontColor      字体颜色,若为null则表示采用随机颜色
     * @param font           字体,若为null则表示采用随机字体
     * @param lineColor      干扰线颜色,若为null则表示采用随机颜色
     * @param ovalColor      干扰圆颜色,若为null则表示采用随机颜色
     * @param noiseRate      噪点率
     * @param distortion     是否图形变形
     * @param resize         宽度不够，是否重置
     * @return {@link BufferedImage}
     * @method generateImageCode
     * @author Blare
     * @date 2020/10/31 17:11
     * @updator Blare
     */
    public static BufferedImage generateImageCode(char[] charArr,
                                                  int width,
                                                  int height,
                                                  Integer interLine,
                                                  Integer interOval,
                                                  boolean randomLocation,
                                                  Color backColor,
                                                  Font font,
                                                  Color fontColor,
                                                  Color lineColor,
                                                  Color ovalColor,
                                                  Float noiseRate,
                                                  boolean distortion,
                                                  boolean resize) {
        int rWidth = width + pixel, rHeight = height + pixel;

        final BufferedImage bufferedImage = createBufferedImage(rWidth, rHeight);
        // 绘制
        Graphics2D graphics = graphics(bufferedImage,
            width,
            height,
            interLine,
            interOval,
            null,
            randomLocation,
            backColor,
            font,
            fontColor,
            lineColor,
            ovalColor,
            null,
            noiseRate,
            distortion);

        //字体大小为图片高度的80%
        int fSize = (int) (height * 0.8);
        //设定字体
        graphics.setFont(null == font ? getRandomFont(fSize) : font);
        // 初次绘制
        int needWidth = draw(graphics, charArr, width, height, randomLocation, fontColor, null);
        // 宽度不够，根据实际情况自旋调整
        if ((needWidth > width) && resize) {
            graphics.dispose();
            return graphicsChineseImage(charArr,
                needWidth,
                height,
                interLine,
                interOval,
                null,
                randomLocation,
                backColor,
                font,
                fontColor,
                lineColor,
                ovalColor,
                null,
                noiseRate,
                distortion,
                false);
        }
        // 绘制结束
        graphics.dispose();
        if (logger.isDebugEnabled()) {
            logger.debug("已完成验证码图片绘制");
        }
        return bufferedImage;
    }

    /**
     * 功能描述: 绘制中文验证码
     *
     * @param charArr         字符数组
     * @param width           图片宽度(注意此宽度若过小,容易造成验证码文本显示不全,如4个字符的文本可使用85到90的宽度)
     * @param height          图片高度
     * @param interLine       图片中干扰线的条数
     * @param interOval       图片中干扰圆的个数
     * @param besselLine      图片中贝塞尔曲线
     * @param randomLocation  每个字符的高低位置是否随机
     * @param backColor       图片颜色,若为null则表示采用随机颜色
     * @param fontColor       字体颜色,若为null则表示采用随机颜色
     * @param font            字体,若为null则表示采用随机字体
     * @param lineColor       干扰线颜色,若为null则表示采用随机颜色
     * @param ovalColor       干扰圆颜色,若为null则表示采用随机颜色
     * @param besselLineColor 贝塞尔曲线颜色,若为null则表示采用随机颜色
     * @param noiseRate       噪点率
     * @param distortion      是否图形变形
     * @param resize          宽度不够，是否重置
     * @return {@link BufferedImage}
     * @method graphicsChineseImage
     * @author Blare
     * @date 2021/4/25 0:24
     * @updator Blare
     */
    public static BufferedImage graphicsChineseImage(char[] charArr,
                                                     int width,
                                                     int height,
                                                     Integer interLine,
                                                     Integer interOval,
                                                     Integer besselLine,
                                                     boolean randomLocation,
                                                     Color backColor,
                                                     Font font,
                                                     Color fontColor,
                                                     Color lineColor,
                                                     Color ovalColor,
                                                     Color besselLineColor,
                                                     Float noiseRate,
                                                     boolean distortion,
                                                     boolean resize) {

        // 预定义长款
        int rWidth = width + pixel, rHeight = height + pixel;
        // 创建内存图像
        final BufferedImage bufferedImage = createBufferedImage(rWidth, rHeight);
        // 绘制
        Graphics2D graphics = graphics(bufferedImage,
            width,
            height,
            interLine,
            interOval,
            besselLine,
            randomLocation,
            backColor,
            font,
            fontColor,
            lineColor,
            ovalColor,
            besselLineColor,
            noiseRate,
            distortion);
        // 画干扰线
        if (Objects.nonNull(interLine) && interLine > 0) {
            graphics.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        }
        // 设定字体
        graphics.setFont(null == font ? new Font("宋体", Font.BOLD, 24) : font);
        // 初次绘制
        int needWidth = draw(graphics, charArr, width, height, randomLocation, fontColor, null);
        // 宽度不够，根据实际情况自旋调整
        if ((needWidth > width) && resize) {
            graphics.dispose();
            return graphicsChineseImage(charArr,
                needWidth,
                height,
                interLine,
                interOval,
                besselLine,
                randomLocation,
                backColor,
                font,
                fontColor,
                lineColor,
                ovalColor,
                besselLineColor,
                noiseRate,
                distortion,
                false);
        }
        graphics.dispose();
        if (logger.isDebugEnabled()) {
            logger.debug("已完成验证码图片绘制");
        }
        return bufferedImage;
    }

    /**
     * 功能描述: 绘制GIF中文验证码
     *
     * @param charArr         字符数组
     * @param width           图片宽度(注意此宽度若过小,容易造成验证码文本显示不全,如4个字符的文本可使用85到90的宽度)
     * @param height          图片高度
     * @param interLine       图片中干扰线的条数
     * @param interOval       图片中干扰圆的个数
     * @param besselLine      图片中贝塞尔曲线
     * @param randomLocation  每个字符的高低位置是否随机
     * @param backColor       图片颜色,若为null则表示采用随机颜色
     * @param fontColor       字体颜色,若为null则表示采用随机颜色
     * @param font            字体,若为null则表示采用随机字体
     * @param lineColor       干扰线颜色,若为null则表示采用随机颜色
     * @param ovalColor       干扰圆颜色,若为null则表示采用随机颜色
     * @param BesselLineColor 贝塞尔曲线颜色,若为null则表示采用随机颜色
     * @param noiseRate       噪点率
     * @param resize          宽度不够，是否重置
     * @return {@link BufferedImage}
     * @method graphicsChineseImage
     * @author Blare
     * @date 2021/4/25 0:24
     * @updator Blare
     */
    public static BufferedImage graphicsChineseGifImage(char[] charArr,
                                                        int width,
                                                        int height,
                                                        Integer interLine,
                                                        Integer interOval,
                                                        Integer besselLine,
                                                        boolean randomLocation,
                                                        Color backColor,
                                                        Font font,
                                                        Color fontColor,
                                                        Color lineColor,
                                                        Color ovalColor,
                                                        Color BesselLineColor,
                                                        Float noiseRate,
                                                        boolean resize,
                                                        Integer alpha,
                                                        int[][] besselXY) {

        // 预定义长款
        int rWidth = width + pixel, rHeight = height + pixel;
        // 创建内存图像
        final BufferedImage bufferedImage = createBufferedImage(rWidth, rHeight);
        // 绘制
        Graphics2D graphics = graphics(bufferedImage,
            width,
            height,
            interLine,
            interOval,
            besselLine,
            randomLocation,
            backColor,
            font,
            fontColor,
            lineColor,
            ovalColor,
            null,
            noiseRate,
            false);
        // 画干扰线
        if (Objects.nonNull(interLine) && interLine > 0) {
            // 设置透明度
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
            graphics.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            // 绘制立方曲线
            CubicCurve2D shape = new CubicCurve2D.Double(besselXY[0][0],
                besselXY[0][1],
                besselXY[1][0],
                besselXY[1][1],
                besselXY[2][0],
                besselXY[2][1],
                besselXY[3][0],
                besselXY[3][1]);
            // 绘制
            graphics.draw(shape);
        }
        // 设定字体
        graphics.setFont(null == font ? new Font("宋体", Font.BOLD, 24) : font);
        // 初次绘制
        int needWidth = draw(graphics, charArr, width, height, randomLocation, fontColor, alpha);
        // 宽度不够，根据实际情况自旋调整
        if ((needWidth > width) && resize) {
            graphics.dispose();
            return graphicsChineseGifImage(charArr,
                needWidth,
                height,
                interLine,
                interOval,
                besselLine,
                randomLocation,
                backColor,
                font,
                fontColor,
                lineColor,
                ovalColor,
                BesselLineColor,
                noiseRate,
                false,
                alpha,
                besselXY);
        }
        graphics.dispose();
        if (logger.isDebugEnabled()) {
            logger.debug("已完成验证码图片绘制");
        }
        return bufferedImage;
    }

    /**
     * 功能描述: 绘制GIF中文验证码
     *
     * @param charArr        字符数组
     * @param width          图片宽度(注意此宽度若过小,容易造成验证码文本显示不全,如4个字符的文本可使用85到90的宽度)
     * @param height         图片高度
     * @param interLine      图片中干扰线的条数
     * @param interOval      图片中干扰圆的个数
     * @param randomLocation 每个字符的高低位置是否随机
     * @param backColor      图片颜色,若为null则表示采用随机颜色
     * @param fontColor      字体颜色,若为null则表示采用随机颜色
     * @param font           字体,若为null则表示采用随机字体
     * @param lineColor      干扰线颜色,若为null则表示采用随机颜色
     * @param ovalColor      干扰圆颜色,若为null则表示采用随机颜色
     * @param noiseRate      噪点率
     * @param resize         宽度不够，是否重置
     * @return {@link BufferedImage}
     * @method graphicsChineseImage
     * @author Blare
     * @date 2021/4/25 0:24
     * @updator Blare
     */
    public static BufferedImage graphicsGifImage(char[] charArr,
                                                 int width,
                                                 int height,
                                                 Integer interLine,
                                                 Integer interOval,
                                                 boolean randomLocation,
                                                 Color backColor,
                                                 Font font,
                                                 Color fontColor,
                                                 Color lineColor,
                                                 Color ovalColor,
                                                 Float noiseRate,
                                                 boolean resize,
                                                 Integer alpha,
                                                 int[][] besselXY) {

        // 预定义长款
        int rWidth = width + pixel, rHeight = height + pixel;
        // 创建内存图像
        final BufferedImage bufferedImage = createBufferedImage(rWidth, rHeight);
        // 绘制
        Graphics2D graphics = graphics(bufferedImage,
            width,
            height,
            null,
            interOval,
            null,
            randomLocation,
            backColor,
            font,
            fontColor,
            null,
            ovalColor,
            null,
            noiseRate,
            false);
        // 画干扰线
        if (Objects.nonNull(interLine) && interLine > 0) {
            graphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f * number(10)));  // 设置透明度
            //graphics.setStroke(new BasicStroke(1.2f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            CubicCurve2D shape = new CubicCurve2D.Double(besselXY[0][0],
                besselXY[0][1],
                besselXY[1][0],
                besselXY[1][1],
                besselXY[2][0],
                besselXY[2][1],
                besselXY[3][0],
                besselXY[3][1]);
            // 绘制
            graphics.draw(shape);
        }
        // 设定字体
        graphics.setFont(null == font ? new Font("宋体", Font.BOLD, 24) : font);
        // 初次绘制
        int needWidth = draw(graphics, charArr, width, height, randomLocation, fontColor, alpha);
        // 宽度不够，根据实际情况自旋调整
        if ((needWidth > width) && resize) {
            graphics.dispose();
            return graphicsGifImage(charArr,
                needWidth,
                height,
                interLine,
                interOval,
                randomLocation,
                backColor,
                font,
                fontColor,
                lineColor,
                ovalColor,
                noiseRate,
                false,
                alpha,
                besselXY);
        }
        graphics.dispose();
        if (logger.isDebugEnabled()) {
            logger.debug("已完成验证码图片绘制");
        }
        return bufferedImage;
    }

    /**
     * 功能描述: 图片绘制
     *
     * @param graphics       图形上下文Graphics对象
     * @param charArr        字符数组
     * @param width          图片宽度(注意此宽度若过小,容易造成验证码文本显示不全,如4个字符的文本可使用85到90的宽度)
     * @param height         图片高度
     * @param randomLocation 每个字符的高低位置是否随机
     * @param fontColor      字体颜色
     * @param alpha          透明度
     * @return {@link int}
     * @method draw
     * @author Blare
     * @date 2021/4/25 0:10
     * @updator Blare
     */
    private static int draw(Graphics2D graphics, char[] charArr, int width, int height, boolean randomLocation, Color fontColor, Integer alpha) {
        // 每一个字符所占的宽度
        FontMetrics fontMetrics = graphics.getFontMetrics();
        int fx = width / charArr.length;
        int fw = (int) fontMetrics.getStringBounds(String.valueOf(charArr[0]), graphics).getWidth();
        int fwsp = (fx - fw) / 2;  // 字符的左右边距
        int offset = 0;
        int needWidth;
        if (fwsp > 0) {
            int fhsp = (height - (int) fontMetrics.getStringBounds(String.valueOf(charArr[0]), graphics).getHeight()) / 2;  // 字符的左右边距
            for (int i = 0; i < charArr.length; i++) {
                int fy = randomLocation ? (int) ((Math.random() * 0.3 + 0.7) * height) : (height - fhsp);  // 文字的纵坐标;
                if (Objects.nonNull(alpha)) {
                    // 设置透明度
                    AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha(alpha, i, charArr.length));
                    graphics.setComposite(alphaComposite);
                }
                graphics.setColor(null == fontColor ? getRandomColor() : fontColor);
                // 每次绘制的偏移量
                offset = i * fx + fwsp;
                //将验证码字符显示到图象中
                graphics.drawString(String.valueOf(charArr[i]), offset + 3, fy);
            }
            needWidth = offset + fx + fwsp;
        } else {
            needWidth = (fw + 2) * charArr.length;
        }

        return needWidth;
    }

    /**
     * 功能描述: 透明度
     *
     * @return {@link float}
     * @method getAlpha
     * @author Blare
     * @date 2021/4/26 0:14
     * @updator Blare
     */
    private static float getAlpha(int i, int j, int len) {
        int num = i + j;
        float r = (float) 1 / (len - 1);
        float s = len * r;
        return num >= len ? (num * r - s) : num * r;
    }

    public static void main(String[] args) {
        final String capCode = getCapCode(7, 10);
        System.out.println(capCode);
    }
}
