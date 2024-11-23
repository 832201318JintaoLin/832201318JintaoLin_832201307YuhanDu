package com.example.files.utils;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**

 * @description 文件工具类
 */
@Component
@Slf4j
public class FileUtils {
    /**
     * AccessKey
     */
    @Value("${ali.oss.accessKeyId}")
    private String accessKeyId;

    /**
     * Secret
     */
    @Value("${ali.oss.accessKeySecret}")
    private String accessKeySecret;

    /**
     * 地域节点
     */
    @Value("${ali.oss.endpoint}")
    private String endpoint;

    /**
     * 路由斜杠
     */
    private final String backslash = "/";

    /**
     * @return java.lang.String
     * @description 生成UUID的文件名称，用于图片存储

     */
    public static String getThirdId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * @param file
     * @return java.lang.String
     * @description 获取文件的后缀

     */
    public static String getSuffix(MultipartFile file) {
        // 使用Optional.ofNullable方法将file对象包装成Optional对象
        return Optional.ofNullable(file)
                // 使用map方法取出文件名并过滤掉为空的情况
                .map(MultipartFile::getOriginalFilename)
                // 过滤掉文件名为空的情况
                .filter(fileName -> !fileName.isEmpty())
                // 使用map方法获取后缀名
                .map(fileName -> fileName.substring(fileName.lastIndexOf(".")))
                // 使用orElse方法来处理返回值为null的情况
                .orElse(null);
    }

    /**
     * @param file       上传的文件
     * @param thirdNo    文件名
     * @param bucketName 桶名
     * @return java.lang.String
     * @description 单文件上传

     */
    public String uploadFile(MultipartFile file, String thirdNo, String bucketName) {
        // 文件后缀
        String suffix = FileUtils.getSuffix(file);
        // 文件全路径
        String filePath =    thirdNo + suffix;
        // 创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            PutObjectResult result = ossClient.putObject(bucketName, filePath, file.getInputStream());
            log.info("result: {}", result);
            return filePath;
        } catch (OSSException ossException) {
            log.error("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            log.error("Error Message:{}" + ossException.getErrorMessage());
            log.error("Error Code:{}" + ossException.getErrorCode());
            log.error("Request ID:{}" + ossException.getRequestId());
            log.error("Host ID:{}" + ossException.getHostId());
            throw new RuntimeException();
        } catch (ClientException clientException) {
            log.error("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            log.error("Error Message:" + clientException.getMessage());
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }


}
