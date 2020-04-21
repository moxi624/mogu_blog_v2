import Vue from 'vue'
import permission from './permission'

const directives = [permission]
directives.forEach(d => Vue.use(d))
