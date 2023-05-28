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
<a target="_blank" href="https://gitee.com/moxi159753/mogu_blog_v2">
    	<img src="https://img.shields.io/hexpm/l/plug.svg" ></img>
		<img src="https://img.shields.io/badge/JDK-1.8+-green.svg" ></img>
        <img src="https://img.shields.io/badge/nodejs-14.x-green" ></img>
        <img src="https://img.shields.io/badge/springboot-2.2.2.RELEASE-green" ></img>
        <img src="https://img.shields.io/badge/SpringCloud-Hoxton.RELEASE-brightgreen" ></img>
        <img src="https://img.shields.io/badge/vue-2.5.17-green" ></img>
        <img src="https://img.shields.io/badge/swagger-3.0.0-brightgreen" ></img>
        <img src="https://img.shields.io/badge/mybatis--plus-3.1.2-green" ></img>
        <img src="https://gitee.com/moxi159753/mogu_blog_v2/badge/star.svg?theme=dark" ></img>
        <img src="https://gitee.com/moxi159753/mogu_blog_v2/badge/fork.svg?theme=dark" ></img>
</a></p>



[项目介绍](#项目介绍) | [站点演示](#站点演示) | [项目特点](#项目特点) | [技术架构](#技术选型) | [项目目录](#项目目录) | [项目文档](#项目文档)  | [快速开始](#快速开始) | [视频教程](#视频教程)| [项目截图](#移动端截图) | [更新记录](https://gitee.com/moxi159753/mogu_blog_v2/releases) | [笔记仓库](https://gitee.com/moxi159753/LearningNotes)

##  前言

[**陌溪**](https://gitee.com/moxi159753/LearningNotes/raw/master/doc/images/qq/添加陌溪.png) 是一个从三本院校一路摸滚翻爬上来的程序员。目前就职于字节跳动的Data商业化广告部门，是字节跳动全线产品的商业变现研发团队。这两天创建了一个 **微信公众号【[陌溪教你学编程](https://gitee.com/moxi159753/LearningNotes/raw/master/doc/images/qq/公众号2.jpg)】**，未来将会在公众号上持续性的输出很多原创小知识以及学习资源，欢迎各位小伙伴关注我，和我一起共同学习，同时我也希望各位小伙伴能够给**蘑菇博客**项目多多 **Star** 支持，您的**点赞**就是我维护的动力！

<p align=center>
    <img src="./doc/images/qq/公众号2.jpg" width="500" />
</p>

项目已有较详细的  [项目搭建文档](http://moxi159753.gitee.io/mogu_blog_doc) ，同时包括了 **Windows**、**Linux** 以及 **Docker** 环境下蘑菇博客的搭建。在使用过程中遇到问题时，首先认真阅读**项目搭建文档** ，同时 [FAQ文档](http://www.moguit.cn/info/288) 还收集了小伙伴咨询的问题，可以提前阅读~

【提问前】可以先 [百度](https://www.baidu.com/) 或者 [Google](https://www.google.com/) 进行解决，有的问题通过**搜索引擎**很快就能得到解决

【提问前】可以首先看看 [issue](https://gitee.com/moxi159753/mogu_blog_v2/issues) 或者  [FAQ文档](http://www.moguit.cn/info/288)，可能你的问题别人也遇到过

【提问前】可以提前阅读 [如何向开源社区提问题](http://www.moguit.cn/info/311)

【提问】推荐使用 [Gitee issue](https://gitee.com/moxi159753/mogu_blog_v2/issues) 进行提问，因为issue解决后能够保留解决记录，帮助其它小伙伴避坑。其次可以使用 [蘑菇社区](http://www.moguit.cn/#/question)， <a href="##关注&交流">QQ群 </a>  或者 <a href="##前言">微信群 </a> 进行提问。群里提问注意提问的时间，把遇到**问题的详细过程都描述清楚**，最好**配上图文信息**，这样能有利于更高效的解决问题。

##  字节内推

目前字节跳动拥有大量岗位需求，欢迎点击下方**内推链接**【内推码: **WYU3X8M**】选择合适的岗位投递。小伙伴们使用**内推链接**投递后，可以通过 **公众号** 联系我跟进面试进度。

提前批投递：[点我传送](https://jobs.toutiao.com/s/eGmrue1) ，社会招聘：[点我传送](https://job.toutiao.com/s/eGmBtNC)

## 项目介绍

蘑菇博客( **MoguBlog** )，一个**基于微服务架构的前后端分离博客系统**。**Web** 端使用 **Vue** + **ElementUi** , 移动端使用 **uniapp** 和 **ColorUI**。后端使用 **SpringCloud** + **SpringBoot** + **Mybatis-plus**进行开发，使用 **Jwt** + **SpringSecurity** 做登录验证和权限校验，使用 **ElasticSearch** 和 **Solr** 作为全文检索服务，使用 **Github Actions**完成博客的持续集成，使用 **ElasticStack** 收集博客日志，文件支持**上传本地**、**七牛云** 和 **Minio**.

- 蘑菇博客大部分功能是我个人进行开发的，因能力有限，其中很多技术都是一边学习一边使用的，可以说蘑菇博客也是一个我用来熟悉技术的项目，所以很多地方可能考虑不周，故有能改正的地方，还请各位老哥能够指出~
- 现在挺多是SSM或者SSH的博客管理系统，想用 **SpringBoot** + **SpringCloud**  + **Vue** 的微服务架构进行尝试项目的构建，里面很多功能可能只是**为了满足自己的学习需求**而引入的，大家可以**根据自己服务器配置来选择启动的服务**，因此本博客也是一个非常好的 **SpringBoot**、**SpringCloud**以及 **Vue** 技术的入门学习项目。
- 原来做过 **Vue** + **ElementUi** 做过管理系统，所以现在打算做一套自己的、基于当前最新技术栈、前后端分离的微服务博客系统。
- [蘑菇博客](http://www.moguit.cn)中的一些文章，很多都来自我平时的学习笔记，目前托管在另一个仓库：[LearningNotes](https://gitee.com/moxi159753/LearningNotes)，里面涵盖了 **JVM**、**JUC**、**Java**，**Spring**，**SpringCloud**，计算机网络，操作系统，数据结构，**Vue**、**Linux** 等 ，感兴趣的小伙伴可以跳转该仓库 [Star支持](https://gitee.com/moxi159753/LearningNotes) 一下

## 运行配置

蘑菇博客使用了一些监控的 **SpringCloud** 组件，但是并不一定都需要部署，必须启动的服务包含

`nacos`，`nginx`，`rabbitmq`， `redis`，`mysql`，`mogu-gateway`，`mogu-sms`，`mogu-picture`， `mogu-web`, `mogu-admin`

其它的服务都可以不启动，也不影正常使用，可以根据自身服务器配置来启动

最低配置：1核2G 【[需开启虚拟内存](http://moguit.cn/info/96)】【容易宕机】

推荐配置：2核4G 【[狂欢特惠](https://curl.qcloud.com/TYzPgyNC)】【博主目前配置】

最近，腾讯云和阿里云的优惠力度非常大，如果有需求的小伙伴，可以了解一下~

> 【阿里云】云服务器狂欢特惠，**2核2G5M** 轻量级应用服务器 **60 元/年** [点我传送](https://www.aliyun.com/minisite/goods?taskPkg=1111ydsrwb&pkgSid=617&recordId=953032&userCode=w7aungxw)
>
> 【腾讯云】云产品限时秒杀，爆款 **2核4G8M** 云服务器，首年 **74元/年、222/3年**【**博主强烈推荐**】[点我传送](https://curl.qcloud.com/TYzPgyNC)

## 站点演示

首先特别感谢群里小伙伴 **@泪梦红尘** 对蘑菇博客提供服务器支持。所以就刚好以此用来做蘑菇博客的演示站点，同时我也在搭建的时候，顺便录制了一套视频：[20分钟部署一个微服务架构的博客系统](https://www.bilibili.com/video/BV13y4y1V7Us) ，感兴趣的小伙伴可以参考视频一起完成部署~

> 【演示前端】：http://demoweb.moguit.cn/
>
> 【演示后端】：http://demoadmin.moguit.cn/
>
> 【演示账号】：[点击获取](https://gitee.com/moxi159753/mogu_blog_v2/raw/Nacos/doc/images/qq/%E8%8E%B7%E5%8F%96%E6%BC%94%E7%A4%BA%E7%8E%AF%E5%A2%83%E8%B4%A6%E5%8F%B7.jpg)
>
> 【小程序】蘑菇博客的移动端版本，可以扫码体验。参考 [蘑菇博客小程序部署](http://www.moguit.cn/info/605) 文档，以及Bilibili上 [20分钟部署博客小程序视频](https://www.bilibili.com/video/BV1sQ4y117nN) 完成小程序的搭建。

|                                                          |
| :------------------------------------------------------: |
| <img src="./doc/images/uniapp/wxCode.jpg" width="200" /> |

## 后期维护

项目后台目前有 **Eureka** 版本和 **Nacos** 版本，其中 **Eureka** 版本使用的是 **SpringCloud** 一些组件，**Nacos** 版本使用的是 **SpringCloudAlibaba** 的一些组件，例如 **Nacos**、**Sentinel** 等

- **Eureka** 版本请切换至 **Eureka** 分支（**已停止维护**，将不再进行新功能的开发）
- **Nacos** 版本请切换至 **Nacos** 分支（作为以后**长期维护**的分支）
- **master** 分支为主分支，是基于 **Nacos** 分支的稳定版本

## 蘑菇博客社区版

蘑菇博客的定位是个人博客系统，而蘑菇博客社区版定位是多人博客系统。蘑菇社区版在原有蘑菇博客的基础上加入了更多的社区玩法，如：支持用户投稿、个人主页、用户签到、关注/取关、收藏/点赞、积分模块、VIP特权功能、用户动态（蘑菇圈）、面经模块、课程模块、消息通知、公众号登录/管理、私信/群聊模块、SEO优化、图片敏感审核、任务模块、成就模块、勋章模块、支付模块、消息触达、资源模块等。。

关于蘑菇社区功能模块的介绍，可查看：[蘑菇社区功能模块介绍](https://vsp58xj3kr.feishu.cn/wiki/wikcnHtRsM9HojaYqrvHWjMu0Ah)

关于蘑菇社区版的**学习**/**商用授权**，以及查看版本对比，可以查看：[点我跳转](https://www.moguit.cn/version)

社区版演示环境如下，目前 [蘑菇官网](https://www.moguit.cn/) 使用的是蘑菇博客社区版代码。

> 【社区演示前端】：http://w.moguit.cn
>
> 【社区演示后端】：http://a.moguit.cn
>
> 【演示账号】：[点击获取](https://gitee.com/moxi159753/mogu_blog_v2/raw/Nacos/doc/images/qq/%E8%8E%B7%E5%8F%96%E6%BC%94%E7%A4%BA%E7%8E%AF%E5%A2%83%E8%B4%A6%E5%8F%B7.jpg)

目前，蘑菇社区版源码暂未开源，可通过 [赞助蘑菇](https://www.moguit.cn/version) 的方式获取源码授权，详情可添加陌溪微信了解：[coder_moxi](https://gitee.com/moxi159753/mogu_blog_v2/raw/Nacos/doc/images/qq/coder_moxi.png) (备注：蘑菇社区)

## 蘑菇博客Go版本

[**mogu_blog_go**](https://gitee.com/moxi159753/mogu_blog_go) 是根据**蘑菇博客**改编的一个 **Golang** 简化版分支，实现了蘑菇博客后台原有的大部分**基础功能**，同时前端代码和数据库基本没有变化，因此可以直接使用蘑菇博客原有的前端页面。

[**mogu_blog_go**](https://gitee.com/moxi159753/mogu_blog_go) 目前采用的技术有：**beego**、**gorm**、**mysql**、**redis** 和 **nginx**。相比于蘑菇博客微服务版，**Golang**版蘑菇能够做到 **1C2G** 的服务器轻松运行，再也不怕网站第二天起来就宕机了~。

参考[蘑菇博客Golang版安装指南](http://moguit.cn/info/593)，快速在 **Windows** 平台搭建蘑菇博客Go版开发环境

## 项目中初始用户和密码

- **后台登录**：用户：admin，密码：mogu2018
- 前台登录：用户：mogu2018，密码：mogu2018
- **Mysql**：用户：root，密码：mogu2018
- **Redis**：密码：mogu2018
- **远程SSH初始密码**：用户：root，密码：mogu2018
- **RabbitMQ管理页面：** 用户：admin，密码：mogu2018
- **Nacos管理页面：** 用户：nacos，密码：nacos
- **Sentinel管理页面：** 用户：sentinel，密码：sentinel
- **蘑菇博客监控页面**：用户：user，密码：password123
- **Druid初始密码：** 用户：admin，密码：123456

## 项目特点

- 友好的代码结构及注释，便于阅读及二次开发
- 实现前后端分离，通过 **Json** 进行数据交互，前端再也不用关注后端技术
- 页面交互使用 **Vue2.x**，极大的提高了开发效率。
- 引入**Swagger** 文档支持，方便编写 **API** 接口文档。
- 引入**RabbitMQ** 消息队列，用于邮件发送、更新 **Redis** 和 **Solr**
- 引入**JustAuth** 第三方登录开源库，支持 **Gitee**、**Github** 账号登录。
- 引入**ElasticSearch** 和 **Solr** 作为全文检索服务，并支持可插拔配置
- 引入**Github Actions** 工作流，完成蘑菇博客的持续集成、持续部署。
- 引入七牛云对象存储，同时支持本地文件存储
- 引入 **RBAC** 权限管理设计，灵活的权限控制，按钮级别的细粒度权限控制，满足绝大部分的权限需求
- 引入 **Zipkin** 链路追踪，聚合各业务系统调用延迟数据，可以一眼看出延迟高的服务
- 采用**自定义参数校验注解**，轻松实现后端参数校验
- 采用 **AOP** + 自定义注解 + **Redis** 实现限制IP接口访问次数
- 采用自研的评论模块，实现评论邮件通知
- 采用 **Nacos** 作为服务发现和配置中心，轻松完成项目的配置的维护
- 采用 **Sentinel** 流量控制框架，通过配置再也不怕网站被爆破
- 采用[uniapp](https://uniapp.dcloud.io/) 和[ColorUi](https://github.com/weilanwl/ColorUI) 完成蘑菇博客的移动端门户页面搭建
- 支持多种文本编辑器，**Markdown** 编辑器([Vditor](https://github.com/Vanessa219/vditor))和富文本编辑器([CKEditor](https://github.com/ckeditor/ckeditor4))随心切换
- 采用 **ElasticStack**【**ElasticSearch** + **Beats** + **Kibana** + **Logstash**】[搭建蘑菇博客日志收集](http://moguit.cn/info/436)
- 采用 **Docker Compose** 完成容器编排，**Portainer** 实现容器可视化，支持[一键部署线上环境](http://www.moguit.cn/info/565)

## 项目文档

文档地址：http://moxi159753.gitee.io/mogu_blog_doc

备用文档地址：http://doc.moguit.cn

## 项目地址

目前项目托管在 **Gitee** 和 **Github** 平台上中，欢迎大家 **Star** 和 **Fork** 支持~

- Gitee地址：https://gitee.com/moxi159753/mogu_blog_v2
- Github地址：https://github.com/moxi624/mogu_blog_v2

## 项目目录

- MoguBlog 是一款基于最新技术开发的多人在线、简洁的博客系统。
- mogu_admin: 提供admin端API接口服务；
- mogu_web：提供web端API接口服务；
- mogu_eureka： 服务发现和注册【注: Nacos分支没有该目录，用Nacos作为服务发现组件】
- mogu_picture： 图片服务，用于图片上传和下载；
- mogu_sms：消息服务，用于更新ElasticSearch、Solr索引、邮件和短信发送
- mogu_monitor：监控服务，集成SpringBootAdmin用于管理和监控SpringBoot应用程序
- mogu_spider：爬虫服务`（目前还未完善）`
- mogu_gateway：网关服务`（目前还未完善）`
- mogu_zipkin：链路追踪服务，`目前使用java -jar的方式启动`
- mogu_search：搜索服务，ElasticSearch和Solr作为全文检索工具，[支持可插拔配置](http://www.moguit.cn/info/119)，默认使用SQL搜索
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

>  蘑菇博客系统架构图，使用 [Processon](https://www.processon.com/i/5e380df1e4b05b335ffa81e9) 在线绘制

### 后端技术

|      技术      |           说明            |                             官网                             |
| :------------: | :-----------------------: | :----------------------------------------------------------: |
|   SpringBoot   |          MVC框架          | [ https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot) |
|  SpringCloud   |        微服务框架         |           https://spring.io/projects/spring-cloud/           |
| SpringSecurity |      认证和授权框架       |          https://spring.io/projects/spring-security          |
|  MyBatis-Plus  |          ORM框架          |                   https://mp.baomidou.com/                   |
|   Swagger-UI   |       文档生产工具        | [ https://github.com/swagger-api/swagger-ui](https://github.com/swagger-api/swagger-ui) |
|     Kibana     |     分析和可视化平台      |               https://www.elastic.co/cn/kibana               |
| Elasticsearch  |         搜索引擎          | [ https://github.com/elastic/elasticsearch](https://github.com/elastic/elasticsearch) |
|     Beats      |     轻量型数据采集器      |               https://www.elastic.co/cn/beats/               |
|    Logstash    | 用于接收Beats的数据并处理 |              https://www.elastic.co/cn/logstash              |
|      Solr      |         搜索引擎          |                http://lucene.apache.org/solr/                |
|    RabbitMQ    |         消息队列          |   [ https://www.rabbitmq.com/](https://www.rabbitmq.com/)    |
|     Redis      |        分布式缓存         |                      https://redis.io/                       |
|     Docker     |        容器化部署         |      [ https://www.docker.com](https://www.docker.com/)      |
|     Druid      |       数据库连接池        | [ https://github.com/alibaba/druid](https://github.com/alibaba/druid) |
|     七牛云     |     七牛云 - 对象储存     |         https://developer.qiniu.com/sdk#official-sdk         |
|      JWT       |        JWT登录支持        |                 https://github.com/jwtk/jjwt                 |
|     SLF4J      |         日志框架          |                    http://www.slf4j.org/                     |
|     Lombok     |     简化对象封装工具      | [ https://github.com/rzwitserloot/lombok](https://github.com/rzwitserloot/lombok) |
|     Nginx      |  HTTP和反向代理web服务器  |                      http://nginx.org/                       |
|    JustAuth    |     第三方登录的工具      |             https://github.com/justauth/JustAuth             |
|     Hutool     |      Java工具包类库       |                  https://hutool.cn/docs/#/                   |
|    阿里大于    |       短信发送平台        |            https://doc.alidayu.com/doc2/index.htm            |
| Github Actions |        自动化部署         |              https://help.github.com/en/actions              |
|     Zipkin     |         链路追踪          |             https://github.com/openzipkin/zipkin             |
| Flexmark-java  |     Markdown转换Html      |            https://github.com/vsch/flexmark-java             |
|   Ip2region    |     离线IP地址定位库      |          https://github.com/lionsoul2014/ip2region           |
|     Minio      |     本地对象存储服务      |                       https://min.io/                        |
| Docker Compose |      Docker容器编排       |               https://docs.docker.com/compose/               |
|   Portainer    |     Docker可视化管理      |            https://github.com/portainer/portainer            |

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

## 快速开始

### 【推荐】一条命令部署蘑菇博客

参考 [如何使用一条命令完成蘑菇博客部署](http://www.moguit.cn/info/597)， 在云服务器上执行下面命令，完成一键部署蘑菇博客

```bash
wget http://oss.moguit.cn/script/moguInit.sh && chmod +x moguInit.sh && sh moguInit.sh
```

Bilibili一键部署视频：[20分钟部署一个微服务架构的博客系统](https://www.bilibili.com/video/BV13y4y1V7Us)

### [推荐] Windows+VMware一键搭建蘑菇博客本地开发环境

参考 [Windows+VMware一键搭建蘑菇博客本地开发环境](http://www.moguit.cn/info/583)，快速在 **Windows** 平台搭建蘑菇博客开发环境，将中间件安装在 **VMware** 中。

### Windows环境下搭建蘑菇博客

参考 [window环境下配置蘑菇博客环境](http://www.moguit.cn/info/14)，能够在 **Windows**下搭建蘑菇博客的开发环境，同时将**所有组件**安装在**Windows** 环境中

### [推荐] DockerCompose一键部署蘑菇博客(Nacos版)

参考 [DockerCompose一键部署蘑菇博客(Nacos版)](http://www.moguit.cn/info/1151) ，在 **Linux** 服务器中，一键完成蘑菇博客项目的部署

### Docker搭建蘑菇博客

参考 [使用Docker快速搭建蘑菇博客(Eureka分支)](http://www.moguit.cn/info/299)，快速在Linux服务器中搭建好博客运行环境，通过发布 **Jar** 包的方式完成部署

参考 [使用Docker快速搭建蘑菇博客(Nacos分支)](http://www.moguit.cn/info/224)，快速在Linux服务器中搭建好博客运行环境，通过发布 **Jar** 包的方式完成部署

### 蘑菇博客部署到云服务器

【**推荐**】参考[IDEA发布蘑菇博客Docker镜像到云服务器(适用于DockerCompose部署)](http://moguit.cn/info/588)，在你修改Nacos分支博客源码后，将重新制作镜像部署到云服务器，然后通过DockerCompose进行启动。

参考[蘑菇博客如何部署到阿里云服务器(Eureka分支)](http://moguit.cn/info/238)，在你修改Eureka分支博客源码后将项目打包部署到云服务器

参考[蘑菇博客如何部署到阿里云服务器(Nacos分支)](http://moguit.cn/info/405)，在你修改Nacos分支博客源码后将项目打包部署到云服务器

### 使用Github Actions完成蘑菇博客持续集成

参考 [使用Github Action完成蘑菇博客持续集成](http://moguit.cn/info/13) ，在你提交一个 **push** 请求后，通过 **Github Actions** 能够完成蘑菇博客自动化 编译、打包、部署等操作。

### 切换搜索模式

参考[蘑菇博客切换搜索模式](http://moguit.cn/info/119)，完成蘑菇博客的搜索引擎切换，目前支持 **Solr**、**ElasticSearch**、**SQL** 的方式

### 配置七牛云对象存储

参考[蘑菇博客切换七牛云存储](http://moguit.cn/info/202)，配置文件的七牛云对象存储，及本地文件存储

### 使用Zipkin搭建蘑菇博客链路追踪

参考[使用Zipkin搭建蘑菇博客链路追踪](http://www.moguit.cn/info/95)，通过聚合各业务系统调用延迟数据，达到链路调用监控跟踪，快速定位其中延迟高的服务

### 使用Nacos搭建蘑菇博客服务注册和配置中心

参考[蘑菇博客Nacos部署指南](http://www.moguit.cn/info/248)，搭建蘑菇博客的服务注册和配置中心

### 使用Sentinel搭建蘑菇博客流量控制

参考[蘑菇博客Sentinel安装指南](http://www.moguit.cn/info/198)，**Sentinel** 被称为分布式系统的流量防卫兵，相当于Hystrix

### 蘑菇博客QQ小程序发布

参考[蘑菇博客QQ小程序发布指南](http://www.moguit.cn/info/223)，完成蘑菇博客 **uniapp** 移动端的启动和发布

### 【推荐】蘑菇博客微信小程序发布

参考[蘑菇博客微信小程序部署指南](http://www.moguit.cn/info/605)，完成蘑菇博客 **uniapp** 移动端的启动和发布

### 蘑菇博客扩展新的功能和页面

参考[蘑菇博客如何扩展新的功能和页面](http://www.moguit.cn/info/141)，在蘑菇博客现有架构基础上，开发自己需要的页面。

###  使用ELK搭建蘑菇博客日志收集

参考[搭建蘑菇博客日志收集](http://www.moguit.cn/info/436)，使用 **Docker** 快速搭建 **ELK** 环境用于蘑菇博客日志收集

## 搭建网站免费CDN加速访问

参考 [网站打开花了20秒，我决定自建蘑菇CDN](http://www.moguit.cn/info/976) ，免费搭建自己的CDN加速

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

**蘑菇博客**起初参考了很多**开源项目**的**解决方案**，**开源不易，感谢分享**

- [<img src="./doc/images/gitee/qiniu.jpg" width="80" />](https://portal.qiniu.com/signup?utm_source=kaiyuan&utm_media=mogu)感谢 [七牛云](https://portal.qiniu.com/signup?utm_source=kaiyuan&utm_media=mogu) 提供的 **免费云存储** 和 **CDN** 服务

- [<img src="./doc/images/gitee/jetbrains.png" width="40" />](https://www.jetbrains.com/?from=mogu_blog_v2)感谢 **[jetbrains](https://www.jetbrains.com/?from=mogu_blog_v2)** 提供的开源 **License** 

- 感谢 **杨青小姐姐** 的博客模板：[http://www.yangqq.com/](http://www.yangqq.com/)
- 感谢**PanJiaChen**的Vue后台管理模板：[vue-element-admin](https://github.com/PanJiaChen/vue-element-admin)
- **Vue** 项目搭建参考这篇博客：[https://segmentfault.com/a/1190000009506097](https://segmentfault.com/a/1190000009506097)
- 感谢 **苞米豆** 提供的 **Mybatis-plus**框架：[http://mp.baomidou.com/](http://mp.baomidou.com/)
- 感谢 **yadong.zhang** 提供的第三方登录开源库：[https://gitee.com/yadong.zhang/JustAuth](https://gitee.com/yadong.zhang/JustAuth)
- 感谢 **bihell** 的 **Dice** 博客项目：[https://github.com/bihell/Dice](https://github.com/bihell/Dice)
- 感谢 **pppercyWang** 提供的Emoji表情评论组件：[vue-emoji-comment](https://github.com/pppercyWang/vue-emoji-comment)
- 感谢 **若依** 提供的 **RuoYi** 项目：[https://gitee.com/y_project/RuoYi](https://gitee.com/y_project/RuoYi)
- 感谢 **yaowei9363** 提供的 **Vue侧目录组件**： [vue-side-catalog](https://github.com/yaowei9363/vue-side-catalog) 
- 感谢 **奇文社区** 提供的 **奇文网盘** 项目：https://gitee.com/qiwen-cloud/qiwen-file
- 感谢 **weilanwl** 提供的 **ColorUI**：https://github.com/weilanwl/ColorUI

## 关注&交流

为了方便小伙伴们沟通交流，我创建了QQ群 (**加群备注**：`蘑菇博客`)  和 **微信群**（备注：**加群**），目前项目还存在很多不足之处，欢迎各位老哥进群进行技术交流，为了防止广告进入，希望加群的时候能添加备注，谢谢~

|                   微信群【备注：加群】                   |          ~~QQ群（备注：`蘑菇博客`）【群满】~~          | QQ群（备注：`蘑菇博客`）【推荐】                       |
| :------------------------------------------------------: | :----------------------------------------------------: | ------------------------------------------------------ |
| <img src="./doc/images/qq/coder_moxi.png" width="200" /> | <img src="./doc/images/qq/qqGroup2.png" width="200" /> | <img src="./doc/images/qq/qqGroup3.png" width="200" /> |

## 未来计划

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
- [x] 使用ELK搭建[蘑菇博客日志收集功能](http://www.moguit.cn/#/info?blogUid=fd9ab58588d93ef792ec72a359a09f6c)
- [x] 使用Minio [搭建对象存储服务](http://www.moguit.cn/#/info?blogUid=a1058b2d030310e2c5d7b0584e514f1f)
- [x] 使用DockerCompose完成 [博客一键部署](http://www.moguit.cn/info/565)
- [x] 使用Portainer对Docker镜像可视化管理
- [x] 适配门户页面的移动端布局
- [x] 完善网盘管理
- [x] 增加更新记录
- [x] 完善爬虫模块
- [x] 完善网关模块
- [ ] 增加K8S部署蘑菇博客教程
- [ ] 增加大屏数据展示页面
- [ ] 增加定时任务模块
- [ ] 使用Freemark页面静态化技术对博客详情页静态化
- [ ] 解决Nuxt_mogu_web中存在的问题，使博客能被搜索引擎收录
- [ ] 让原创文章能够同步到多平台，如：CSDN，掘金，博客园等
- [ ] 增加博客迁移功能，让其它平台的博客，如：CSDN、博客园，WordPress能够同步到蘑菇博客中

## 贡献代码

开源项目离不开大家的支持，如果您有好的想法，遇到一些 **BUG** 并修复了，以及 [蘑菇博客文档](https://gitee.com/moxi159753/mogu_blog_doc) 上有错误的地方订正过来了，欢迎小伙伴们提交 **Pull Request** 参与开源贡献

1. **fork** 本项目到自己的 **repo**
2. 把 **fork** 过去的项目也就是你仓库中的项目 **clone** 到你的本地
3. 修改代码
4. **commit** 后 **push** 到自己的库
5. 发起**PR**（ **pull request**） 请求，提交到  **Nacos** 分支
6. 等待作者合并

## 开源协议

[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)

## 赞赏

**服务器**和**域名**等服务的购买和续费都会**产生一定的费用**，为了**维持项目的正常运作**，如果觉得本项目**对您有帮助**的话，欢迎朋友能够**给予一些支持**，陌溪将用于**提升服务器配置**，感谢小伙伴们的支持（ **ps**: 小伙伴赞赏的时候可以备注一下下~）

|                       微信                       |                      支付宝                       |
| :----------------------------------------------: | :-----------------------------------------------: |
| <img src="./doc/images/qq/wx.png" width="200" /> | <img src="./doc/images/qq/zfb.png" width="200" /> |

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
