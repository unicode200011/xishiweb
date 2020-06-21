package com.stylefeng.guns.core.util;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * @author: LEIYU
 * @description: 图片上传工具类
 */
@Component
public class FileUploadUtil {
    @Value(value = "${guns.file-upload-path}")
    private String fileUploadPath;

    /**
     * @author: LEIYU
     * @description: 文件输入对象 请求对象 文件类型名字
     */
    public String fileUploadSingle(MultipartFile file, HttpServletRequest request, String type) {
        String realPath =request.getScheme()+"://"+request.getHeader("Host")
                + request.getContextPath()+"/upload";
        String fileName = UUID.randomUUID().toString() + "." +file.getOriginalFilename().split("\\.")[1];
        //文件访问路径
        String imagePath = realPath+File.separator+type+File.separator+fileName;
        //文件保存名字;
        String saveFileToDB=type+File.separator+fileName;
        //文件转储路径;
        String downloadPath = fileUploadPath + getPath(type);
        try {
            File saveFile = new File(downloadPath);
            File sourceFile = new File(downloadPath,fileName);
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }
            file.transferTo(sourceFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imagePath;
    }

    /**
     * @author: LEIYU
     * @description: 文件输入对象 请求对象 文件类型名字
     */
    public Map<String,Object> fileUploadSingleForMap(MultipartFile file, HttpServletRequest request, String type) {
        Map<String,Object> resultMap = new HashMap<>();
        String fileName = UUID.randomUUID().toString() + "." +file.getOriginalFilename().split("\\.")[1];
        //文件访问路径
        String imagePath = "/upload"+File.separator+type+File.separator+fileName;
        //文件保存名字;
        String saveFileToDB=type+"/"+fileName;
        //文件转储路径;
        String downloadPath = fileUploadPath + getPath(type);
        try {
            File saveFile = new File(downloadPath);
            File sourceFile = new File(downloadPath,fileName);
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }
            file.transferTo(sourceFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultMap.put("imagePath",imagePath);
        resultMap.put("savePath",saveFileToDB);
        resultMap.put("fileName",file.getOriginalFilename().split("\\.")[0]);

        return resultMap;
    }

    /**
     * @author: LEIYU
     * @description:  base64字符串 请求对象 文件类型名字
     */
    public String fileUploadSingle4Base64(String imageFile, HttpServletRequest request, String type) {
        String realPath = "http://" + request.getServerName() + ":" + request.getServerPort()
                + request.getContextPath();
        String fileName = UUID.randomUUID().toString() + ".jpg";
        //文件访问路径
        String imagePath = realPath + fileUploadPath + getPath(type) + fileName;
        //文件转储路径;
        String savePath = fileUploadPath + getPath(type);
        try {
            // 使用Base64解析成二进制流
            byte[] bs = imageParse(imageFile);
            try {
                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                File realFile = new File(file, fileName);
                OutputStream out = new FileOutputStream(realFile);
                out.write(bs);
                out.flush();
                out.close();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagePath;
    }


   /**
    * @author: LEIYU
    * @description: 文件输入对象 请求对象 文件类型名字
    */
    public List<String> fileUploadBatch(MultipartFile[] fileArr, HttpServletRequest request, String type) {
        List<String> imagePathList = new ArrayList<>();
        if (null != fileArr && fileArr.length > 0) {
            for (MultipartFile file : fileArr) {
                String realPath = "http://" + request.getServerName() + ":" + request.getServerPort()
                        + request.getContextPath();
                String fileName = UUID.randomUUID().toString() + ".jpg";
                //文件访问路径
                String imagePath = realPath + fileUploadPath + getPath(type) + fileName;
                //文件转储路径;
                String downloadPath = fileUploadPath + getPath(type);
                try {
                    File saveFile = new File(downloadPath);
                    File sourceFile = new File(downloadPath,fileName);
                    if (!saveFile.exists()) {
                        saveFile.mkdirs();
                    }
                    file.transferTo(sourceFile);
                    imagePathList.add(imagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return imagePathList;
    }


    /**
     * @author: LEIYU
     * @description:  base64字符串 请求对象 文件类型名字
     */
    public List<String> fileUploadBatch4Base64(String[] imageFileArr, HttpServletRequest request, String type) {
        List<String> imagePathList = new ArrayList<>();
        if (null != imageFileArr && imageFileArr.length > 0) {
            for (String imageFile : imageFileArr) {
                String realPath = "http://" + request.getServerName() + ":" + request.getServerPort()
                        + request.getContextPath();
                String fileName = UUID.randomUUID().toString() + ".jpg";
                //文件访问路径
                String imagePath = realPath + fileUploadPath + getPath(type) + fileName;
                //文件转储路径;
                String savePath = fileUploadPath + getPath(type);
                try {
                    // 使用Base64解析成二进制流
                    byte[] bs = imageParse(imageFile);
                    try {
                        File file = new File(savePath);
                        if (!file.exists()) {
                            file.mkdirs();
                        }
                        File realFile = new File(file, fileName);
                        OutputStream out = new FileOutputStream(realFile);
                        out.write(bs);
                        out.flush();
                        out.close();
                        imagePathList.add(imagePath);
                    } catch (Exception ee) {
                        ee.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return imagePathList;
    }


    /**
     * @author: LEIYU
     * @description: 获取路径
     */
    public static String getPath(String type) {
        DateTime now = DateTime.now();
//        return  type + File.separator + now.getYear() +
//                File.separator + now.getMonthOfYear() + File.separator + now.getDayOfMonth()+ File.separator;
        return  type + File.separator;
    }

  /**
   * @author: LEIYU
   * @description:  base64转二进制流
   */
    public static byte[] imageParse(String baseImage) throws Exception {
        @SuppressWarnings("restriction")
        byte[] decodeBuffer = new BASE64Decoder().decodeBuffer(baseImage);
        for (int i = 0; i < decodeBuffer.length; ++i) {
            if (decodeBuffer[i] < 0) {// 调整异常数据
                decodeBuffer[i] += 256;
            }
        }
        return decodeBuffer;
    }
}
