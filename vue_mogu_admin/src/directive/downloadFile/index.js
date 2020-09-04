import download from './download'

const install = function(Vue) {
  Vue.directive('download', download)
}

if (window.Vue) {
  window['download'] = download
  Vue.use(install); // eslint-disable-line
}

download.install = install
export default download
