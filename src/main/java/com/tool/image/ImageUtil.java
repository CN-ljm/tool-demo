package com.tool.image;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Created by liangjiaming on 2020/12/30
 * @title 图片工具
 * @Desc
 */
@Slf4j
public class ImageUtil {

    /**
     * 生成缩略图
     * @param srcImagePath 原图路径
     */
    public static void compression(String srcImagePath, OutputStream outputStream) throws IOException {
        File file = new File(srcImagePath);
        if (!file.exists()) {
            log.warn("给定路径图片不存在");
            return;
        }
        Thumbnails.of(srcImagePath).scale(0.1).toOutputStream(outputStream);
    }

}
