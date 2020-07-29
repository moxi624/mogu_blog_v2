<template name="basics">
	<view>
		<scroll-view scroll-y class="page" @scrolltolower="loadData">

			<view class="cu-bar search bg-white">
				<view class="search-form round">
					<text class="cuIcon-search"></text>
					<input @focus="InputFocus" @blur="InputBlur" :adjust-position="false" type="text" placeholder="搜索文章" confirm-type="search"></input>
				</view>
				<view class="action">
					<button class="cu-btn bg-green shadow-blur round">搜索</button>
				</view>
			</view>
			 
			<swiper class="card-swiper" :class="dotStyle?'square-dot':'round-dot'" :indicator-dots="true" :circular="true"
			 :autoplay="true" interval="5000" duration="500" @change="cardSwiper" indicator-color="#8799a3"
			 indicator-active-color="#0081ff">
				<swiper-item v-for="(item,index) in fristData" :key="index" :class="cardCur==index?'cur':''">
					<view class="swiper-item">
						<image v-if="item.photoList" :src="item.photoList[0]" mode="aspectFill"></image>
						<text class="text-cut" style="font-size: 18px; margin: 0 auto;">{{item.title}}</text>
					</view>
					<view class="cu-bar bg-shadeBottom" style="margin-top: -50px;"> 
						<text class="text-cut" style="font-size: 18px; margin: 0 auto;">{{item.title}}</text>
					</view>
				</swiper-item>
			</swiper>
			 
			<!--文章类卡片-->
			<view class="cu-card case" :class="isCard?'no-card':''">
				<view class="cu-item shadow" v-for="item in newBlogData" :key="item.uid">
					<view class="image" style="height: 370rpx;">
						<image v-if="item.photoList" :src="item.photoList[0]" mode="aspectFit"></image>
						<view class="cu-tag bg-blue">{{item.blogSort.sortName}}</view>
						<view class="cu-bar bg-shadeBottom"> <text class="text-cut" style="font-size: 18px; margin: 0 auto;">{{item.title}}</text></view>
					</view>
					<view class="cu-list menu-avatar">
						<view class="cu-item" style="height: 40px;">						
							<view class="content flex-sub">
								<view class="text-gray text-sm flex justify-between">								
									<view class="text-gray text-sm" v-for="(tag, index) in item.tagList" :key="item.uid + tag.uid" style="margin-left: -100rpx;">										
										<view v-if="index%3==0" class="cu-tag bg-red light sm round">{{tag.content}}</view>
										<view v-if="index%3==1" class="cu-tag bg-green light sm round">{{tag.content}}</view>
										<view v-if="index%3==2" class="cu-tag bg-brown light sm round">{{tag.content}}</view>										
									</view>
									
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
			
			<view class="cu-tabbar-height"></view>
		</scroll-view>
	</view>
</template>

<script>
	import {getNewBlog, getBlogByLevel} from "../../api/index";
	export default {
		name: "basics",
		props:{
			isRefresh:{
				type : String,
				default: "blogHome"
			}
		},
		data() {
			return {
				fristData: [], // 一级推荐
				newBlogData: [], // 最新博客
				isEnd: false, //是否到底底部了
				loading: false, //是否正在加载
				total: 0,
				currentPage: 1,
				pageSize: 20,				
				dotStyle: false,
				isCard: false,
				cardCur: 0,
			};
		},		
		watch:{
			isRefresh:{
				deep: true,
				handler(newValue,oldValue){
					console.log("改变了值")
					if(newValue === 0){
						uni.showToast({
							title: '首页刷新'
						});
						this.$emit("cref",9)
					}
				}
			}
		},
		created() {
			this.getBlogList()
			this.getLevelBlog()
		},
		methods:{
			// cardSwiper
			cardSwiper(e) {
				this.cardCur = e.detail.current
			},
			loadData: function() {		
				console.log("上拉加载数据", this.newBlogData.length, this.total)
				if(this.newBlogData.length >= this.total) {
					return;
				}
				this.currentPage = this.currentPage + 1;
				this.getBlogList();
			},
			getLevelBlog() {
				var that = this
				let params = {}
				params.level = 1;
				params.useSort = 1;
				getBlogByLevel(params).then(res =>{					
					if(res.code == "success") {
						that.fristData = res.data.records;	
					}
				})
			},
			getBlogList() {
				var that = this
				let params = {}
				params.currentPage = that.currentPage;
				params.pageSize = that.pageSize;
				that.loading = true;
				getNewBlog(params).then(res =>{					
					if(res.code == "success") {
						var newData = that.newBlogData.concat(res.data.records);
						that.newBlogData = newData;						
						that.total = res.data.total;
						that.currentPage = res.data.current;
						that.pageSize = res.data.size;											
					}
					//全部加载完毕
					if (that.newBlogData.length >= that.total) {
					  that.isEnd = true;
					} else {
						that.isEnd = false;
					}
					that.loading = false;
					console.log(that.isEnd, that.loading)
				})
			},
		}
	}
</script>

<style>
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
