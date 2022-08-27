package com.itblare.itools.converter;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.itools.converter
 * ClassName:   VideoProcesser
 * Author:   Blare
 * Date:     Created in 2021/5/8 10:06
 * Description:    视频处理器
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/5/8 10:06    1.0.0         视频处理器
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.MultimediaInfo;
import ws.schild.jave.info.VideoInfo;
import ws.schild.jave.info.VideoSize;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 视频处理器
 *
 * @author Blare
 * @create 2021/5/8 10:06
 * @since 1.0.0
 */
public class VideoProcessor {

    public static Logger Log = LoggerFactory.getLogger(VideoProcessor.class);

    /**
     * 视频文件转音频文件
     *
     * @param videoPath
     * @param audioPath
     * @return
     */
    public static boolean videoToAudio(String videoPath, String audioPath) {
        File fileMp4 = new File(videoPath);
        File fileMp3 = new File(audioPath);

        //Audio Attributes
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(128000);
        audio.setChannels(2);
        audio.setSamplingRate(44100);

        //Encoding attributes
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp3");
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        MultimediaObject mediaObject = new MultimediaObject(fileMp4);
        try {
            encoder.encode(mediaObject, fileMp3, attrs);
            Log.info("File MP4 convertito in MP3");
            return true;
        } catch (Exception e) {
            Log.error("File non convertito");
            Log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 获取视频的基本信息，视频长宽高，视频的大小等
     *
     * @param fileSource
     * @return
     */
    public static VideoItem getVideoInfo(String fileSource) {
        // String filePath =
        // Utils.class.getClassLoader().getResource(fileSource).getPath();
        File source = new File(fileSource);
        //Encoder encoder = new Encoder();
        FileInputStream fis = null;
        FileChannel fc = null;
        VideoItem videoInfo = null;
        try {
            MultimediaObject MultimediaObject = new MultimediaObject(source);
            MultimediaInfo m = MultimediaObject.getInfo();
            fis = new FileInputStream(source);
            fc = fis.getChannel();
            videoInfo = new VideoItem(fc.size(),
                m.getDuration(),
                m.getVideo().getSize().getWidth(),
                m.getVideo().getSize().getHeight(),
                m.getFormat());
            System.out.println(videoInfo);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != fc) {
                try {
                    fc.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return videoInfo;
    }

    /**
     * 截取视频中某一帧作为图片
     *
     * @param videoPath
     * @param imagePath
     * @return
     */
    public static boolean getVideoProcessImage(String videoPath, String imagePath) {
        long times = System.currentTimeMillis();
        File videoSource = new File(videoPath);
        File imageTarget = new File(imagePath);
        MultimediaObject object = new MultimediaObject(videoSource);
        try {
            MultimediaInfo multimediaInfo = object.getInfo();
            VideoInfo videoInfo = multimediaInfo.getVideo();
            VideoAttributes video = new VideoAttributes();
            video.setCodec("png");
            video.setSize(videoInfo.getSize());
            EncodingAttributes attrs = new EncodingAttributes();
            //VideoAttributes attrs = ecodeAttrs.getVideoAttributes().get();
            attrs.setOutputFormat("image2");
            attrs.setOffset(11f);//设置偏移位置，即开始转码位置（11秒）
            attrs.setDuration(0.01f);//设置转码持续时间（1秒）
            attrs.setVideoAttributes(video);
            Encoder encoder = new Encoder();
            encoder.encode(object, imageTarget, attrs);
            return true;
        } catch (EncoderException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * m4r音频格式转换为mp3，audioPath可更换为要转换的音频格式
     *
     * @param audioPath
     * @param mp3Path
     */
    public static void m4rToMp3(String audioPath, String mp3Path) {
        File source = new File(audioPath);
        File target = new File(mp3Path);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(new Integer(128000));
        audio.setChannels(new Integer(2));
        audio.setSamplingRate(new Integer(44100));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp3");
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        try {
            encoder.encode(new MultimediaObject(source), target, attrs);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从和视频中提取音频wav
     *
     * @param aviPath
     * @param targetWavPath
     */
    public static void videoExtractAudio(String aviPath, String targetWavPath) {
        File source = new File(aviPath);
        File target = new File(targetWavPath);
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("pcm_s16le");
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("wav");
        attrs.setAudioAttributes(audio);
        Encoder encoder = new Encoder();
        try {
            encoder.encode(new MultimediaObject(source), target, attrs);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    /**
     * 视频转换为手机可播放的格式
     *
     * @param sourceVideo sourceVideo.avi
     * @param targetVideo targetVideo.3gp
     */
    public static void videoToMobileVideo(String sourceVideo, String targetVideo) {
        File source = new File("source.avi");
        File target = new File("target.3gp");
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libfaac");
        audio.setBitRate(new Integer(128000));
        audio.setSamplingRate(new Integer(44100));
        audio.setChannels(new Integer(2));
        VideoAttributes video = new VideoAttributes();
        video.setCodec("mpeg4");
        video.setBitRate(new Integer(160000));
        video.setFrameRate(new Integer(15));
        video.setSize(new VideoSize(176, 144));
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("3gp");
        attrs.setAudioAttributes(audio);
        attrs.setVideoAttributes(video);
        Encoder encoder = new Encoder();
        try {
            encoder.encode(new MultimediaObject(source), target, attrs);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //System.out.println("数据 = [" + getVideoProcessImage("E:/MyFile/mylove/video/dPQRVRFuZHJ8.mp4","E:/MyFile/sfaafasddxg.jpg") + "]");
        //System.out.println("数据 = [" + getVideoInfo("E:/MyFile/mylove/video/dPQRVRFuZHJ8.mp4") + "]");
        //System.out.println("数据 = [" + videoToAudio("E:/MyFile/mylove/video/dPQRVRFuZHJ8.mp4","E:/MyFile/sfaafasddxgd.mp3") + "]");
    }
}
