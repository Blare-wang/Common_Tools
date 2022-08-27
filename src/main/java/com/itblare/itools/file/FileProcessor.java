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
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * 文件处理工具
 *
 * @author Blare
 * @create 2021/4/12 11:37
 * @since 1.0.0
 */
public class FileProcessor {

    private static final int MAGIC_NUM = 1024;

    /**
     * 文件路径
     *
     * @param prefix 前缀
     * @param suffix 后缀
     * @return {@link String} 返回拼接路径
     * @method getPath
     * @date 2021/4/13 9:29
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
     * 获取文件后缀
     *
     * @param fileName 文件名
     * @return {@link String} 返回文件后缀
     * @method getSuffix
     * @date 2021/4/13 9:30
     */
    public static String getSuffix(String fileName) {
        int index = fileName.lastIndexOf(".");
        index = -1 == index ? fileName.length() : index;
        fileName = fileName.substring(index);
        int pIndex = fileName.indexOf("?");
        if (pIndex > 0) {
            fileName = fileName.substring(0, pIndex);
        }
        return fileName;
    }

    /**
     * 获取文件后缀
     *
     * @param fileName 文件名
     * @return {@link String} 返回文件后缀
     * @method getSuffix
     * @date 2021/4/13 9:30
     */
    public static String getSuffixToUpperCase(String fileName) {
        return getSuffix(fileName).toUpperCase();
    }

    /**
     * 获取文件前缀
     *
     * @param fileName 文件名
     * @return {@link String} 返回文件前缀
     * @method getPrefix
     * @date 2021/4/13 9:34
     */
    public static String getPrefix(String fileName) {
        int idx = fileName.lastIndexOf(".");
        int xie = fileName.lastIndexOf("/");
        idx = idx == -1 ? fileName.length() : idx;
        xie = xie == -1 ? 0 : xie + 1;
        return fileName.substring(xie, idx);
    }

    /**
     * 获取网络文件流
     *
     * @param url     URL连接
     * @param referer referer验证，不加referer可能会403
     * @return {@link InputStream}
     * @method getInputStreamByUrl
     * @date 2021/4/13 9:51
     */
    public static InputStream getInputStreamByUrl(String url, String referer) {
        HttpGet httpGet = new HttpGet(detectUrl(url));
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
        if (Objects.nonNull(referer) && !"".equals(referer)) {
            httpGet.setHeader("referer", referer);
        }
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response;
        InputStream in;
        try {
            response = httpclient.execute(httpGet);
            in = response.getEntity().getContent();
            if (response.getStatusLine().getStatusCode() == 200) {
                return in;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检查URL完整性
     *
     * @param url URL连接
     * @return {@link String}
     * @method detectUrl
     * @date 2021/4/13 9:50
     */
    public static String detectUrl(String url) {
        if (Objects.isNull(url) || "".equals(url)) {
            if (url.startsWith("http://") || url.startsWith("https://")) {
                return url;
            }
            return url.startsWith("//") ? "https:" + url : "http://" + url;
        }
        return null;
    }

    /**
     * 检测有效链接
     *
     * @param urlLink            链接URL
     * @param timeOutMillSeconds 超时时间
     * @return {@link boolean}
     * @method detectValidLinks
     * @date 2021/5/6 10:26
     */
    public static boolean detectValidLinks(String urlLink, Integer timeOutMillSeconds) {
        final long lo = System.currentTimeMillis();
        URL url;
        try {
            url = new URL(urlLink);
            final HttpURLConnection connect = (HttpURLConnection) url.openConnection();
            if (Objects.nonNull(timeOutMillSeconds)) {
                connect.setConnectTimeout(timeOutMillSeconds);
            }
            connect.setRequestMethod("HEAD");
            String strMessage = connect.getResponseMessage();
            if (strMessage.compareTo("Not Found") == 0) {
                return false;
            }
            if (strMessage.compareTo("OK") == 0) {
                return true;
            }
            connect.disconnect();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据byte数组，生成文件
     *
     * @param bfile    byte字节流
     * @param filePath 文件存放目录
     * @param fileName 文件名称(不带后缀的)
     * @return {@link String}
     * @method getFile
     * @date 2021/4/13 16:02
     */
    public String getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file;
        String path;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && !dir.isDirectory()) {//判断文件目录是否存在
                if (dir.mkdirs()) {
                    System.out.println("判断文件目录,进行创建完成。");
                }
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
     * 读取字节输入流内容
     *
     * @param is 文件流
     * @return {@link byte[]}
     * @method readInputStream
     * @date 2021/4/13 16:32
     */
    private static byte[] readInputStream(InputStream is) {
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        byte[] buff = new byte[1024 * 2];
        int len;
        try {
            while ((len = is.read(buff)) != -1) {
                writer.write(buff, 0, len);
            }
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return writer.toByteArray();
    }

    /**
     * 根据文件大小，获取格式化文件描述
     *
     * @param size 文件大小
     * @return {@link String}
     * @method getFormattedSize
     * @date 2021/4/17 23:34
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

    /**
     * 获取指定目录创建临时文件
     *
     * @param directory  临时文件目录
     * @param mount      目录文件保留时间
     * @param chronoUnit 时间单位
     * @return {@link File}
     * @author Blare
     */
    public static File createTempFile(File directory, String suffix, long mount, ChronoUnit chronoUnit) throws IOException {
        mount = mount < 0 ? 0 : mount;
        final File tempFile = File.createTempFile("watermark", suffix, directory);
        final File temDir = tempFile.toPath().getParent().toFile(); // 临时文件目录
        long finalMount = mount;
        CompletableFuture.runAsync(() -> {
            final File[] files = temDir.listFiles(file -> {
                final BasicFileAttributeView fileAttributeView = Files.getFileAttributeView(file.toPath(),
                        BasicFileAttributeView.class,
                        LinkOption.NOFOLLOW_LINKS);
                try {
                    // 前一天的临时文件
                    return fileAttributeView.readAttributes().creationTime().toInstant().compareTo(Instant.now().minus(finalMount, ChronoUnit.DAYS)) > 0;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            });
            if (Objects.nonNull(files)) {
                Stream.of(files).parallel().forEach(f -> {
                    final boolean delete = f.delete();
                    // if (log.isDebugEnabled()) {
                    //     log.debug("临时文件：{} 删除结果：{}", f.getName(), delete);
                    // }
                });
            }
        });
        return tempFile;
    }

    /**
     * Base64图片转文件
     *
     * @param imageBase64Str Base64图片
     * @param suffix         文件后缀
     * @return {@link File}rows
     * @author Blare
     */
    public static File baseStrToImage(String imageBase64Str, String suffix) {
        try {
            final Path path = createTempFile(null, suffix, 1, ChronoUnit.DAYS).toPath();
            Files.write(path, Base64.getDecoder().decode(imageBase64Str), StandardOpenOption.CREATE);
            return path.toFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片文件转Base64字符串
     *
     * @param image 图片文件
     * @return {@link String}
     * @author Blare
     */
    public static String imageToBase64Str(File image) {
        try (final FileInputStream inputStream = new FileInputStream(image)) {
            final byte[] bytes = new byte[inputStream.available()];
            final int read = inputStream.read(bytes);
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 图片文件转Base64字符串
     *
     * @param image 图片文件
     * @return {@link String}
     * @author Blare
     */
    public static String imageToBase64(File image) {
        final byte[] bytes;
        try {
            bytes = Files.readAllBytes(image.toPath());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断图片base64字符串的文件格式
     *
     * @param imageBase64Str Base64图片
     * @return {@link String}
     * @author Blare
     */
    public static String getImageBase64Format(String imageBase64Str) {
        byte[] b = Base64.getDecoder().decode(imageBase64Str);
        String type = "";
        if (0x424D == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
            type = "bmp";
        } else if (0x8950 == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
            type = "png";
        } else if (0xFFD8 == ((b[0] & 0xff) << 8 | (b[1] & 0xff))) {
            type = "jpg";
        }
        return type;
    }
}