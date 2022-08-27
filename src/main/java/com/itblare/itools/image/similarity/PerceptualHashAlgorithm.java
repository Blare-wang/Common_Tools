package com.itblare.itools.image.similarity;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.image.similarity
 * ClassName:   PerceptualHashAlgorithm
 * Author:   Blare
 * Date:     Created in 2021/5/10 23:47
 * Description:    感知哈希算法
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/5/10 23:47    1.0.0         感知哈希算法
 */

import java.awt.image.BufferedImage;

/**
 * 感知哈希算法
 * 主要流程
 * 1.缩小尺寸为8*8
 * 2.简化色彩，转变为灰度图
 * 3.计算DCT，得到32*32的DCT系数矩阵
 * 4.缩小DCT，只保留左上角的8*8的矩阵
 * 5.计算DCT的平均值
 * 6.计算哈希值
 * 参考：参考：https://blog.csdn.net/luoweifu/article/details/8214959/
 *
 * @author Blare
 * @create 2021/5/10 23:47
 * @since 1.0.0
 */
public class PerceptualHashAlgorithm implements ImageHashAlgorithm {

    @Override
    public char[] hash(BufferedImage src) {
        int width = 8;
        int height = 8;
        BufferedImage image = this.resize(src, width, height);
        int[] dctDate = new int[width * height];
        int index = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int pixel = image.getRGB(j, i);
                int gray = this.gray(pixel);
                dctDate[index++] = gray;
            }
        }
        // 计算DCT
        dctDate = DCT(dctDate, width);
        // 计算灰度图像的均值
        int avg = averageGray(dctDate, width, height);
        // 计算哈希值
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (dctDate[i * height + j] >= avg) {
                    sb.append("1");
                } else {
                    sb.append("0");
                }
            }
        }
        //
        long result;
        if (sb.charAt(0) == '0') {
            result = Long.parseLong(sb.toString(), 2);
        } else {
            //如果第一个字符是1，则表示负数，不能直接转换成long，
            result = 0x8000000000000000L ^ Long.parseLong(sb.substring(1), 2);
        }
        sb = new StringBuilder(Long.toHexString(result));
        if (sb.length() < 16) {
            int n = 16 - sb.length();
            for (int i = 0; i < n; i++) {
                sb.insert(0, "0");
            }
        }
        return sb.toString().toCharArray();
    }

    /**
     * 求灰度图像的均值
     *
     * @param pix    图像的像素矩阵
     * @param width  图像的宽
     * @param height 图像的高
     * @return {@link int}
     * @method averageGray
     * @date 2021/5/11 1:07
     */
    private int averageGray(int[] pix, int width, int height) {
        int sum = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sum = sum + pix[i * width + j];
            }
        }
        return sum / (width * height);
    }

    /**
     * 计算DCT，得到32*32的DCT系数矩阵
     *
     * @param pix 原图像的数据矩阵
     * @param n   原图像(n*n)的高或宽
     * @return {@link int[]}
     * @method DCT
     * @date 2021/5/11 0:51
     */
    private int[] DCT(int[] pix, int n) {
        final double[][] iMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                iMatrix[i][j] = (double) (pix[i * n + j]);
            }
        }
        //求系数矩阵
        double[][] quotient = coefficient(n);
        //转置系数矩阵
        double[][] quotientT = transposingMatrix(quotient, n);
        // DCT系数矩阵转置为数组
        int[] newPix = new int[n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                newPix[i * n + j] = (int) iMatrix[i][j];
            }
        }
        return newPix;
    }

    /**
     * 矩阵转置
     *
     * @param matrix 原矩阵
     * @param n      矩阵(n*n)的高或宽
     * @return {@link double[][]}
     * @method transposingMatrix
     * @date 2021/5/11 1:05
     */
    private double[][] transposingMatrix(double[][] matrix, int n) {
        double[][] nMatrix = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                nMatrix[i][j] = matrix[j][i];
            }
        }
        return nMatrix;
    }

    /**
     * 求离散余弦变换的系数矩阵
     * https://baike.baidu.com/item/%E7%A6%BB%E6%95%A3%E4%BD%99%E5%BC%A6%E5%8F%98%E6%8D%A2/7118270?fr=aladdin
     *
     * @param n n*n矩阵的大小
     * @return {@link double[][]}
     * @method coefficient
     * @date 2021/5/11 0:55
     */
    private double[][] coefficient(int n) {
        final double[][] coeff = new double[n][n];
        // 1/n的平方
        double sqrt = 1.0 / Math.sqrt(n);
        for (int i = 0; i < n; i++) {
            coeff[0][i] = sqrt;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                coeff[i][j] = Math.sqrt(2.0 / n) * Math.cos(i * Math.PI * (j + 0.5) / (double) n);
            }
        }
        return coeff;
    }
}
