<template>
	<view>
		<blogHome :isRefresh='isRefresh' v-if="PageCur=='blogHome'"></blogHome>
		<blogSort v-if="PageCur=='blogSort'"></blogSort>
		<blogTag v-if="PageCur=='blogTag'"></blogTag>
		<blogClassify v-if="PageCur=='blogClassify'"></blogClassify>
		<myCenter v-if="PageCur=='myCenter'"></myCenter>
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
	export default {
		data() {
			return {
				isRefresh: "",
				PageCur: 'blogHome'
			}
		},
		onShow: function() {
			console.log('显示APP')
		},
		onHide: function() {
			console.log('隐藏App')
		},
		onPullDownRefresh() {
			this.isRefresh = this.PageCur
			setTimeout(function () {
				uni.stopPullDownRefresh()
			}, 1000);
		},
		methods: {
			NavChange: function(e) {
				this.PageCur = e.currentTarget.dataset.cur
				console.log("跳转", this.PageCur)
			}
		}
	}
</script>

<style>

</style>
