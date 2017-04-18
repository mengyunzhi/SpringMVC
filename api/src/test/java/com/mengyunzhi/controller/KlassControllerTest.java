package com.mengyunzhi.controller;

import com.mengyunzhi.repository.Klass;
import com.mengyunzhi.repository.Teacher;
import com.mengyunzhi.service.KlassService;
import com.mengyunzhi.service.TeacherService;
import org.junit.Before;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.NestedServletException;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Autowired
    private TeacherService teacherService;

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
        // 创建一个新班级
        Klass klass = klassService.getNewKlass();

        // 更新这个班级的名称
        String klassName = "testName";

        // 断言返回状态码为200
        this.mockMvc.perform(
                put("/klass/" + klass.getId().toString())
                        .contentType("application/json")
                        .content("{\"name\":\"" + klassName + "\",\"teacherId\":" + klass.getTeacher().getId().toString() + "}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(status().is(200));

        // 获取这个班级的名称，并断言其更新成功
        Klass newKlass = klassService.get(klass.getId());
        assertThat(klassName).isEqualTo(newKlass.getName());

        // 创建一个教师
        Teacher teacher = teacherService.getNewTeacher();

        // 为新教师来更新这个班级的teacherId
        this.mockMvc.perform(
                put("/klass/" + klass.getId().toString())
                        .contentType("application/json")
                        .content("{\"name\":\"" + klassName + "\",\"teacherId\":" + teacher.getId().toString() + "}"))
                .andDo(print())
                .andExpect(status().isOk());

        // 获取这个班级的教师信息，并断言其更新成功
        Klass newKlassWithNewTeacher = klassService.get(klass.getId());
        assertThat(teacher.getId()).isEqualTo(newKlassWithNewTeacher.getTeacher().getId());

        // 更新这个班级的名称为空
        String newKlassName = "";

        // 断言其会发生400异常
        this.mockMvc.perform(
                put("/klass/" + klass.getId().toString())
                        .contentType("application/json")
                        .content("{\"name\":\"" + newKlassName + "\",\"teacherId\":" + teacher.getId().toString() + "}"))
                .andDo(print())
                .andExpect(status().is(400));

        // 更新这个班级的teacherId为不可能存在的值
        String teacherId = "3000";
        this.mockMvc.perform(
                put("/klass/" + klass.getId().toString())
                        .contentType("application/json")
                        .content("{\"name\":\"" + klassName + "\",\"teacherId\":" + teacherId + "}"))
                .andDo(print())
                .andExpect(status().isOk());

        // 断言对应的教师信息为null
        Klass newKlassWithNullTeacher = klassService.get(klass.getId());
        assertThat(newKlassWithNullTeacher.getTeacher()).isNull();
    }

    @Test
    // 改名为deleteTest，避免与测试中的delete()方法冲突
    public void deleteTest() throws Exception {
        // 创建班级
        Klass klass = klassService.getNewKlass();

        // 删除班级
        this.mockMvc.perform(delete("/klass/" + klass.getId().toString()))
                .andDo(print())
                .andExpect(status().isOk());

        // 断言删除成功
        Klass newKlass = klassService.get(klass.getId());
        assertThat(newKlass).isNull();

        // 删除一个不存在的班级
        // 断言发生错误
        this.mockMvc.perform(
                delete("/klass/3000")
                        .contentType("application/json")
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}

