package com.itblare.itools.file.storage;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.tools.file.storage
 * ClassName:   QiniuStorage
 * Author:   Blare
 * Date:     Created in 2021/4/12 18:35
 * Description:    七牛云存储
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/12 18:35    1.0.0             七牛云存储
 */

import com.itblare.itools.exception.OssApiException;
import com.itblare.itools.file.FileProcessor;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 一句话功能简述：七牛云存储
 *
 * @author Blare
 * @create 2021/4/12 18:35
 * @since 1.0.0
 */
public class QiniuStorage extends BaseStorage {

    private UploadManager uploadManager;
    private BucketManager bucketManager;
    private String token;

    public QiniuStorage(StorageConfig config) {
        super(config);
        //初始化
        init();
    }

    private void init() {
        Auth auth = Auth.create(
            storageConfig.getQiniuConfig().getAccessKey(),
            storageConfig.getQiniuConfig().getSecretKey());
        //StringMap policy = new StringMap();
        //policy.put("returnBody", "{\"key\":${key},\"bucket\":${bucket},\"mimeType\":$(mimeType),\"duration\":$(avinfo.format.duration),\"name\":$(fname),\"size\":$(fsize),\"w\":$(imageInfo.width),\"h\":$(imageInfo.height),\"hash\":$(etag)}");
        uploadManager = new UploadManager(new Configuration(Region.autoRegion()));
        bucketManager = new BucketManager(auth, new Configuration(Region.autoRegion()));
        token = auth.uploadToken(storageConfig.getQiniuConfig().getBucketName());
    }

    @Override
    public String upload(byte[] data, String key) {
        try {
            Response res = uploadManager.put(data, key, token);
            if (!res.isOK()) {
                throw new RuntimeException("上传七牛出错：" + res.toString());
            }
        } catch (Exception e) {
            throw new OssApiException("上传文件失败，请核对七牛配置信息", e);
        }
        String domain = storageConfig.getQiniuConfig().getDomain();
        if (!domain.endsWith("/")) {
            domain += "/";
        }
        return domain + key;
    }

    @Override
    public String upload(InputStream inputStream, String key) {
        try {
            //final int length = IOUtils.toByteArray(inputStream).length;
            Response response = uploadManager.put(inputStream, inputStream.available(), key, token, null, null, true);
            if (!response.isOK()) {
                throw new OssApiException("上传七牛出错：" + response.toString());
            }
            return storageConfig.getQiniuConfig().getDomain() + key;
        } catch (IOException e) {
            throw new OssApiException("上传文件失败", e);
        }
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, FileProcessor.getPath(storageConfig.getQiniuConfig().getPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, FileProcessor.getPath(storageConfig.getQiniuConfig().getPrefix(), suffix));
    }

    @Override
    public boolean removeFile(String key) {
        if (Objects.isNull(key) || "".equals(key)) {
            throw new OssApiException("[七牛云存储]删除文件失败：文件key为空");
        }
        try {
            Response re = bucketManager.delete(storageConfig.getQiniuConfig().getBucketName(), key);
            return re.isOK();
        } catch (QiniuException e) {
            throw new OssApiException("[七牛云存储]删除文件发生异常：" + e.response.toString());
        }
    }

    @Override
    public InputStream downloadFile(String key) {
        return null;
    }

    @Override
    public String saveToCloudStorage(String imgUrl, String referer, String key) {
        return null;
    }

    @Override
    public String multipartUpload(File file, String key) {
        return null;
    }

    @Override
    public Map<String, String> getUploadToken() {
        StorageConfig.QiniuConfig qiniu = storageConfig.getQiniuConfig();
        final String accessKey = qiniu.getAccessKey();
        final String secretKey = qiniu.getSecretKey();
        final String bucketName = qiniu.getBucketName();
        long expires = qiniu.getExpires();
        if (expires < 3600) {
            expires = 43200L;
        }
        Auth auth = Auth.create(accessKey, secretKey);
        StringMap policy = new StringMap();
        policy.put("returnBody", "{\"key\":${key},\"bucket\":${bucket},\"mimeType\":$(mimeType),\"duration\":$(avinfo.format.duration),\"name\":$(fname),\"size\":$(fsize),\"w\":$(imageInfo.width),\"h\":$(imageInfo.height),\"hash\":$(etag)}");
        final String token = auth.uploadToken(bucketName, null, expires, policy, true);
        final HashMap<String, String> map = new HashMap<>();
        map.put("token", token);
        return map;
    }
}
