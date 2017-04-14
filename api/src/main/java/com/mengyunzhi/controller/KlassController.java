package com.mengyunzhi.controller;

import com.mengyunzhi.service.KlassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Created by panjie on 17/4/13.
 */
@RestController
@RequestMapping("/klass")
public class KlassController {
    // 引入日志类的固有写法
    private final static Logger logger = LoggerFactory.getLogger(KlassController.class);

    @Autowired
    private KlassService klassService;



    @RequestMapping("/test/")
    public void test(@Valid @RequestBody JsonInput jsonInput, BindingResult result) {
        if (result.hasErrors()) {
            logger.info(result.getAllErrors().toString());
        }
        logger.info("获取到的RequestBody为:" + jsonInput.toString());
    }

    private static class JsonInput {
        @NotNull
        private String name;
        private String sex;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        @Override
        public String toString() {
            return "JsonInput{" +
                    "name='" + name + '\'' +
                    ", sex='" + sex + '\'' +
                    '}';
        }

        public JsonInput() {
        }

        public JsonInput(String name, String sex) {
            this.name = name;
            this.sex = sex;
        }
    }
}
