# 蘑菇博客

<p align=center>
  <a href="http://www.moguit.cn">
    <img src="./doc/images/gitee/favicon2.ico" alt="蘑菇博客" style="width:200px;height:200px">
  </a>
</p>
<p align=center>
   蘑菇博客，一个基于微服务架构的前后端分离博客系统
</p>
<p align="center">
<a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
    	<img src="https://img.shields.io/hexpm/l/plug.svg" ></img>
		<img src="https://img.shields.io/badge/JDK-1.8+-green.svg" ></img>
        <img src="https://img.shields.io/badge/springboot-2.2.2.RELEASE-green" ></img>
<img src="https://img.shields.io/badge/SpringCloud-Hoxton.RELEASE-brightgreen" ></img><img src="https://img.shields.io/badge/vue-2.5.17-green" ></img>
<img src="https://img.shields.io/badge/swagger-2.6.1-green" ></img>
<img src="https://img.shields.io/badge/mybatis--plus-3.1.2-green" ></img></a></p>


##  前言

虽然我知道很多人来到这里，都是pull 或者dowonload了一下代码，就把项目关闭了。突然有一天想起了看看蘑菇博客这个项目有没有更新，然后继续执行以下操作：

- 更新了，pull或者download代码
- 没更新，直接关闭项目 

但是我还是非常希望各位小伙伴能够 **多多star支持**，您的**点赞**就是我维护的动力！

如果您有任何相关建议，欢迎在issue或者群里提出，再次感谢大家的支持~

## 后期维护

项目后台目前有Eureka版本和Nacos版本，其中Eureka版本使用的是SpringCloud一些组件，Nacos版本使用的是SpringCloudAlibaba的一些组件，例如Nacos、Sentinel

- Eureka版本请切换至Eureka分支（将不再进行新功能的开发）
- Nacos版本请切换至Nacos分支（作为以后长期维护的分支）
- master分支为主分支，是基于Nacos分支的稳定版本
- 目前移动端版本的代码已经开源至Nacos分支，如果想体验的小伙伴可以移动至Nacos分支~

## 项目介绍

蘑菇博客，一个基于微服务架构的前后端分离博客系统。前台使用Vue + Element , 后端使用spring boot + spring cloud + mybatis-plus进行开发，使用  Jwt + Spring Security做登录验证和权限校验，使用ElasticSearch和Solr作为全文检索服务，使用Github Actions完成博客的持续集成，文件支持上传七牛云，使用uniapp和ColorUi搭建移动端博客平台。

- 蘑菇博客大部分功能是我个人进行开发的，因能力有限，其中很多技术都是一边学习一边使用的，可以说蘑菇博客也是一个我用来熟悉技术的项目，所以很多地方可能考虑不周，故有能改正的地方，还请各位老哥能够指出~
- 现在挺多是SSM或者SSH的博客管理系统，想用spring boot + spring cloud  + vue 的微服务架构进行尝试项目的构建，里面很多功能可能只是为了满足自己的学习需求而引入的，大家可以根据自己服务器配置来选择启动的服务，因此本博客也是一个非常好的SpringBoot、SpringCloud以及Vue技术的入门学习项目。
- 原来做过vue + element-ui 做过管理系统，所以现在打算做一套自己的、基于当前最新技术栈、前后端分离的微服务博客系统。
- 考虑到门户网站使用Vue不是很好支持SEO优化，所以门户网站采用Nuxt.js进行开发（因开发时遇到问题，nuxt的项目暂时搁置...欢迎有了解nuxt或者Vue SEO优化的老哥一起探讨~）

- [蘑菇博客](http://moguit.cn/#/)中的一些文章，很多都来自我平时的学习笔记，目前托管在另一个仓库：[LearningNotes](https://gitee.com/moxi159753/LearningNotes)，里面涵盖了JVM、JUC、Java，Spring，SpringCloud，计算机网络，操作系统，数据结构，Vue、Linux 等 ，感兴趣的小伙伴可以跳转该仓库 [Star支持](https://gitee.com/moxi159753/LearningNotes)一下

## 站点演示

首先特别感谢阿里云提供的 [学生“在家实践” 计划](https://developer.aliyun.com/adc/student/?spm=a2c6h.14062461.J_7747362070.1.5b6933e1X3rvHS)，免费提供的6个月的2核4G 1M的服务器，所以就刚好以此用来做蘑菇博客的演示站点，同时我也在搭建的时候，顺便录制了一套视频：[利用阿里云免费服务器搭建个人博客](https://www.bilibili.com/video/BV1c5411b7EZ?t=17) ，感兴趣的小伙伴可以参考视频一起完成部署~

> 【演示前端】：http://demoweb.moguit.cn/
>
> 【演示后端】：http://demoadmin.moguit.cn/
>
> 【演示账号】：mogu2018   mogu2018
>
> 【QQ小程序】蘑菇博客的移动端版本，可以扫码体验（代码在Nacos分支）

<img src="./doc/images/uniapp/qqCode.png" width="200" />

## 项目中初始用户和密码

- **后台登录**：用户：admin，密码：mogu2018
- **Mysql**：用户：root，密码：mogu2018
- **Redis**：密码：mogu2018
- **远程SSH初始密码**：用户：root，密码：mogu2018
- **RabbitMQ管理页面：** 用户：guest，密码：guest
- **Nacos管理页面：** 用户：nacos，密码：nacos
- **Sentinel管理页面：** 用户：sentinel，密码：sentinel
- **蘑菇博客监控页面**：用户：user，密码：password123
- **Druid初始密码：** 用户：admin，密码：123456

## 运行配置

蘑菇博客使用了一些监控的Spring Cloud组件，但是并不一定都需要部署，必须启动的服务包含

`nacos`，`nginx`，`rabbitmq`， `redis`，`mysql`，`mogu-sms`，`mogu-picture`， `mogu-web`, `mogu-admin`

其它的服务都可以不启动，也不影正常使用，可以根据自身服务器配置来启动

最低配置：1核2G `需要开启虚拟内存`

推荐配置：2核4G

> 【阿里云】 限量爆款低至91.8元/年 [点我进入](https://www.aliyun.com/minisite/goods?userCode=w7aungxw)
>
> 【腾讯云】十周年感恩回馈，1核2G云服务器首年95元 [点我进入](https://cloud.tencent.com/act/cps/redirect?redirect=1067&cps_key=4e9b8ce643afe47621493331d101dd6e&from=console)
>
> 【阿里云翼计划】 轻量级应用服务器 1核2G 5M / 年 (博主目前使用的) 仅需114元  [点我进入 ](https://promotion.aliyun.com/ntms/act/campus2018.html?spm=5176.10695662.1244717.1.641e5a06KpmU4A&accounttraceid=3ac1b990a4f445859080d2555566af8fiirr?userCode=w7aungxw&tag=share_component&share_source=copy_link?userCode=w7aungxw&tag=share_component&share_source=copy_link?userCode=w7aungxw&tag=share_component&share_source=copy_link&userCode=w7aungxw&tag=share_component&share_source=copy_link) `(仅限学生或未满24岁的用户)`

## 项目特点

- 友好的代码结构及注释，便于阅读及二次开发
- 实现前后端分离，通过Json进行数据交互，前端再也不用关注后端技术
- 页面交互使用Vue2.x，极大的提高了开发效率。
- 引入swagger文档支持，方便编写API接口文档。
- 引入RabbitMQ 消息队列，用于邮件发送、更新Redis和Solr
- 引入JustAuth第三方登录开源库，支持Gitee、Github账号登录。
- 引入ElasticSearch 和 Sol r作为全文检索服务，并支持可插拔配置
- 引入Github Actions工作流，完成蘑菇博客的持续集成、持续部署。
- 引入七牛云对象存储，同时支持本地文件存储
- 引入RBAC权限管理设计，灵活的权限控制，按钮级别的细粒度权限控制，满足绝大部分的权限需求
- 引入Zipkin链路追踪，聚合各业务系统调用延迟数据，可以一眼看出延迟高的服务
- 采用自定义参数校验注解，轻松实现后端参数校验
- 采用AOP+自定义注解+Redis实现限制IP接口访问次数
- 采用自研的评论模块，实现评论邮件通知
- 采用Nacos作为服务发现和配置中心，轻松完成项目的配置的维护
- 采用Sentinel流量控制框架，通过配置再也不怕网站被爆破
- 采用[uniapp](https://uniapp.dcloud.io/) 和[ColorUi](https://github.com/weilanwl/ColorUI) 完成蘑菇博客的移动端门户页面搭建
- 支持多种文本编辑器，Markdown编辑器([Vditor](https://github.com/Vanessa219/vditor))和富文本编辑器([CKEditor](https://github.com/ckeditor/ckeditor4))随心切换

## 项目文档

文档地址：http://moxi159753.gitee.io/mogu_blog_doc

备用文档地址：http://doc.moguit.cn

## 项目地址

目前项目托管在Gitee和Github平台上中，欢迎大家star 和 fork 支持~

- Gitee地址：https://gitee.com/moxi159753/mogu_blog_v2
- Github地址：https://github.com/moxi624/mogu_blog_v2

## FAQ文档

[FAQ文档](./doc/FAQ)汇集了一些小伙伴在使用过程中遇到的问题，在进行蘑菇博客的搭建和部署的时，遇到问题后可以优先查看本[FAQ文档](./doc/FAQ)看看是否出现，这里将收集各个小伙伴遇到的问题，希望各位小伙伴能自己提前翻阅一下，高效提问，避免重复提问。

如果你遇到了[FAQ文档](./doc/FAQ)中没有出现的问题，并且已经解决了，欢迎能够提交Pull Request更新文档，或者直接在群里给我反馈，感谢各位小伙伴~，希望大家在部署和使用的时候能够少走弯路。

## 目录介绍

- MoguBlog 是一款基于最新技术开发的多人在线、简洁的博客系统。
- mogu_admin: 提供admin端API接口服务；
- mogu_web：提供web端API接口服务；
- mogu_eureka： 服务发现和注册
- mogu_picture： 图片服务，用于图片上传和下载；
- mogu_sms：消息服务，用于更新ElasticSearch、Solr索引、邮件和短信发送
- mogu_monitor：监控服务，集成SpringBootAdmin用于管理和监控SpringBoot应用程序
- mogu_spider：爬虫服务`（目前还未完善）`
- mogu_gateway：网关服务`（目前还未完善）`
- mogu_zipkin：链路追踪服务，`目前使用java -jar的方式启动`
- mogu_search：搜索服务，ElasticSearch和Solr作为全文检索工具，[支持可插拔配置](http://moguit.cn/#/info?blogUid=4042b4f4088e4e37e95d9fc75d97298b)，默认使用SQL搜索
- mogu_commons：公共模块，主要用于存放Entity实体类、Feign远程调用接口、以及公共config配置
- mogu_utils: 是常用工具类；
- mogu_xo: 是存放 VO、Service，Dao层的
- mogu_base: 是一些Base基类
- doc: 是蘑菇博客的一些文档和数据库文件
- vue_mogu_admin：VUE的后台管理页面
- vue_mogu_web：VUE的门户网站
- uniapp_mogu_web：基于uniapp 和 colorUi 的蘑菇博客移动端门户页面（Nacos分支）
- nuxt_mogu_web：Nuxt的门户网站，主要用于支持SEO搜索引擎优化`（目前还未完善）`

## 技术选型

### 系统架构图

![image text](./doc/images/gitee/server.jpg)

### 后端技术

|      技术      |          说明           |                             官网                             |
| :------------: | :---------------------: | :----------------------------------------------------------: |
|   SpringBoot   |         MVC框架         | [ https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot) |
|  SpringCloud   |       微服务框架        |           https://spring.io/projects/spring-cloud/           |
| SpringSecurity |     认证和授权框架      |          https://spring.io/projects/spring-security          |
|  MyBatis-Plus  |         ORM框架         |                   https://mp.baomidou.com/                   |
|   Swagger-UI   |      文档生产工具       | [ https://github.com/swagger-api/swagger-ui](https://github.com/swagger-api/swagger-ui) |
|     Kibana     |    分析和可视化平台     |               https://www.elastic.co/cn/kibana               |
| Elasticsearch  |        搜索引擎         | [ https://github.com/elastic/elasticsearch](https://github.com/elastic/elasticsearch) |
|      Solr      |        搜索引擎         |                http://lucene.apache.org/solr/                |
|    RabbitMQ    |        消息队列         |   [ https://www.rabbitmq.com/](https://www.rabbitmq.com/)    |
|     Redis      |       分布式缓存        |                      https://redis.io/                       |
|     Docker     |       容器化部署        |      [ https://www.docker.com](https://www.docker.com/)      |
|     Druid      |      数据库连接池       | [ https://github.com/alibaba/druid](https://github.com/alibaba/druid) |
|     七牛云     |    七牛云 - 对象储存    |         https://developer.qiniu.com/sdk#official-sdk         |
|      JWT       |       JWT登录支持       |                 https://github.com/jwtk/jjwt                 |
|     SLF4J      |        日志框架         |                    http://www.slf4j.org/                     |
|     Lombok     |    简化对象封装工具     | [ https://github.com/rzwitserloot/lombok](https://github.com/rzwitserloot/lombok) |
|     Nginx      | HTTP和反向代理web服务器 |                      http://nginx.org/                       |
|    JustAuth    |    第三方登录的工具     |             https://github.com/justauth/JustAuth             |
|     Hutool     |     Java工具包类库      |                  https://hutool.cn/docs/#/                   |
|    阿里大于    |      短信发送平台       |            https://doc.alidayu.com/doc2/index.htm            |
| Github Actions |       自动化部署        |              https://help.github.com/en/actions              |
|     Zipkin     |        链路追踪         |             https://github.com/openzipkin/zipkin             |
| Flexmark-java  |    Markdown转换Html     |            https://github.com/vsch/flexmark-java             |
|   Ip2region    |    离线IP地址定位库     |          https://github.com/lionsoul2014/ip2region           |

### 前端技术

|         技术          |                  说明                   |                             官网                             |
| :-------------------: | :-------------------------------------: | :----------------------------------------------------------: |
|        Vue.js         |                前端框架                 |                      https://vuejs.org/                      |
|      Vue-router       |                路由框架                 |                  https://router.vuejs.org/                   |
|         Vuex          |            全局状态管理框架             |                   https://vuex.vuejs.org/                    |
|        Nuxt.js        |        创建服务端渲染 (SSR) 应用        |                    https://zh.nuxtjs.org/                    |
|        Element        |               前端ui框架                |    [ https://element.eleme.io](https://element.eleme.io/)    |
|         Axios         |              前端HTTP框架               | [ https://github.com/axios/axios](https://github.com/axios/axios) |
|        Echarts        |                图表框架                 |                      www.echartsjs.com                       |
|       CKEditor        |              富文本编辑器               |                    https://ckeditor.com/                     |
|     Highlight.js      |            代码语法高亮插件             |         https://github.com/highlightjs/highlight.js          |
|        Vditor         |             Markdown编辑器              |             https://github.com/Vanessa219/vditor             |
|      vue-cropper      |              图片裁剪组件               |           https://github.com/xyxiao001/vue-cropper           |
| vue-image-crop-upload |           vue图片剪裁上传组件           |      https://github.com/dai-siki/vue-image-crop-upload       |
|   vue-emoji-comment   |          Vue Emoji表情评论组件          |       https://github.com/pppercyWang/vue-emoji-comment       |
|     clipboard.js      |            现代化的拷贝文字             |                  http://www.clipboardjs.cn/                  |
|      js-beautify      |           美化JavaScript代码            |         https://github.com/beautify-web/js-beautify          |
|     FileSaver.js      |            保存文件在客户端             |           https://github.com/eligrey/FileSaver.js            |
|      SortableJS       |       功能强大的JavaScript 拖拽库       |                  http://www.sortablejs.com/                  |
|   vue-side-catalog    |               目录导航栏                |        https://github.com/yaowei9363/vue-side-catalog        |
|        uniapp         |            移动端跨平台语言             |                  https://uniapp.dcloud.io/                   |
|        colorUi        |         专注视觉的小程序组件库          |             https://github.com/weilanwl/ColorUI              |
|       showdown        | 用Javascript编写的Markdown 到Html转换器 |            https://github.com/showdownjs/showdown            |
|       turndown        | 用JavaScript编写的HTML到Markdown转换器  |           https://github.com/domchristie/turndown            |

## 项目搭建

### Windows环境下搭建蘑菇博客

参考 [window环境下配置蘑菇博客环境](http://www.moguit.cn/#/info?blogUid=082ca226cf2e4103b0ffa6e6c13d7b14)，能够在window下搭建蘑菇博客的开发环境

### Docker搭建蘑菇博客（Eureka分支）

参考 [使用Docker快速搭建蘑菇博客](http://www.moguit.cn/#/info?blogUid=ab8377106a0d4b9f8d66131e4312c69e)，能够快速在Linux服务器中，搭建好博客系统  

### Docker搭建蘑菇博客（Nacos分支）

参考 [使用Docker快速搭建蘑菇博客（Nacos分支）](http://moguit.cn/#/info?blogUid=8100dcb585fff77e3fa25eed50e3708e)，能够快速在Linux服务器中，搭建好博客系统  

### 蘑菇博客部署到云服务器

参考[蘑菇博客如何部署到阿里云服务器](http://www.moguit.cn/#/info?blogUid=89defe3f4a3f317cba9aa0cdb9ff879e)，在你修改蘑菇博客源码后将项目打包部署到云服务器

### 使用Github Actions完成蘑菇博客持续集成

参考 [使用Github Action完成蘑菇博客持续集成](http://moguit.cn/#/info?blogUid=0762bfb392c2cf0a94c8a7934fe46f8f) ，在你提交一个push请求后，通过Github Actions能够完成蘑菇博客自动化 编译、打包、部署等操作。

### 切换搜索模式

参考[蘑菇博客切换搜索模式](http://moguit.cn/#/info?blogUid=4042b4f4088e4e37e95d9fc75d97298b)，完成蘑菇博客的搜索引擎切换，目前支持Solr、ElasticSearch、mysql的方式

### 配置七牛云对象存储

参考[蘑菇博客切换七牛云存储](http://moguit.cn/#/info?blogUid=735ed389c4ad1efd321fed9ac58e646b)，配置文件的七牛云对象存储，及本地文件存储

### 使用Zipkin搭建蘑菇博客链路追踪

参考[使用Zipkin搭建蘑菇博客链路追踪](http://moguit.cn/#/info?blogUid=35bd93cabc08611c7f74ce4564753ef9)，通过聚合各业务系统调用延迟数据，达到链路调用监控跟踪，快速定位其中延迟高的服务

### 使用Nacos搭建蘑菇博客服务注册和配置中心

参考[蘑菇博客Nacos部署指南](http://www.moguit.cn/#/info?blogUid=8dc52bd61e36fa56cfc7699815375572)，搭建蘑菇博客的服务注册和配置中心

### 使用Sentinel搭建蘑菇博客流量控制

参考[蘑菇博客Sentinel安装指南](http://www.moguit.cn/#/info?blogUid=7135efc7f536769efd0d0483c687ba07)，Sentinel被称为分布式系统的流量防卫兵，相当于Hystrix

### 蘑菇博客小程序发布

参考[蘑菇博客QQ小程序发布指南](http://www.moguit.cn/#/info?blogUid=80d3eae77c16cea10e119b9f1a1da4c8)，完成蘑菇博客uniapp移动端的启动和发布

### 蘑菇博客扩展新的功能和页面

参考[蘑菇博客如何扩展新的功能和页面](http://moguit.cn/#/info?blogUid=4eb7694c58cf8205885b1e8565b1a94e)，在蘑菇博客现有架构基础上，开发自己需要的页面。

## 环境搭建

### 开发工具

|     工具     |       说明        |                             官网                             |
| :----------: | :---------------: | :----------------------------------------------------------: |
|     IDEA     |    Java开发IDE    |           https://www.jetbrains.com/idea/download            |
|   WebStorm   |    前端开发IDE    |             https://www.jetbrains.com/webstorm/              |
| RedisDesktop |  Redis可视化工具  | [ https://redisdesktop.com/download](https://redisdesktop.com/download) |
| SwitchHosts  |   本地Host管理    |             https://oldj.github.io/SwitchHosts/              |
|   X-shell    | Linux远程连接工具 |               https://xshell.en.softonic.com/                |
|    X-ftp     | Linux文件传输工具 |         https://www.netsarang.com/zh/all-downloads/          |
|    SQLyog    |  数据库连接工具   |               https://sqlyog.en.softonic.com/                |
| ScreenToGif  |    Gif录制工具    | [ https://www.screentogif.com/](https://www.screentogif.com/) |

### 开发环境

|     工具      | 版本号 |                             下载                             |
| :-----------: | :----: | :----------------------------------------------------------: |
|      JDK      |  1.8   | https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html |
|     Maven     | 3.3.0+ |                   http://maven.apache.org/                   |
| Elasticsearch | 6.3.0  |               https://www.elastic.co/downloads               |
|     Solr      |  7.0   |                http://lucene.apache.org/solr/                |
|     MySQL     |  5.6   |                    https://www.mysql.com/                    |
|    Erlang     |  20.3  |                   https://www.erlang.org/                    |
|   RabbitMQ    | 3.7.4  |            http://www.rabbitmq.com/download.html             |
|     Nginx     |  1.10  |              http://nginx.org/en/download.html               |
|     Redis     | 3.3.0  |                  https://redis.io/download                   |
|    Zipkin     | 2.12.5 | https://search.maven.org/remote_content?g=io.zipkin.java&a=zipkin-server&v=LATEST&c=exec |
|     Nacos     | 1.3.2  |          https://github.com/alibaba/nacos/releases           |
|   Sentinel    | 1.7.2  |         https://github.com/alibaba/Sentinel/releases         |

## 致谢

项目起初参考了很多开源项目的解决方案，开源不易，感谢分享

- 感谢**杨青小姐姐**的博客模板：[http://www.yangqq.com/](http://www.yangqq.com/)
- 感谢**PanJiaChen**提供的：[vue-element-admin](https://github.com/PanJiaChen/vue-element-admin)
- Vue项目搭建参考这篇博客：[https://segmentfault.com/a/1190000009506097](https://segmentfault.com/a/1190000009506097)
- 感谢**苞米豆**提供的**Mybatis-plus**框架：[http://mp.baomidou.com/](http://mp.baomidou.com/)
- 感谢**JetBrains**提供的免费开源License： [https://www.jetbrains.com/?from=mogu_blog_v2](https://www.jetbrains.com/?from=mogu_blog_v2) 
- 感谢**yadong.zhang**提供的第三方登录开源库：[https://gitee.com/yadong.zhang/JustAuth](https://gitee.com/yadong.zhang/JustAuth)
- 感谢 **bihell** 的 Dice 博客项目：[https://github.com/bihell/Dice](https://github.com/bihell/Dice)
- 感谢 **pppercyWang** 提供的Emoji表情评论组件：[vue-emoji-comment](https://github.com/pppercyWang/vue-emoji-comment)
- 感谢 **若依** 提供的RuoYi项目：[https://gitee.com/y_project/RuoYi](https://gitee.com/y_project/RuoYi)
- 感谢 **yaowei9363** 提供的 侧目录组件： [vue-side-catalog](https://github.com/yaowei9363/vue-side-catalog) 
- 感谢 **奇文社区** 提供的 奇文网盘：https://gitee.com/qiwen-cloud/qiwen-file
- 感谢 **weilanwl** 提供的 ColorUI：https://github.com/weilanwl/ColorUI

## 关注&交流

刚刚创建了一个QQ群 (**加群备注**：`蘑菇博客`) <a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=88bc57d77601a3c5ae97fe6d9c0bfa25c2ae166d8f0b9f6da6f7294097be6d08"><img border="0" src="./doc/images/qq/group.png" alt="蘑菇博客交流群" title="蘑菇博客交流群"></a>，目前项目还存在很多不足之处，欢迎各位老哥进群进行技术交流，为了防止广告进入，希望加群的时候能添加备注，谢谢~

|             QQ群（加群备注：`蘑菇博客`）              |              QQ（备注：`蘑菇博客`）              |
| :---------------------------------------------------: | :----------------------------------------------: |
| <img src="./doc/images/qq/qqGroup.png" width="200" /> | <img src="./doc/images/qq/qq.png" width="200" /> |

## 将来要做的事

- [x] 集成Github Actions，完成蘑菇博客持续集成服务
- [x] 门户网站增加登录页面
- [x] 支持第三方登录
- [x] 集成ElasticSearch和Solr
- [x] 将图片存储在七牛云中
- [x] 写一个评论模块，用于替换搜狐畅言
- [x] 按钮级别的细粒度权限控制
- [x] 增加评论表情
- [x] 增加数据字典管理
- [x] 前端增加用户个人中心
- [x] 增加一个FAQ常见问题文档
- [x] 集成表单构建页面，来源于[RuoYi](https://gitee.com/y_project/RuoYi)
- [x] 增加博客详情页目录导航，来源于[vue-side-catalog](https://github.com/yaowei9363/vue-side-catalog)
- [x] 资源管理页面集成网盘管理
- [x] 新建Nacos分支，用于替换Eureka作为服务发现组件和配置中心
- [x] 使用Sentinel做服务限流和熔断
- [x] 增加蘑菇博客小程序项目 uniapp_mogu_web，基于[ColorUI](https://github.com/weilanwl/ColorUI) 和 [Uniapp](https://uniapp.dcloud.io/)
- [x] 富文本编辑器和Markdown编辑器任意切换
- [ ] 完善网盘管理
- [ ] 增加更新记录
- [ ] 完善爬虫模块
- [ ] 完善网关模块
- [ ] 使用Freemark页面静态化技术对博客详情页静态化
- [ ] 解决Nuxt_mogu_web中存在的问题，使博客能被搜索引擎收录
- [ ] 让原创文章能够同步到多平台，如：CSDN，掘金，博客园等
- [ ] 增加博客迁移功能，让其它平台的博客，如：CSDN、博客园，WordPress能够同步到蘑菇博客中
- [ ] 适配门户页面的移动端布局

## 贡献代码

开源项目离不开大家的支持，如果您有好的想法或者代码实现，欢迎提交Pull Request~

1. fork本项目到自己的repo
2. 把fork过去的项目也就是你仓库中的项目clone到你的本地
3. 修改代码
4. commit后push到自己的库
5. 发起PR（pull request） 请求，提交到`Nacos`分支
6. 等待作者合并

## 赞赏

服务器和域名等服务的购买和续费都会产生一定的费用，为了维持项目的正常运作，如果觉得本项目对您有帮助的话，欢迎朋友能够给予一些支持，非常感谢~

|                       微信                       |                      支付宝                       |
| :----------------------------------------------: | :-----------------------------------------------: |
| <img src="./doc/images/qq/wx.png" width="200" /> | <img src="./doc/images/qq/zfb.png" width="200" /> |

## 视频教程

特别感谢 [俺是程序狮](https://space.bilibili.com/277038643) 在B站上给蘑菇博客录制的视频教程，课程讲的非常细致，手把手带着开发博客系统，感兴趣的小伙伴可以去学习和支持一下~

- [项目介绍](https://www.bilibili.com/video/BV1Si4y1u7H4)
- [结构介绍与本地Nginx本地图片服务器启动](https://www.bilibili.com/video/BV1AA411e7W5)
- [mysql脚本准备](https://www.bilibili.com/video/BV1kv411v7ND)
- [后台服务启动](https://www.bilibili.com/video/BV1Nv411i7wu)
- [RabbitMQ启动](https://www.bilibili.com/video/BV1mD4y1U7GT)
- [前端项目启动](https://www.bilibili.com/video/BV1B541187Ez)
- [后台管理系统的前端工程结构介绍](https://www.bilibili.com/video/BV1D54y1U78F)
- [后台管理系统登录页](https://www.bilibili.com/video/BV1854y127d6)
- [后台管理系统登录页2](https://www.bilibili.com/video/BV1DD4y1d7Tx)
- [登录页请求处理](https://www.bilibili.com/video/BV1aT4y1w7Ux)
- [前端发起登录认证](https://www.bilibili.com/video/BV1Rp4y1Y7fj)
- [vuerouter路由配置](https://www.bilibili.com/video/BV14A411n72S)
- .....

## 移动端截图

> 目前移动端版本的代码已经开源至Nacos分支，如果想体验的小伙伴可以移动至Nacos分支

![image text](./doc/images/uniapp/uniapp.gif)

## 网站截图

|                        Admin端                         |                                                       |
| :----------------------------------------------------: | :---------------------------------------------------: |
|      ![image text](./doc/images/admin/login.png)       |    ![image text](./doc/images/admin/dashboard.png)    |
|       ![image text](./doc/images/admin/blog.png)       |    ![image text](./doc/images/admin/blogEdit.png)     |
|    ![image text](./doc/images/admin/addPicture.png)    |    ![image text](./doc/images/admin/blogSort.png)     |
|     ![image text](./doc/images/admin/blogTag.png)      |  ![image text](./doc/images/admin/blogRecommend.png)  |
|     ![image text](./doc/images/admin/blogLink.png)     |   ![image text](./doc/images/admin/systemConf.png)    |
|     ![image text](./doc/images/admin/aboutMe.png)      |      ![image text](./doc/images/admin/user.png)       |
|     ![image text](./doc/images/admin/comment.png)      |     ![image text](./doc/images/admin/webConf.png)     |
|      ![image text](./doc/images/admin/admin.png)       |  ![image text](./doc/images/admin/categoryMenu.png)   |
|                                                        |                                                       |
|      ![image text](./doc/images/admin/sysLog.png)      |    ![image text](./doc/images/admin/exception.png)    |
|      ![image text](./doc/images/admin/visit.png)       |     ![image text](./doc/images/admin/picture.png)     |
|     ![image text](./doc/images/admin/swagger.png)      |   ![image text](./doc/images/admin/pictureSort.png)   |
|                                                        |                                                       |
|   ![image text](./doc/images/admin/monitor_solr.png)   | ![image text](./doc/images/admin/monitor_eureka.png)  |
|  ![image text](./doc/images/admin/monitor_druid.png)   |  ![image text](./doc/images/admin/monitor_admin.png)  |
|  ![image text](./doc/images/admin/monitor_zipkin.png)  | ![image text](./doc/images/admin/monitor_elastic.png) |
| ![image text](./doc/images/admin/monitor_rabbitmq.png) |                                                       |
|                       **Web端**                        |                                                       |
|       ![image text](./doc/images/web/index.png)        |      ![image text](./doc/images/web/index2.png)       |
|       ![image text](./doc/images/web/index2.png)       |      ![image text](./doc/images/web/content.png)      |
|       ![image text](./doc/images/web/login.png)        |       ![image text](./doc/images/web/about.png)       |
|        ![image text](./doc/images/web/sort.png)        |     ![image text](./doc/images/web/classify.png)      |
|        ![image text](./doc/images/web/time.png)        |    ![image text](./doc/images/web/messageBox.png)     |
