<template name="basics">
	
	<view>
		<nav-bar home :bgColor="['#f37402','#0f0']" bgColorAngle="90" :backState="1000" fontColor="#000" title="搜索详情页"></nav-bar>
		
		<scroll-view scroll-y class="page" @scrolltolower="loadData">
			<view class="cu-bar search bg-white">
				<view class="search-form round">
					<text class="cuIcon-search"></text>
					<input v-model="keyword" :adjust-position="false" type="text" placeholder="搜索文章" confirm-type="search"></input>
				</view>
				<view class="action">
					<button class="cu-btn bg-green shadow-blur round" @click="clickSearch">搜索</button>
				</view>
			</view>
			<view class="cu-card article">
				<view class="cu-item shadow" v-for="(item, index) in searchData" :key="index" @tap="goInfo(item.uid)">
					<view class="title">
						<view class="text-cut" v-html="item.title"></view>
					</view>
					<view class="content">
						<image v-if="item.photoUrl" :src="item.photoUrl" mode="aspectFill"></image>
						<view class="desc">
							<view class="text-content" v-html="item.summary"></view>
														
							<view style="margin-bottom: 10px;">
								<view class="cu-tag bg-red light sm round">{{item.blogSortName}}</view>
							</view>
		
							<view class="text-gray text-sm">
								<text class="cuIcon-myfill margin-lr-xs"></text> {{item.author}}
								<text class="cuIcon-attentionfill margin-lr-xs"></text> {{item.clickCount}}
								<text class="cuIcon-appreciatefill margin-lr-xs"></text> {{item.collectCount}}
								<text class="cuIcon-messagefill margin-lr-xs"></text> {{item.collectCount}}
							</view>
						</view>
					</view>
				</view>
			</view>
			<view class="loadStyle" v-if="!isEnd && !loading">下拉加载</view>
			<view class="loadStyle" v-if="!isEnd && loading">正在加载中</view>
			<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>		
		</scroll-view>
	</view>	
</template>

<script>
	import {searchBlog} from "../../api/search";
	export default {
		name: "search",
		data() {
			return {
				keyword: "",
				searchData: [],
				total: 0,
				currentPage: 1,
				pageSize: 4,
				isEnd: false, //是否到底底部了
				loading: false, //是否正在加载
			}
		},
		onLoad(option) {
			this.keyword = option.keyword
			this.search();
		},
		methods: {
			loadData: function() {
				console.log("上拉加载数据", this.searchData.length, this.total)
				if(this.searchData.length >= this.total) {
					return;
				}
				this.currentPage = this.currentPage + 1;
				this.search();
			},
			clickSearch() {
				// 点击搜索按钮，清空原来搜索的内容
				this.searchData = []
				this.currentPage = 1
				this.search()
			},
			goInfo(blogUid) {
				console.log("跳转", blogUid)
				uni.navigateTo({
					url: '/pages/info/home?blogUid=' + blogUid,
				});
			},
			search() {
				if(this.keyword == "") {
					uni.showToast({
						icon: "none",
						title: "搜索内容不能为空",
					})
					return;
				}
				let params = {}
				params.currentPage = this.currentPage
				params.pageSize = this.pageSize
				params.keywords = this.keyword;
				searchBlog(params).then(res =>{	
					if(res.code == "success") {
						var newData = this.searchData.concat(res.data.blogList);
						this.searchData = newData;						
						this.total = res.data.total;
						this.currentPage = res.data.currentPage;
						this.pageSize = res.data.pageSize;	
					}
					//全部加载完毕
					if (this.searchData.length >= this.total) {
					  this.isEnd = true;
					} else {
						this.isEnd = false;
					}
					this.loading = false;
					console.log(this.isEnd, this.loading)
				})
			},
		}
	}
</script>


<style scoped>
	.page {
		height: 100vh;
	}
	.loadStyle {
		width: 100%;
		height: 60rpx;
		text-align: center;		
		color: #bfbfbf;
	}
</style>
