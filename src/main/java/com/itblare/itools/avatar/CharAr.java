package com.itblare.itools.avatar;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.avatar
 * ClassName:   CharAr
 * Author:   Blare
 * Date:     Created in 2021/4/19 17:57
 * Description:    字符头像
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/19 17:57    1.0.0         字符头像
 */

import com.itblare.itools.exception.FileException;
import com.itblare.itools.security.Base64Processor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符头像
 *
 * @author Blare
 * @create 2021/4/19 17:57
 * @since 1.0.0
 */
public class CharAr {

    /**
     * 获取头像输出到文件
     *
     * @param charStr      字符
     * @param file         输出文件
     * @param width        宽度
     * @param height       高度度
     * @param cornerRadius 圆角度
     * @method generateImg
     * @date 2021/4/19 18:07
     */
    public static void outputFileImg(String charStr, Integer width, Integer height, Integer cornerRadius, File file) throws IOException {
        if (file.isDirectory()) {
            throw new FileException("传入文件对象不能是目录！");
        }
        // 获取图片
        final BufferedImage bufferedImage = generateImgBuffered(charStr, width, height, cornerRadius);
        // 写入
        ImageIO.write(bufferedImage, "png", file);
    }

    /**
     * 获取头像输出到文件
     *
     * @param charStr 字符
     * @param file    输出文件
     * @method outputFileImg
     * @date 2021/4/19 18:24
     */
    public static void outputFileImg(String charStr, File file) throws IOException {
        if (file.isDirectory()) {
            throw new FileException("传入文件对象不能是目录！");
        }
        // 获取图片
        final BufferedImage bufferedImage = generateImgBuffered(charStr, null, null, null);
        // 写入
        ImageIO.write(bufferedImage, "png", file);
    }

    /**
     * 获取头像输出BASE64
     *
     * @param charStr      字符
     * @param width        宽度
     * @param height       高度度
     * @param cornerRadius 圆角度
     * @method generateImg
     * @date 2021/4/19 18:07
     */
    public static String outputBase64Img(String charStr, Integer width, Integer height, Integer cornerRadius) throws IOException {
        // 获取图片
        final BufferedImage bufferedImage = generateImgBuffered(charStr, width, height, cornerRadius);
        //输出流
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", stream);
        // 生成base64
        final String base64 = Base64Processor.encode(stream.toByteArray());
        return String.join(",", "data:image/png;base64", base64);
    }

    /**
     * 获取头像输出BASE64
     *
     * @param charStr 字符
     * @return {@link String}
     * @method outputBase64Img
     * @date 2021/4/19 18:26
     */
    public static String outputBase64Img(String charStr) throws IOException {
        // 获取图片
        final BufferedImage bufferedImage = generateImgBuffered(charStr, null, null, null);
        //输出流
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", stream);
        // 生成base64
        final String base64 = Base64Processor.encode(stream.toByteArray());
        return String.join(",", "data:image/png;base64", base64);
    }

    /**
     * 绘制字体头像
     * 如果是英文名，只显示首字母大写
     * 如果是中文名，只显示最后两个字
     *
     * @param charStr      字符
     * @param cornerRadius 圆角度
     * @method generateImg
     * @date 2021/4/19 17:58
     */
    private static BufferedImage generateImgBuffered(String charStr, Integer width, Integer height, Integer cornerRadius) {
        if (Objects.isNull(cornerRadius)) {
            cornerRadius = 99;
        }
        if (Objects.isNull(width)) {
            width = 100;
        }
        if (Objects.isNull(height)) {
            height = 100;
        }
        int nameLen = charStr.length();
        String nameWritten;
        //如果用户输入的姓名少于等于2个字符，不用截取
        if (nameLen <= 2) {
            nameWritten = charStr;
        } else {
            //如果用户输入的姓名大于等于3个字符，截取后面两位
            String first = charStr.substring(0, 1);
            if (isChinese(first)) {
                //截取倒数两位汉字
                nameWritten = charStr.substring(nameLen - 2);
            } else {
                //截取前面的两个英文字母
                nameWritten = charStr.substring(0, 2).toUpperCase();
            }
        }


        //Font font = new Font("微软雅黑", Font.PLAIN, 30);

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2 = (Graphics2D) bi.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2.setBackground(getRandomColor());

        g2.clearRect(0, 0, width, height);

        g2.setPaint(Color.WHITE);


        Font font;
        //两个字及以上
        if (nameWritten.length() >= 2) {
            font = new Font("微软雅黑", Font.PLAIN, 30);
            g2.setFont(font);

            String firstWritten = nameWritten.substring(0, 1);
            String secondWritten = nameWritten.substring(1, 2);

            //两个中文 如 言曌
            if (isChinese(firstWritten) && isChinese(secondWritten)) {
                g2.drawString(nameWritten, 20, 60);
            }
            //首中次英 如 罗Q
            else if (isChinese(firstWritten) && !isChinese(secondWritten)) {
                g2.drawString(nameWritten, 27, 60);
                //首英,如 AB
            } else {
                nameWritten = nameWritten.substring(0, 1);
            }
        }
        //一个字
        if (nameWritten.length() == 1) {
            //中文
            if (isChinese(nameWritten)) {
                font = new Font("微软雅黑", Font.PLAIN, 50);
                g2.setFont(font);
                g2.drawString(nameWritten, 25, 70);
            }
            //英文
            else {
                font = new Font("微软雅黑", Font.PLAIN, 55);
                g2.setFont(font);
                g2.drawString(nameWritten.toUpperCase(), 33, 67);
            }

        }

        return makeRoundedCorner(bi, cornerRadius);
    }

    /**
     * 判断字符串是否为中文
     *
     * @param charStr 字符
     * @return {@link boolean}
     * @method isChinese
     * @date 2021/4/19 18:01
     */
    public static boolean isChinese(String charStr) {
        String regEx = "[\\u4e00-\\u9fa5]+";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(charStr);
        return m.find();
    }

    /**
     * 获得随机颜色
     *
     * @return {@link Color}
     * @method getRandomColor
     * @date 2021/4/19 18:01
     */
    private static Color getRandomColor() {
        String[] beautifulColors = new String[] {"232,221,203", "205,179,128", "3,101,100", "3,54,73", "3,22,52",
            "237,222,139", "251,178,23", "96,143,159", "1,77,103", "254,67,101", "252,157,154",
            "249,205,173", "200,200,169", "131,175,155", "229,187,129", "161,23,21", "34,8,7",
            "118,77,57", "17,63,61", "60,79,57", "95,92,51", "179,214,110", "248,147,29",
            "227,160,93", "178,190,126", "114,111,238", "56,13,49", "89,61,67", "250,218,141",
            "3,38,58", "179,168,150", "222,125,44", "20,68,106", "130,57,53", "137,190,178",
            "201,186,131", "222,211,140", "222,156,83", "23,44,60", "39,72,98", "153,80,84",
            "217,104,49", "230,179,61", "174,221,129", "107,194,53", "6,128,67", "38,157,128",
            "178,200,187", "69,137,148", "117,121,71", "114,83,52", "87,105,60", "82,75,46",
            "171,92,37", "100,107,48", "98,65,24", "54,37,17", "137,157,192", "250,227,113",
            "29,131,8", "220,87,18", "29,191,151", "35,235,185", "213,26,33", "160,191,124",
            "101,147,74", "64,116,52", "255,150,128", "255,94,72", "38,188,213", "167,220,224",
            "1,165,175", "179,214,110", "248,147,29", "230,155,3", "209,73,78", "62,188,202",
            "224,160,158", "161,47,47", "0,90,171", "107,194,53", "174,221,129", "6,128,67",
            "38,157,128", "201,138,131", "220,162,151", "137,157,192", "175,215,237", "92,167,186",
            "255,66,93", "147,224,255", "247,68,97", "185,227,217"};
        int len = beautifulColors.length;
        Random random = new Random();
        String[] color = beautifulColors[random.nextInt(len)].split(",");
        return new Color(Integer.parseInt(color[0]), Integer.parseInt(color[1]), Integer.parseInt(color[2]));
    }


    /**
     * 图片做圆角处理
     *
     * @param image        图片
     * @param cornerRadius 圆角度
     * @return {@link BufferedImage}
     * @method makeRoundedCorner
     * @date 2021/4/19 18:02
     */
    private static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = output.createGraphics();
        g2.setComposite(AlphaComposite.Src);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.WHITE);
        g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));
        g2.setComposite(AlphaComposite.SrcAtop);
        g2.drawImage(image, 0, 0, null);
        g2.dispose();
        return output;
    }

    public static void main(String[] args) throws IOException {
        String name = "王冬喜";
        final File file = new File("C:\\Users\\10275\\Desktop\\王冬喜.png");
        //outputFileImg(name, file);
        final String s = outputBase64Img(name);
        System.out.println(s);
    }
}