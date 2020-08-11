<template name="basics">
	<view>

		<nav-bar home :bgColor="['#f37402','#0f0']" bgColorAngle="90" :backState="1000" fontColor="#000" title="我的回复"></nav-bar>
		
		<scroll-view scroll-y class="page" @scrolltolower="loadData">
			<view class="cu-list menu-avatar comment solids-top">
				<view class="cu-item" v-for="item in commentList" :key="item.uid">
					<image class="cu-avatar radius lg" v-if="item.user.photoUrl"  :src="item.user.photoUrl"></image>
					<view class="content">
						<view class="justify-between">
							<view class="text-grey">{{item.user.nickName}}</view>
							<view class="text-gray text-content text-df">
								<view class="cu-tag bg-blue light sm round" @tap="goSource(item)">{{item.sourceName}}</view>
							</view>
						</view>
						
						<view class="text-gray text-content text-df" style="margin-top: 10rpx;" v-html="item.content"></view>
						
						<view class="margin-top-sm flex justify-between">
							<view class="text-gray text-df">{{timeFormat(item.createTime)}}</view>
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
	import {timeAgo} from "../../utils/webUtils.js";
	import {getCommentListByUser} from "../../api/comment.js";
	export default {
		name: "myReply",
		data() {
			return {
				commentList: [], // 评论列表
				isEnd: false, //是否到底底部了
				loading: false, //是否正在加载
				total: 0,
				currentPage: 1,
				pageSize: 20,	
			}
		},
		onLoad() {

		},
		created() {
			this.getCommentList()
		},
		methods: {
			timeFormat(time) {
				return timeAgo(time)
			},
			loadData() {
				console.log("上拉加载数据", this.commentList.length, this.total)
				if(this.commentList.length >= this.total) {
					return;
				}
				this.currentPage = this.currentPage + 1;
				this.getBlogList();
			},
			 // 跳转到资源详情
			goSource: function(comment) {
				let source = comment.source
				switch(source) {
				  case "MESSAGE_BOARD": {
					// 跳转到留言板
					uni.navigateTo({
						url: '/pages/info/home?blogUid=' + comment.blogUid,
					});
				  };break;
				  case "BLOG_INFO": {
					// 跳转到博客详情
					uni.navigateTo({
						url: '/pages/info/home?blogUid=' + comment.blogUid,
					});
				  };break;
				  case "ABOUT": {
					// 跳转到关于我
					uni.navigateTo({
						url: '/pages/info/home?blogUid=' + comment.blogUid,
					});
				  };break;
				}
			 },
			// 获取点赞列表
			getCommentList: function() {
				let params = {}
				params.pageSize = this.pageSize;
				params.currentPage = this.currentPage;
				getCommentListByUser(params).then(response => {
				  console.log("获取评论列表", response)
				  if(response.code == this.$ECode.SUCCESS) {
					this.commentList = response.data.replyList;
				  }
				  //全部加载完毕
				  if (this.commentList.length >= this.total) {
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
	.cu-avatar {
		width: 40px;
		height: 40px;
	}
	.loadStyle {
		width: 100%;
		margin-top: 10px;
		height: 60rpx;
		text-align: center;		
		color: #bfbfbf;
	}
</style>
