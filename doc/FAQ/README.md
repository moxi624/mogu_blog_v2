# 常见问题汇总

以下收集了一些小伙伴在使用过程中遇到的一些场景问题



## 1、启动出现 I/O error on POSt request for "http://localhost:9411/api/v2/span" ：connect timeout

![image-20200512145014851](images/image-20200512145014851.png)

这是因为没有启动链路追踪服务 zipkin，不过不启动也没有关系，只在启动的时候出错一次，不影响正常的使用，如果需要引入，那么参考博客：[使用Zipkin搭建蘑菇博客链路追踪](http://moguit.cn/#/info?blogUid=35bd93cabc08611c7f74ce4564753ef9)