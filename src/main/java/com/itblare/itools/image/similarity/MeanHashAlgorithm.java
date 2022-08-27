package com.itblare.itools.image.similarity;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.image.similarity
 * ClassName:   MeanHashAlgorithm
 * Author:   Blare
 * Date:     Created in 2021/5/10 23:49
 * Description:    均值哈希算法
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/5/10 23:49    1.0.0         均值哈希算法
 */

import java.awt.image.BufferedImage;

/**
 * 均值哈希算法
 * 主要流程
 * 1.缩小尺寸为8*8
 * 2.简化色彩，转变为灰度图
 * 3.计算64个像素的灰度平均值
 * 4.比较每个像素的灰度
 * 5.计算哈希值
 *
 * @author Blare
 * @create 2021/5/10 23:49
 * @since 1.0.0
 */
public class MeanHashAlgorithm implements ImageHashAlgorithm {

    @Override
    public char[] hash(BufferedImage src) {
        int width = 8;
        int height = 8;
        BufferedImage image = this.resize(src, width, height);
        int total = 0;
        int[] ints = new int[width * height];
        int index = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = image.getRGB(j, i);
                int gray = this.gray(pixel);
                ints[index++] = gray;
                total = total + gray;
            }
        }
        StringBuilder res = new StringBuilder();
        int grayAvg = total / (width * height);
        for (int anInt : ints) {
            // 像素大于等于均值，计“1”，否则计“0”
            if (anInt >= grayAvg) {
                res.append("1");
            } else {
                res.append("0");
            }
        }
        return res.toString().toCharArray();
    }
}
