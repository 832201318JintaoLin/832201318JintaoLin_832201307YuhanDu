package com.example.files.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.ListUtils;
import com.example.files.entity.User;
import com.example.files.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description
 */
public class UserListener extends AnalysisEventListener<User> {
    private  final UserService userService;

    private List<User> users = ListUtils.newArrayList();

    public UserListener(UserService userService) {
        this.userService = userService;
    }
    @Override
    public void invoke(User user, AnalysisContext context) {
        users.add(user);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveUsersToDatabase(users);
    }

    private void saveUsersToDatabase(List<User> users) {
        userService.saveBatch(users);
    }
}
