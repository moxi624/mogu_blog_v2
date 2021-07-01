# nuxt-mogu_web_yunho

这个前端分支是基于nuxtjs开发的，我最近也是刚撸完相关的教程，大家如果有兴趣的话可以到我的博客去看看相关的笔记：https://yunho.io/nuxtjs/01.html，如果想看视频的话也有对应的视频：https://www.bilibili.com/video/BV13Z4y1T74J?p=1

## 本地环境安装

下载源码后，本地安装依赖npm install

```shell
D:\my\idea_test\mogu_blog_v2\nuxt-mogu_web_yunho>npm install
npm WARN deprecated har-validator@5.1.5: this library is no longer supported
npm WARN deprecated uuid@3.4.0: Please upgrade  to version 7 or higher.  Older versions may use Math.random() in certain circumstances, which is known to be problematic.  See https://v8.dev/blog/math-random for details.
npm WARN deprecated request@2.88.2: request has been deprecated, see https://github.com/request/request/issues/3142

added 1446 packages, and audited 1447 packages in 24s

3 packages are looking for funding
  run `npm fund` for details

20 vulnerabilities (8 moderate, 12 high)

To address issues that do not require attention, run:
  npm audit fix

To address all issues (including breaking changes), run:
  npm audit fix --force

Run `npm audit` for details.

```

安装之后直接运行即可

```shell
npm run dev
```



## 部署

前后端都需要部署至服务器环境

服务器端口配置：

```js
//nuxt.config.js
  server: {
    port: 8000, // default: 3000
    host: '0.0.0.0' // default: localhost,服务端一般设置为本地操作
  },
  router:{
    middleware: 'auth',
    //扩展路由
    extendRoutes(routes,resolve){
      console.log(routes);
      routes.push({
        name:'root',//路由别名
        path:'/index',
        component: resolve(__dirname,'pages/index.vue')

      });

    }
  },
```

### 打包

```
npm run  build
```

将文件.nuxt`,`static`,`package.json,package-lock.json`,`nuxt.config.js,拷贝到服务器目录

### 安装依赖

```shell
npm install
#也可以直接从本地将node_modules目录拷贝上来
```

### 运行

需要pm2

```shell
npm install -g pm2 #安装
```

运行nuxt

```
pm2 --name=nuxt3000 start npm --run start 
```
