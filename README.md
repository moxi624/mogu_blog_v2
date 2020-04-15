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

## 项目介绍
蘑菇博客，一个基于微服务架构的前后端分离博客系统。前台使用Vue + Element , 后端使用spring boot + spring cloud + mybatis-plus进行开发，使用  Jwt + Spring Security做登录验证和权限校验，使用ElasticSearch和Solr作为全文检索服务，使用Github Actions完成博客的持续集成，文件支持上传七牛云。

- 从大学的时候开始，就一直想要搭建一套属于自己的博客系统，但是一直被没有去弄，现在时间多了，结合目前流行的技术栈，使用前后端分离架构进行项目的开发，也希望自己能够将项目一步步进行完善。
- 蘑菇博客大部分功能是我个人进行开发的，因能力有限，其中很多技术都是一边学习一边使用的，可以说蘑菇博客也是一个我用来熟悉技术的项目，所以很多地方可能考虑不周，在加上没怎么接触公司实际项目，故有能改正的地方，还请各位老哥能够指出~
- 现在挺多是SSM或者SSH的博客管理系统，想用spring boot + spring cloud  + vue 的微服务架构进行尝试项目的构建，里面很多功能可能只是为了满足自己的学习需求而引入的，因此本博客也是一个非常好的SpringBoot、SpringCloud以及Vue技术的入门学习项目。
- 由于原来做过vue + element-ui 做过管理系统，所以现在打算做一套自己的、基于当前最新技术栈、前后端分离的微服务博客系统。
- 考虑到门户网站使用Vue不是很好支持SEO优化，所以门户网站采用Nuxt.js进行开发（因部署时遇到问题，无法完成正常部署，故搁置...欢迎有了解nuxt或者SEO优化的老哥一起探讨~）

## 站点演示

首先特别感谢阿里云提供的 [学生“在家实践” 计划](https://developer.aliyun.com/adc/student/?spm=a2c6h.14062461.J_7747362070.1.5b6933e1X3rvHS)，免费提供的6个月的2核4G 1M的服务器，所以就刚好以此用来做蘑菇博客的演示站点，同时我也在搭建的时候，顺便录制了一套视频：[利用阿里云免费服务器搭建个人博客](https://www.bilibili.com/video/BV1c5411b7EZ?t=17) ，感兴趣的小伙伴可以参考视频一起完成部署~

> 【演示前端】：http://demoweb.moguit.cn/
>
> 【演示后端】：http://demoadmin.moguit.cn/
>
> 【演示账号】：mogu2018   mogu2018

## 运行配置

蘑菇博客使用了一些监控的Spring Cloud组件，但是并不一定都需要部署，必须启动的服务包含

`mogu-eureka`，`mogu-sms`，`mogu-picture`， `mogu-web`, `mogu-admin`

其它的jar都可以不启动，也不影正常使用

最低配置：1核2G `需要开启虚拟内存`

推荐配置：2核4G

> 【阿里云】 限量爆款 低至69元/年 [点我进入](https://www.aliyun.com/minisite/goods?userCode=w7aungxw) `(仅限新用户)`
>
> 【华为云】 1核2G 低至88元/年，2核4G 5M 仅需332元/年  [点我进入](https://activity.huaweicloud.com/discount_area_v5/index.html?fromacct=0410a9ff-25e4-49c6-858b-56cf2f86f07e&utm_source=aHc0OTI0NzI2MA==&utm_medium=cps&utm_campaign=201905) `(仅限新用户)`
>
> 【腾讯云】云产品限时秒杀，爆款1核2G云服务器，首年99元 [点我进入](https://cloud.tencent.com/act/cps/redirect?redirect=1054&cps_key=4e9b8ce643afe47621493331d101dd6e&from=console) `(仅限新用户)`
>
> 【阿里云翼计划】 1核2G 5M / 年 (博主目前使用的) 仅需114元  [点我进入 ](https://promotion.aliyun.com/ntms/act/campus2018.html?spm=5176.10695662.1244717.1.641e5a06KpmU4A&accounttraceid=3ac1b990a4f445859080d2555566af8fiirr?userCode=w7aungxw&tag=share_component&share_source=copy_link?userCode=w7aungxw&tag=share_component&share_source=copy_link?userCode=w7aungxw&tag=share_component&share_source=copy_link) `(仅限学生或未满24岁的用户)`

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

## 项目文档

文档地址：http://moxi159753.gitee.io/mogu_blog_doc

备用文档地址：http://doc.moguit.cn

## 项目地址

目前项目托管在Gitee和Github平台上中，欢迎大家star 和 fork 支持~

- Gitee地址：https://gitee.com/moxi159753/mogu_blog_v2
- Github地址：https://github.com/moxi624/mogu_blog_v2

## 目录介绍

- MoguBlog 是一款基于最新技术开发的多人在线、简洁的博客系统。
- mogu_admin: 提供admin端API接口服务；
- mogu_web：提供web端API接口服务；
- mogu_eureka： 服务发现和注册
- mogu_picture： 图片服务，用于图片上传和下载；
- mogu_sms：消息服务，用于更新ElasticSearch、Solr索引、邮件和短信发送
- mogu_monitor：监控服务，集成SpringBootAdmin用于管理和监控SpringBoot应用程序
- mogu_spider：爬虫服务（目前还未完善）
- mogu_spider：网关服务（目前还未完善）
- mogu_zipkin：链路追踪服务，目前使用java -jar的方式启动
- mogu_search：搜索服务，ElasticSearch和Solr作为检索工具，可插拔配置
- mogu_utils: 是常用工具类；
- mogu_xo: 是存放 Entity，Service，Dao层的
- mogu_base: 是一些Base基类
- mogu_config: 是存放一些配置
- doc: 是蘑菇博客的一些文档和数据库文件
- vue_mogu_admin：VUE的后台管理页面
- vue_mogu_web：VUE的门户网站
- nuxt_mogu_web：Nuxt的门户网站（目前还未完善）

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
|     Kibana     |    分析和可视化平台     |                                                              |
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

### 前端技术

|         技术          |           说明            |                             官网                             |
| :-------------------: | :-----------------------: | :----------------------------------------------------------: |
|        Vue.js         |         前端框架          |                      https://vuejs.org/                      |
|      Vue-router       |         路由框架          |                  https://router.vuejs.org/                   |
|         Vuex          |     全局状态管理框架      |                   https://vuex.vuejs.org/                    |
|        Nuxt.js        | 创建服务端渲染 (SSR) 应用 |                    https://zh.nuxtjs.org/                    |
|        Element        |        前端ui框架         |    [ https://element.eleme.io](https://element.eleme.io/)    |
|         Axios         |       前端HTTP框架        | [ https://github.com/axios/axios](https://github.com/axios/axios) |
|        Echarts        |         图表框架          |                      www.echartsjs.com                       |
|       CKEditor        |       富文本编辑器        |                    https://ckeditor.com/                     |
|     Highlight.js      |     代码语法高亮插件      |         https://github.com/highlightjs/highlight.js          |
|      Tui-editor       |      Markdown编辑器       |              https://github.com/nhn/tui.editor               |
|      vue-cropper      |       图片裁剪组件        |           https://github.com/xyxiao001/vue-cropper           |
| vue-image-crop-upload |    vue图片剪裁上传组件    |      https://github.com/dai-siki/vue-image-crop-upload       |

## 项目搭建

### Windows环境下搭建蘑菇博客

参考 [window环境下配置蘑菇博客环境](http://www.moguit.cn/#/info?blogUid=082ca226cf2e4103b0ffa6e6c13d7b14)，能够在window下搭建蘑菇博客的开发环境

### Docker搭建蘑菇博客

参考 [使用Docker快速搭建蘑菇博客](http://www.moguit.cn/#/info?blogUid=ab8377106a0d4b9f8d66131e4312c69e)，能够快速在服务器中，搭建好博客系统  

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

## 项目中初始用户和密码

- **后台登录**：用户：admin，密码：mogu2018
- **Mysql**：用户：root，密码：mogu2018
- **Redis**：密码：mogu2018
- **远程SSH初始密码**：用户：root，密码：mogu2018
- **RabbitMQ管理页面：** 用户：guest，密码：guest
- **Eureka管理页面：** 用户：user，密码：password123
- **蘑菇博客监控页面**：用户：user，密码：password123

## 致谢

项目起初参考了很多开源项目的解决方案，开源不易，感谢分享

- 感谢**杨青小姐姐**的博客模板，http://www.yangqq.com/
- 感谢**PanJiaChen**提供的**vue-element-admin**： https://github.com/PanJiaChen/vue-element-admin 
- Vue项目参考这篇博客：https://segmentfault.com/a/1190000009506097
- 感谢**苞米豆**提供的**Mybatis-plus**框架：http://mp.baomidou.com/
- 感谢**JetBrains**提供的免费开源License： https://www.jetbrains.com/?from=mogu_blog_v2 
- 感谢**yadong.zhang**提供的第三方登录开源库：https://gitee.com/yadong.zhang/JustAuth.git
- ......

## 关注&交流

刚刚创建了一个QQ群 **(备注：蘑菇博客)** <a target="_blank" href="//shang.qq.com/wpa/qunwpa?idkey=88bc57d77601a3c5ae97fe6d9c0bfa25c2ae166d8f0b9f6da6f7294097be6d08"><img border="0" src="./doc/images/qq/group.png" alt="蘑菇博客交流群" title="蘑菇博客交流群"></a>，目前项目还存在很多不足之处，欢迎各位老哥进群进行技术交流，为了防止广告进入，希望加群的时候能添加备注，谢谢~

|                QQ群（备注：蘑菇博客）                 |               QQ（备注：蘑菇博客）               |
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
- [ ] 增加更新记录
- [ ] 完善爬虫模块
- [ ] 完善网关模块
- [x] 增加数据字典管理
- [ ] 使用Freemark页面静态化技术对博客详情页静态化
- [ ] 解决Nuxt_mogu_web中存在的问题，使博客能被搜索引擎收录
- [x] 前端增加用户个人中心
- [ ] 让原创文章能够同步到多平台，如：CSDN，掘金，博客园等

## 贡献代码

开源项目离不开大家的支持，如果您有好的想法或者代码实现，欢迎提交Pull Request~

1. fork本项目到自己的repo
2. 把fork过去的项目也就是你仓库中的项目clone到你的本地
3. 修改代码
4. commit后push到自己的库
5. 发起PR（pull request） 请求，提交到`dev`分支
6. 等待作者合并

## 赞赏

服务器和域名等服务的购买和续费都会产生一定的费用，为了维持项目的正常运作，如果觉得本项目对您有帮助的话，欢迎朋友能够给予一些支持，非常感谢~

|                       微信                       |                      支付宝                       |
| :----------------------------------------------: | :-----------------------------------------------: |
| <img src="./doc/images/qq/wx.png" width="200" /> | <img src="./doc/images/qq/zfb.png" width="200" /> |

## 相关截图

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
