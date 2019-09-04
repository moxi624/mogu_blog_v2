importScripts('/_nuxt/workbox.476439e0.js')

const workboxSW = new self.WorkboxSW({
  "cacheId": "xc-portal",
  "clientsClaim": true,
  "directoryIndex": "/"
})

workboxSW.precache([
  {
    "url": "/_nuxt/app.c55b5c2b739008798721.js",
    "revision": "ca7cfdd0ad46c482ff9b45a4077351f9"
  },
  {
    "url": "/_nuxt/layouts_default.bac92f2457933bf4f380.js",
    "revision": "ac7d26a619fb9051ca1647c69713eddb"
  },
  {
    "url": "/_nuxt/layouts_default1.0f9dcd1094ad37090da2.js",
    "revision": "d4c301d14bea0a596e1a8b943ff55178"
  },
  {
    "url": "/_nuxt/layouts_empty-page.6467f2b4bdec0ddefda2.js",
    "revision": "262143c2957315ba39bd671af9d61ccb"
  },
  {
    "url": "/_nuxt/manifest.14e869679ecea99515d7.js",
    "revision": "b04ee6dbc24152790cb7185533353a28"
  },
  {
    "url": "/_nuxt/pages_about_index.a599c1bb4b6484a9b70f.js",
    "revision": "12c2a0ec63f47693afd26ae008834598"
  },
  {
    "url": "/_nuxt/pages_course_search_index.6e9e2ebb561efeb8dd56.js",
    "revision": "30137e213a29b1c6b19787252d6fab21"
  },
  {
    "url": "/_nuxt/pages_home_index.c5696a0301d355db8c4e.js",
    "revision": "98c7b09380cd34113b437e74e828553c"
  },
  {
    "url": "/_nuxt/pages_index.37f2c7bd7631ebe78eec.js",
    "revision": "661e956992596e0b54209689c1ba24ae"
  },
  {
    "url": "/_nuxt/pages_info__uid.612595a754d644e49c18.js",
    "revision": "76b6a8693164f190bd3afc08c8c44747"
  },
  {
    "url": "/_nuxt/pages_list__uid.98bb898a34ad1e848925.js",
    "revision": "ce5e1b0f0fabdc283b74466fdb880cc7"
  },
  {
    "url": "/_nuxt/pages_time_index.e2141d2257a0ce624ebd.js",
    "revision": "895ddaf4026084bd70c8fbc343b4e7fa"
  },
  {
    "url": "/_nuxt/vendor.c4c93bd8f68ce049e84c.js",
    "revision": "870f4403436cc3501d3db20ed8e797cc"
  }
])


workboxSW.router.registerRoute(new RegExp('/_nuxt/.*'), workboxSW.strategies.cacheFirst({}), 'GET')

workboxSW.router.registerRoute(new RegExp('/.*'), workboxSW.strategies.networkFirst({}), 'GET')

