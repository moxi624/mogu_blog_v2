<template>
	<view>
		<!--以下是评论内容-->
		<view class="cu-list menu-avatar comment solids-top">
			<view class="cu-form-group margin-top">
				<textarea maxlength="-1" v-model="firstCommentContent" placeholder="既然来了,那就留下些什么吧~"></textarea>
			</view>
			<view class="padding flex flex-direction">
				<button class="cu-btn bg-blue lg" @click="submit">评论一下</button>
			</view>
			<view class="cu-item" style="margin-top: 10rpx" v-for="(item, index) in comments" :key="item.uid">
				<image class="cu-avatar round" :src="item.user.photoUrl"></image>
				<view class="content">
					<view class="justify-between">
						<text class="text-orange">{{item.user.nickName}}</text>
						<text class="text-grey">#{{index + 1}}楼</text>
					</view>
					<view class="text-gray text-content text-df" style="margin-top: 10rpx;" v-html="item.content"></view>
					<view class="bg-grey padding-sm radius margin-top-sm  text-sm" v-if="item.toComment">
						<view class="flex">
							<view class="flex-sub">{{item.toComment.content}}</view>
						</view>
					</view>
					<view class="margin-top-sm flex justify-between">
						<view class="text-gray text-df">{{timeFromat(item.createTime)}}</view>
						<view>
							<text class="cuIcon-deletefill text-gray" v-if="item.userUid == userInfo.uid" @tap="deleteC(item.uid)"></text>
							<text class="cuIcon-flashbuyfill text-gray" style="margin-left: 15rpx;" v-if="item.userUid != userInfo.uid" @tap="report(item.uid)"></text>
							<text class="cuIcon-messagefill text-gray margin-left-sm" @tap="showCommentModal(item)"></text>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!--以下是模态框-->
		<view class="cu-modal show" v-if="isShow">
			<view class="cu-dialog">
				<view class="cu-bar bg-white justify-end">
					<view class="content">评论</view>
					<view class="action" @tap="hideModal">
						<text class="cuIcon-close text-red"></text>
					</view>
				</view>
				<view class="padding-xl">
					<view class="cu-form-group margin-top">
						<textarea maxlength="-1" v-model="content" placeholder="既然来了,那就留下些什么吧~"></textarea>
					</view>
					<view class="padding flex flex-direction">
						<button class="cu-btn bg-blue lg" @click="apply">发表评论</button>
					</view>
				</view>
			</view>
		</view>
	</view>


	
</template>

<script>
	import {timeAgo} from "../../utils/webUtils.js"
	import {addComment, deleteComment, reportComment} from "../../api/comment.js"
	export default {
		name: "CommentList",
		props: ["comments", "source", "blogUid"],
		data() {
			return {
				userInfo: {},
				isShow: false,
				content: "",
				selectComment: {}, // 选中的评论
				firstCommentContent: "", // 一级评论
			}
		},
		created() {
			this.getUserInfo()
		},
		methods: {
			hideModal(e) {
				this.isShow = false
			},
			submit() {
				if(this.firstCommentContent == "") {
					uni.showToast({
						title: "评论的内容不能为空",
						icon: "none"
					})
					return
				}

				let params = {};
				params.userUid = this.userInfo.uid;
				params.content = this.firstCommentContent;
				params.blogUid = this.blogUid;
				params.source = this.source
				addComment(params).then(response => {
					if(response.code == "success") {
						uni.showToast({
							title: "评论成功"
						})
						this.isShow = false
						this.firstCommentContent = ""
						this.$emit("commentSuccess", response.data)
					} else {
						uni.showToast({
							title: response.data,
							icon: "none"
						})
					}
					console.log("评论成功", response)
				})
			},
			// 获取用户信息
			getUserInfo() {
				this.userInfo = uni.getStorageSync("userInfo")
				console.log("获得的用户信息", this.userInfo)
			},
			timeFromat(str) {
				return timeAgo(str)
			},
			deleteC(commentUid) {
				if(this.userInfo.uid == undefined) {
					uni.showToast({
						title: "请先登录",
						icon: "none"
					})
					return
				}
				let params = {}
				params.uid = commentUid;
				params.userUid = this.userInfo.uid
				deleteComment(params).then(response => {
				  console.log("获取点赞列表", response)
				  if(response.code == "success") {
					    // 删除成功
						this.$emit("deleteSuccess", commentUid)
				  }
				})
			},
			report(commentUid) {
				console.log("举报评论")
				uni.showModal({
					title: '警告',
					content: '确定要举报该评论？',
					cancelText: '再看看',
					confirmText: '举报',
					success: res => {
						if (res.confirm) {
							let params = {}
							params.uid = commentUid;
							params.userUid = this.userInfo.uid
							reportComment(params).then(response => {
							  if(response.code == "success") {
									uni.showToast({
										title: response.data,
									})
									this.content = ""
							  } else {
								  uni.showToast({
								  	title: response.data,
									icon: "none"
								  })
							  }
							})
						}
					}
				})
			},
			showCommentModal(item) {
				this.isShow = true
				this.selectComment = item
			},
			apply() {
				if(this.content == "") {
					uni.showToast({
						title: "评论的内容不能为空",
						icon: "none"
					})
					return
				}
				console.log("回复评论")
				let item = this.selectComment
				let params = {};
				params.userUid = this.userInfo.uid;
				params.content = this.content;
				params.blogUid = item.blogUid;
				params.toUid = item.uid;
				params.toUserUid = item.userUid;
				params.source = this.source
				addComment(params).then(response => {
					if(response.code == "success") {
						uni.showToast({
							title: "评论成功"
						})
						this.isShow = false
						let returnComment = response.data
						returnComment.toComment = this.selectComment
						this.$emit("commentSuccess", returnComment)
					} else {
						uni.showToast({
							title: response.data,
							icon: "none"
						})
					}
					console.log("评论成功", response)
				})
			}
		}
	}
</script>

<style>
</style>
