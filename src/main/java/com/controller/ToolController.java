package com.controller;

import com.tool.image.pdf.PdfUtil;
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
        response.setContentType("application/octet-stream");
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

}
