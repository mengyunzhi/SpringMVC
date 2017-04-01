---
layout: post
title:  "第五节：HelloWorld"
description: "你好，世界"
date:   2017-04-01 15:55:20 +0800
categories: chapter1
author: "潘杰"
---

# 建立C层
*   建立第一个包，控制器`controller`: 在`com.mengyunzhi`上点击右键，选择`package`, 然后输入`controller` -> `ok`
*   在`controller`上点右键，新建一个`java class`, 文件名为`HelloController`

![new Hello class]({{site.imageurl}}/chapter1/12.png)

然后<b>使用`RestController`注解来说明该类是一个控制器。</b>

```
package com.mengyunzhi.controller;

import org.springframework.web.bind.annotation.RestController;

/**
 * Created by panjie on 17/4/1.
 */
@RestController
public class HelloController {
}
```

> 在SpringMVC中，将大量的来使用注解`@`来进行声明。

# 新建 funciton

```
package com.mengyunzhi.controller;
...
@Controller
public class HelloController {

    public String world() {
        return "Hello World!";
    }
}
```
 <b> 使用`RequestMapping`注解来声明该方法是一个触发器，并设置该触发器对应的路由信息.</b>

```
package com.mengyunzhi.controller;
...
import org.springframework.web.bind.annotation.RequestMapping;
...
    // 设置该方法为一个触发器，并设置该触发器对应的路由信息"/"
    @RequestMapping("/")
    public String world() {
        return "Hello World!";
    }
}
```

最后，我们点击控制台左上角的重启按钮，重新启动应用（重新进行编译）。

![rerun]({{site.imageurl}}/chapter1/13.png)

IDEA在编写的过程中，没有提示错误，且启动时控制台无报错信息，则说明代码正确。

# 测试
此时，我们打开浏览器，输入:`http://127.0.0.1:8080/`
将得到如下界面。
![hello world]({{site.imageurl}}/chapter1/14.png)

<hr />
### 总结：
没错，在强大的idea的帮助下，搭建一个SpringMVC API后台接口环境，就是如此的简单。

