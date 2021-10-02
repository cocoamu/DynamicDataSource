package com.cocoamu.dynamicdatasource.controller;

import com.cocoamu.dynamicdatasource.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping("/test1")
    public List<Map<String, Object>> selectMaster(){
        return userService.queryForMaster();
    }

    @RequestMapping("/test2")
    public List<Map<String, Object>> selectSlave(){
        return userService.queryForSlave();
    }

}
