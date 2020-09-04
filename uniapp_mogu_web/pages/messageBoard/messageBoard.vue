<template name="basics">
	<view>

		<nav-bar home :bgColor="['#f37402','#0f0']" bgColorAngle="90" :backState="1000" fontColor="#000" title="留言板"></nav-bar>
		
		<scroll-view class="page" scroll-y @scrolltolower="loadData">

			<!--以下是评论内容-->
			<CommentList :comments="comments" @deleteSuccess="deleteSuccess" @commentSuccess="commentSuccess" :source="source"></CommentList>

			<view class="loadStyle" v-if="!isEnd && !loading">下拉加载</view>
			<view class="loadStyle" v-if="!isEnd && loading">正在加载中</view>
			<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
			
		</scroll-view>
	</view>
</template>

<script>
	import CommentList from "../../components/CommentList/index.vue";
	import {getCommentListByApp} from "../../api/comment.js"
	export default {
		name: "messageBoard",
		data() {
			return {
				currentPage: 1,
				pageSize: 10,
				total: 0, //总数量
				comments: [],
				source: "MESSAGE_BOARD", // 评论来源
				isEnd: false, //是否到底底部了
				loading: false, //是否正在加载
			}
		},
		components: {
			CommentList
		},
		created() {
			this.commentList()
		},
		methods: {
			loadData: function() {
				console.log("上拉加载数据", this.comments.length, this.total)
				if(this.comments.length >= this.total) {
					return;
				}
				this.currentPage = this.currentPage + 1;
				this.commentList(this.selectBlogSortUid);
			},
			commentList() {
				var that = this
				let params = {};
				params.source = this.source;
				params.currentPage = that.currentPage
				params.pageSize = that.pageSize;
				this.loading = true;
				getCommentListByApp(params).then(response => {
					if (response.code == this.$ECode.SUCCESS) {
					  this.comments = that.comments.concat(response.data.records);
					  that.currentPage = response.data.current;
					  that.pageSize = response.data.size;
					  that.total = response.data.total;
					}
					//全部加载完毕
					if (this.comments.length >= this.total) {
					  this.isEnd = true;
					} else {
						this.isEnd = false;
					}
					this.loading = false;
				});
			},
			deleteSuccess(commentUid) {
				// 找到被删除的uid，移除
				let comments = this.comments
				this.comments = []
				let newCommentList = []
				for(let a=0; a<comments.length; a++) {
					if(comments[a].uid == commentUid) {
						continue;
					}
					newCommentList.push(comments[a])
				}
				this.comments = newCommentList
			},
			commentSuccess(comment) {
				// 评论成功后，需要更新一下
				let comments = this.comments
				this.comments = []
				let newCommentList = []
				newCommentList.push(comment)
				for(let a=0; a<comments.length; a++) {
					newCommentList.push(comments[a])
				}
				this.comments = newCommentList
			}
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
