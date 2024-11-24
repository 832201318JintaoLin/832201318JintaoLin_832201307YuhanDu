package com.example.files.utils;

import com.example.files.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Autowired
    private UserService userService;

    @Bean
    public UserListener userListener() {
        return new UserListener(userService);
    }
}
