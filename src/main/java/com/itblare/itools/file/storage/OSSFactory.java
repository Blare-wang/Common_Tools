package com.itblare.itools.file.storage;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.tools.file.storage
 * ClassName:   OSSFactory
 * Author:   Blare
 * Date:     Created in 2021/4/13 10:39
 * Description:    对象存储工厂
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/13 10:39    1.0.0             对象存储工厂
 */

import com.itblare.itools.exception.OssApiException;

import java.util.Objects;

/**
 * 对象存储工厂
 *
 * @author Blare
 * @create 2021/4/13 10:39
 * @since 1.0.0
 */
public class OSSFactory {

    public static BaseStorage build(StorageConfig config) {

        if (config.getType() == StorageTypeSource.QINIU.getValue()) {
            return new QiniuStorage(config);
        } else if (config.getType() == StorageTypeSource.ALIYUN.getValue()) {
            return new AliyunStorage(config);
        } else if (config.getType() == StorageTypeSource.QCLOUD.getValue()) {
            return new QcloudStorage(config);
        } else if (config.getType() == StorageTypeSource.LOCAL.getValue()) {
            if (Objects.nonNull(config.getLocalConfig())) {
                //未指定存储目录，默认存储于项目静态文件夹内
                String localDirectory = System.getProperty("user.dir") + "/src/main/resources/static";
                config.getLocalConfig().setLocalDirectory(localDirectory);
                config.getLocalConfig().setLocalDomain("");
            }
            return new LocalStorage(config);
        }
        throw new OssApiException("未配置存储类型");
    }
}
