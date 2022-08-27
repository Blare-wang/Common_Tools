package com.itblare.itools.image.similarity;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.image.similarity
 * ClassName:   DifferenceHashAlgorithm
 * Author:   Blare
 * Date:     Created in 2021/5/10 23:48
 * Description:    差值哈希算法
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/5/10 23:48    1.0.0         差值哈希算法
 */

import java.awt.image.BufferedImage;

/**
 * 差值哈希算法
 * 主要流程
 * 1. 缩小尺寸为8*8
 * 2. 简化色彩，转变为灰度图
 * 3. 计算灰度差值
 * 4. 计算哈希值
 *
 * @author Blare
 * @create 2021/5/10 23:48
 * @since 1.0.0
 */
public class DifferenceHashAlgorithm implements ImageHashAlgorithm {

    @Override
    public char[] hash(BufferedImage src) {
        int width = 8;
        int height = 8;
        // 缩放图片
        BufferedImage image = this.resize(src, width, height);
        // 像素矩阵
        final int[] ints = new int[width * height];
        // 像素点索引
        int index = 0;
        /*
         * 像素填充
         * (0,0),(0,1),(0,2),(0,3)......(0,7)
         * (1,0),(1,1),(1,2),(1,3)......(1,7)
         * ......
         * (7,0),(7,1),(7,2),(7,3)......(7,7)
         */
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // 像素点
                final int pixel = image.getRGB(j, i);
                // 简化像素(灰度化)
                int gray = this.gray(pixel);
                // 填充
                ints[index++] = gray;
            }
        }
        // 计数查找HASH
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                /*
                 * 前面一个像素比后面一个像素大，计“1”，否则计“0”
                 * (0)>=(1)，(8)>=(9)，...，(56)>=(57)
                 * (1)>=(2)，(9)>=(10)，...，(57)>=(59)
                 * ...
                 * (8)>=(9)，(15)>=(16)，...，(63)>=(64)
                 */
                if (ints[8 * j + i] >= ints[8 * j + i + 1]) {
                    builder.append(1);
                } else {
                    builder.append(0);
                }
            }
        }
        return builder.toString().toCharArray();
    }
}
