package tool.image.pdf;

import sun.reflect.generics.tree.Tree;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

public class PdfUtil {

    private static String FILEPATH = ClassLoader.getSystemResource("").getPath();

    public static List<String> validImageType = Arrays.asList(".jpg", ".png", ".bmp", ".jpeg", ".gif");

    public static int CONCURRENT_PROCESS_THRESHOLD = 20;

    /**
     * 图片转PDF
     * @param pdfPath
     * @param imageDirPath
     */
    public static void imagesToPdf(String pdfPath, String imageDirPath) throws IOException {
        PdfDoc pdfDoc = new PdfDoc();
        // 文件夹
        File imageDirFile = new File(imageDirPath);
        if (!imageDirFile.isDirectory()) {
            throw new RuntimeException("请传入图片文件夹");
        }
        // PDF文件存不存在
        File pdfFile = new File(pdfPath);
        File pdfFileParent = pdfFile.getParentFile();
        if (!pdfFileParent.exists()) {
            pdfFileParent.mkdirs();
        }
        File[] files = imageDirFile.listFiles(f -> {
            String path = f.getAbsolutePath().toLowerCase();
            String type = path.substring(path.indexOf("."));
            return validImageType.contains(type);
        });
        // 少的话就不用多线程了
        if (files.length > CONCURRENT_PROCESS_THRESHOLD) {
            pdfDoc.imagesToPdf(pdfPath, imageDirPath);
        } else {
            // temp 存放临时pdf
            String tempPath = pdfFileParent.getAbsolutePath() + File.separator + "temp";
            File tempFile = new File(tempPath);
            if (tempFile.exists()) {
                tempFile.deleteOnExit();
            }
            tempFile.mkdirs();
        }

    }

}
