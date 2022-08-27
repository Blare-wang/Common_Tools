package com.itblare.itools.file.storage.listener;
/*
 * Copyright (C), 2020-2021, 逝者如斯夫，不舍昼夜
 * PackageName: com.itblare.tools.listener
 * ClassName:   UploadProgressListener
 * Author:   Blare
 * Date:     Created in 2021/4/13 10:04
 * Description:    上传进度监听
 * History:
 * <author>        <time>             <version>          <desc>
 * 作者姓名         修改时间             版本号              描述
 * Blare           2021/4/13 10:04    1.0.0             上传进度监听
 */

import com.itblare.itools.listener.ProgressListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * 上传进度监听
 *
 * @author Blare
 * @create 2021/4/13 10:04
 * @since 1.0.0
 */
public class UploadProgressListener implements ProgressListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(UploadProgressListener.class);

    @Override
    public void start(String msg) {
        LOGGER.info("开始处理任务,文件名为 {}", msg);
    }

    @Override
    public void process(int finished, int sum) {
        float percent = new BigDecimal(finished)
            .divide(new BigDecimal(sum), 4, BigDecimal.ROUND_HALF_UP)
            .multiply(new BigDecimal(100))
            .setScale(2, BigDecimal.ROUND_HALF_UP)
            .floatValue();
        //BigDecimal bigDecimal = new BigDecimal(downloadBytes.toString());
        //final String speed = StringUtils.convertToDownloadSpeed(new BigDecimal(downloadBytes.toString()).subtract(bigDecimal), 2) + "/s";
        LOGGER.info("已下载" + finished + "个\t一共" + sum + "个\t已完成" + percent + "%"+percent + "% (" + finished + "/" + sum + ")");
        //""+"下载速度为"+speed);
    }

    @Override
    public void end(String result) {
        LOGGER.info("任务处理完成,完成的地址为{}", result);
    }
}
