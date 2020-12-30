package com.controller;

import com.tool.image.ImageUtil;
import com.tool.image.pdf.PdfUtil;
import com.tool.zip.ZipUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author Created by liangjiaming on 2020/12/29
 * @title
 * @Desc
 */
@RestController
@RequestMapping("/tool")
@Slf4j
public class ToolController {

    @PostMapping("/getImagePdf")
    public void getImagePdf(HttpServletResponse response, HttpServletRequest request) throws IOException {

        String pdfPath = request.getParameter("pdfPath");
        log.info("生成PDF路径：{}", pdfPath);
        String imageDirPath = request.getParameter("imageDirPath");
        log.info("原图片路径：{}", imageDirPath);

        // 开始处理
        PdfUtil.imagesToPdf(pdfPath, imageDirPath);
        File pdfFile = new File(pdfPath);
        if (!pdfFile.exists()) {
            log.warn("生成PDF文件失败");
            return;
        }

        response.setHeader("content-type", "application/pdf");
//        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + pdfPath);

        BufferedInputStream bin = null;
        InputStream in = null;
        OutputStream out = null;
        byte[] buff = new byte[1024*1024*10];
        try {
            in = new FileInputStream(pdfFile);
            bin = new BufferedInputStream(in);
            out = response.getOutputStream();
            int read = bin.read(buff);
            while (read != -1) {
                out.write(buff, 0, read);
                read = bin.read(buff);
            }
            out.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
            if (bin != null) {
                bin.close();
            }
            if (in != null) {
                in.close();
            }
        }

    }

    @PostMapping("/getZipFile")
    public void getZipFile(HttpServletRequest request, HttpServletResponse response) {
        String zipPath = request.getParameter("zipPath");
        log.info("生成ZIP路径：{}", zipPath);
        String srcDirPath = request.getParameter("srcDirPath");
        log.info("压缩源文件路径：{}", srcDirPath);

        // 压缩
        ZipUtil.zipFileDir(zipPath, srcDirPath);

        File zipFile = new File(zipPath);
        if (!zipFile.exists()) {
            return;
        }
        response.setHeader("content-type", "application/zip");
//        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + zipPath);
        try(OutputStream out = response.getOutputStream(); InputStream in = new FileInputStream(zipFile)) {
            byte[] buff = new byte[1024*1024*10];
            int read = in.read(buff);
            while (read != -1) {
                out.write(buff,0, read);
                read = in.read(buff);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/getCompressionImage")
    public void getCompressionImage(HttpServletRequest request, HttpServletResponse response) {
        String srcImagePath = request.getParameter("srcImagePath");
        response.setHeader("content-type", "image/jpeg");// application/x-png;image/jpeg;
//        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=" + srcImagePath);
        try(OutputStream out = response.getOutputStream()) {
            ImageUtil.compression(srcImagePath, out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
