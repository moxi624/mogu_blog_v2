export default {
  // Global page headers: https://go.nuxtjs.dev/config-head
  head: {
    title: '蘑菇博客 - 专注于技术分享的博客平台',
    htmlAttrs: {
      lang: 'en'
    },
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: '' },
      { hid: 'keywords-h', name: 'keywords', content: '蘑菇博客,蘑菇社区,蘑菇技术社区,,蘑菇IT社区,IT社区,技术社区,Java技术分享,Spring教程,开发者社区' },
    ],
    link: [
      { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }
    ]
  },
  server: {
    port: 3000, // default: 3000
    host: '0.0.0.0' // default: localhost,
  },
  router:{
    //扩展路由
    extendRoutes(routes,resolve){
      // console.log(routes);
      routes.push({
        name:'root',//路由别名
        path:'/index',
        component: resolve(__dirname,'pages/index.vue')
      });

    }
  },

  // Global CSS: https://go.nuxtjs.dev/config-css
  css: [
    'element-ui/lib/theme-chalk/index.css',
    'assets/css/emoji.css'
  ],

  // Plugins to run before rendering page: https://go.nuxtjs.dev/config-plugins
  plugins: [
    {
      src:'~/plugins/mixins',ssr:true
    },
    {
      src:'~/plugins/axios',
      ssr:true//开启服务端渲染
    },
    {
      src:'~/plugins/element-ui',
      ssr:true,//不支持ssr的插件只会在客户端渲染 不要设置为true
      // mode:'server'// client // v2.4+才会出现
    },
    '~/plugins/router'
  ],

  // Auto import components: https://go.nuxtjs.dev/config-components
  components: true,

  // Modules for dev and build (recommended): https://go.nuxtjs.dev/config-modules
  buildModules: [
  ],

  // Modules: https://go.nuxtjs.dev/config-modules
  modules: [ //nuxtjs中自带的模块都需要在这里配置
    // https://go.nuxtjs.dev/axios
    '@nuxtjs/axios',
    //引入cookie插件
    'cookie-universal-nuxt',
    '@nuxtjs/style-resources'
  ],
  axios: {
    proxy: true,// 开启axios 跨域行为
    // prefix: '/api' //基本uri地址
  },
  proxy:{
    '/api/':{
      target:'http://localhost:8607/mogu-web', //代理转发的地址
      changeOrigin:true, //
      pathRewrite:{
        '^/api':''//将 api开头的路径替换为''
      }
    }
  },
  // Build Configuration: https://go.nuxtjs.dev/config-build
  build: {
    transpile:[/^element-ui/],
  }
}
