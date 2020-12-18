package tool.image.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PdfUtil {

    private static String FILEPATH = ClassLoader.getSystemResource("").getPath();

    /**
     *
     * @param fileName
     *            生成pdf文件
     * @param imagesPath
     *            需要转换的图片路径的数组
     */
    public static void imagesToPdf(String fileName, String imagesPath) {
        try {
            System.out.println("FILEPATH: " + FILEPATH);
            fileName = FILEPATH + fileName+".pdf";
            File file = new File(fileName);
            // 第一步：创建一个document对象。
            Document document = new Document();
            document.setMargins(0, 0, 0, 0);
            // 第二步：
            // 创建一个PdfWriter实例，
            PdfWriter.getInstance(document, new FileOutputStream(file));
            // 第三步：打开文档。
            document.open();
            // 第四步：在文档中增加图片。
            File files = new File(FILEPATH + imagesPath);
            System.out.println("files: " + files.getAbsolutePath());
            String[] images = files.list();
            int len = images.length;

            for (int i = 0; i < len; i++)
            {
                if (images[i].toLowerCase().endsWith(".bmp")
                        || images[i].toLowerCase().endsWith(".jpg")
                        || images[i].toLowerCase().endsWith(".jpeg")
                        || images[i].toLowerCase().endsWith(".gif")
                        || images[i].toLowerCase().endsWith(".png")) {
                    String temp = FILEPATH + imagesPath + "/" + images[i];
                    Image img = Image.getInstance(temp);
                    img.setAlignment(Image.ALIGN_CENTER);
                    // 根据图片大小设置页面，一定要先设置页面，再newPage（），否则无效
                    document.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
                    document.newPage();
                    document.add(img);
                }
            }
            // 第五步：关闭文档。
            document.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 添加图片
     * @param imagePath
     */
    public static void addImage(String pdfPath, String imagePath) throws IOException, DocumentException {
        Document pdfDoc = new Document();
        // 将要生成的 pdf 文件的路径输出流
        FileOutputStream pdfFile = new FileOutputStream(new File(pdfPath));

        // 添加图片
        Image image = Image.getInstance(imagePath);

        // 用 Document 对象、File 对象获得 PdfWriter 输出流对象
        PdfWriter.getInstance(pdfDoc, pdfFile);
        pdfDoc.open(); // 打开 Document 文档

        // 一张图片
        pdfDoc.add(image);

        pdfDoc.close();
    }

    /**
     * 添加内容
     * @param content
     */
    public static void addContent(String content) {

    }

}
