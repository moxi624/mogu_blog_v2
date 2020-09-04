<template name="sort">
	<view>
		<swiper class="screen-swiper round-dot" :indicator-dots="false" :circular="true" :autoplay="true" interval="5000"
		 duration="500">
			<swiper-item v-for="(item,index) in levelData" :key="index" @tap="goInfo(item.uid)">
				<image v-if="item.photoList[0]" :src="item.photoList[0]" mode="aspectFill"></image>
				<view class="cu-bar bg-shadeBottom" style="margin-top: -50px;" @tap="goInfo(item.uid)">
					<text class="text-cut" style="font-size: 18px; margin: 0 auto;">{{item.title}}</text>
				</view>
			</swiper-item>
		</swiper>
		 
		<scroll-view scroll-x class="bg-white nav" scroll-with-animation :scroll-left="scrollLeft">
			<view class="cu-item" :class="index==TabCur?'text-green cur':''" v-for="(item, index) in activities" :key="index" @tap="tabSelect" :data-id="index">
				{{item.content}}
			</view>
		</scroll-view>
		
		<!--文章类卡片-->

		<scroll-view scroll-y class="page" @scrolltolower="loadData">
			<!--文章类卡片-->
			<view class="cu-card case" :class="isCard?'no-card':''">
				<view class="cu-item shadow" v-for="item in itemByDate" :key="item.uid" @tap="goInfo(item.uid)">
					<view class="image" style="height: 370rpx;">
						<image v-if="item.photoList" :src="item.photoList[0]" mode="aspectFit"></image>
						<view class="cu-tag bg-blue">{{item.blogSort.sortName}}</view>
						<view class="cu-bar bg-shadeBottom"> <text class="text-cut" style="font-size: 18px; margin: 0 auto;">{{item.title}}</text></view>
					</view>
					<view class="cu-list menu-avatar">
						<view class="cu-item" style="height: 40px;">						
							<view class="content flex-sub">
								<view class="text-gray text-sm flex justify-between">
									
									<view class="text-gray text-sm" v-for="(tag, index) in item.tagList" :key="tag.uid" style="margin-left: -100rpx;">										
										<view v-if="index%3==0" class="cu-tag bg-red light sm round">{{tag.content}}</view>
										<view v-if="index%3==1" class="cu-tag bg-green light sm round">{{tag.content}}</view>
										<view v-if="index%3==2" class="cu-tag bg-brown light sm round">{{tag.content}}</view>										
									</view>
									
									<view class="text-gray text-sm">
										<text class="cuIcon-peoplefill margin-lr-xs"></text> {{item.author}}
										<text class="cuIcon-attentionfill margin-lr-xs"></text> {{item.clickCount}}
										<text class="cuIcon-appreciatefill margin-lr-xs"></text> {{item.collectCount}}
										<!-- <text class="cuIcon-messagefill margin-lr-xs"></text> {{item.collectCount}} -->
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
			<view class="cu-tabbar-height"></view>
		</scroll-view>		
	</view>
</template>

<script>
	import {getBlogByLevel} from "../../api/index";
	import {getArticleByTagUid, getTagList} from "../../api/tag.js";
	export default {
		name: "tag",
		props:{
			isRefresh:{
				type : String,
				default: "blogTag"
			}
		},
		data() {
			return {
				TabCur: 0,
				scrollLeft: 0,
				isCard: false,
				selectBlogTagUid: "",
				reverse: false,
				levelData: [], // 三级推荐博客
				activities: [], // 所有标签
				itemByDate: [], // 标签下的博客
			    currentPage: 1,
			    pageSize: 10,
				total: 0,
				isEnd: false, //是否到底底部了
				loading: false, //是否正在加载
			};
		},
		watch:{
			isRefresh:{
				deep: true,
				handler(newValue,oldValue){
					console.log("下拉刷新")
					this.levelData = []
					this.activities = []
					this.itemByDate = []
					this.blogSortList()
					this.getLevelBlog()
				}
			}
		},
		created() {
			this.blogSortList()
			this.getLevelBlog()
		},
		methods: {
			getLevelBlog() {
				var that = this
				let params = {}
				params.level = 3;
				params.useSort = 1;
				getBlogByLevel(params).then(res =>{
					console.log("获取推荐博客", res)
					if(res.code == this.$ECode.SUCCESS) {
						that.levelData = res.data.records;	
					}
				})
			},
			goInfo(blogUid) {
				console.log("跳转", blogUid)
				uni.navigateTo({
					url: '/pages/info/home?blogUid=' + blogUid,
				});
			},
			tabSelect(e) {
				let index = e.currentTarget.dataset.id;
				this.TabCur = index
				this.scrollLeft = (e.currentTarget.dataset.id - 1) * 60
				console.log("开始切换", this.activities[index])
				this.itemByDate = []
				this.getBlogList(this.activities[index].uid);
			},
			loadData: function() {
				console.log("上拉加载数据", this.itemByDate.length, this.total)
				if(this.itemByDate.length >= this.total) {
					return;
				}
				this.currentPage = this.currentPage + 1;
				this.getBlogList(this.selectBlogTagUid);
			},
			blogSortList() {
				var that = this
				let params = {}
				getTagList(params).then(res =>{					
					console.log("返回的博客标签数据", res)
					if(res.code == this.$ECode.SUCCESS) {
						that.activities = res.data;
						that.getBlogList(that.activities[0].uid)
					}
				})
			},
			getBlogList(blogTagUid) {
			  this.selectBlogTagUid = blogTagUid;
			  var params = {};
			  params.currentPage = this.currentPage;
			  params.pageSize = this.pageSize;
			  params.tagUid = blogTagUid
			  this.loading = true;
			  getArticleByTagUid(params).then(response => {
				console.log("通过标签uid获取文章列表", response)
				if (response.code == this.$ECode.SUCCESS) {
				  var newData = this.itemByDate.concat(response.data.records);
				  this.itemByDate = newData;
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
