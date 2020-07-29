<template name="sort">
	<view>
		<image src="https://ossweb-img.qq.com/images/lol/web201310/skin/big39000.jpg"
		 mode="widthFix" class="response"></image>
		<scroll-view scroll-x class="bg-white nav" scroll-with-animation :scroll-left="scrollLeft">
			<view class="cu-item" :class="index==TabCur?'text-green cur':''" v-for="(item,index) in 10" :key="index" @tap="tabSelect" :data-id="index">
				Tab{{index}}
			</view>
		</scroll-view>
		
		<!--文章类卡片-->
		<view class="cu-card article" :class="isCard?'no-card':''">
			
			<view class="cu-item shadow">
				<view class="title"><view class="text-cut">测试博客1</view></view>
				<view class="content">
					<image src="https://ossweb-img.qq.com/images/lol/web201310/skin/big10006.jpg"
					 mode="aspectFill"></image>
					<view class="desc">
						<view class="text-content"> 测试简介！</view>	
						<view class="text-gray text-sm flex justify-between">	
							<view class="text-gray text-sm">
								<text class="cuIcon-attentionfill margin-lr-xs"></text> 10
								<text class="cuIcon-appreciatefill margin-lr-xs"></text> 20
								<text class="cuIcon-messagefill margin-lr-xs"></text> 30
							</view>
						</view>
						
						<view>
							<view class="cu-tag bg-red light sm round">正义天使</view>
							<view class="cu-tag bg-green light sm round">史诗</view>
						</view>
					</view>
				</view>
			</view>
			
			<view class="cu-item shadow">
				<view class="title"><view class="text-cut">测试博客1</view></view>
				<view class="content">
					<image src="https://ossweb-img.qq.com/images/lol/web201310/skin/big10006.jpg"
					 mode="aspectFill"></image>
					<view class="desc">
						<view class="text-content"> 测试简介！</view>	
						<view class="text-gray text-sm flex justify-between">	
							<view class="text-gray text-sm">
								<text class="cuIcon-attentionfill margin-lr-xs"></text> 10
								<text class="cuIcon-appreciatefill margin-lr-xs"></text> 20
								<text class="cuIcon-messagefill margin-lr-xs"></text> 30
							</view>
						</view>
						
						<view>
							<view class="cu-tag bg-red light sm round">正义天使</view>
							<view class="cu-tag bg-green light sm round">史诗</view>
						</view>
					</view>
				</view>
			</view>
					
		</view>
		
		
	</view>
</template>

<script>
	export default {
		name: "sort",
		data() {
			return {
				TabCur: 0,
				scrollLeft: 0
			};
		},
		onShow() {
			console.log("success")
		},
		methods: {
			tabSelect(e) {
				this.TabCur = e.currentTarget.dataset.id;
				this.scrollLeft = (e.currentTarget.dataset.id - 1) * 60
				console.log("开始切换")
			}
		}
	}
</script>

<style>
	.page {
		height: 100vh;
	}
</style>
