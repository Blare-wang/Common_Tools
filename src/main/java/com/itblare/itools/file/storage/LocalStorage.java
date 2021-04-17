package com.itblare.itools.file.storage;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.tools.file.storage
 * ClassName:   LocalStorage
 * Author:   Blare
 * Date:     Created in 2021/4/13 10:43
 * Description:    本地存储
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/13 10:43    1.0.0             本地存储
 */

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * 一句话功能简述：本地存储
 *
 * @author Blare
 * @create 2021/4/13 10:43
 * @since 1.0.0
 */
public class LocalStorage extends BaseStorage {

    public LocalStorage(StorageConfig storageConfig) {
        super(storageConfig);
    }

    @Override
    public String upload(byte[] data, String key) {
        return null;
    }

    @Override
    public String uploadSuffix(byte[] data, String suffix) {
        return null;
    }

    @Override
    public String upload(InputStream inputStream, String key) {
        return null;
    }

    @Override
    public String uploadSuffix(InputStream inputStream, String suffix) {
        return null;
    }

    @Override
    public boolean removeFile(String key) {
        return false;
    }

    @Override
    public InputStream downloadFile(String key) {
        return null;
    }

    @Override
    public String multipartUpload(File file, String key) {
        return null;
    }

    @Override
    public Map<String, String> getUploadToken() {
        return null;
    }
}
