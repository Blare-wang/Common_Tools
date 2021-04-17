package com.itblare.itools.file;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.tools.file
 * ClassName:   FileUtil
 * Author:   Blare
 * Date:     Created in 2021/4/12 11:37
 * Description:    文件处理工具
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/12 11:37    1.0.0             文件处理工具
 */

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 一句话功能简述：文件处理工具
 *
 * @author Blare
 * @create 2021/4/12 11:37
 * @since 1.0.0
 */
public class FileProcessor {

    private static final int MAGIC_NUM = 1024;

    /**
     * 功能描述: 文件路径
     *
     * @param prefix 前缀
     * @param suffix 后缀
     * @return {@link String} 返回拼接路径
     * @method getPath
     * @author Blare
     * @date 2021/4/13 9:29
     * @updator Blare
     */
    public static String getPath(String prefix, String suffix) {
        //文件路径
        LocalDateTime dateTime = LocalDateTime.now();
        String path = dateTime.getYear() + "/" + dateTime.getMonthValue() + "/" + dateTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        if (Objects.isNull(prefix) || "".equals(prefix)) {
            return prefix.endsWith("/") ? prefix + path : prefix + "/" + path;
        }
        return path + suffix;
    }

    /**
     * 功能描述: 获取文件后缀
     *
     * @param fileName 文件名
     * @return {@link String} 返回文件后缀
     * @method getSuffix
     * @author Blare
     * @date 2021/4/13 9:30
     * @updator Blare
     */
    public static String getSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        index = -1 == index ? fileName.length() : index;
        return fileName.substring(index);
    }

    /**
     * 功能描述: 获取文件后缀
     *
     * @param fileName 文件名
     * @return {@link String} 返回文件后缀
     * @method getSuffix
     * @author Blare
     * @date 2021/4/13 9:30
     * @updator Blare
     */
    public static String getSuffixToUpperCase(String fileName) {
        return getSuffix(fileName).toUpperCase();
    }

    /**
     * 功能描述: 获取文件前缀
     *
     * @param fileName 文件名
     * @return {@link String} 返回文件前缀
     * @method getPrefix
     * @author Blare
     * @date 2021/4/13 9:34
     * @updator Blare
     */
    public static String getPrefix(String fileName) {
        int idx = fileName.lastIndexOf(".");
        int xie = fileName.lastIndexOf("/");
        idx = idx == -1 ? fileName.length() : idx;
        xie = xie == -1 ? 0 : xie + 1;
        return fileName.substring(xie, idx);
    }

    /**
     * 功能描述: 获取网络文件流
     *
     * @param url     URL连接
     * @param referer referer验证，不加referer可能会403
     * @return {@link InputStream}
     * @method getInputStreamByUrl
     * @author Blare
     * @date 2021/4/13 9:51
     * @updator Blare
     */
    public static InputStream getInputStreamByUrl(String url, String referer) {
        HttpGet httpGet = new HttpGet(checkUrl(url));
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
        if (Objects.nonNull(referer) && !"".equals(referer)) {
            httpGet.setHeader("referer", referer);
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        InputStream in = null;
        try {
            response = httpclient.execute(httpGet);
            in = response.getEntity().getContent();
            if (response.getStatusLine().getStatusCode() == 200) {
                return in;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return in;
    }

    /**
     * 功能描述: 检查URL完整性
     *
     * @param url URL连接
     * @return {@link String}
     * @method checkUrl
     * @author Blare
     * @date 2021/4/13 9:50
     * @updator Blare
     */
    private static String checkUrl(String url) {
        if (Objects.isNull(url) || "".equals(url)) {
            if (url.startsWith("http://") || url.startsWith("https://")) {
                return url;
            }
            return url.startsWith("//") ? "https:" + url : "http://" + url;
        }
        return null;
    }


    /**
     * 功能描述: 根据byte数组，生成文件
     *
     * @method getFile
     * @param bfile byte字节流
     * @param filePath 文件存放目录
     * @param fileName 文件名称(不带后缀的)
     * @return {@link String}
     * @author Blare
     * @date 2021/4/13 16:02
     * @updator Blare
     */
    public String getFile(byte[] bfile, String filePath,String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        String path = "";
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&!dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            //创建临时文件的api参数 (文件前缀,文件后缀,存放目录)
            file = File.createTempFile(fileName, "IMG_SUFFIX", dir);
            String tempFileName = file.getName();
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
            path = file.getPath();
            return path;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("创建临时文件失败!" + e.getMessage());
            return null;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 功能描述: 读取字节输入流内容
     *
     * @method readInputStream
     * @param is 文件流
     * @return {@link byte[]}
     * @author Blare
     * @date 2021/4/13 16:32
     * @updator Blare
     */
    private static byte[] readInputStream(InputStream is) {
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        byte[] buff = new byte[1024 * 2];
        int len = 0;
        try {
            while((len = is.read(buff)) != -1) {
                writer.write(buff, 0, len);
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toByteArray();
    }

    /**
     * 功能描述: 根据文件大小，获取格式化文件描述
     *
     * @method getFormattedSize
     * @param size 文件大小
     * @return {@link String}
     * @author Blare
     * @date 2021/4/17 23:34
     * @updator Blare
     */
    public static String getFormattedSize(long size) {
        DecimalFormat df = new DecimalFormat("#.00");
        double KB = (double) size / MAGIC_NUM;
        if (KB <= MAGIC_NUM) {
            return df.format(KB) + "KB";
        }
        double MB = KB / MAGIC_NUM;
        if (MB <= MAGIC_NUM) {
            return df.format(MB) + "MB";
        }
        double GB = MB / MAGIC_NUM;
        if (GB <= MAGIC_NUM) {
            return df.format(GB) + "GB";
        }
        throw new RuntimeException("File size too Big!!!");
    }
}
