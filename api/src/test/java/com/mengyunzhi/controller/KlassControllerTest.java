package com.mengyunzhi.controller;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import com.mengyunzhi.service.TeacherService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by panjie on 17/4/14.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(KlassController.class)
public class KlassControllerTest {

    @MockBean
    private TeacherService teacherService;

    @LocalServerPort        // 服务端口号
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void save() throws Exception {
        this.mockMvc.perform(get())
    }

    @Test
    public void get1() throws Exception {

    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }

}