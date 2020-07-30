<template name="sort">
	<view>
		<image src="https://ossweb-img.qq.com/images/lol/web201310/skin/big84000.jpg"
		 mode="widthFix" class="response"></image>
		<scroll-view scroll-x class="bg-white nav" scroll-with-animation :scroll-left="scrollLeft">
			<view class="cu-item" :class="index==TabCur?'text-green cur':''" v-for="(item, index) in activities" :key="index" @tap="tabSelect" :data-id="index">
				{{item.sortName}}
			</view>
		</scroll-view>
		
		<!--文章类卡片-->

		<scroll-view scroll-y class="page" @scrolltolower="loadData">
			<!--文章类卡片-->
			<view class="cu-card case" :class="isCard?'no-card':''">
				<view class="cu-item shadow" v-for="item in itemByDate" :key="item.uid">
					<view class="image" style="height: 370rpx;">
						<image v-if="item.photoList" :src="item.photoList[0]" mode="aspectFit"></image>
						<view class="cu-tag bg-blue">{{item.blogSort.sortName}}</view>
						<view class="cu-bar bg-shadeBottom"> <text class="text-cut" style="font-size: 18px; margin: 0 auto;">{{item.title}}</text></view>
					</view>
					<view class="cu-list menu-avatar">
						<view class="cu-item" style="height: 40px;">						
							<view class="content flex-sub">
								<view class="text-gray text-sm flex justify-between">								
<!-- 									<view class="text-gray text-sm" v-for="(tag, index) in item.tagList" :key="item.uid + tag.uid" style="margin-left: -100rpx;">										
										<view v-if="index%3==0" class="cu-tag bg-red light sm round">{{tag.content}}</view>
										<view v-if="index%3==1" class="cu-tag bg-green light sm round">{{tag.content}}</view>
										<view v-if="index%3==2" class="cu-tag bg-brown light sm round">{{tag.content}}</view>										
									</view> -->
									
									<view class="text-gray text-sm">
										<text class="cuIcon-attentionfill margin-lr-xs"></text> {{item.clickCount}}
										<text class="cuIcon-appreciatefill margin-lr-xs"></text> {{item.collectCount}}
										<text class="cuIcon-messagefill margin-lr-xs"></text> {{item.collectCount}}
									</view>	
		
								</view>
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
	import {getArticleByBlogSortUid, getBlogSortList} from "../../api/classify.js";
	export default {
		name: "sort",
		data() {
			return {
				sortList: ["后端开发","学习笔记","技术新闻","慢生活","机器学习","软件推荐"],
				TabCur: 0,
				scrollLeft: 0,
				isCard: false,
				selectBlogSortUid: "",
				reverse: false,
				activities: [], // 所有分类
				itemByDate: [], // 分类下的博客
			    currentPage: 1,
			    pageSize: 10,
				total: 0,
				isEnd: false, //是否到底底部了
				loading: false, //是否正在加载
			};
		},
		onShow() {
			console.log("success")
		},
		created() {
			this.blogSortList()
		},
		methods: {
			tabSelect(e) {
				let index = e.currentTarget.dataset.id;
				this.TabCur = index
				this.scrollLeft = (e.currentTarget.dataset.id - 1) * 60
				console.log("开始切换", this.activities[index])
				this.getBlogList(this.activities[index].uid);
			},
			loadData: function() {
				console.log("上拉加载数据", this.newBlogData.length, this.total)
				if(this.newBlogData.length >= this.total) {
					return;
				}
				this.currentPage = this.currentPage + 1;
				this.getBlogList(this.selectBlogSortUid);
			},
			blogSortList() {
				var that = this
				let params = {}
				getBlogSortList(params).then(res =>{					
					console.log("返回的博客分类数据", res)
					if(res.code == "success") {
						that.activities = res.data;
						that.getBlogList(that.activities[0].uid)
					}
				})
			},
			getBlogList(blogSortUid) {
			  this.selectBlogSortUid = blogSortUid;
			  var params = {};
			  params.blogSortUid = blogSortUid
			  this.loading = true;
			  getArticleByBlogSortUid(params).then(response => {
				console.log("通过分类uid获取文章列表", response)
				if (response.code == "success") {
				  this.itemByDate = response.data.records;
				  this.currentPage = response.data.current;
				  this.pageSize = response.data.size;
				  this.total = response.data.total;
				}
				//全部加载完毕
				if (this.itemByDate.length >= this.total) {
				  this.isEnd = true;
				} else {
					this.isEnd = false;
				}
				this.loading = false;
			  });
			},
		}
	}
</script>

<style>
	.page {
		height: 100vh;
	}
	.loadStyle {
		margin-top: 20rpx;
		width: 100%;
		height: 60rpx;
		text-align: center;		
		color: #bfbfbf;
	}
</style>
