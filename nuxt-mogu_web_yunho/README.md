# nuxt-mogu_web_yunho

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
