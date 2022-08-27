package com.itblare.itools.file.storage;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.tools.file.storage
 * ClassName:   AliyunStorage
 * Author:   Blare
 * Date:     Created in 2021/4/12 18:18
 * Description:    阿里云存储
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/12 18:18    1.0.0             阿里云存储
 */

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.auth.sts.AssumeRoleRequest;
import com.aliyuncs.auth.sts.AssumeRoleResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.utils.StringUtils;
import com.itblare.itools.exception.OssApiException;
import com.itblare.itools.file.FileProcessor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * 阿里云存储
 *
 * @author Blare
 * @create 2021/4/12 18:18
 * @since 1.0.0
 */
public class AliyunStorage extends BaseStorage {

    private static OSSClient client;

    public AliyunStorage(StorageConfig storageConfig) {
        super(storageConfig);
        init();
    }

    private void init() {
        client = (OSSClient) new OSSClientBuilder().build(
            storageConfig.getAliyunConfig().getEndPoint(),
            storageConfig.getAliyunConfig().getAccessKeyId(),
            storageConfig.getAliyunConfig().getAccessKeySecret()
        );
    }

    @Override
    public String upload(byte[] data, String key) {
        return upload(new ByteArrayInputStream(data), key);
    }

    @Override
    public String upload(InputStream inputStream, String key) {
        String bucketName = storageConfig.getAliyunConfig().getBucketName();
        // 文件是否已存在
        boolean exists = client.doesObjectExist(bucketName, key);
        if (exists) {
            final String suffix = FileProcessor.getSuffix(key);
            final String prefix = FileProcessor.getPrefix(key);
            key = FileProcessor.getPath(prefix, suffix);
        }
        try {
            client.putObject(bucketName, key, inputStream);
        } catch (Exception e) {
            throw new OssApiException("[阿里云存储]上传文件失败，请检查配置信息", e);
        }
        String domain = storageConfig.getAliyunConfig().getDomain();
        if (!domain.endsWith("/")) {
            domain += "/";
        }
        return domain + key;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return upload(data, FileProcessor.getPath(storageConfig.getAliyunConfig().getPrefix(), suffix));
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return upload(inputStream, FileProcessor.getPath(storageConfig.getAliyunConfig().getPrefix(), suffix));
    }

    @Override
    public boolean removeFile(String key) {
        String bucketName = storageConfig.getAliyunConfig().getBucketName();
        try {
            if (Objects.isNull(key) || "".equals(key)) {
                throw new OssApiException("[阿里云存储]删除文件失败：文件key为空");
            }
            if (!client.doesBucketExist(bucketName)) {
                throw new OssApiException("[阿里云存储] 文件删除失败！文件不存在：" + bucketName + "/" + key);
            }
            client.deleteObject(bucketName, key);
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            client.shutdown();
        }
    }

    @Override
    public InputStream downloadFile(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new OssApiException("[阿里云存储]下载文件失败：文件key为空");
        }
        String bucketName = storageConfig.getAliyunConfig().getBucketName();
        try {
            boolean exists = client.doesBucketExist(bucketName);
            if (!exists) {
                throw new OssApiException("[阿里云存储] 文件删除失败！Bucket不存在：" + bucketName);
            }
            if (!client.doesObjectExist(bucketName, key)) {
                throw new OssApiException("[阿里云存储] 文件下载失败！文件不存在：" + bucketName + "/" + key);
            }
            OSSObject object = client.getObject(bucketName, key);
            return object.getObjectContent();
        } finally {
            client.shutdown();
        }
    }

    @Override
    public String multipartUpload(File file, String key) {
        String bucketName = storageConfig.getAliyunConfig().getBucketName();
        if (!client.doesBucketExist(bucketName)) {
            throw new OssApiException("[阿里云OSS] 无法上传文件！Bucket不存在：" + bucketName);
        }
        boolean exists = client.doesObjectExist(bucketName, key);
        String suffix = FileProcessor.getSuffix(key);
        String realSuffix = FileProcessor.getSuffix(file.getName());
        final boolean contains = realSuffix.equals(suffix);
        if (exists || !contains) {
            String prefix = FileProcessor.getPrefix(key);
            if (!contains) {
                key = FileProcessor.getPath(prefix, realSuffix);
            } else {
                key = FileProcessor.getPath(prefix, suffix);
            }
        }
        // 创建InitiateMultipartUploadRequest对象。
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, key);
        // 初始化分片。
        InitiateMultipartUploadResult upresult = client.initiateMultipartUpload(request);
        // 返回uploadId，它是分片上传事件的唯一标识，您可以根据这个uploadId发起相关的操作，如取消分片上传、查询分片上传等。
        String uploadId = upresult.getUploadId();
        // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
        List<PartETag> partETags = new ArrayList<>();
        // 计算文件有多少个分片。
        final long partSize = 5 * 1024 * 1024L;
        long fileLength = file.length();
        int partCount = (int) (fileLength / partSize);
        if (fileLength % partSize != 0) {
            partCount++;
        }
        progressListener.start(key);
        // 遍历分片上传。
        for (int i = 0; i < partCount; i++) {
            long startPos = i * partSize;
            long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
            try (InputStream instream = new FileInputStream(file)) {
                // 跳过已经上传的分片。
                //noinspection ResultOfMethodCallIgnored
                instream.skip(startPos);
                UploadPartRequest uploadPartRequest = new UploadPartRequest();
                uploadPartRequest.setBucketName(bucketName);
                uploadPartRequest.setKey(key);
                uploadPartRequest.setUploadId(uploadId);
                uploadPartRequest.setInputStream(instream);
                // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100 KB。
                uploadPartRequest.setPartSize(curPartSize);
                // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围，OSS将返回InvalidArgument的错误码。
                uploadPartRequest.setPartNumber(i + 1);
                // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
                UploadPartResult uploadPartResult = client.uploadPart(uploadPartRequest);
                // 每次上传分片之后，OSS的返回结果包含PartETag。PartETag将被保存在partETags中。
                partETags.add(uploadPartResult.getPartETag());
                progressListener.process(i, partCount);
            } catch (Exception e) {
                throw new OssApiException("[阿里云存储]文件分片上传失败：" + e.getMessage());
            }
        }
        // 创建CompleteMultipartUploadRequest对象。
        // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
            new CompleteMultipartUploadRequest(bucketName, key, uploadId, partETags);
        // 如果需要在完成文件上传的同时设置文件访问权限，请参考以下示例代码。
        // completeMultipartUploadRequest.setObjectACL(CannedAccessControlList.PublicRead);
        CompleteMultipartUploadResult complete = client.completeMultipartUpload(completeMultipartUploadRequest);
        // 关闭OSSClient。
        client.shutdown();
        String url = storageConfig.getAliyunConfig().getDomain() + complete.getKey();
        progressListener.end(url);
        return url;
    }

    @SuppressWarnings("unused")
    public String multipartUpload_(File file, String key) {
        String bucketName = storageConfig.getAliyunConfig().getBucketName();
        String realSuffix = FileProcessor.getSuffix(file.getName());
        String suffix = FileProcessor.getSuffix(key);
        final boolean contains = realSuffix.equals(suffix);
        boolean exists = client.doesObjectExist(bucketName, key);
        if (exists || !contains) {
            String prefix = FileProcessor.getPrefix(key);
            if (!contains) {
                key = FileProcessor.getPath(prefix, realSuffix);
            } else {
                key = FileProcessor.getPath(prefix, suffix);
            }
        }
        // 计算文件有多少个分片。
        final long partSize = 5 * 1024 * 1024L;
        long fileLength = file.length();
        int partCount = (int) (fileLength / partSize);
        if (fileLength % partSize != 0) {
            partCount++;
        }
        String tempPath = "";
        final UploadFileRequest uploadFileRequest = new UploadFileRequest(bucketName, key, tempPath, partSize, partCount, true);
        try {
            client.uploadFile(uploadFileRequest);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            client.shutdown();
        }
        String domain = storageConfig.getAliyunConfig().getDomain();
        if (!domain.endsWith("/")) {
            domain += "/";
        }
        return domain + key;
    }

    @Override
    public Map<String, String> getUploadToken() {
        StorageConfig.AliyunConfig aliyun = storageConfig.getAliyunConfig();
        String policy = aliyun.getStsPolicy();
        if (Objects.isNull(policy) || "".equals(policy)) {
            policy = "{\n" +
                "    \"Version\": \"1\", \n" +
                "    \"Statement\": [\n" +
                "        {\n" +
                "            \"Action\": [\n" +
                "                \"oss:*\"\n" +
                "            ], \n" +
                "            \"Resource\": [\n" +
                "                \"acs:oss:*:*:*\" \n" +
                "            ], \n" +
                "            \"Effect\": \"Allow\"\n" +
                "        }\n" +
                "    ]\n" +
                "}";
        }
        String endpoint = aliyun.getStsEndpoint();
        String AccessKeyId = aliyun.getStsAccessKeyId();
        String accessKeySecret = aliyun.getStsAccessKeySecret();
        String roleArn = aliyun.getStsRoleArn();
        String roleSessionName = aliyun.getStsRoleSessionName();

        try {
            // 添加endpoint（直接使用STS endpoint，前两个参数留空，无需添加region ID）
            DefaultProfile.addEndpoint("", "Sts", endpoint);
            // 构造default profile（参数留空，无需添加region ID）
            IClientProfile profile = DefaultProfile.getProfile("", AccessKeyId, accessKeySecret);
            // 用profile构造client
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setSysMethod(MethodType.POST);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy); // 若policy为空，则用户将获得该角色下所有权限
            request.setDurationSeconds(1000L); // 设置凭证有效时间
            AssumeRoleResponse response = null;
            try {
                response = client.getAcsResponse(request);
            } catch (ClientException e) {
                e.printStackTrace();
            }
            final HashMap<String, String> map = new HashMap<>(4);
            if (Objects.isNull(response)) {
                return map;
            }
            map.put("expiration", response.getCredentials().getExpiration());
            map.put("id", response.getCredentials().getAccessKeyId());
            map.put("secret", response.getCredentials().getAccessKeySecret());
            map.put("token", response.getCredentials().getSecurityToken());
            map.put("requestId", response.getRequestId());
            return map;
        } catch (Exception e) {
            throw new OssApiException("[阿里云存储]token获取失败", e);
        }
    }
}
