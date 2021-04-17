package com.itblare.itools.file.storage;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.tools.file.storage
 * ClassName:   StorageType
 * Author:   Blare
 * Date:     Created in 2021/4/13 10:44
 * Description:    存储类型
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/13 10:44    1.0.0             存储类型
 */

/**
 * 一句话功能简述：存储类型
 *
 * @author Blare
 * @create 2021/4/13 10:44
 * @since 1.0.0
 */
public enum StorageTypeSource {

    /**
     * 七牛云
     */
    QINIU(1),

    /**
     * 阿里云
     */
    ALIYUN(2),

    /**
     * 腾讯云
     */
    QCLOUD(3),

    /**
     * 本地
     */
    LOCAL(4);

    private int value;

    StorageTypeSource(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
