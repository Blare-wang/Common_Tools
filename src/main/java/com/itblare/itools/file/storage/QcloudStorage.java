package com.itblare.itools.file.storage;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.tools.file.storage
 * ClassName:   QcloudStorage
 * Author:   Blare
 * Date:     Created in 2021/4/12 18:42
 * Description:    腾讯云存储
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/12 18:42    1.0.0             腾讯云存储
 */

import com.itblare.itools.exception.OssApiException;
import com.itblare.itools.file.FileProcessor;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sts.v20180813.StsClient;
import com.tencentcloudapi.sts.v20180813.models.GetFederationTokenRequest;
import com.tencentcloudapi.sts.v20180813.models.GetFederationTokenResponse;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 一句话功能简述：腾讯云存储
 *
 * @author Blare
 * @create 2021/4/12 18:42
 * @since 1.0.0
 */
public class QcloudStorage extends BaseStorage {

    private COSClient client;

    public QcloudStorage(StorageConfig config) {
        super(config);
        //初始化
        init();
    }

    private void init() {
        COSCredentials credentials = new BasicCOSCredentials(
            //storageConfig.getQcloudConfig().getAppId(),
            storageConfig.getQcloudConfig().getSecretId(),
            storageConfig.getQcloudConfig().getSecretKey());

        //初始化客户端配置
        ClientConfig clientConfig = new ClientConfig();
        //设置bucket所在的区域，华南：gz 华北：tj 华东：sh
        clientConfig.setRegion(new Region(storageConfig.getQcloudConfig().getRegion()));

        client = new COSClient(credentials, clientConfig);
    }

    @Override
    public String upload(byte[] data, String key) {

        //腾讯云必需要以"/"开头
        if (!key.startsWith("/")) {
            key = "/" + key;
        }
        return this.upload(new ByteArrayInputStream(data), key);
    }

    /**
     * 功能描述:  path必须是文件全路径+文件名
     */
    @Override
    public String upload(InputStream inputStream, String key) {

        PutObjectRequest putObjectRequest = new PutObjectRequest(storageConfig.getQcloudConfig().getBucketName(), key, inputStream, null);
        putObjectRequest.setStorageClass(StorageClass.Standard);
        PutObjectResult putObjectResult = client.putObject(putObjectRequest);
        // putobjectResult会返回文件的etag
        if (Objects.isNull(putObjectResult.getETag()) || "".equals(putObjectResult.getETag())) {
            throw new OssApiException("[腾讯云存储]上传失败");
        }
        String domain = storageConfig.getQcloudConfig().getDomain();
        if (domain.endsWith("/")) {
            domain = domain.substring(0, domain.length() - 1);
        }
        return domain + key;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, FileProcessor.getPath(storageConfig.getQcloudConfig().getPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, FileProcessor.getPath(storageConfig.getQcloudConfig().getPrefix(), suffix));
    }

    @Override
    public boolean removeFile(String key) {
        if (Objects.isNull(key) || "".equals(key)) {
            throw new OssApiException("[腾讯云存储]删除文件失败：文件key为空");
        }
        final String bucketName = storageConfig.getQcloudConfig().getBucketName();
        try {
            // 删除文件
            client.deleteObject(bucketName, key);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public InputStream downloadFile(String key) {
        final String bucketName = storageConfig.getQcloudConfig().getBucketName();
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, key);
        COSObject object = client.getObject(getObjectRequest);
        return object.getObjectContent();
    }

    @Override
    public String multipartUpload(File file, String key) {
        final StorageConfig.QcloudConfig qcloud = storageConfig.getQcloudConfig();
        final String bucketName = qcloud.getBucketName();
        String domain = qcloud.getDomain();

        // 初始化分块上传的请求，调用COS InitiateMultipartUploadRequest 方法
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, key);
        // 设置存储类型：标准存储（Standard）， 低频存储存储（Standard_IA），归档存储（ARCHIVE）。默认是标准（Standard）
        request.setStorageClass(StorageClass.Standard);
        String uploadId;
        try {
            InitiateMultipartUploadResult initResult = client.initiateMultipartUpload(request);
            // 获取uploadid
            uploadId = initResult.getUploadId();
        } catch (CosServiceException e) {
            throw new RuntimeException("分块上传失败");
        }

        // 计算文件有多少个分片。
        final long partSize = 5 * 1024 * 1024L;
        long fileLength = file.length();
        int partCount = (int) (fileLength / partSize);
        if (fileLength % partSize != 0) {
            partCount++;
        }
        // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
        final ArrayList<PartETag> partETags = new ArrayList<>();
        progressListener.start(key);
        for (int i = 0; i < partCount; i++) {
            boolean isLastPart = false;
            if (i == partCount - 1) {
                isLastPart = true;
            }
            long startPos = i * partSize;
            long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
            // 分块上传
            //final PartETag partETag = batchUpload(uploadId, strings.get(i), i + 1, key, bucketName, isLastPart, client);
            try (InputStream instream = new FileInputStream(file)) {
                // 跳过已经上传的分片。
                //noinspection ResultOfMethodCallIgnored
                instream.skip(startPos);
                UploadPartRequest uploadPartRequest = new UploadPartRequest()
                    .withBucketName(bucketName)
                    .withKey(key)
                    .withUploadId(uploadId)
                    .withPartNumber(i + 1)
                    .withInputStream(instream)
                    .withPartSize(curPartSize)// 设置数据长度
                    .withLastPart(isLastPart);
                UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);
                PartETag partETag = uploadPartResult.getPartETag();
                partETags.add(partETag);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 完成分片上传
        CompleteMultipartUploadRequest compRequest = new CompleteMultipartUploadRequest(bucketName, key, uploadId, partETags);
        client.completeMultipartUpload(compRequest);
        client.shutdown();

        if (domain.endsWith("/")) {
            domain = domain.substring(0, domain.length() - 1);
        }
        String url = domain + key;
        progressListener.end(url);
        return url;
    }

    @Override
    public Map<String, String> getUploadToken() {
        final StorageConfig.QcloudConfig qcloud = storageConfig.getQcloudConfig();
        String policy = qcloud.getStsPolicy();
        if (Objects.isNull(policy) || "".equals(policy)) {
            policy = "{\n" +
                "    \"version\":\"2.0\",\n" +
                "    \"statement\":[\n" +
                "        {\n" +
                "            \"effect\":\"allow\",\n" +
                "            \"action\":[\n" +
                "                \"cos:*\"\n" +
                "            ],\n" +
                "            \"resource\":[\n" +
                "                \"*\"\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        }
        final String secretId = qcloud.getStsSecretId();
        final String secretKey = qcloud.getStsSecretKey();
        final String endpoint = qcloud.getStsEndpoint();
        final String region = qcloud.getRegion();
        final String name = qcloud.getStsName();
        try {

            Credential cred = new Credential(secretId, secretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint(endpoint);
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            StsClient client = new StsClient(cred, region, clientProfile);
            GetFederationTokenRequest req = new GetFederationTokenRequest();
            req.setName(name);
            req.setPolicy(policy);
            GetFederationTokenResponse response = client.GetFederationToken(req);
            final HashMap<String, String> map = new HashMap<>(4);
            if (Objects.isNull(response)) {
                return map;
            }
            map.put("expiration", response.getExpiration());
            map.put("id", response.getCredentials().getTmpSecretId());
            map.put("secret", response.getCredentials().getTmpSecretKey());
            map.put("token", response.getCredentials().getToken());
            map.put("requestId", response.getRequestId());
            return map;
        } catch (TencentCloudSDKException e) {
            throw new OssApiException("[腾讯云存储]token获取失败", e);
        }
    }
}
