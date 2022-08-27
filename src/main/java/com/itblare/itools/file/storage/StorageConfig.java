package com.itblare.itools.file.storage;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.tools.file.storage
 * ClassName:   StorageConfig
 * Author:   Blare
 * Date:     Created in 2021/4/12 11:39
 * Description:    存储配置
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/12 11:39    1.0.0             存储配置
 */

/**
 * 存储配置
 *
 * @author Blare
 * @create 2021/4/12 11:39
 * @since 1.0.0
 */
public class StorageConfig {


    /**
     * 类型 1：七牛云  2：阿里云  3：腾讯云  4：本地
     */
    private Integer type;
    /**
     * 七牛云配置
     */
    private QiniuConfig qiniuConfig;

    /**
     * 腾讯云配置
     */
    private QcloudConfig qcloudConfig;

    /**
     * 阿里云配置
     */
    private AliyunConfig aliyunConfig;

    /**
     * 本地配置
     */
    private LocalConfig localConfig;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public QiniuConfig getQiniuConfig() {
        return qiniuConfig;
    }

    public void setQiniuConfig(QiniuConfig qiniuConfig) {
        this.qiniuConfig = qiniuConfig;
    }

    public QcloudConfig getQcloudConfig() {
        return qcloudConfig;
    }

    public void setQcloudConfig(QcloudConfig qcloudConfig) {
        this.qcloudConfig = qcloudConfig;
    }

    public AliyunConfig getAliyunConfig() {
        return aliyunConfig;
    }

    public void setAliyunConfig(AliyunConfig aliyunConfig) {
        this.aliyunConfig = aliyunConfig;
    }

    public LocalConfig getLocalConfig() {
        return localConfig;
    }

    public void setLocalConfig(LocalConfig localConfig) {
        this.localConfig = localConfig;
    }

    public QiniuConfig qiniuBuild() {
        return new QiniuConfig();
    }

    public QcloudConfig qcloudBuild() {
        return new QcloudConfig();
    }

    public AliyunConfig aliyunBuild() {
        return new AliyunConfig();
    }

    public LocalConfig localBuild() {
        return new LocalConfig();
    }

    public static class QiniuConfig {

        /**
         * 七牛云绑定的域名
         */
        private String domain;

        /**
         * 七牛云路径前缀
         */
        private String prefix;

        /**
         * 七牛云ACCESS_KEY
         */
        private String accessKey;

        /**
         * 七牛云SECRET_KEY
         */
        private String secretKey;

        /**
         * 七牛云存储空间名
         */
        private String bucketName;

        /**
         * 七牛云 STS Policy
         */
        private String stsPolicy;

        /**
         * 七牛云 过期试卷
         */
        private long expires;

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getAccessKey() {
            return accessKey;
        }

        public void setAccessKey(String accessKey) {
            this.accessKey = accessKey;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

        public String getStsPolicy() {
            return stsPolicy;
        }

        public void setStsPolicy(String stsPolicy) {
            this.stsPolicy = stsPolicy;
        }

        public long getExpires() {
            return expires;
        }

        public void setExpires(long expires) {
            this.expires = expires;
        }
    }

    public static class QcloudConfig {
        /**
         * 腾讯云绑定的域名
         */
        private String domain;

        /**
         * 腾讯云路径前缀
         */
        private String prefix;

        /**
         * 腾讯云AppId
         */
        private Integer appId;

        /**
         * 腾讯云SecretId
         */
        private String secretId;

        /**
         * 腾讯云SecretKey
         */
        private String secretKey;

        /**
         * 腾讯云BucketName
         */
        private String bucketName;

        /**
         * 腾讯云COS所属地区
         */
        private String region;

        /**
         * 腾讯云 STS SecretId
         */
        private String stsSecretId;

        /**
         * 腾讯云 STS SecretKey
         */
        private String stsSecretKey;

        /**
         * 腾讯云 STS Endpoint
         */
        private String stsEndpoint;

        /**
         * 腾讯云 STS Name
         */
        private String stsName;

        /**
         * 腾讯云 STS Policy
         */
        private String stsPolicy;

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public Integer getAppId() {
            return appId;
        }

        public void setAppId(Integer appId) {
            this.appId = appId;
        }

        public String getSecretId() {
            return secretId;
        }

        public void setSecretId(String secretId) {
            this.secretId = secretId;
        }

        public String getSecretKey() {
            return secretKey;
        }

        public void setSecretKey(String secretKey) {
            this.secretKey = secretKey;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getStsSecretId() {
            return stsSecretId;
        }

        public void setStsSecretId(String stsSecretId) {
            this.stsSecretId = stsSecretId;
        }

        public String getStsSecretKey() {
            return stsSecretKey;
        }

        public void setStsSecretKey(String stsSecretKey) {
            this.stsSecretKey = stsSecretKey;
        }

        public String getStsEndpoint() {
            return stsEndpoint;
        }

        public void setStsEndpoint(String stsEndpoint) {
            this.stsEndpoint = stsEndpoint;
        }

        public String getStsName() {
            return stsName;
        }

        public void setStsName(String stsName) {
            this.stsName = stsName;
        }

        public String getStsPolicy() {
            return stsPolicy;
        }

        public void setStsPolicy(String stsPolicy) {
            this.stsPolicy = stsPolicy;
        }
    }

    public static class AliyunConfig {
        /**
         * 阿里云绑定的域名
         */
        private String domain;

        /**
         * 阿里云路径前缀
         */
        private String prefix;

        /**
         * 阿里云EndPoint
         */
        private String endPoint;

        /**
         * 阿里云AccessKeyId
         */
        private String accessKeyId;

        /**
         * 阿里云AccessKeySecret
         */
        private String accessKeySecret;

        /**
         * 阿里云BucketName
         */
        private String bucketName;

        /**
         * 阿里云STS AccessKeyId
         */
        private String stsAccessKeyId;

        /**
         * 阿里云STS AccessKeySecret
         */
        private String stsAccessKeySecret;

        /**
         * 阿里云STS 角色ARN
         */
        private String stsRoleArn;

        /**
         * 腾讯云 STS Policy
         */
        private String stsPolicy;

        /**
         * 阿里云 STS RoleSessionName
         */
        private String stsRoleSessionName;

        /**
         * 阿里云STS接入地址
         */
        private String stsEndpoint;

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getEndPoint() {
            return endPoint;
        }

        public void setEndPoint(String endPoint) {
            this.endPoint = endPoint;
        }

        public String getAccessKeyId() {
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getAccessKeySecret() {
            return accessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

        public String getStsAccessKeyId() {
            return stsAccessKeyId;
        }

        public void setStsAccessKeyId(String stsAccessKeyId) {
            this.stsAccessKeyId = stsAccessKeyId;
        }

        public String getStsAccessKeySecret() {
            return stsAccessKeySecret;
        }

        public void setStsAccessKeySecret(String stsAccessKeySecret) {
            this.stsAccessKeySecret = stsAccessKeySecret;
        }

        public String getStsRoleArn() {
            return stsRoleArn;
        }

        public void setStsRoleArn(String stsRoleArn) {
            this.stsRoleArn = stsRoleArn;
        }

        public String getStsPolicy() {
            return stsPolicy;
        }

        public void setStsPolicy(String stsPolicy) {
            this.stsPolicy = stsPolicy;
        }

        public String getStsRoleSessionName() {
            return stsRoleSessionName;
        }

        public void setStsRoleSessionName(String stsRoleSessionName) {
            this.stsRoleSessionName = stsRoleSessionName;
        }

        public String getStsEndpoint() {
            return stsEndpoint;
        }

        public void setStsEndpoint(String stsEndpoint) {
            this.stsEndpoint = stsEndpoint;
        }
    }

    public static class LocalConfig {
        /**
         * 本地存储目录
         */
        private String localDirectory;
        /**
         * 本地路径前缀
         */
        private String localPrefix;
        /**
         * 本地目录映射的域名
         */
        private String localDomain;

        public String getLocalDirectory() {
            return localDirectory;
        }

        public void setLocalDirectory(String localDirectory) {
            this.localDirectory = localDirectory;
        }

        public String getLocalPrefix() {
            return localPrefix;
        }

        public void setLocalPrefix(String localPrefix) {
            this.localPrefix = localPrefix;
        }

        public String getLocalDomain() {
            return localDomain;
        }

        public void setLocalDomain(String localDomain) {
            this.localDomain = localDomain;
        }
    }

}
