package com.stylefeng.guns.core.util;

import com.alibaba.fastjson.JSONObject;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@PropertySource("classpath:cos.properties")
@Component
@Data
public class COSUtil {

    private static final Logger logger = LoggerFactory.getLogger(COSUtil.class);


    @Value("${appId}")
    private String APPID;

    @Value("${secretId}")
    private String SECRETID;

    @Value("${secretKey}")
    private String SECRETKEY;


    @Value("${bucketArea}")
    private String BUCKETAREA;

    @Value("${bucketName}")
    private String BUCKETNAME;

    @Value("${diskNamePrefix}")
    private String DISKNAMEPREFIX;

    @Value("${requestUrl}")
    private String REQUESTURL;
    @Value("${ENV}")
    private Boolean ENV;

    /**
     * @description: 获取COS客户端对象
     * @author: lx
     */
    public final COSClient getCOSClient() {
        COSCredentials cred = new BasicCOSCredentials(SECRETID, SECRETKEY);
        Region region = new Region(BUCKETAREA);
        ClientConfig clientConfig = new ClientConfig(region);
        return new COSClient(cred, clientConfig);
    }

    /**
     * @description:创建储存桶
     * @author: lx
     */
    public boolean createBucket(COSClient client, String bucketName) {
        CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
        // 设置 bucket 的权限为 PublicRead(公有读私有写), 其他可选有私有读写, 公有读写
        createBucketRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        try{
            Bucket bucketResult  = client.createBucket(createBucketRequest);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
            return false;
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 查询存储桶列表
     * @param cosClient
     * @return
     */
    public List<Bucket> getListBucket(COSClient cosClient){
        List<Bucket> buckets = new ArrayList<>();
        try {
            buckets = cosClient.listBuckets();
            System.out.println(buckets);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
        return buckets;
    }


    /**
     * @description: 删除Bucket
     * @author: lx
     */
    public void deleteBucket(String bucketName) {
        this.getCOSClient().deleteBucket(bucketName);
        logger.info("删除 {} Bucket成功", bucketName);
    }

    /**
     * @description: 上传腾讯云cos
     * @author: lx
     */
    public String upload2COS(MultipartFile multfile, String fileType) {
        String filePath = null;
        try {
            String fileName = multfile.getOriginalFilename();
            fileName = UUID.randomUUID().toString().replaceAll("\\-", "")  + fileName.substring(fileName.lastIndexOf("."));
            filePath = DISKNAMEPREFIX + fileType + "/" + fileName;
            Long fileSize = multfile.getSize();
            String prefix = fileName.substring(fileName.lastIndexOf("."));
            // 用uuid作为文件名，防止生成的临时文件重复
            final File excelFile = File.createTempFile(UUID.randomUUID().toString(), prefix);
            // MultipartFile to File
            multfile.transferTo(excelFile);
            PutObjectResult putObjectResult = this.getCOSClient().putObject(BUCKETNAME, filePath, excelFile);
            String etag = putObjectResult.getETag();  // 获取文件的 etag
        }catch (Exception e) {
            logger.error("上传腾讯云cos服务器异常.", e);
        }
        return filePath;

        // 方法2 从输入流上传(需提前告知输入流的长度, 否则可能导致 oom)
//        FileInputStream fileInputStream = new FileInputStream(localFile);
//        ObjectMetadata objectMetadata = new ObjectMetadata();
//        // 设置输入流长度为500
//        objectMetadata.setContentLength(500);
//        // 设置 Content type, 默认是 application/octet-stream
//        objectMetadata.setContentType("application/pdf");
//        PutObjectResult putObjectResult = this.getCOSClient().putObject(bucketName, key, fileInputStream, objectMetadata);
//        String etag = putObjectResult.getETag();
            // 关闭输入流...

        // 方法3 提供更多细粒度的控制, 常用的设置如下
        // 1 storage-class 存储类型, 枚举值：Standard，Standard_IA，Archive。默认值：Standard
        // 2 content-type, 对于本地文件上传, 默认根据本地文件的后缀进行映射, 如 jpg 文件映射 为image/jpeg
        //   对于流式上传 默认是 application/octet-stream
        // 3 上传的同时指定权限(也可通过调用 API set object acl 来设置)
         // 4 若要全局关闭上传MD5校验, 则设置系统环境变量, 此设置会对所有的会影响所有的上传校验。 默认是进行校验的。
         // 关闭MD5校验：  System.setProperty(SkipMd5CheckStrategy.DISABLE_PUT_OBJECT_MD5_VALIDATION_PROPERTY, "true");
        // 再次打开校验  System.setProperty(SkipMd5CheckStrategy.DISABLE_PUT_OBJECT_MD5_VALIDATION_PROPERTY, null);
//        File localFile = new File("picture.jpg");
//        String key = "picture.jpg";
//        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file);
//        // 设置存储类型为低频
//        putObjectRequest.setStorageClass(StorageClass.Standard_IA);
//        // 设置自定义属性(如 content-type, content-disposition 等)
//        ObjectMetadata objectMetadata = new ObjectMetadata();
//        // 设置 Content type, 默认是 application/octet-stream
//        objectMetadata.setContentType("image/jpeg");
//        putObjectRequest.setMetadata(objectMetadata);
//        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
//        String etag = putObjectResult.getETag();  // 获取文件的 etag
    }
    /**
     * 删除
     *
     * @param files
     */
    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }


    /**
     * @description: 根据key获取COS服务器上的文件输入流
     * key 指定对象在 COS 上的对象键
     * bucketName 指定对象所在的存储桶
     * downloadPath 指定要下载到的本地路径
     * @author: lx
     */
    public InputStream getCOS2InputStream(String key) {
        // Bucket的命名格式为 BucketName-APPID ，此处填写的存储桶名称必须为此格式
        // 方法1 获取下载输入流
        key = key.substring(key.indexOf("com")+4,key.length());
        GetObjectRequest getObjectRequest = new GetObjectRequest(BUCKETNAME, key);
        COSObject cosObject = this.getCOSClient().getObject(getObjectRequest);
        COSObjectInputStream cosObjectInput = cosObject.getObjectContent();
        return cosObjectInput;
    }
    /**
     * @description: 根据key删除COS服务器上的文件
     * @author: lx
     */
    public void deleteFile(COSClient client, String bucketName, String diskName, String key) {
        client.deleteObject(bucketName, diskName + key);
        logger.info("删除 {} 下的文件 {}{} 成功", bucketName, diskName, key);
    }

    /**
     * @description: 通过文件名判断并获取OSS服务文件上传时文件的contentType
     * @author: lx
     */
    public String getContentType(String fileName) {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1);
        if ("bmp".equalsIgnoreCase(fileExtension))
            return "image/bmp";
        if ("mp4".equalsIgnoreCase(fileExtension))
            return "audio/mp4";
        if ("apk".equalsIgnoreCase(fileExtension) || "ipa".equalsIgnoreCase(fileExtension))
            return "application/octet-stream";
        if ("gif".equalsIgnoreCase(fileExtension))
            return "image/gif";
        if ("jpeg".equalsIgnoreCase(fileExtension) || "jpg".equalsIgnoreCase(fileExtension)
                || "png".equalsIgnoreCase(fileExtension))
            return "image/jpeg";
        if ("html".equalsIgnoreCase(fileExtension))
            return "text/html";
        if ("txt".equalsIgnoreCase(fileExtension))
            return "text/plain";
        if ("vsd".equalsIgnoreCase(fileExtension))
            return "application/vnd.visio";
        if ("ppt".equalsIgnoreCase(fileExtension) || "pptx".equalsIgnoreCase(fileExtension))
            return "application/vnd.ms-powerpoint";
        if ("doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension))
            return "application/msword";
        if ("xml".equalsIgnoreCase(fileExtension))
            return "text/xml";
        if ("mp3".equalsIgnoreCase(fileExtension) || "m4a".equalsIgnoreCase(fileExtension) || "WMA".equalsIgnoreCase(fileExtension))
            return "audio/x-wav";
        return "text/html";
    }

}

