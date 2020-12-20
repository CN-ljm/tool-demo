package tool.image.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * pdf文档基础类
 */
public class PdfDoc {

    /**
     * 创建一个pdf文件
     * @param pdfPath
     * @return
     */
    public void createPdfFile(String pdfPath) {

//        //页面大小
//        Rectangle rect = new Rectangle(PageSize.A7.rotate());
//        //页面背景色
//        rect.setBackgroundColor(BaseColor.GRAY);

//        FileOutputStream os = null;
        Document doc = new Document();
        doc.addTitle("demo");
        doc.addSubject("demo");
        doc.addAuthor("ljm");

        try(FileOutputStream os = new FileOutputStream(pdfPath)) {

            PdfWriter.getInstance(doc, os);
            doc.open();
            doc.newPage();
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            Font font = new Font(baseFont);
            doc.add(new Paragraph("图片转PDF", font));
            doc.close();

        } catch (Exception e) {
            e.printStackTrace();
            if (doc != null) {
                doc.close();
            }
        }
    }

    /**
     * 添加图片
     * @param pdfPath
     * @param tempPdfPath
     * @param imageDirPath
     * @throws Exception
     */
    public void andImagePage(String pdfPath, String tempPdfPath, String imageDirPath) throws Exception {

        PdfStamper stamper = null;
        PdfReader reader = null;
        try(FileOutputStream os = new FileOutputStream(pdfPath)) {
            reader = new PdfReader(tempPdfPath);
            stamper = new PdfStamper(reader, os);
            File file = new File(imageDirPath);
            Rectangle pageSizeWithRotation = reader.getPageSizeWithRotation(1);
            for (File f: file.listFiles()) {
                stamper.insertPage(Integer.MAX_VALUE, pageSizeWithRotation);
                PdfContentByte lastPageContent = stamper.getOverContent(reader.getNumberOfPages());
                Image image = Image.getInstance(f.getAbsolutePath());
                image.setAbsolutePosition(0,0);
                lastPageContent.addImage(image);
            }

            stamper.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (stamper != null) {
                stamper.close();
            }
            if (reader != null) {
                reader.close();
            }
        }

        /*PdfReader reader = new PdfReader("/Users/jiamingl/Desktop/material/temp/demo2.pdf");
        PdfStamper stamper = new PdfStamper(reader, new FileOutputStream("/Users/jiamingl/Desktop/material/temp/demo3.pdf"));
        stamper.insertPage(Integer.MAX_VALUE, reader.getPageSizeWithRotation(1));
        PdfContentByte lastPage = stamper.getOverContent(reader.getNumberOfPages());
        File file = new File("/Users/jiamingl/Desktop/material/image/imge00001.jpg");
        Image image = Image.getInstance(file.getAbsolutePath());
        image.setAbsolutePosition(0,0);
        lastPage.addImage(image);

        stamper.close();
        reader.close();*/

    }

    /**
     * 多个图片转pdf
     * @param pdfPath
     * @param imageDir
     */
    public void imagesToPdf(String pdfPath, String imageDir) {
        List<String> validImageType = Arrays.asList(".jpg", ".png", ".bmp", ".jpeg", ".gif");
        Document doc = new Document();
        doc.addTitle("ImageToPdf");
        doc.addSubject("ImageToPdf");
        doc.addAuthor("ljm");
        File file = new File(imageDir);
        try(FileOutputStream os = new FileOutputStream(pdfPath)) {
            PdfWriter.getInstance(doc, os);
            doc.open();
            for (File f: file.listFiles()) {
                String type = f.getAbsolutePath().toLowerCase().substring(f.getAbsolutePath().toLowerCase().lastIndexOf("."));
                if (!validImageType.contains(type)) {
                    continue;
                }
                Image image = Image.getInstance(f.getAbsolutePath());
                image.setAbsolutePosition(0,0);
                doc.newPage();
                doc.add(image);
            }
            doc.close();
        } catch (Exception e){
            e.printStackTrace();
            if (doc != null) {
                doc.close();
            }
        }

    }

    /**
     * 合并pdf
     * @param dstPdfPath
     * @param srcPdfPath
     */
    public void mergePdf(String dstPdfPath, String[] srcPdfPath){

        Document doc = new Document();
        PdfWriter writer = null;

        try(FileOutputStream os = new FileOutputStream(dstPdfPath)) {
            writer = PdfWriter.getInstance(doc, os);
            doc.open();
            PdfContentByte directContent = writer.getDirectContent();
            java.util.List<PdfReader> readers = new ArrayList<>();
            for (String path: srcPdfPath) {
                PdfReader reader = new PdfReader(path);
                readers.add(reader);
            }

            // 开始处理合并
            int pageCount = 0;
            for(PdfReader reader: readers) {

                while (pageCount < reader.getNumberOfPages()) {
                    doc.newPage();
                    pageCount++;
                    PdfImportedPage page = writer.getImportedPage(reader, pageCount);
                    directContent.addTemplate(page, 0, 0);
                }
                pageCount = 0;
            }
            os.flush();
            doc.close();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
            if (writer != null) {
                writer.close();
            }
            if (doc != null) {
                doc.close();
            }
        }

    }

}
