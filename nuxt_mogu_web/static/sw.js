importScripts('/_nuxt/workbox.476439e0.js')

const workboxSW = new self.WorkboxSW({
  "cacheId": "xc-portal",
  "clientsClaim": true,
  "directoryIndex": "/"
})

workboxSW.precache([
  {
    "url": "/_nuxt/app.3e295b65bace35777c87.js",
    "revision": "fd0c047e9a84fae1e72456c4e478a26d"
  },
  {
    "url": "/_nuxt/layouts_default.91f85dbd840edecea28f.js",
    "revision": "d1c47e21738cd976d05038673ce86029"
  },
  {
    "url": "/_nuxt/layouts_default1.6c52e070f06005de8008.js",
    "revision": "ab05eef8e527024335ecf8ea52fa39de"
  },
  {
    "url": "/_nuxt/layouts_empty-page.5c23f1782142a97e043e.js",
    "revision": "a58701a22606860391ed107f78fca163"
  },
  {
    "url": "/_nuxt/manifest.c926f095ba614f17b48d.js",
    "revision": "330e1d5bbcfc74ef11419cb19dea0142"
  },
  {
    "url": "/_nuxt/pages_about.5755cfeca05f5c0b13a2.js",
    "revision": "f5a6cafd8a2856cb0a1fb9fa911c8694"
  },
  {
    "url": "/_nuxt/pages_course_search_index.24379fb6cdf90091c07b.js",
    "revision": "1e7df8d6bc9b5b8845a081819ce9c399"
  },
  {
    "url": "/_nuxt/vendor.6693671584ac1c8fa551.js",
    "revision": "36077a521c668c878f575efad1861c3f"
  }
])


workboxSW.router.registerRoute(new RegExp('/_nuxt/.*'), workboxSW.strategies.cacheFirst({}), 'GET')

workboxSW.router.registerRoute(new RegExp('/.*'), workboxSW.strategies.networkFirst({}), 'GET')

