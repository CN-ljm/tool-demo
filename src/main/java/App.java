import tool.image.pdf.PdfDoc;

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
//        doc.imagesToPdf(pdfPath3, imageDir);
        String[] srcPath = new String[]{pdfPath,pdfPath2,pdfPath3};
        doc.mergePdf(mergePath, srcPath);

//        PdfUtil.imagesToPdf("material/pdf/pdf00001", "material/image");

    }
}
