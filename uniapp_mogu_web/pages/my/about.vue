<template name="basics">
	<view>
	
		<nav-bar home :bgColor="['#f37402','#0f0']" bgColorAngle="90" :backState="1000" fontColor="#000" title="关于我们"></nav-bar>

		<scroll-view scroll-y class="DrawerPage page" @scrolltolower="loadData">
			
			<view class="box">
				<view class="cu-bar">
					<view class="action border-title">
						<text class="text-xl text-bold text-blue">联系方式</text>
						<text class="bg-gradual-blue" style="width:3rem"></text>
					</view>
				</view>
			</view>
			
			<view>
				<view class="action" style="margin-top: 10px;" v-if="contact.qqGroup">
					<text class="text-black" style="font-size: 16px; margin-left: 15px; margin-right: 5px;">QQ群:</text> <text class="text-gray">{{contact.qqGroup}}</text>
				</view>
				<view class="action" style="margin-top: 10px;" v-if="contact.qqNumber">
					<text class="text-black" style="font-size: 16px; margin-left: 15px; margin-right: 5px;">QQ:</text> <text class="text-gray">{{contact.qqNumber}}</text>
				</view>
				<view class="action" style="margin-top: 10px;" v-if="contact.email">
					<text class="text-black" style=" font-size: 16px; margin-left: 15px; margin-right: 5px;">Email:</text> <text class="text-gray">{{contact.email}}</text>
				</view>
				<view class="action" style="margin-top: 10px;" v-if="contact.github">
					<text class="text-black" style="font-size: 16px; margin-left: 15px; margin-right: 5px;">Github:</text> <text class="text-gray">{{contact.github}}</text>
				</view> 
				<view class="action" style="margin-top: 10px;" v-if="contact.gitee">
					<text class="text-black" style="font-size: 16px; margin-left: 15px; margin-right: 5px;">Gitee:</text> <text class="text-gray">{{contact.gitee}}</text>
				</view>
			</view>
			
			<view class="box">
				<view class="cu-bar">
					<view class="action border-title">
						<text class="text-xl text-bold text-blue">关于我</text>
						<text class="bg-gradual-blue" style="width:3rem"></text>
					</view>
				</view>
			</view>
			
			<jyf-parser class="ck-content margin-sm " :html="userInfo.personResume"></jyf-parser>
			
			<view class="box" v-if="openMobileComment == '1'">
				<view class="cu-bar">
					<view class="action border-title">
						<text class="text-xl text-bold text-blue">留言</text>
						<text class="bg-gradual-blue" style="width:3rem"></text>
					</view>
				</view>
			</view>
			
			<!--以下是评论内容-->
			<CommentList v-if="openMobileComment == '1'" :comments="comments" @deleteSuccess="deleteSuccess" @commentSuccess="commentSuccess" :source="source"></CommentList>

			<view class="loadStyle" v-if="!isEnd && !loading">下拉加载</view>
			<view class="loadStyle" v-if="!isEnd && loading">正在加载中</view>
			<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
			<view class="cu-tabbar-height"></view>
		</scroll-view>
	</view>
</template>

<script>
	import CommentList from "../../components/CommentList/index.vue";
	import jyfParser from "../../components/jyf-parser/jyf-parser";
	import {getMe, getWebConfig} from "../../api/about.js";
	import {getCommentListByApp} from "../../api/comment.js"
	export default {
		name: "about",
		data() {
			return {
				userInfo: {}, // 关于我
				contact: {}, // 联系方式
				currentPage: 1,
				pageSize: 10,
				total: 0, //总数量
				comments: [],
				source: "ABOUT", // 评论来源
				isEnd: false, //是否到底底部了
				loading: false, //是否正在加载
				openMobileComment: "0", // 是否开启移动端评论，（1：是，0：否）
				openMobileAdmiration: "0", // 是否开启移动端赞赏，（1：是，0：否）
			}
		},
		components: {
			jyfParser,
			CommentList
		},
		onLoad(option) {

		},
		created() {
			this.getMeInfo()
			this.getContactData()
			this.commentList()
		},
		methods: {
			getMeInfo() {
				var that = this
				let params = {}
				getMe(params).then(res =>{
					console.log(res)
					if(res.code == this.$ECode.SUCCESS) {
						that.userInfo = res.data;
						this.openMobileComment = res.data.openMobileComment
						this.openMobileAdmiration = res.data.openMobileAdmiration
					}
				})
			},
			getContactData() {
				var that = this
				let params = {}
				getWebConfig(params).then(res =>{
					console.log("获取联系方式", res)
					if(res.code == this.$ECode.SUCCESS) {
						that.contact = res.data;	
					}
				})
			},
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
				that.loading = true;
				getCommentListByApp(params).then(response => {
					console.log("得到的评论列表", response)
					if (response.code == this.$ECode.SUCCESS) {
					  that.comments = that.comments.concat(response.data.records);
					  that.currentPage = response.data.current;
					  that.pageSize = response.data.size;
					  that.total = response.data.total;
					}
					//全部加载完毕
					if (that.comments.length >= that.total) {
					  that.isEnd = true;
					} else {
						that.isEnd = false;
					}
					that.loading = false;
				});
			},
			deleteSuccess(commentUid) {
				// 找到被删除的uid，移除
				console.log("找到被移除的uid", commentUid)
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
/* @import '../../static/css/ckeditor.css'; */

/* @import '../../static/iconfont/iconfont.css'; */

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

image[class*="gif-"] {
    /* border-radius: 6rpx; */
    display: block;
}

.gif-wave{
    /* position: absolute; */
    width: 100%;
    bottom: -2rpx;
    left: 0;
    z-index: 99;
    mix-blend-mode: screen;
    height: 100rpx;
}

page {
    background-image: var(--gradualShadow);
    width: 100vw;
    overflow: hidden;
}

.DrawerPage {
    position: fixed;
    width: 100vw;
    height: 100vh;
    left: 0vw;
    background-color: #f1f1f1;
    transition: all 0.4s;
}

.DrawerPage.show {
    transform: scale(0.9, 0.9);
    left: 85vw;
    box-shadow: 0 0 60rpx rgba(0, 0, 0, 0.2);
    transform-origin: 0;
}

.DrawerPage .cu-bar.tabbar .action button.icon {
    width: 64rpx;
    height: 64rpx;
    line-height: 64rpx;
    margin: 0;
    display: inline-block;
}

.DrawerPage .cu-bar.tabbar .action .cu-avatar {
    margin: 0;
}

.DrawerPage .nav {
    flex: 1;
}

.DrawerPage .nav .cu-item.cur {
    border-bottom: 0;
    position: relative;
}

.DrawerPage .nav .cu-item.cur::after {
    content: "";
    width: 10rpx;
    height: 10rpx;
    background-color: currentColor;
    position: absolute;
    bottom: 10rpx;
    border-radius: 10rpx;
    left: 0;
    right: 0;
    margin: auto;
}

.DrawerPage .cu-bar.tabbar .action {
    flex: initial;
}
</style>
