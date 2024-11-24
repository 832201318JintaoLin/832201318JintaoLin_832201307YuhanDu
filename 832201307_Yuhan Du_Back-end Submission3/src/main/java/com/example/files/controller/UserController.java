package com.example.files.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.files.entity.User;
import com.example.files.service.FilesService;
import com.example.files.service.UserService;
import com.example.files.utils.ExcelExportUtil;
import com.example.files.utils.UserListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FilesService filesService;
    @Resource
    private UserListener userListener;
    //电话列表
    @GetMapping("/list")
    public List<User> list(String name) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.isNotEmpty(name), User::getUsername, name);
        return userService.list(wrapper);
    }

    //书签列表
    @GetMapping("/collectList")
    public List<User> list() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getCollect, 2);
        return userService.list(wrapper);
    }

    //添加
    @PostMapping("/add")
    public String add(@RequestBody User user) {
        if (userService.save(user)) {
            return "ok";
        }
        return null;
    }

    //修改
    @PutMapping("/update")
    public String update(@RequestBody User user) {
        if (userService.updateById(user)) {
            return "ok";
        }
        return null;
    }

    //删除
    @DeleteMapping("/delete")
    public String delete(Integer id) {
        if (userService.removeById(id)) {
            return "ok";
        }
        return null;
    }

    //上传头像
    @PostMapping("/uploadImage")
    public String upload(MultipartFile file) {
        return filesService.uploadSingleFile(file);
    }

    //修改书签状态
    @PutMapping("/toggleBookmark")
    public String updateCollect(@RequestBody User user) {
        if (userService.updateById(user)) {
            return "ok";
        }
        return null;
    }

    //导出
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws IOException {
        List<User> list = userService.list();
        ExcelExportUtil.exportExcel(response, "电话簿", list, User.class);
    }

    //导入
    @PostMapping("/importExcel")
    public String importExcel(@RequestParam("file") MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), User.class, userListener).sheet().doRead();
            return "ok";
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}

