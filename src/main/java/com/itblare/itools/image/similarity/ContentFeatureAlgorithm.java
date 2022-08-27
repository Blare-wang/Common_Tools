package com.itblare.itools.image.similarity;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.image.similarity
 * ClassName:   ContentFeatureAlgorithm
 * Author:   Blare
 * Date:     Created in 2021/5/11 1:14
 * Description:    内容特征算法
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/5/11 1:14    1.0.0         内容特征算法
 */

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.util.Arrays;

/**
 * 内容特征算法
 * 参考1：http://www.ruanyifeng.com/blog/2013/03/similar_image_search_part_ii.html
 *
 * @author Blare
 * @create 2021/5/11 1:14
 * @since 1.0.0
 */
public class ContentFeatureAlgorithm {

    // 两个特征矩阵的不同之处越少，就代表两张图片越相似。这可以用"异或运算"实现（即两个值之中只有一个为1，则运算结果为1，否则运算结果为0）。对不同图片的特征矩阵进行"异或运算"，结果中的1越少，就是越相似的图片。

    /**
     * 获取直方图数据
     *
     * @param src 灰度图像字节数据
     * @return {@link byte[]}
     * @method getHistogramPlotData
     * @date 2021/5/11 15:26
     */
    private byte[] getHistogramPlotData(BufferedImage src) {
        // 获取原始图像的像素数据(Raster 用于存储图像的像素数据【图像内容】)
        Raster raster = src.getData();
        DataBuffer buffer = raster.getDataBuffer();
        if (buffer.getDataType() != DataBuffer.TYPE_BYTE) {
            return null;
        }
        if (buffer.getNumBanks() != 1) {
            return null;
        }
        DataBufferByte byteBuffer = (DataBufferByte) buffer;
        //
        byte[] srcData = byteBuffer.getData(0);
        // 获取原始图片分辨率
        int width = src.getWidth();
        int height = src.getHeight();
        // 健全性检查图片
        if (width * height != srcData.length) {
            return null;
        }
        System.out.printf("Loaded image: width: %d, height: %d, num bytes: %d\n", width, height, srcData.length);
        // 获取直方图数据
        int[] histData = getHistogramData(srcData);
        // 确定一个合理的阈值
        int threshold = getThreshold(srcData, histData);

        // todo 获取直方图 ？？？？？？
        int numPixels = 256 * 100;
        byte[] histPlotData = new byte[numPixels];
        int maxLevelValue = 0;
        final int max = Arrays.stream(histData).max().orElse(0);
        //final byte[] dstData = getDstData(srcData, histData);
        for (int l = 0; l < 256; l++) {
            int ptr = (numPixels - 256) + l;
            int val = (100 * histData[l]) / max;

            if (l == threshold) {
                for (int i = 0; i < 100; i++, ptr -= 256) {
                    histPlotData[ptr] = (byte) 128;
                }
            } else {
                for (int i = 0; i < 100; i++, ptr -= 256) {
                    histPlotData[ptr] = (val < i) ? (byte) 255 : 0;
                }
            }
        }
        return histPlotData;
    }

    /**
     * 应用阈值创建二进制图像（黑白缩略图）
     *
     * @param srcData   灰度图像字节数据
     * @param threshold 阈值
     * @return {@link byte[]}
     * @method getDstSrcData
     * @date 2021/5/11 14:09
     */
    public byte[] getDestBinaryData(byte[] srcData, int threshold) {
        byte[] dstData = new byte[srcData.length];
        int ptr = 0;
        while (ptr < srcData.length) {
            // 0xFF表示的数二进制1111 1111 占一个字节。和其进行&操作的数，最低8位，不会发生变化
            dstData[ptr] = ((0xFF & srcData[ptr]) >= threshold) ? (byte) 255 : 0;
            ptr++;
        }
        return dstData;
    }

    /**
     * 获取直方图数据
     *
     * @param srcData 灰度图像字节数据
     * @return {@link int[]}
     * @method getHistData
     * @date 2021/5/11 14:57
     */
    private int[] getHistogramData(byte[] srcData) {

        int[] histData = new int[256];
        // 清除直方图数据，将所有值设置为零
        int ptr = 0;
        while (ptr < histData.length) {
            histData[ptr++] = 0;
        }
        // 计算直方图并找到最大值的水平
        while (ptr < srcData.length) {
            int h = 0xFF & srcData[ptr];
            histData[h]++;
            ptr++;
        }
        return histData;
    }

    /**
     * 获取一个合理的阈值，正确呈现照片中的轮廓
     * 参考：http://www.labbookpages.co.uk/software/imgProc/otsuThreshold.html
     *
     * @param srcData 灰度图像字节数据
     * @return {@link int}
     * @method getThreshold
     * @date 2021/5/11 11:23
     */
    private int getThreshold(byte[] srcData, int[] histData) {
        // 总像素数
        int total = srcData.length;
        float sum = 0;
        for (int t = 0; t < 256; t++) {
            sum += t * histData[t];
        }
        float sumB = 0;
        int wB = 0;
        int wF = 0;
        float varMax = 0;
        int threshold = 0;
        /*
         * 1. 类内差异 = w1(σ1的平方) + w2(σ2的平方)
         * 2. 类间差异 = w1w2(μ1-μ2)^2
         * 两个式子是等价的：得到"类内差异"的最小值，等同于得到"类间差异"的最大值。
         * 前景色与背景色反差越大，轮廓就越明显，意味着使得前景色和背景色各自的"类内差异最小"，或者"类间差异最大"，那么这个值就是理想的阈值。
         * 将阈值从灰度的最低值到最高值，依次取一遍，分别代入上面的算式。使得"类内差异最小"或"类间差异最大"的那个值，就是最终的阈值。
         */
        for (int t = 0; t < 256; t++) {
            // 重量背景
            wB += histData[t];
            if (wB == 0) {
                break;
            }
            // 重量前景
            wF = total - wB;
            if (wF == 0) {
                break;
            }
            sumB += (float) (t * histData[t]);
            // 平均背景
            float mB = sumB / wB;
            // 平均前景
            float mF = (sum - sumB) / wF;
            // 计算类间差异（类内差异 = w1(σ1的平方) + w2(σ2的平方)，类间差异 = w1w2(μ1-μ2)^2）
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
            // 检查是否找到新的最大值
            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = t;
            }
        }
        return threshold;
    }
}