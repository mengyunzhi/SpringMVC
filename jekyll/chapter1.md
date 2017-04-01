---
layout: page_list
title: 第一章：准备知识!
menuTitle: 第一章
permalink: /chapter1/
search_omit: true
---
<section>
    <table>
      <thead>
        <tr>
          <th style="text-align: left" width="41%">标题</th>
          <th style="text-align: left" width="41%">描述</th>
          <th style="text-align: left" width="20%">创建时间</th>
        </tr>
      </thead>
      <tbody>
        {% assign items = site.categories.chapter1 | sort:'date' %}
        {% for post in items %}
        <tr>
          <td style="text-align: left">
            <a href="{{ post.url | relative_url }}">
                {{post.title}}
            </a>
          </td>
          <td style="text-align: left">
            {% if post.description %}
                {{post.description}}
            {% endif %}
          </td>
          <td style="text-align: left">
            {% if post.date %}
                {{post.date | date: "%b %-d, %Y" }}
            {% endif %}
          </td>
        </tr>
        {% endfor %}
      </tbody>
    </table>
</section>