<template name="basics">
	<view>

		<view class="fixed">
			<cu-custom :isBack="true" bgColor="bg-shadeTop text-white">
				<block slot="content" >搜索详情页</block>
			</cu-custom>
		</view>
		
		<view class="cu-bar search bg-white">
			<view class="search-form round">
				<text class="cuIcon-search"></text>
				<input v-model="keyword" :adjust-position="false" type="text" placeholder="搜索文章" confirm-type="search"></input>
			</view>
			<view class="action">
				<button class="cu-btn bg-green shadow-blur round" @click="search">搜索</button>
			</view>
		</view>
		
		<scroll-view scroll-y>
			<view class="cu-card article">
				<view class="cu-item shadow" v-for="item in searchData">
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

			<view class="cu-tabbar-height"></view>
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
			}
		},
		onLoad(option) {
			this.keyword = option.keyword
			console.log("传递过来的关键字：", option.keyword)
			this.search();
		},
		methods: {
			search() {
				console.log("搜索博客");
				if(this.keyword == "") {
					uni.showToast({
						icon: "none",
						title: "搜索内容不能为空",
					})
					return;
				}
				let params = {}
				params.keywords = this.keyword;
				searchBlog(params).then(res =>{	
					console.log("查询的数据", res)
					if(res.code == "success") {
						this.searchData = res.data.blogList;	
					}
				})
			},
		}
	}
</script>


<style>

</style>
