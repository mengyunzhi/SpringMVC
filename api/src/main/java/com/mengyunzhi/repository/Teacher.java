package com.mengyunzhi.repository;


import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by panjie on 17/4/5.
 */
// 使用@Entity 来说明该类对应一个数据表
@Entity
public class Teacher {
    //    声明主键
    @Id
    //    声明主键生成策略为 自动
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 25)            // 声明字段的长度为25
    @Size(min = 2, max = 25)        // 最小长度为2（不能为空），最大长度为25
    private String name = "";       // 姓名

    @Column(length = 50)
    @NotNull                        // 此列不能为空
    @Email                          // 邮箱验证
    @Size(max = 50)                 // 最大长度为50
    private String email = "";      // 邮箱

    @Size(max = 255)                // 最大长度为255
    private String address = "";    // 地址. 不进行@Column声明，则默认长度为255

//    @OneToMany
//    private Set<Klass> klass = new HashSet<Klass>();

//    public Set<Klass> getKlass() {
//        return klass;
//    }
//
//    public void setKlass(Set<Klass> klass) {
//        this.klass = klass;
//    }

    private Boolean sex = false;    // 性别：false(0)，男。true(1)，女.

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public Boolean getSex() {
        return sex;
    }

    public Teacher(String name, String email, String address, Boolean sex) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.sex = sex;
    }

    public Teacher() {
    }


    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", sex=" + sex +
                '}';
    }
}