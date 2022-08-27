package com.itblare.itools.captcha;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.tools.captcha
 * ClassName:   SliderCaptcha
 * Author:   Blare
 * Date:     Created in 2021/4/13 16:55
 * Description:    滑动验证码
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/13 16:55    1.0.0             滑动验证码
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static java.awt.Font.BOLD;

/**
 * 滑动验证码
 *
 * @author Blare
 * @create 2021/4/13 16:55
 * @since 1.0.0
 */
public class SliderCaptcha {

    private static final Logger logger = LoggerFactory.getLogger(SliderCaptcha.class);

    /**
     * 线宽
     */
    private static final float BASIC_STROKE_WIDTH = 5F;

    /**
     * 图片类型
     */
    private static final String IMG_FILE_TYPE = "jpg";

    /**
     * 临时图片类型
     */
    private static final String TEMP_IMG_FILE_TYPE = "png";

    /**
     * 安全随机数
     */
    private static final SecureRandom random = new SecureRandom();

    /**
     * 高斯模糊高斯矩阵阶数
     */
    private static final int shu = 1;

    /**
     * 数组大小
     */
    private static final int size = 2 * shu + 1;

    /**
     * base64 Decoder
     */
    private static final Base64.Decoder decodeObj = Base64.getDecoder();

    /**
     * base64 Decoder
     */
    private static final Base64.Encoder encodeObj = Base64.getEncoder();

    /**
     * 默认字符集
     */
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    /**
     * 根据模板切图,并返回背景图和滑块图
     *
     * @param templateFile 模板图片
     * @param targetFile   目标原图片
     * @return {@link Map}
     * @method pictureTemplatesCut
     * @date 2020/10/31 20:58
     */
    public static Map<String, Object> pictureTemplatesCut(File templateFile, File targetFile) throws Exception {
        return pictureTemplatesCut(templateFile, targetFile, false);
    }

    /**
     * 根据模板切图,并返回背景图和滑块图
     *
     * @param templateFile 模板图片
     * @param targetFile   目标原图片
     * @param gaussianBlur 高斯模糊处理
     * @return {@link Map}
     * @method pictureTemplatesCut
     * @date 2020/10/31 20:58
     */
    public static Map<String, Object> pictureTemplatesCut(File templateFile, File targetFile, boolean gaussianBlur) throws Exception {
        Map<String, Object> pictureMap = new HashMap<>();
        // 读取模板图
        BufferedImage imageTemplate = ImageIO.read(templateFile);
        // 宽
        int templateWidth = imageTemplate.getWidth();
        // 高
        int templateHeight = imageTemplate.getHeight();

        // 读取原图
        BufferedImage oriImage = ImageIO.read(targetFile);
        // 宽
        int oriImageWidth = oriImage.getWidth();
        // 高
        int oriImageHeight = oriImage.getHeight();

        // 随机生成抠图坐标X,Y
        // X轴距离右端targetWidth
        int widthRandom = random.nextInt(oriImageWidth - 2 * templateWidth) + templateWidth;
        // Y轴距离底部targetHeight以上
        int heightRandom = random.nextInt(oriImageHeight - templateHeight);
        logger.info("原图大小{} x {},随机生成的坐标 X,Y 为（{}，{}）", oriImageWidth, oriImageHeight, widthRandom, heightRandom);

        /*新建透明画布*/
        // 新建一个和模板一样大小的图像画布，TYPE_4BYTE_ABGR表示具有8位RGBA颜色分量的图像，正常取imageTemplate.getType()
        BufferedImage newImageCanvas = new BufferedImage(templateWidth, templateHeight, imageTemplate.getType());
        // 得到画笔对象
        Graphics2D graphics = newImageCanvas.createGraphics();
        // 使得背景透明：如果需要生成RGB格式，需要做如下配置：参数width 和 height 要和是前面画布的对应，Transparency 设置透明
        newImageCanvas = graphics.getDeviceConfiguration().createCompatibleImage(templateWidth, templateHeight, Transparency.TRANSLUCENT);
        /*新建透明画布*/

        /*新建的图像根据模板颜色赋值,源图生成遮罩*/
        cutByTemplate(oriImage, imageTemplate, newImageCanvas, widthRandom, heightRandom, gaussianBlur);
        /*新建的图像根据模板颜色赋值,源图生成遮罩*/

        // 设置“抗锯齿”的属性：去除锯齿(当设置的字体过大的时候,会出现锯齿)
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Stroke：描边决定着图形或文字的轮廓。通过setStroke()方法定义描边。BasicStroke：指定线宽，端点样式和接头样式
        graphics.setStroke(new BasicStroke(BASIC_STROKE_WIDTH, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
        // 画图：参数x，y为图片左上角坐标
        graphics.drawImage(newImageCanvas, 0, 0, null);
        // 绘制完成
        graphics.dispose();

        // 新建字节数组流（盛装newImageCanvas）
        ByteArrayOutputStream newImageOs = new ByteArrayOutputStream();
        // 利用ImageIO类提供的write方法，将newImage以png图片的数据模式写入流
        ImageIO.write(newImageCanvas, TEMP_IMG_FILE_TYPE, newImageOs);
        // 获取新的图片字节数组
        byte[] newImageByte = newImageOs.toByteArray();

        // 新建字节数组流（盛装oriImagesOs）
        ByteArrayOutputStream oriImagesOs = new ByteArrayOutputStream();
        // 原图加水印
        addWatermark(oriImage, "1027515034@qq.com", 0.5F, 0.95F);
        // 利用ImageIO类提供的write方法，将oriImage以jpg图片的数据模式写入流。
        ImageIO.write(oriImage, IMG_FILE_TYPE, oriImagesOs);
        // 获取原背景图片字节数组
        byte[] oriImageByte = oriImagesOs.toByteArray();

        // BASE64编码 滑块图
        pictureMap.put("slidingImage", encodeToString(newImageByte));
        // BASE64编码 背景图
        pictureMap.put("backImage", encodeToString(oriImageByte));
        // 随机生成抠图坐标X
        pictureMap.put("xWidth", widthRandom);
        // 随机生成抠图坐标Y
        pictureMap.put("yHeight", heightRandom);
        // 返回最终数据
        return pictureMap;
    }

    /**
     * 添加水印
     *
     * @param oriImage  待添加水印图
     * @param watermark 水印字符
     * @param rx        背景图X相对位置
     * @param ry        背景图Y相对位置
     * @method addWatermark
     * @date 2020/10/31 22:01
     */
    @SuppressWarnings("SameParameterValue")
    private static void addWatermark(BufferedImage oriImage, String watermark, float rx, float ry) {
        Graphics2D graphics2D = oriImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        // 设置水印文字颜色
        graphics2D.setColor(Color.BLACK);
        // 设置水印文字Font
        graphics2D.setFont(new Font("宋体", BOLD, 14));
        // 设置水印文字透明度
        graphics2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.5f));
        // 第一参数->设置的内容，后面两个参数->文字在图片上的坐标位置(x,y)
        graphics2D.drawString(watermark, (int) (oriImage.getWidth() * rx), (int) (oriImage.getHeight() * ry));
        graphics2D.dispose();
    }

    /**
     * 根据模板进行抠图
     *
     * @param oriImage      原图
     * @param templateImage 模板图
     * @param newImage      新抠出的小图
     * @param x             随机扣取坐标X
     * @param y             随机扣取坐标y
     * @param gaussianBlur  高斯模糊
     * @method cutByTemplate
     * @date 2020/10/31 21:44
     */
    private static void cutByTemplate(BufferedImage oriImage, BufferedImage templateImage, BufferedImage newImage, int x, int y, boolean gaussianBlur) {
        // 临时数组遍历用于高斯模糊存周边像素值
        int[][] matrix = new int[3][3];
        int[] values = new int[9];

        // 模板图的宽
        int xWidth = templateImage.getWidth();
        // 模板图的高
        int yLength = templateImage.getHeight();
        // 模板图像宽度
        for (int i = 0; i < xWidth; i++) {
            // 模板图片高度
            for (int j = 0; j < yLength; j++) {
                // 当前像素点RGB值【getRGB()：返回的值是sRGB模式的一个负整数。（指定像素的颜色和Alpha分量）】
                int rgb = templateImage.getRGB(i, j);
                // 如果模板图像当前像素点不是透明色 copy源文件信息到目标图片中（rgb < 0 当前像素点不是透明的，有颜色的）
                if (rgb < 0) {
                    // 读取背景图（原图）像素
                    newImage.setRGB(i, j, oriImage.getRGB(x + i, y + j));

                    /*抠图区域模糊*/
                    // 读取原图指定像素点的RGB值
                    readPixel(oriImage, x + i, y + j, values);
                    // 将抠图区域点的RGB值存到缓存矩阵中
                    fillMatrix(matrix, values);
                    // 模糊处理
                    int avgMatrix;
                    if (gaussianBlur) {
                        // 高斯模糊
                        float[][] weightMatrix = getWeightMatrix(shu, 2.5f);
                        final float[][] meanWeightMatrix = getMeanWeightMatrix(weightMatrix, getSumWeight(weightMatrix));
                        avgMatrix = avgMatrixWithWeight(matrix, meanWeightMatrix);
                    } else {
                        // 均值模糊
                        avgMatrix = avgMatrix(matrix);
                    }
                    /*抠图区域模糊*/
                    oriImage.setRGB(x + i, y + j, avgMatrix); // 模糊矩阵代表的像素点
                }

                // 防止数组越界判断
                if (i == (xWidth - 1) || j == (yLength - 1)) {
                    continue;
                }
                // 当前像素点右侧RGB值
                int rightRgb = templateImage.getRGB(i + 1, j);
                // 当前像素点下侧RGB值
                int downRgb = templateImage.getRGB(i, j + 1);
                // 描边处理，,取带像素和无像素的界点，判断该点是不是临界轮廓点,如果是设置该坐标像素是白色
                if ((rgb >= 0 && rightRgb < 0) || (rgb < 0 && rightRgb >= 0) || (rgb >= 0 && downRgb < 0) || (rgb < 0 && downRgb >= 0)) {
                    newImage.setRGB(i, j, Color.WHITE.getRGB());
                    oriImage.setRGB(x + i, y + j, Color.WHITE.getRGB());
                }
            }
        }
    }

    /**
     * 读取原图指定像素点的RGB值L: 图像像素矩阵
     *
     * @param img    原图
     * @param x      随机扣取坐标X
     * @param y      随机扣取坐标y
     * @param pixels 像素点的RGB值数组
     * @method readPixel
     * @date 2020/10/31 23:07
     */
    private static void readPixel(BufferedImage img, int x, int y, int[] pixels) {
        int xStart = x - shu;
        int yStart = y - shu;
        int current = 0;
        for (int i = xStart; i < size + xStart; i++)
            for (int j = yStart; j < size + yStart; j++) {
                int tx = i;
                if (tx < 0) {
                    tx = -tx;

                } else if (tx >= img.getWidth()) {
                    tx = x;
                }
                int ty = j;
                if (ty < 0) {
                    ty = -ty;
                } else if (ty >= img.getHeight()) {
                    ty = y;
                }
                pixels[current++] = img.getRGB(tx, ty);

            }
    }

    /**
     * 将抠图区域点的RGB值存到缓存矩阵中
     *
     * @param matrix 矩阵
     * @param values 像素点的RGB值数组
     * @method fillMatrix
     * @date 2020/11/1 1:28
     */
    private static void fillMatrix(int[][] matrix, int[] values) {
        int filled = 0;
        for (int[] x : matrix) {
            for (int j = 0; j < x.length; j++) {
                x[j] = values[filled++];
            }
        }
    }

    /**
     * 平均化矩阵中每个值（均值模糊化）
     *
     * @param matrix 矩阵
     * @return {@link int}
     * @method avgMatrix
     * @date 2020/10/31 23:22
     */
    private static int avgMatrix(int[][] matrix) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (int i = 0; i < matrix.length; i++) {
            int[] x = matrix[i];
            for (int j = 0; j < x.length; j++) {
                //if (j == 1) {
                //    continue;
                //}
                if (j == 1 && i == 1) {
                    continue;
                }
                Color c = new Color(x[j]);
                r += c.getRed();
                g += c.getGreen();
                b += c.getBlue();
            }
        }
        return new Color(r / 8, g / 8, b / 8).getRGB();
    }

    /**
     * 高斯模糊化
     *
     * @param matrix 矩阵
     * @return {@link int}
     * @method avgMatrixWithWeight
     * @date 2020/10/31 23:22
     */
    private static int avgMatrixWithWeight(int[][] matrix, float[][] weightMatrix) {
        int r = 0;
        int g = 0;
        int b = 0;
        for (int i = 0; i < matrix.length; i++) {
            int[] x = matrix[i];
            for (int j = 0; j < x.length; j++) {
                if (j == 1) {
                    continue;
                }
                Color c = new Color(x[j]);
                r += c.getRed() * weightMatrix[i][j];
                g += c.getGreen() * weightMatrix[i][j];
                b += c.getBlue() * weightMatrix[i][j];
            }
        }
        return new Color(r, g, b).getRGB();
    }

    /**
     * 获取高斯权重矩阵
     *
     * @param n     高斯模糊半径
     * @param sigma 高斯函数的σ值
     * @return {@link float[][]}
     * @method getWeightMatrix
     * @date 2020/11/1 1:28
     */
    @SuppressWarnings("SameParameterValue")
    private static float[][] getWeightMatrix(int n, float sigma) {
        // 数组大小
        int size = 2 * n + 1;
        float sigma22 = 2 * sigma * sigma;
        float sigma22PI = (float) Math.PI * sigma22;

        float[][] kernalData = new float[size][size];
        int row = 0;
        for (int i = -n; i <= n; i++) {
            int column = 0;
            for (int j = -n; j <= n; j++) {
                float xDistance = i * i;
                float yDistance = j * j;
                kernalData[row][column] = (float) Math.exp(-(xDistance + yDistance) / sigma22) / sigma22PI;
                column++;
            }
            row++;
        }
        return kernalData;
    }

    /**
     * 获取矩阵权重总和
     *
     * @param weightMatrix 权重矩阵
     * @return {@link float}
     * @method getSumWeight
     * @date 2020/11/1 1:54
     */
    private static float getSumWeight(float[][] weightMatrix) {
        float sum = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                sum += weightMatrix[i][j];
            }
        }

        return sum;
    }

    /**
     * 矩阵个点除以总值这个过程也叫做”归一问题“,目的是让滤镜的权重总值等于1。否则的话，使用总值大于1的滤镜会让图像偏亮，小于1的滤镜会让图像偏暗
     *
     * @param kernalData 矩阵
     * @param sum        总值
     * @return {@link float[][]}
     * @method getMeanWeightMatrix
     * @date 2020/11/1 15:11
     */
    private static float[][] getMeanWeightMatrix(float[][] kernalData, float sum) {
        for (int i = 0; i < kernalData.length; i++) {
            for (int j = 0; j < kernalData.length; j++) {
                kernalData[i][j] = kernalData[i][j] / sum;
            }
        }
        return kernalData;
    }

    /**
     * 转换字节数组为Base64字符串
     *
     * @param src 字节数组
     * @return {@link String}
     * @method encodeToString
     * @date 2021/1/27 10:48
     */
    private static String encodeToString(byte[] src) {
        if (src.length == 0)
            return "";
        return new String(encodeObj.encode(src), DEFAULT_CHARSET);
    }

    /**
     * 转换Base64字符串为数组
     *
     * @param src Base64字符串
     * @return {@link byte[]}
     * @method decodeFromString
     * @date 2021/1/27 10:49
     */
    private static byte[] decodeFromString(String src) {
        if (src.isEmpty())
            return new byte[0];
        return decodeObj.decode(src.getBytes(DEFAULT_CHARSET));
    }
}