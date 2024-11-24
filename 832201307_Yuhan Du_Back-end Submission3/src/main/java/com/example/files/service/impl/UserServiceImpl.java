package com.example.files.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.files.entity.User;
import com.example.files.mapper.UserMapper;
import com.example.files.service.UserService;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
