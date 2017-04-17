package com.mengyunzhi.controller;

import com.mengyunzhi.repository.Klass;
import com.mengyunzhi.service.KlassService;
import org.junit.Before;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;


/**
 * Created by panjie on 17/4/14.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc       // 自动配置 模似MVC
public class KlassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private KlassService klassService;

    private Klass klass;
    private Long teacherId;

    @Before
    public void before() {
        this.klass = klassService.getNewKlass();
        this.teacherId = klass.getTeacher().getId();
    }

    @Test
    public void save() throws Exception {
        String content = "{\"name\":\"hello\",\"teacherId\":" + teacherId.toString() + "}";

        // 提交JSON POST请求, 断言收到json数据，并存在id值
        this._save(content).andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    // 检测异常(用户名过短）
    public void saveWithNameEmpty() throws Exception {
        String content = "{\"name\":\"\",\"teacherId\":" + teacherId.toString() + "}";

        this._save(content).andExpect(status().is4xxClientError());
    }


    @Test
    // 检测异常(用户名过长)
    public void saveWithNameTooLong() throws Exception {
        String content = "{\"name\":\"sdfsdfsdfsdfsdf\",\"teacherId\":" + teacherId.toString() + "}";

        this._save(content).andExpect(status().is4xxClientError());
    }

    // 新增数据
    private ResultActions _save(String content) throws Exception {
        return this.mockMvc.perform(post("/klass/")
                .contentType("application/json")
                .content(content))
                .andDo(print());
    }

    @Test
    // 由于get()与http请求的get命名相同，在这里重新命名以防止冲突。
    public void getTest() throws Exception {
        // 直接添加一个实体
        Klass klass = klassService.getNewKlass();

        // 发起http请求来查询这个实体
        this.mockMvc.perform(get("/klass/" + klass.getId().toString()))
                .andDo(print())
                .andExpect(jsonPath("$.id", is(klass.getId().intValue())));
    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }
}

