import {appConfig} from '../config/config.js'

export const token = {
    set: (s) => {
        uni.setStorageSync(appConfig.tokenKey, s)
    },
    get: () => {
        return uni.getStorageSync(appConfig.tokenKey)
    },
    clear: () => {
        uni.removeStorageSync(appConfig.tokenKey)
    }
}