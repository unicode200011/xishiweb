package com.stylefeng.guns.core.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@PropertySource("classpath:oss.properties")
@Component("ossUtil")
@Data
public class OSSUtil {

    private static final Logger logger = LoggerFactory.getLogger(OSSUtil.class);

    @Value("${endpoint}")
    private String ENDPOINT;
    @Value("${access.key.id}")
    private String ACCESS_KEY_ID;
    @Value("${access.key.secret}")
    private String ACCESS_KEY_SECRET;
    @Value("${bucket.name}")
    private String BUCKET_NAME;
    @Value("${bucket.name.test}")
    private String BUCKET_NAME_TEST;
    @Value("${disk.name.prefix}")
    private String DISK_NAME_PREFIX;
    @Value("${request.url}")
    private String REQUEST_URL;
    @Value("${request.url.test}")
    private String REQUEST_URL_TEST;
    @Value("${prod.env}")
    private Boolean ENV;

    /**
     * @description: 获取阿里云OSS客户端对象
     * @author: LEIYU
     */
    public final OSSClient getOSSClient() {
        return new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    /**
     * @description: 新建Bucket --Bucket权限:私有
     * @author: LEIYU
     */
    public boolean createBucket(OSSClient client, String bucketName) {
        Bucket bucket = this.getOSSClient().createBucket(bucketName);
        return bucketName.equals(bucket.getName());
    }

    /**
     * @description: 删除Bucket
     * @author: LEIYU
     */
    public void deleteBucket(String bucketName) {
        this.getOSSClient().deleteBucket(bucketName);
        logger.info("删除 {} Bucket成功", bucketName);
    }

    /**
     * @description: 上传阿里云OSS
     * @author: LEIYU
     */
    public String uploadObject2OSS(MultipartFile file, String fileType) {
        String filePath = null;
        try {
            String fileName = file.getOriginalFilename();
            InputStream is = file.getInputStream();
            fileName = UUID.randomUUID().toString().replaceAll("\\-", "") + "." + file.getOriginalFilename().substring(fileName.lastIndexOf(".") + 1);
            filePath = DISK_NAME_PREFIX + fileType + "/" + fileName;
            Long fileSize = file.getSize();
            upload(filePath, fileName, fileType, is, fileSize);
        } catch (Exception e) {
            logger.error("上传阿里云OSS服务器异常.", e);
        }
        return filePath;
    }

    /**
     *  分片上传
     * @param file
     * @param fileType
     * @return
     */
    public String uploadObject2OSSOfPart(MultipartFile file, String fileType) {
        String filePath = null;
        try {
            String fileName = file.getOriginalFilename();
            InputStream is = file.getInputStream();
            fileName = UUID.randomUUID().toString().replaceAll("\\-", "") + "." + file.getOriginalFilename().substring(fileName.lastIndexOf(".") + 1);
            filePath = DISK_NAME_PREFIX + fileType + "/" + fileName;
            logger.info("保存路径为【{}】",filePath);
            this.uploadMultipart(file,filePath);
        } catch (Exception e) {
            logger.error("上传阿里云OSS服务器异常.", e);
        }
        return filePath;
    }

    public void upload(String filePath, String fileName, String fileType, InputStream is, Long fileSize) {
        uploadFile(filePath, fileName, fileType, is, fileSize);
    }

    @Async
    public void uploadFile(String filePath, String fileName, String fileType, InputStream is, Long fileSize) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(is.available());
            metadata.setCacheControl("no-cache");
            metadata.setHeader("Pragma", "no-cache");
            metadata.setContentEncoding("utf-8");
            metadata.setContentType(getContentType(fileName));
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");
            logger.info("bucketName:{},diskName:{} fileName:{} is:{},metadata:{}", BUCKET_NAME_TEST, DISK_NAME_PREFIX + fileType, fileName, is, metadata);
            this.getOSSClient().putObject(ENV ? BUCKET_NAME : BUCKET_NAME_TEST, filePath, is, metadata);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("上传阿里云OSS服务器异常.", e);
        }
    }

    public void uploadMultipart(MultipartFile file,String objectName) throws IOException {
        logger.info("开始分片上传，保存路径为【{}】",objectName);
        // 创建InitiateMultipartUploadRequest对象。
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(BUCKET_NAME, objectName);

        // 如果需要在初始化分片时设置文件存储类型，请参考以下示例代码。
        // ObjectMetadata metadata = new ObjectMetadata();
        // metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
        // request.setObjectMetadata(metadata);

        // 初始化分片。
        InitiateMultipartUploadResult upresult = this.getOSSClient().initiateMultipartUpload(request);
        // 返回uploadId，它是分片上传事件的唯一标识，您可以根据这个ID来发起相关的操作，如取消分片上传、查询分片上传等。
        String uploadId = upresult.getUploadId();

        // partETags是PartETag的集合。PartETag由分片的ETag和分片号组成。
        List<PartETag> partETags =  new ArrayList<PartETag>();
        // 计算文件有多少个分片。
        final long partSize = 1 * 1024 * 1024L;   // 1MB

        // 获取文件后缀
        String subfix=objectName.substring(objectName.lastIndexOf("."));
        String prefix=objectName.substring(0,objectName.lastIndexOf("."));
        final File sampleFile = File.createTempFile(prefix, subfix);
        file.transferTo(sampleFile);
        long fileLength = sampleFile.length();
        int partCount = (int) (fileLength / partSize);
        if (fileLength % partSize != 0) {
            partCount++;
        }
        // 遍历分片上传。
        for (int i = 0; i < partCount; i++) {
            if(i == 0){
                logger.info("开始遍历分片上传");
            }
            long startPos = i * partSize;
            long curPartSize = (i + 1 == partCount) ? (fileLength - startPos) : partSize;
            InputStream instream = null;
            try {
                instream = new FileInputStream(sampleFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                // 跳过已经上传的分片。
                instream.skip(startPos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(BUCKET_NAME);
            uploadPartRequest.setKey(objectName);
            uploadPartRequest.setUploadId(uploadId);
            uploadPartRequest.setInputStream(instream);
            // 设置分片大小。除了最后一个分片没有大小限制，其他的分片最小为100KB。
            uploadPartRequest.setPartSize(curPartSize);
            // 设置分片号。每一个上传的分片都有一个分片号，取值范围是1~10000，如果超出这个范围，OSS将返回InvalidArgument的错误码。
            uploadPartRequest.setPartNumber( i + 1);
            // 每个分片不需要按顺序上传，甚至可以在不同客户端上传，OSS会按照分片号排序组成完整的文件。
            UploadPartResult uploadPartResult = this.getOSSClient().uploadPart(uploadPartRequest);
            // 每次上传分片之后，OSS的返回结果会包含一个PartETag。PartETag将被保存到partETags中。
            partETags.add(uploadPartResult.getPartETag());
        }


        // 创建CompleteMultipartUploadRequest对象。
        // 在执行完成分片上传操作时，需要提供所有有效的partETags。OSS收到提交的partETags后，会逐一验证每个分片的有效性。当所有的数据分片验证通过后，OSS将把这些分片组合成一个完整的文件。
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(BUCKET_NAME, objectName, uploadId, partETags);

        // 如果需要在完成文件上传的同时设置文件访问权限，请参考以下示例代码。
        // completeMultipartUploadRequest.setObjectACL(CannedAccessControlList.PublicRead);

        // 完成上传。
        CompleteMultipartUploadResult completeMultipartUploadResult = this.getOSSClient().completeMultipartUpload(completeMultipartUploadRequest);
        logger.info("分片上传结束");
        // 关闭OSSClient。
        this.getOSSClient().shutdown();
    }


    /**
     * @description: 根据key获取OSS服务器上的文件输入流
     * @author: LEIYU
     */
    public InputStream getOSS2InputStream(OSSClient client, String bucketName, String diskName,
                                          String key) {
        OSSObject ossObj = this.getOSSClient().getObject(bucketName, diskName + key);
        return ossObj.getObjectContent();
    }

    /**
     * @description: 根据key删除OSS服务器上的文件
     * @author: LEIYU
     */
    public void deleteFile(OSSClient client, String bucketName, String diskName, String key) {
        client.deleteObject(bucketName, diskName + key);
        logger.info("删除 {} 下的文件 {}{} 成功", bucketName, diskName, key);
    }

    /**
     * @description: 通过文件名判断并获取OSS服务文件上传时文件的contentType
     * @author: LEIYU
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

    public String uploadQrCode2OSS(MultipartFile file, String fileType, String InFileName) {
        String filePath = null;
        try {
            InputStream is = file.getInputStream();
            String fileName = UUID.randomUUID().toString().replaceAll("\\-", "") + "." + InFileName.split("\\.")[1];
            filePath = DISK_NAME_PREFIX + fileType + "/" + fileName;
            Long fileSize = file.getSize();
            upload(filePath, fileName, fileType, is, fileSize);
        } catch (Exception e) {
            logger.error("上传阿里云OSS服务器异常.", e);
        }
        return this.REQUEST_URL+filePath;
    }
}

