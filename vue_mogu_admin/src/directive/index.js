import Vue from 'vue'
import permission from './permission'
import downloadFile from './downloadFile'

const directives = [permission, downloadFile]
directives.forEach(d => Vue.use(d))
