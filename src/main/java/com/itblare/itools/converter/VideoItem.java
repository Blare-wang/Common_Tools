package com.itblare.itools.converter;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.converter
 * ClassName:   VideoItem
 * Author:   Blare
 * Date:     Created in 2021/4/26 13:38
 * Description:    视频信息类
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/26 13:38    1.0.0         视频信息类
 */

/**
 * 视频信息类
 *
 * @author Blare
 * @create 2021/4/26 13:38
 * @since 1.0.0
 */
public class VideoItem {

    public VideoItem(long size, long length, int width, int height, String format) {
        this.size = size;
        this.length = length;
        this.width = width;
        this.height = height;
        this.format = format;
    }

    /**
     * 视频大小
     */
    private long size;

    /**
     * 视频时长
     */
    private long length;

    /**
     * 视频宽度
     */
    private int width;

    /**
     * 视频高度
     */
    private int height;

    /**
     * 视频格式
     */
    private String format;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
