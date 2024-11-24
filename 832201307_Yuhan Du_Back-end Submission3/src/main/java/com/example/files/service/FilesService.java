package com.example.files.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 文件列表 服务类
 * </p>
 */
public interface FilesService {

    /**
     * @param file
     * @return java.lang.Object
     * @description 上传单个文件
     *
     */
    String uploadSingleFile(MultipartFile file);

}
