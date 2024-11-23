package com.example.files.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;


@Data
@EqualsAndHashCode(callSuper = false)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名")
    private String username;

    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号码")
    private String phone;

    /**
     * 家庭电话
     */
    @ExcelProperty(value = "家庭电话")
    private String phone1;

    /**
     * 邮箱
     */
    @ExcelProperty(value = "邮箱地址")
    private String email;

    /**
     * 社交媒体
     */
    @ExcelProperty(value = "社交媒体")
    private String socialmedia;

    /**
     * 地址
     */
    @ExcelProperty(value = "真实地址")
    private String address;

    /**
     * 头像
     */
    private String image;

    /**
     * 加入书签 1:已加入，2:未加入
     */
    private Integer collect;

    @TableField(exist = false)
    private MultipartFile file;


}