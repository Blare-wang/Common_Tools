package com.itblare.itools.image.similarity;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.image.similarity
 * ClassName:   ColorHistogramAlgorithm
 * Author:   Blare
 * Date:     Created in 2021/5/11 9:42
 * Description:    颜色分布法
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/5/11 9:42    1.0.0         颜色分布法
 */

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Objects;

/**
 * 颜色分布法
 * 参考1：http://www.ruanyifeng.com/blog/2013/03/similar_image_search_part_ii.html
 * 参考2：http://www.ruanyifeng.com/blog/2013/03/cosine_similarity.html
 * 参考3：https://en.wikipedia.org/wiki/Pearson_correlation_coefficient
 *
 * @author Blare
 * @create 2021/5/11 9:42
 * @since 1.0.0
 */
public class ColorHistogramAlgorithm {

    /**
     * 获取余弦相似度
     * 余弦相似度计算 = E(XY)/Math.sql(Math.pow(X,2))*Math.sql(Math.pow(Y,2))
     * 余弦值越接近1，就表明夹角越接近0度，也就是两个向量越相似，这就叫"余弦相似性"
     *
     * @param src1 源图1
     * @param src2 源图1
     * @return {@link double}
     * @method getCosineSimilarity
     * @date 2021/5/11 10:38
     */
    private double getCosineSimilarity(BufferedImage src1, BufferedImage src2) {
        // 获取像素的64种组合
        final int[] array1 = statisticalPixelCombination(src1);
        final int[] array2 = statisticalPixelCombination(src2);
        double sumXY = 0;
        double sumXX = 0;
        double sumYY = 0;
        // 计算
        int n = 64;
        for (int i = 0; i < 64; i++) {
            int x = array1[i];
            int y = array2[i];
            sumXY += (x * y);
            sumXX += Math.pow(x, 2);
            sumYY += Math.pow(y, 2);
        }
        // 计算皮尔系数分子
        double molecular = sumXY;
        // 计算皮尔系数分母
        double denominator = Math.sqrt(sumXX) * Math.sqrt(sumYY);
        // 分母不为0
        if (0 == denominator) {
            return 0;
        }
        return molecular / denominator;
    }

    /**
     * 获取皮尔逊相关系数
     * 皮尔逊相关系数计算相似度：皮尔系数（X,Y）=(E(XY)-E(X)*E(Y))/( (Math.sqrt( E(Math.pow(X,2))-Math.pow(E(X),2) ) )* (Math.sqrt( E(Math.pow(y,2))-Math.pow(E(y),2) ) ) )
     *
     * @param src1 源图1
     * @param src2 源图1
     * @return {@link double}
     * @method getPearsonCorrelationCoefficient
     * @date 2021/5/11 10:13
     */
    public double getPearsonCorrelationCoefficient(BufferedImage src1, BufferedImage src2) {
        // 获取像素的64种组合
        final int[] array1 = statisticalPixelCombination(src1);
        final int[] array2 = statisticalPixelCombination(src2);
        // 定义：E(X)
        double sumX = 0;
        //定义：E(Y)、
        double sumY = 0;
        // 定义：Math.sqrt( E(Math.pow(X,2))-Math.pow(E(X),2) )
        double sumX_sq = 0;
        // 定义：Math.sqrt( E(Math.pow(y,2))-Math.pow(E(y),2) )
        double sumY_sq = 0;
        // 定义：E(XY)
        double sumXY = 0;
        // 计算
        int n = 64;
        for (int i = 0; i < 64; i++) {
            int x = array1[i];
            int y = array2[i];
            sumX += x;
            sumY += y;
            sumX_sq += Math.pow(x, 2);
            sumY_sq += Math.pow(y, 2);
            sumXY += (x * y);
        }

        // 计算皮尔系数分子
        double molecular = sumXY - (sumX * sumY) / n;
        // 计算皮尔系数分母
        double denominator = Math.sqrt(sumX_sq - Math.pow(sumX, 2) / n) * Math.sqrt(sumY_sq - Math.pow(sumY, 2) / n);
        // 分母不为0
        if (0 == denominator) {
            return 0;
        }
        return molecular / denominator;
    }

    /**
     * 统计每一种组合包含的像素数量
     * 组成一个64维向量(7414, 230, 0, 0, 8, ..., 109, 0, 0, 3415, 53929)。
     * 这个向量就是这张图片的特征值或者叫"指纹"。
     *
     * @param src 源图片
     * @return {@link int[]}
     * @method statisticalPixelCombination
     * @date 2021/5/11 9:50
     */
    private int[] statisticalPixelCombination(BufferedImage src) {
        int width = src.getWidth();
        int height = src.getHeight();
        // 统计map, 用于记录每一种组合数量
        final HashMap<String, Integer> map = new HashMap<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                final int rgb = src.getRGB(j, i);
                //取出次高位（16-23）红色分量的信息
                int r = (rgb >> 16) & 0xff;
                //取出中位（8-15）绿色分量的信息
                int g = (rgb >> 8) & 0xff;
                //取出低位（0-7）蓝色分量的信息
                int b = rgb & 0xff;
                String key = getDivideArea(r) + getDivideArea(g) + getDivideArea(b);
                final Integer count = map.get(key);
                if (Objects.isNull(count)) {
                    map.put(key, 1);
                } else {
                    map.put(key, count + 1);
                }
            }
        }

        // 统计结果进行一个64维向量的组成‘
        final int[] result = new int[64];
        String key;
        int index = 0;
        for (int r = 0; r < 4; r++) {
            for (int g = 0; g < 4; g++) {
                for (int b = 0; b < 4; b++) {
                    key = String.valueOf(r) + g + b;
                    final Integer count = map.get(key);
                    if (Objects.isNull(count)) {
                        result[index] = 0;
                    } else {
                        result[index] = count;
                    }
                    index++;
                }
            }
        }
        return result;
    }

    /**
     * 每种原色都可以取256个值，划分四个区域。
     * 将0～255分成四个区：0～63为第0区，64～127为第1区，128～191为第2区，192～255为第3区。
     * 意味着红绿蓝分别有4个区，总共可以构成64种组合（4的3次方）
     *
     * @param color 颜色值
     * @return {@link String}
     * @method getDivideArea
     * @date 2021/5/11 9:47
     */
    private String getDivideArea(int color) {
        if (color < 64) {
            return "0";
        } else if (color < 128) {
            return "1";
        } else if (color < 192) {
            return "3";
        } else if (color < 256) {
            return "4";
        } else {
            return null;
        }
    }
}
