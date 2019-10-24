importScripts('/_nuxt/workbox.476439e0.js')

const workboxSW = new self.WorkboxSW({
  "cacheId": "xc-portal",
  "clientsClaim": true,
  "directoryIndex": "/"
})

workboxSW.precache([
  {
    "url": "/_nuxt/app.ece48b79f62cd9e9f469.js",
    "revision": "3a729e8df258f08b6e240f3df887f0a2"
  },
  {
    "url": "/_nuxt/layouts_default.3822203ae8b2fd0d4694.js",
    "revision": "68b197a6b085472dff0c5fe1a3d900c4"
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
    "url": "/_nuxt/manifest.14b5dbb36c0e94de80a7.js",
    "revision": "24983b23869e0e13092087e8db8d03d2"
  },
  {
    "url": "/_nuxt/pages_about_index.805a37eb72772bc0c76c.js",
    "revision": "b0386e228dcd1d7016f05506a4f38d91"
  },
  {
    "url": "/_nuxt/pages_home_index.006bc1a3d5e2e0cefc04.js",
    "revision": "015ccfc6e9477565774aa13a368150c8"
  },
  {
    "url": "/_nuxt/pages_index.552073d6ef16f188de8c.js",
    "revision": "ea234704fd0bce0c1abc16cc9632ba15"
  },
  {
    "url": "/_nuxt/pages_info__uid.303886a54a13a0572bdd.js",
    "revision": "546da0fed512c07c3effa1a0c8826193"
  },
  {
    "url": "/_nuxt/pages_list__uid.122df4f64305529b28f8.js",
    "revision": "e783855acfd751e9440e47d774e08492"
  },
  {
    "url": "/_nuxt/pages_time_index.baf6d6282550fb4096e1.js",
    "revision": "9be297f78cff861afd18fbc6a1fdb419"
  },
  {
    "url": "/_nuxt/vendor.61a156fdc1551a1ce420.js",
    "revision": "c887e920a6e05b4b8d7f67859a6e4dda"
  }
])


workboxSW.router.registerRoute(new RegExp('/_nuxt/.*'), workboxSW.strategies.cacheFirst({}), 'GET')

workboxSW.router.registerRoute(new RegExp('/.*'), workboxSW.strategies.networkFirst({}), 'GET')

