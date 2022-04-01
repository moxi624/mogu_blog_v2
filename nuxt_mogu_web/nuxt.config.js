
module.exports = {
  /*
  ** Headers of the page
  */
  head: {
    title: 'xc-portal',
    meta: [
      { charset: 'utf-8' },
      { name: 'viewport', content: 'width=device-width, initial-scale=1' },
      { hid: 'description', name: 'description', content: 'xc-portal' }
    ],
    link: [
      { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' }
    ]
  },
  server: {
    port: 21000, // default: 3000
    host: '0.0.0.0' // default: localhost,
  },
  css: [
    'highlight.js/styles/github.css',
    'element-ui/lib/theme-chalk/index.css',
  ],
  env: {
    WEB_API: process.env.WEB_API,
    BASE_BLOG_API: process.env.BASE_BLOG_API,
  },
  loaders: [
    {
      test: /\.(woff2?|eot|ttf|otf)(\?.*)?$/,
      loader: 'url-loader',
      query: {
        limit: 1000, // 1 KO
        name: 'fonts.[name].[hash:7].[ext]'
      }
    }

  ],
  /*
  ** Customize the progress bar color
  */
  loading: { color: '#3B8070' },
  /*
  ** Build configuration
  */
  build: {
    vendor: ['axios', 'element-ui'],
    /*extend (config, { isDev, isClient }) {
      if (isDev && isClient) {
        config.module.rules.push({
          enforce: 'pre',
          test: /\.(js|vue)$/,
          loader: 'eslint-loader',
          exclude: /(node_modules)/
        })
      }
    }*/


  },
  css: ["~static/css/index.css", "~static/highlight/styles/zenburn.css"],
  modules: ['@nuxtjs/pwa', '@nuxtjs/axios', '@nuxtjs/proxy'],
  plugins: [{ src: '~plugins/element-ui', ssr: true }, { src: '~plugins/highlight', ssr: true }],
  proxy: [
    /*    ['/group1', { target: 'http://192.168.101.64' }],
        ['/search', { target: 'http://127.0.0.1:40100',pathRewrite: { '^/search' : '/' } }]*/
  ]
}
