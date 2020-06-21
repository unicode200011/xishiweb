package com.stylefeng.guns.rest.core.util;

import java.io.*;
import java.util.zip.GZIPInputStream;

/**
 * @author: lx
 */
public class GzipUtil {

    /**
     * 获取文件名（去掉.gz后缀）
     *
     * @param path
     * @return
     */
    public static String getPrefix(String path) {
        int index = path.lastIndexOf('.');
        return path.substring(0, index);
    }

    /**
     * 解压文件
     *
     * @param srcPath
     */
    public static void unGzip(String srcPath) {
        unGzip(new File(srcPath));

    }

    /**
     * 解压Gzip
     *
     * @param src 压缩文件
     */
    public static boolean unGzip(File src) {

        String path = getPrefix(src.getAbsolutePath());
        File dataFile = new File(path);
        if (dataFile.exists()) {
            dataFile.delete();
        }
        GZIPInputStream gzs = null;
        BufferedOutputStream bos = null;
        try {
            gzs = new GZIPInputStream(new FileInputStream(src));
            bos = new BufferedOutputStream(new FileOutputStream(path));

            byte[] buf = new byte[102400];
            int len = -1;
            while ((len = gzs.read(buf)) != -1) {
                bos.write(buf, 0, len);
            }
            bos.flush();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(gzs, bos);
        }
        return false;
    }

    /**
     * 读取文件内容
     *
     * @param file
     * @return
     */
    public static String readFileContent(File file) {
        StringBuilder result = new StringBuilder();
        try {
            //构造一个BufferedReader类来读取文件
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            //使用readLine方法，一次读一行
            while ((s = br.readLine()) != null) {
                result.append(System.lineSeparator() + s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    /**
     * 关闭流
     *
     * @param io
     */
    public static void close(Closeable... io) {
        for (Closeable temp : io) {
            try {
                if (temp != null) {
                    temp.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
