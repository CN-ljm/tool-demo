package com.tool.zip;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * 压缩打包
 */
public class ZipUtil {

    /**
     * 压缩打包多个文件目录内文件成一个文件
     * @param zipPath
     * @param fileDirList
     */
    public static void zipFiles(String zipPath, List<String> fileDirList) {

    }

    /***
     * 打包文件夹（支持递归）
     * @param zipPath
     * @param fileDir
     */
    public static void zipFileDir(String zipPath, String fileDir) {
        File zipFile = new File(zipPath);
        if (!zipFile.exists()) {
            File zipFileParent = zipFile.getParentFile();
            if (!zipFileParent.exists()) {
                zipFileParent.mkdirs();
            }

        }
        ZipOutputStream zout = null;
        try {
            zipFile.createNewFile();
            zout = new ZipOutputStream(zipFile);
            zip(zout, new File(fileDir), "");
            zout.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (zout != null) {
                try {
                    zout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 递归压缩文件
     * @param output ZipOutputStream 对象流
     * @param file 压缩的目标文件流
     * @param childPath 条目目录
     */
    private static void zip(ZipOutputStream output,File file,String childPath){
        FileInputStream input = null;
        try {
            // 文件为目录
            if (file.isDirectory()) {
                // 得到当前目录里面的文件列表
                File list[] = file.listFiles();
                childPath = childPath + (childPath.length() == 0 ? "" : "/") + file.getName();
                // 循环递归压缩每个文件
                for (File f : list) {
                    zip(output, f, childPath);
                }
            } else {
                // 压缩文件
                childPath = (childPath.length() == 0 ? "" : childPath + "/") + file.getName();
                output.putNextEntry(new ZipEntry(childPath));
                input = new FileInputStream(file);
                int readLen = 0;
                byte[] buffer = new byte[1024 * 1024];
                while ((readLen = input.read(buffer, 0, 1024 * 8)) != -1) {
                    output.write(buffer, 0, readLen);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // 关闭流
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    /**
     * 合并压缩文件
     * @param tartetZipFile
     * @param sourceZipFiles
     * @return
     */
    public static boolean Marge(String tartetZipFile,String... sourceZipFiles) {
        boolean flag = true;
        ZipOutputStream out = null;
        List<ZipInputStream> ins = new ArrayList<ZipInputStream>();
        try{
            out = new ZipOutputStream(new FileOutputStream(tartetZipFile));
            HashSet<String> names = new HashSet<String>();
            for(String sourceZipFile : sourceZipFiles){
                ZipFile zipFile = new ZipFile(sourceZipFile, Charset.forName("GBK"));
                ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(sourceZipFile));
                ins.add(zipInputStream);
                ZipEntry ze;
                Enumeration<? extends ZipEntry> enumeration = (Enumeration<? extends ZipEntry>) zipFile.entries();
                while(enumeration.hasMoreElements()){
                    ze = enumeration.nextElement();
                    if(ze.isDirectory()){
                    }else {
                        if(names.contains(ze.getName())){
                            continue;
                        }
                        ZipEntry oze = new ZipEntry(ze.getName());
                        out.putNextEntry(oze);
                        if(ze.getSize()>0){
                            DataInputStream dis = new DataInputStream(zipFile.getInputStream(ze));
                            int len = 0;
                            byte[] bytes = new byte[1024];
                            while((len = dis.read(bytes))>0){
                                out.write(bytes,0,len);
                            }
                            out.closeEntry();
                            out.flush();
                        }
                        names.add(oze.getName());
                    }

                }
                zipInputStream.closeEntry();
                flag = true;
            }
        }catch(Exception ex){
            ex.printStackTrace();
            flag = false;
        }
        finally {
            try{
                if(out != null){
                    out.close();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
            try{
                for(ZipInputStream in : ins){
                    if(in != null){
                        in.close();
                    }
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return flag;
    }
}
