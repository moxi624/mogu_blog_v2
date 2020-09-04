<template>
	<view>
		<blogHome :isRefresh='isRefresh' v-if="PageCur=='blogHome'"></blogHome>
		<blogSort :isRefresh='isRefresh' v-if="PageCur=='blogSort'"></blogSort>
		<blogTag :isRefresh='isRefresh' v-if="PageCur=='blogTag'"></blogTag>
		<blogClassify :isRefresh='isRefresh' v-if="PageCur=='blogClassify'"></blogClassify>
		<myCenter :isRefresh='isRefresh' v-if="PageCur=='myCenter'"></myCenter>
		<view class="cu-bar tabbar bg-white shadow foot">
			
			<view class="action" @click="NavChange" data-cur="blogHome">
				<view class='cuIcon-cu-image'>
					<image :src="'/static/tabbar/home' + [PageCur=='blogHome'?'_cur':''] + '.png'"></image>
				</view>
				<view :class="PageCur=='blogHome'?'text-blue':'text-gray'">首页</view>
			</view>
			
			<view class="action" @click="NavChange" data-cur="blogClassify">
				<view class='cuIcon-cu-image'>
					<image :src="'/static/tabbar/classify' + [PageCur == 'blogClassify'?'_cur':''] + '.png'"></image>
				</view>
				<view :class="PageCur=='blogClassify'?'text-blue':'text-gray'">分类</view>				
			</view>
			<view class="action" @click="NavChange" data-cur="blogTag">
				<view class='cuIcon-cu-image'>
					<image :src="'/static/tabbar/tag' + [PageCur == 'blogTag'?'_cur':''] + '.png'"></image>
				</view>
				<view :class="PageCur=='blogTag'?'text-blue':'text-gray'">标签</view>
			</view>
			
			<view class="action" @click="NavChange" data-cur="blogSort">
				<view class='cuIcon-cu-image'>
					<image :src="'/static/tabbar/sort' + [PageCur == 'blogSort'?'_cur':''] + '.png'"></image>
				</view>
				<view :class="PageCur=='blogSort'?'text-blue':'text-gray'">归档</view>
			</view>

			<view class="action" @click="NavChange" data-cur="myCenter">
				<view class='cuIcon-cu-image'>
					<image :src="'/static/tabbar/about' + [PageCur == 'myCenter'?'_cur':''] + '.png'"></image>
				</view>
				<view :class="PageCur=='myCenter'?'text-blue':'text-gray'">我的</view>
			</view>
						
		</view>
	</view>
</template>


<script>
	import {getWebConfig} from "../../api/about.js";
	export default {
		data() {
			return {
				isRefresh: "",
				PageCur: 'blogHome'
			}
		},
		onLoad(options) {
			console.log("传递到主页的内容", options)
			if(options.PageCur) {
				this.PageCur = options.PageCur
			}
		},
		onShow: function() {
			console.log('显示APP')
		},
		onHide: function() {
			console.log('隐藏App')
		},
		onPullDownRefresh() {
			this.isRefresh =  (new Date()).getTime().toString();
			setTimeout(function () {
				uni.stopPullDownRefresh()
			}, 500);
		},
		onShareAppMessage(res) {
			if (res.from === 'button') {// 来自页面内分享按钮
			  console.log(res.target)
			}
			return {
			  title: '蘑菇博客',
			  path: '/pages/index/index?PageCur=blogHome'
			}
		},
		created() {
			// this.getWebConfigData();
		},
		methods: {
			NavChange: function(e) {
				this.PageCur = e.currentTarget.dataset.cur
				console.log("跳转", this.PageCur)
			},
			// getWebConfigData() {
			// 	var that = this
			// 	let params = {}
			// 	getWebConfig(params).then(res =>{
			// 		console.log("获取网站配置", res)
			// 		if(res.code == "success") {
			// 			uni.setStorageSync("webConfig", res.data)
			// 		}
			// 	})
			// },
		}
	}
</script>

<style>

</style>
