## 安装Zipkin
参考博客：

在 SpringBoot 2.x 版本后就不推荐自定义 zipkin server 了，推荐使用官网下载的 jar 包方式

也就是说我们不需要编写一个mogu-zipkin服务了，而改成直接启动jar包即可

**下载地址：**

```
https://search.maven.org/remote_content?g=io.zipkin.java&a=zipkin-server&v=LATEST&c=exec
```

**运行：**

```
java -jar zipkin-server-2.12.5-exec.jar

# 或集成RabbitMQ

java -jar zipkin-server-2.12.5-exec.jar --zipkin.collector.rabbitmq.addresses=127.0.0.1
```