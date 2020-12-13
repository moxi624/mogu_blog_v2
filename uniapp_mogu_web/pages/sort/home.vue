<template>
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
		
		<view class="VerticalBox">
			<scroll-view class="VerticalNav nav" scroll-y scroll-with-animation :scroll-top="verticalNavTop" style="height:calc(100vh - 375upx)">
				<view class="cu-item" :class="index==tabCur?'text-green cur':''" v-for="(item,index) in list" :key="index" @tap="TabSelect" :data-id="index">
					{{item}}
				</view>
			</scroll-view>
			
			<scroll-view class="VerticalMain" scroll-y scroll-with-animation style="height:calc(100vh - 375upx)">
				<view class="padding-top padding-lr">
					<view class="cu-bar solid-bottom bg-white">
						<view class="action">
							<text class="cuIcon-title text-green"></text> {{selectContent}} </view>
					</view>
					<view class="cu-list menu-avatar">
						<view class="cu-item cur" v-for="(item,index) in itemByDate" :key="index" @tap="goInfo(item.uid)">
							
							<image class="cu-avatar radius lg" v-if="item.photoList"  :src="item.photoList[0]"></image>

							<view class="content">
								<view>
									<text class="text-cut text-gray" style="font-size: 14px;">{{item.title}}</text>
								</view>
								
								<view>
									<view class='cu-tag line-blue sm' style="margin-bottom: 5px;">{{item.blogSort.sortName}}</view>
								</view>

								<view class="text-gray text-sm flex" v-for="(tag, index) in item.tagList" v-if="tag.uid" :key="tag.uid" style="float: left;">
									<view v-if="index%3==0" class="cu-tag bg-red light sm round">{{tag.content}}</view>
									<view v-if="index%3==1" class="cu-tag bg-green light sm round">{{tag.content}}</view>
									<view v-if="index%3==2" class="cu-tag bg-brown light sm round">{{tag.content}}</view>
								</view>
							</view>
							
							<view class="action">
								<view style="font-size: 12px;">
									<text class="cuIcon-attentionfill text-grey margin-lr-xs"></text> <span class="text-grey">{{item.clickCount}}</span>
								</view>
								<view style="font-size: 12px;">
									<text class="cuIcon-appreciatefill text-grey margin-lr-xs"></text> <span class="text-grey">{{item.collectCount}}</span>
								</view>
							</view>
						</view>
					</view>
				</view>
				<view class="cu-tabbar-height"></view>
			</scroll-view>
		</view>
	</view>
</template>

<script>
	import {getBlogByLevel} from "../../api/index";
	import {getArticleByMonth, getSortList} from "../../api/sort.js";
	export default {
		props:{
			isRefresh:{
				type : String,
				default: "blogSort"
			}
		},
		data() {
			return {
				selectContent: "",
				list: [],
				itemByDate: [],
				levelData: [], // 四级推荐博客
				tabCur: 0,
				mainCur: 0,
				verticalNavTop: 0,
				load: true
			};
		},
		watch:{
			isRefresh:{
				deep: true,
				handler(newValue,oldValue){
					console.log("下拉刷新")
					this.levelData = []
					this.list = []
					this.itemByDate = []
					this.sortList()
					this.getLevelBlog()
				}
			}
		},
		created() {
			this.sortList()
			this.getLevelBlog()
		},
		methods: {
			getLevelBlog() {
				var that = this
				let params = {}
				params.level = 4;
				params.useSort = 1;
				getBlogByLevel(params).then(res =>{					
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
			sortList() {
				var that = this
				let params = {}
				getSortList(params).then(res =>{					
					console.log("返回的博客标签数据", res)
					if(res.code == this.$ECode.SUCCESS) {
						let list = res.data;
						let newList = []
						for(let a = list.length -1; a >= 0 ; a--) {
							newList.push(list[a])
						}
						that.list = newList
						that.clickTime(that.list[0])
					}
				})
			},
			clickTime(content) {
			  this.selectContent = content;
			  let params = {}
			  params.monthDate = content
			  getArticleByMonth(params).then(response => {
				if (response.code == this.$ECode.SUCCESS) {
					this.itemByDate = response.data;
				}
			  });
			},
			TabSelect(e) {
				console.log("选中的", e.currentTarget.dataset.id)
				this.tabCur = e.currentTarget.dataset.id;
				this.clickTime(this.list[this.tabCur])
			},
		},
	}
</script>

<style scoped>
	.cu-item .cu-avatar {
		width: 60px;
		height: 52px;
	}
	.cu-list.menu-avatar>.cu-item .content {
		width: calc(100% - 52px - 33px - 35px);
		position: absolute;
		left: 158upx;
		
	}
	.fixed {
		position: fixed;
		z-index: 99;
	}

	.VerticalNav.nav {
		width: 200upx;
		white-space: initial;
	}

	.VerticalNav.nav .cu-item {
		width: 100%;
		text-align: center;
		background-color: #fff;
		margin: 0;
		border: none;
		height: 50px;
		position: relative;
	}

	.VerticalNav.nav .cu-item.cur {
		background-color: #f1f1f1;
	}

	.VerticalNav.nav .cu-item.cur::after {
		content: "";
		width: 8upx;
		height: 30upx;
		border-radius: 10upx 0 0 10upx;
		position: absolute;
		background-color: currentColor;
		top: 0;
		right: 0upx;
		bottom: 0;
		margin: auto;
	}

	.VerticalBox {
		display: flex;
	}

	.VerticalMain {
		background-color: #f1f1f1;
		flex: 1;
	}
</style>
