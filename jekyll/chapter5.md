---
layout: page_list
title: 第五章：高级查询
menuTitle: 第五章
permalink: /chapter5/
search_omit: true
---
{% assign items = site.categories.chapter4 | sort:'date' %}
{% include post_list.html %}

我们在第三章中，对输入的几种获取、验证方式进行讲解。在前面的数据输出中，我们一直采用系统的默认输出。最后往往返回的是一个实体的信息。那么`SpringMVC`是如何自定义数据输出的呢？

# 设置忽略字段


