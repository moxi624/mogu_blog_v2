<template name="basics">
	<view>

		<!-- <cu-custom bgColor="bg-gradual-blue" :isBack="true" style="height: 45px;"><block slot="backText">返回</block><block slot="content">{{blogData.title}}</block></cu-custom> -->
		<nav-bar home :bgColor="['#f37402','#0f0']" bgColorAngle="90" :backState="1000" fontColor="#000" :title="blogData.title"></nav-bar>

		<scroll-view scroll-y class="DrawerPage page" @scrolltolower="loadData">
		    <view class="cf">
		        <view class="margin-sm">
					<view class="cu-capsule round">
					    <view class="cu-tag bg-blue sm">
					        <text class="cuIcon-peoplefill"></text>
					    </view>
					    <view class="cu-tag line-blue sm">
					    {{blogData.author}}
					    </view>
					</view>
					
					<view class="cu-capsule round">
					    <view class="cu-tag bg-mauve sm">
					        <text class="cuIcon-file"></text>
					    </view>
					    <view class="cu-tag line-mauve sm" v-if="blogData.isOriginal == 1">
					    原创
					    </view>
						<view class="cu-tag line-blue sm" v-else>
						转载
						</view>
					</view>
					
					<view class="cu-capsule round">
					    <view class="cu-tag bg-orange sm">
					        <text class="cuIcon-attentionfill"></text>
					    </view>
					    <view class="cu-tag line-orange sm">
					    {{blogData.clickCount}}
					    </view>
					</view>
					
		            <view class="cu-capsule round">
		                <view class="cu-tag bg-red sm">
		                    <text class="cuIcon-appreciatefill"></text>
		                </view>
		                <view class="cu-tag line-red sm">
		                {{blogData.collectCount}}
		                </view>
		            </view>

		            <view class="cu-capsule round">
		                <view class="cu-tag  bg-blue sm">
		                    <text class="cuIcon-timefill"></text>
		                </view>
		                <view class="cu-tag line-gray sm">
		                {{blogData.createTime}}
		                </view>
		            </view>
		            <text class="cu-capsule">&nbsp;</text>
		        </view>
		    </view>
			
			<view class="text-gray text-sm flex justify-start">
				<view class="text-gray text-sm" v-for="(tag, index) in blogData.tagList" :key="tag.uid" style="margin-left: 20px;">
					<view v-if="index%3==0" class="cu-tag bg-red light sm round">{{tag.content}}</view>
					<view v-if="index%3==1" class="cu-tag bg-green light sm round">{{tag.content}}</view>
					<view v-if="index%3==2" class="cu-tag bg-brown light sm round">{{tag.content}}</view>			
				</view>
			</view>
			
			
<!-- 			<view class="padding">
				<view class="padding bg-grey radius">{{blogData.copyright}}</view>
			</view> -->
		
			<!-- <jyf-parser class="ck-content margin-sm" :html="blogData.content"></jyf-parser> -->
			
			<jyf-parser class="ck-content" :html="blogData.content" lazy-load ref="article" selectable
			 show-with-animation use-anchor @error="error" @imgtap="imgtap" @linkpress="linkpress" @ready="ready">加载中...</jyf-parser>
			 
			<view class="box">
				<view class="cu-bar">
					<view class="action border-title">
						<text class="text-xl text-bold text-blue">赞赏</text>
						<text class="bg-gradual-blue" style="width:3rem"></text>
					</view>
				</view>
			</view>
			
			<view class="margin-tb-sm text-center">
				<button class="cu-btn bg-orange round" @click="praiseBlog">很赞哦！<text v-if="praiseCount > 0">({{praiseCount}})</text></button>
				<button class="cu-btn bg-brown round  margin-lr-xs" @click="goAppreciate">打赏本站</button>
			</view>
			
			<view class="box">
				<view class="cu-bar">
					<view class="action border-title">
						<text class="text-xl text-bold text-blue">评论</text>
						<text class="bg-gradual-blue" style="width:3rem"></text>
					</view>
				</view>
			</view>
			
			<CommentList :comments="comments" @deleteSuccess="deleteSuccess" @commentSuccess="commentSuccess" source="BLOG_INFO" :blogUid="blogUid"></CommentList>

			<view class="loadStyle" v-if="!isEnd && !loading">下拉加载</view>
			<view class="loadStyle" v-if="!isEnd && loading">正在加载中</view>
			<view class="loadStyle" v-if="isEnd">我也是有底线的~</view>
			<view class="cu-tabbar-height"></view>
		</scroll-view>
	</view>
</template>

<script>
	import jyfParser from "../../components/jyf-parser/jyf-parser";
	import CommentList from "../../components/CommentList/index.vue";
	import {getBlogByUid, praiseBlogByUid} from "../../api/blogContent.js";
	import {getCommentListByApp} from "../../api/comment.js"
	export default {
		name: "basics",
		data() {
			return {
				blogUid: null,
				linenums: false,
				showLoading: false,
				decode: true,
				blogData : {},
				currentPage: 1,
				pageSize: 10,
				total: 0, //总数量
				comments: [],
				isEnd: false, //是否到底底部了
				loading: false, //是否正在加载
				praiseCount: 0,
			}
		},
		components: {
			jyfParser,
			CommentList,
		},
		onLoad(option) {
			this.blogUid = option.blogUid
			uni.showLoading({
			    title: '加载中'
			})
			this.getBlogInfo()
			this.commentList()
		},
		mounted() {

		},
		created() {
			
		},
		methods: {
			cutText(text, count) {
				if(text.length < count) {
					return text
				} else {
					return text.substring(0, count) + ".."
				}
			},
			commentList() {
				var that = this
				let params = {};
				params.source = "BLOG_INFO";
				params.blogUid = this.blogUid
				params.currentPage = that.currentPage
				params.pageSize = that.pageSize;
				this.loading = true;
				getCommentListByApp(params).then(response => {
					console.log("得到的博客列表", response)
					if (response.code == "success") {
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
			loadData: function() {
				console.log("上拉加载数据", this.comments.length, this.total)
				if(this.comments.length >= this.total) {
					return;
				}
				this.currentPage = this.currentPage + 1;
				this.commentList(this.selectBlogSortUid);
			},
			getBlogInfo() {
				var that = this
				let params = {}
				params.uid = this.blogUid;
				getBlogByUid(params).then(res =>{
					console.log(res)
					if(res.code == "success") {
						that.blogData = res.data;
						that.praiseCount = res.data.collectCount
					}
				})
			},
			deleteSuccess(commentUid) {
				// 找到被删除的uid，移除
				console.log("找到被移除的uid", commentUid)
				let comments = this.comments
				this.comments = []
				let newCommentList = []
				for(let a=0; a<comments.length; a++) {
					if(comments[a].uid == commentUid) {
						console.log("删除成功")
						continue;
					}
					newCommentList.push(comments[a])
				}
				this.comments = newCommentList
				// 删除后的值
				console.log("删除后的值", this.comments)
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
			},
			praiseBlog() {
				var that = this
				let params = {}
				params.uid = this.blogUid;
				praiseBlogByUid(params).then(res =>{
					console.log("点赞成功", res)
					if(res.code == "success") {
						uni.showToast({
							title: "点赞成功"
						})
						this.praiseCount = res.data
					} else {
						uni.showToast({
							title: res.data,
							icon: "none"
						})
					}
				})
			},
			goAppreciate() {
				uni.navigateTo({
					url: '/pages/my/myAppreciate',
				});
			},
			ready(e) {
				console.log('ready', e);
				// console.log('api: getText', this.$refs.article.getText());
				// console.log('imgList', this.$refs.article.imgList);
			},
			imgtap(e) {
				console.log('imgtap', e);
			},
			linkpress(e) {
				console.log('linkpress', e);
			},
			error(e) {
				console.error(e);
			}
		}
	}
</script>


<style scoped>
/* @import '../../static/css/ckeditor.css'; */

.ck-content {
	margin: 20rpx;
}
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
