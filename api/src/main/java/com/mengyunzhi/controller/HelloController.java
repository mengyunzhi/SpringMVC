package com.mengyunzhi.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by panjie on 17/4/1.
 */
@RestController
public class HelloController {
    // 设置该方法为一个触发器，并设置该触发器对应的路由信息"/"
    @RequestMapping("/")
    public String world() {
        return "Hello World!";
    }
}
