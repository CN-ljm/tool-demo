package tool.image.pdf;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;

public class PdfUtil {

    private static String FILEPATH = ClassLoader.getSystemResource("").getPath();

    public static List<String> validImageType = Arrays.asList(".jpg", ".png", ".bmp", ".jpeg", ".gif");

    // 并发处理图片数阈值
    public static int CONCURRENT_PROCESS_THRESHOLD = 20;

    // 并发线程数
    public static int CONCURRENT_THREADS = 4;

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
        int images = files.length;
        List<String> imagePaths = Arrays.asList(imageDirFile.listFiles()).stream().map(one -> one.getAbsolutePath()).collect(Collectors.toList());
        if (images > CONCURRENT_THREADS && images > CONCURRENT_PROCESS_THRESHOLD) {
            pdfDoc.imagesToPdf(pdfPath, imagePaths);
        } else {
            // temp 存放临时pdf
            String tempPath = pdfFileParent.getAbsolutePath() + File.separator + "temp";
            File tempFile = new File(tempPath);
            if (tempFile.exists()) {
                tempFile.deleteOnExit();
            }
            tempFile.mkdirs();

            // 创建内存墙
            CyclicBarrier cyclicBarrier = new CyclicBarrier(CONCURRENT_THREADS, () -> pdfDoc.mergePdf(pdfPath, tempPath));

            // 计算每个线程分配任务数
            int taskImageCount = images/(CONCURRENT_THREADS - 1);
            for (int i=0; i<CONCURRENT_THREADS; i++) {
                int from = i * taskImageCount;
                int to = ((i + 1) * taskImageCount) <= images ? (i + 1) * taskImageCount : images;
                List<String> taskImageList = imagePaths.subList(from, to);
                String pdfTempPath = tempPath + File.separator + i + ".pdf";
                Runnable run = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            pdfDoc.imagesToPdf(pdfTempPath, taskImageList);
                            cyclicBarrier.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (BrokenBarrierException e) {
                            e.printStackTrace();
                        }
                    }
                };
                Thread task = new Thread(run);
                task.start();
            }

        }

    }

}
