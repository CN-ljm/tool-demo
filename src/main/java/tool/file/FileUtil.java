package tool.file;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Created by liangjiaming on 2020/7/14
 * @title 文件工具类，读写文件
 * @Desc
 */
public class FileUtil {

    public static void main(String[] args) {

    }

    /**
     * 缓存区方式读取文件内容
     * @param path 文件路径
     * @return
     */
    public static byte[] readFileWithBuffer(String path) {
        InputStream in = null;
        BufferedInputStream bin = null;
        try {
            File file = new File(path);
            in = new FileInputStream(file);
            bin = new BufferedInputStream(in);
            byte[] bytes = new byte[(int) file.length()];
            bin.read(bytes);
            bin.close();
            in.close();

            return bytes;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(in);
            close(bin);
        }

        return null;
    }

    /**
     * 缓存区方式写文件
     * @param bytes 字节数组
     * @param path 文件路径，反斜杠划分层级 如：D:/aa/bb/cc.txt
     */
    public static void writeFileWithBuffer(byte[] bytes, String path) {
        FileOutputStream fout = null;
        BufferedOutputStream bout = null;
        try {
            //先建好文件
            String prefix = path.substring(0, path.lastIndexOf("/"));
            String[] tmp = path.split("/");
            if (tmp.length < 2) {
                throw new RuntimeException("文件路径错误");
            }
            File file = new File(prefix);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            fout = new FileOutputStream(file);
            bout = new BufferedOutputStream(fout);
            bout.write(bytes);

            bout.close();
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(bout);
            close(fout);
        }
    }

    /**
     * NIO方式读取文件
     * @param path 文件路径
     * @return
     */
    public static byte[] readFileWithNIO(String path) {
        FileInputStream fin = null;
        FileChannel channel = null;
        try {
            fin = new FileInputStream(path);
            channel = fin.getChannel();
            // 这里使用直接内存会不会好一点
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            channel.read(byteBuffer);
            channel.close();
            fin.close();
            return byteBuffer.array();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(channel);
            close(fin);
        }

        return null;
    }

    /**
     * NIO方式写文件
     * @param bytes 字节数组
     * @param path 文件路径，反斜杠划分层级 如：D:/aa/bb/cc.txt
     */
    public static void writeFileWithNIO(byte[] bytes, String path) {
        FileOutputStream fout = null;
        FileChannel channel = null;
        try {
            //先建好文件
            String prefix = path.substring(0, path.lastIndexOf("/"));
            String[] tmp = path.split("/");
            if (tmp.length < 2) {
                throw new RuntimeException("文件路径错误");
            }
            File file = new File(prefix);
            if (!file.exists()) {
                file.mkdirs();
            }
            file = new File(path);
            if (!file.exists()) {
                file.createNewFile();
            }
            fout = new FileOutputStream(file);
            channel = fout.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            channel.write(byteBuffer);

            close(channel);
            close(fout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            close(channel);
            close(fout);
        }
    }

    // 关闭流
    private static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
