---
layout: page
title: 我是编辑
permalink: /jekyll/
---
本文档使用`jekyll`自动化生成，如果你对你想了解更多关于jekyll的信息，请登录[http://jekyllrb.com/docs/usage/](http://jekyllrb.com/docs/usage/)
# 操作说明（编辑）
*   安装jekyll, 请参考：[http://jekyllcn.com/docs/installation/](http://jekyllcn.com/docs/installation/)
*   clone本项目后，新建自己的分支，然后进入`/jekyll`文件夹，执行`bundle install`进行主题安装
*   初次使用`bundle exec jekyll serve`构建项目，并启动服务.
*   非初次使用，使用`jekyll serve`来启动服务.
*   文章撰写，请参考已有文件结构及[http://jekyllcn.com/docs/posts/](http://jekyllcn.com/docs/posts/)
*   编辑完成后，使用`jekyll build`来生成项目。项目生成后，将在`/docs`下生成静态的`html`文件。我们可以在项目的**根文件夹**下，执行`http-server`来启动浏览器查看生成效果。
*   查看效果无误后，进行`pull request`进行代码的提交。

# 操作说明(初始化)
*   `jekyll new projectName` 创建新项目
*   `jekyll build --destination` 构建项目
*   `jekyll serve` 启动项目并自动监视
*   `open $(bundle show minima)`打开模板并进行编辑
*   修改`_config.yml`增加项目信息
    *   `destination: ./../docs`配置项目生成目标文件夹
*   在Gemfile中，增加主题信息,比如：gem "jekyll-theme-awesome"
*   执行`bundle install`安装主题信息
*   配置_config.yml，设计主题: theme: jekyll-theme-awesome
*   使用新的主题构建项目，并启动服务`bundle exec jekyll serve`

参考：
[http://jekyllrb.com/docs/usage/](http://jekyllrb.com/docs/usage/)

[http://jekyllrb.com/docs/themes/](http://jekyllrb.com/docs/themes/)
