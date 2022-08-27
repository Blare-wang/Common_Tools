package com.itblare.itools.image.similarity;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.image.similarity
 * ClassName:   ImageHashAlgorithm
 * Author:   Blare
 * Date:     Created in 2021/5/11 0:31
 * Description:    图片相似度HASH算法
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/5/11 0:31    1.0.0         图片相似度HASH算法
 */

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图片相似度HASH算法
 *
 * @author Blare
 * @create 2021/5/11 0:31
 * @since 1.0.0
 */
public interface ImageHashAlgorithm {

    /**
     * 计算图片HASH值
     * 优化：由于Java对图片的读取速度非常慢，可使用OpenCV进行算法重写
     *
     * @param src 源图片
     * @return {@link char[]}
     * @method hash
     * @date 2021/5/11 0:34
     */
    char[] hash(BufferedImage src);

    /**
     * 计算汉明距离
     * 注：两个等长字符串之间的汉明距离（英语：Hamming distance）是两个字符串对应位置的不同字符的个数
     * 换句话说，它就是将一个字符串变换成另外一个字符串所需要替换的字符个数
     * 参考：https://zh.wikipedia.org/wiki/%E6%B1%89%E6%98%8E%E8%B7%9D%E7%A6%BB
     *
     * @param hashChar1 图片HASH值1
     * @param hashChar2 图片HASH值1
     * @return {@link int}
     * @method diff
     * @date 2021/5/11 0:35
     */
    default int diff(char[] hashChar1, char[] hashChar2) {
        int diff = 0;
        for (int i = 0; i < hashChar1.length; i++) {
            if (hashChar1[i] != hashChar2[i]) {
                diff++;
            }
        }
        return diff;
    }

    /**
     * 简化色彩，灰度化处理
     *
     * @param rgb RGB像素
     * @return {@link int}
     * @method gray
     * @date 2021/5/11 0:13
     */
    default int gray(int rgb) {
        // 将最高位（24-31）的信息（alpha通道）存储到a变量
        int a = rgb & 0xff000000;
        // 取出次高位（16-23）红色分量的信息
        int r = (rgb >> 16) & 0xff;
        //取出中位（8-15）绿色分量的信息
        int g = (rgb >> 8) & 0xff;
        //取出低位（0-7）蓝色分量的信息
        int b = rgb & 0xff;
        // NTSC luma，算出灰度值
        rgb = (r * 77 + g * 151 + b * 28) >> 8;
        //(int)(r * 0.3 + g * 0.59 + b * 0.11)
        //将灰度值送入各个颜色分量
        return a | (rgb << 16) | (rgb << 8) | rgb;

    }

    /**
     * 重置图片大小
     *
     * @param src    原图片
     * @param width  像素宽
     * @param height 像素高
     * @return {@link BufferedImage}
     * @method resize
     * @date 2021/5/10 23:52
     */
    default BufferedImage resize(BufferedImage src, int width, int height) {
        // 构建不带透明色的画布，8位RGB颜色分量的图像
        final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        // 获取绘笔
        final Graphics graphics = image.getGraphics();
        // 绘制一副比例图像，observer ：绘制过程中以通告为目的的对象（可能为null）
        graphics.drawImage(src, width, height, null);
        return image;
    }
}
