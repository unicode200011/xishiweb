package com.stylefeng.guns.admin.modular.system.controller;

import com.alibaba.fastjson.JSONArray;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ListObjectsRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.admin.config.properties.GlobleProperties;
import com.stylefeng.guns.admin.core.util.DateUtils;
import com.stylefeng.guns.admin.core.util.Result;
import com.stylefeng.guns.core.base.controller.BaseController;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.util.COSUtil;
import com.stylefeng.guns.core.util.FileUploadUtil;
import com.stylefeng.guns.core.util.OSSUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Controller
@RequestMapping("/upload")
public class FileUploadController extends BaseController {

    @Value(value = "${guns.file-upload-path}")
    private String fileUploadPath;

    @Autowired
    FileUploadUtil uploadUtill;

    @Autowired
    OSSUtil ossUtil;

    @Autowired
    COSUtil cosUtil;

    @Autowired
    private GlobleProperties globleProperties;

    private final ResourceLoader resourceLoader;

    @Autowired
    public FileUploadController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @RequestMapping("/ue_upload")
    @ResponseBody
    public Object ueUpload(@RequestParam(value = "file") MultipartFile file,
                           @RequestParam(required = false, defaultValue = "default") String fileType,
                           HttpServletRequest request) {
//        Map<String,Object> fileName = uploadUtill.fileUploadSingleForMap(file, request, fileType);
        String originalFilename = file.getOriginalFilename();
        String fileSize = DateUtils.getFileSize(file);

        String imageSize = "";
//        if(!fileType.equals("galaxy_audio")&&!fileType.equals("galaxy_video")){
//            try {
//                imageSize  = DateUtils.getImageSize(file.getInputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        String filePath = ossUtil.uploadObject2OSS(file, fileType);
//        String filePath = cosUtil.upload2COS(file, fileType);
        filePath = (ossUtil.getENV() ? ossUtil.getREQUEST_URL() : ossUtil.getREQUEST_URL_TEST()) + filePath;
//        filePath = cosUtil.getREQUESTURL() + filePath;
        Map<String, Object> resultMap = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add(filePath);
        resultMap.put("errno", 0);
        resultMap.put("code",200);
        resultMap.put("data", list);
        resultMap.put("savePath", filePath);
        resultMap.put("originalFilename", originalFilename);
        resultMap.put("fileSize", fileSize);
        resultMap.put("imageSize", imageSize);
        return resultMap;
    }

    @RequestMapping("/part_upload")
    @ResponseBody
    public Object partUpload(@RequestParam(value = "file") MultipartFile file,
                           @RequestParam(required = false, defaultValue = "default") String fileType) {
        String filePath = ossUtil.uploadObject2OSSOfPart(file, fileType);
        filePath = (ossUtil.getENV() ? ossUtil.getREQUEST_URL() : ossUtil.getREQUEST_URL_TEST()) + filePath;
        Map<String, Object> resultMap = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add(filePath);
        resultMap.put("code",200);
        resultMap.put("data", list);
        resultMap.put("savePath", filePath);
        return resultMap;
    }

    @RequestMapping("/app_upload")
    @ResponseBody
    public Object appUpload(@RequestParam(value = "file") MultipartFile file,
                           @RequestParam(required = false, defaultValue = "app") String fileType,
                           HttpServletRequest request) {
        Map<String,Object> fileName = uploadUtill.fileUploadSingleForMap(file, request, fileType);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("savePath", "http://douxiv.com"+fileName);
        return resultMap;
    }

    @GetMapping(value = "/{type}/{fileName:.+}")
    public ResponseEntity<?> getFile(@PathVariable(value = "fileName") String fileName, @PathVariable(value = "type") String type) {
        try {
            return ResponseEntity.ok(this.resourceLoader.getResource("file:" + Paths.get(this.fileUploadPath + FileUploadUtil.getPath(type), fileName).toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping("/download")
    public void downloadFile(HttpServletResponse response, @RequestParam("fileName") String fileUrlName) {
        String fileName = fileUrlName.substring(fileUrlName.lastIndexOf("/") + 1);
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

        String objectName = fileUrlName.replaceAll(this.ossUtil.getREQUEST_URL_TEST(), "");
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(this.ossUtil.getENDPOINT(), this.ossUtil.getACCESS_KEY_ID(), this.ossUtil.getACCESS_KEY_SECRET());
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(this.ossUtil.getBUCKET_NAME(), objectName);
        // 读取文件内容。
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            InputStream inputStream = ossObject.getObjectContent();
            byte[] b = new byte[1024];
            int n;
            while ((n = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, n);
            }
            inputStream.close();
        } catch (Exception e) {
            log.info("下载文件失败");
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
    }

    @RequestMapping("/downloadPdf")
    @ResponseBody
    public Object downloadPdf(HttpServletResponse response, String fileUrlName) {
        String fileName = fileUrlName.substring(fileUrlName.lastIndexOf("/") + 1);
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

        String objectName = fileUrlName.replaceAll(this.ossUtil.getREQUEST_URL_TEST(), "");
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(this.ossUtil.getENDPOINT(), this.ossUtil.getACCESS_KEY_ID(), this.ossUtil.getACCESS_KEY_SECRET());
        // ossObject包含文件所在的存储空间名称、文件名称、文件元信息以及一个输入流。
        OSSObject ossObject = ossClient.getObject(this.ossUtil.getBUCKET_NAME(), objectName);
        // 读取文件内容。

        try {
            String path = "guns-admin/src/main/webapp/static/img/";
            File file = new File(path+fileName);//文件路径（路径+文件名）
            if (!file.exists()) {   //文件不存在则创建文件，先创建目录
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }else {
                return SUCCESS_TIP;
            }
            FileOutputStream outputStream = new FileOutputStream(file);
//            ServletOutputStream outputStream = response.getOutputStream();
            InputStream inputStream = ossObject.getObjectContent();
            byte[] b = new byte[1024];
            int n;
            while ((n = inputStream.read(b)) != -1) {
                outputStream.write(b, 0, n);
            }
            inputStream.close();
        } catch (Exception e) {
           throw new GunsException(502,"下载出错");
        } finally {
            // 关闭OSSClient。
            ossClient.shutdown();
        }
        return SUCCESS_TIP;
    }
}
