importScripts('/_nuxt/workbox.476439e0.js')

const workboxSW = new self.WorkboxSW({
  "cacheId": "xc-portal",
  "clientsClaim": true,
  "directoryIndex": "/"
})

workboxSW.precache([
  {
    "url": "/_nuxt/app.84b1d7b0c6431367f528.js",
    "revision": "3c13caf95e514838f0417c242bc8dbda"
  },
  {
    "url": "/_nuxt/layouts_default.bac92f2457933bf4f380.js",
    "revision": "ac7d26a619fb9051ca1647c69713eddb"
  },
  {
    "url": "/_nuxt/layouts_default1.189188f5368cd69cf723.js",
    "revision": "f540ee4d99e835c9439f341ed2d85dc2"
  },
  {
    "url": "/_nuxt/layouts_empty-page.52bafed3daafddf28efe.js",
    "revision": "17cb96d6c378c41bd40e998d4251dc1f"
  },
  {
    "url": "/_nuxt/manifest.e2d389e8cbfb78263369.js",
    "revision": "b9b1ff400d8787a94da06eb9bc1f71cf"
  },
  {
    "url": "/_nuxt/pages_about_index.a6a14a6fffb1f424e214.js",
    "revision": "6e2e948198e9078f695a7e0033d3951f"
  },
  {
    "url": "/_nuxt/pages_home_index.0ec59bf04d8cd6a5f0ff.js",
    "revision": "f180cf2353f15600ca5429efc617526d"
  },
  {
    "url": "/_nuxt/pages_index.0c55454dd56d5b1d3a9b.js",
    "revision": "acb21da943603c322ac89439f27355c6"
  },
  {
    "url": "/_nuxt/pages_info__uid.57b9440661c84022bf4e.js",
    "revision": "1b3608342ddb08bb26f1f05be8b3958e"
  },
  {
    "url": "/_nuxt/pages_list__uid.98bb898a34ad1e848925.js",
    "revision": "ce5e1b0f0fabdc283b74466fdb880cc7"
  },
  {
    "url": "/_nuxt/pages_time_index.2cf903fb23bbbff39c6f.js",
    "revision": "6263c5040e11cda3cb8b3f199c721a38"
  },
  {
    "url": "/_nuxt/vendor.61a156fdc1551a1ce420.js",
    "revision": "c887e920a6e05b4b8d7f67859a6e4dda"
  }
])


workboxSW.router.registerRoute(new RegExp('/_nuxt/.*'), workboxSW.strategies.cacheFirst({}), 'GET')

workboxSW.router.registerRoute(new RegExp('/.*'), workboxSW.strategies.networkFirst({}), 'GET')

