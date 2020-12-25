import tool.file.FileUtil;
import tool.image.pdf.PdfDoc;

import java.io.File;

public class App {

    private static String basePath = "/Users/jiamingl/Desktop/material/";



    public static void main(String[] args) throws Exception {
        System.out.println("hello tools");
        String pdfTemp = basePath + "temp/temp.pdf";
        String pdfPath = basePath + "temp/demo.pdf";
        String pdfPath2 = basePath + "temp/demo2.pdf";
        String pdfPath3 = basePath + "temp/demo3.pdf";
        String imageDir = basePath + "image/";
        String mergePath = basePath + "temp/demoMerge.pdf";

        PdfDoc doc = new PdfDoc();

//        doc.createPdfFile(pdfPath);
//        doc.andImagePage(pdfPath, pdfTemp, imageDir);
        /*long startPdf = System.currentTimeMillis();
        doc.imagesToPdf("E:\\material\\temp\\pdf1000.pdf ", "E:\\material\\image1000");
        System.out.println(System.currentTimeMillis() - startPdf);*/
//        String[] srcPath = new String[]{pdfPath,pdfPath2,pdfPath3};
//        doc.mergePdf(mergePath, srcPath);

//        PdfUtil.imagesToPdf("material/pdf/pdf00001", "material/image");

        /*String basePath = "E:/material/image1000/";
        byte[] bytes = FileUtil.readFileWithNIO("E:/material/image100/image0001.jpg");
        long start = System.currentTimeMillis();
        for (int i=1; i<=1000;i++) {
            String num = String.format("%04d", i);
            FileUtil.writeFileWithNIO(bytes, basePath + "image" + num + ".jpg");
        }
        System.out.println(System.currentTimeMillis() - start);*/

        String aa = "E:\\material\\aa\\bb\\11.pdf";
        File file = new File(aa);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        String temp = parentFile.getAbsolutePath() + File.separator + "temp";
        File tempFile = new File(temp);
        if (!tempFile.exists()) {
            tempFile.mkdirs();
        }


    }
}
