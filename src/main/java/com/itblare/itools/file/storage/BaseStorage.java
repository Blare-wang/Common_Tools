package com.itblare.itools.file.storage;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.tools.file.storage
 * ClassName:   BaseStorage
 * Author:   Blare
 * Date:     Created in 2021/4/12 11:38
 * Description:    数据存储
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/12 11:38    1.0.0             数据存储
 */

import com.aliyuncs.utils.StringUtils;
import com.itblare.itools.exception.OssApiException;
import com.itblare.itools.file.FileProcessor;
import com.itblare.itools.listener.ProgressListener;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * 一句话功能简述：数据存储(支持本地、自定义存储、七牛云、阿里云、腾讯云
 *
 * @author Blare
 * @create 2021/4/12 11:38
 * @since 1.0.0
 */
public abstract class BaseStorage {

    StorageConfig storageConfig;
    public ProgressListener progressListener = newListener();

    public BaseStorage(StorageConfig storageConfig) {
        this.storageConfig = storageConfig;
    }

    protected ProgressListener newListener() {
        return new ProgressListener() {
            @Override
            public void start(String msg) {
            }

            @Override
            public void process(int finished, int sum) {
            }

            @Override
            public void end(String result) {
            }
        };
    }

    /**
     * 功能描述: 文件上传
     *
     * @param data 文件字节数组
     * @param key  文件路径，包含文件名
     * @return {@link String} 返回http地址
     * @method upload
     * @author Blare
     * @date 2021/4/12 14:55
     * @updator Blare
     */
    public abstract String upload(byte[] data, String key);

    /**
     * 功能描述: 文件上传
     *
     * @param data   文件字节数组
     * @param suffix 后缀
     * @return {@link String} 返回http地址
     * @method uploadSuffix
     * @author Blare
     * @date 2021/4/12 14:56
     * @updator Blare
     */
    public abstract String uploadSuffix(byte[] data, String suffix);

    /**
     * 功能描述: 文件上传
     *
     * @param inputStream 字节流
     * @param key         文件路径，包含文件名
     * @return {@link String} 返回http地址
     * @method upload
     * @author Blare
     * @date 2021/4/12 18:14
     * @updator Blare
     */
    public abstract String upload(InputStream inputStream, String key);

    /**
     * 功能描述: 文件上传
     *
     * @param inputStream 字节流
     * @param suffix      后缀
     * @return {@link String} 返回http地址
     * @method uploadSuffix
     * @author Blare
     * @date 2021/4/12 18:14
     * @updator Blare
     */
    public abstract String uploadSuffix(InputStream inputStream, String suffix);

    /**
     * 功能描述: 删除文件
     *
     * @param key 路径（包含名称）
     * @return {@link boolean}
     * @method removeFile
     * @author Blare
     * @date 2021/4/13 10:57
     * @updator Blare
     */
    public abstract boolean removeFile(String key);

    /**
     * 功能描述:
     *
     * @param key 路径（包含名称）
     * @return {@link InputStream}
     * @method downloadFile
     * @author Blare
     * @date 2021/4/13 10:57
     * @updator Blare
     */
    public abstract InputStream downloadFile(String key);

    /**
     * 功能描述: 图片文件URL转存
     *
     * @param imgUrl  图片连接
     * @param referer 为了预防某些网站做了权限验证，不加referer可能会403
     * @param key     路径（包含名称）
     * @return {@link String}
     * @method saveToCloudStorage
     * @author Blare
     * @date 2021/4/13 10:59
     * @updator Blare
     */
    public String saveToCloudStorage(String imgUrl, String referer, String key) {
        try (InputStream is = FileProcessor.getInputStreamByUrl(imgUrl, referer)) {
            if (StringUtils.isEmpty(key)) {
                key = imgUrl;
            }
            return this.upload(is, key);
        } catch (Exception e) {
            throw new OssApiException(e.getMessage());
        }
    }

    /**
     * 功能描述: 分片上传文件
     *
     * @param file 待上传文件
     * @param key  路径（包含名称）
     * @return {@link String}
     * @method multipartUpload
     * @author Blare
     * @date 2021/4/13 11:00
     * @updator Blare
     */
    public abstract String multipartUpload(File file, String key);

    /**
     * 功能描述: 获取上传token
     *
     * @method getUploadToken
     * @return {@link Map}
     * @author Blare
     * @date 2021/4/13 15:12
     * @updator Blare
     */
    public abstract Map<String, String> getUploadToken();
}
