<template name="basics">
	<view>

		<view class="fixed">
			<cu-custom :isBack="true" bgColor="bg-shadeTop text-white">
				<block slot="content" > 我的点赞 </block>
			</cu-custom>
		</view>
		
		<scroll-view scroll-y class="page" @scrolltolower="loadData">
			<view class="cu-timeline" v-for="item in praiseList" :key="item.uid" >
				<view class="cu-time">{{timeFormat(item.createTime)}}</view>
				<view class="cu-item">
					<view class="content">
						<text>点赞文章：</text> 
						<view @tap="goInfo(item.blog.uid)" class="cu-tag bg-brown light sm round" style="font-size: 15px;">{{item.blog.title}}</view>
					</view>
				</view>
			</view>
		</scroll-view>
	</view>
</template>

<script>
	import {timeAgo} from "../../utils/webUtils.js";
	import {getPraiseListByUser} from "../../api/comment.js";
	export default {
		name: "myPraise",
		data() {
			return {
				// 点赞列表
				praiseList: [],
			}
		},
		onLoad() {

		},
		created() {
			this.getPraiseList()
		},
		methods: {
			timeFormat(time) {
				return timeAgo(time)
			},
			loadData() {
				console.log("下拉加载")
			},
			goInfo(blogUid) {
				console.log("跳转", blogUid)
				uni.navigateTo({
					url: '/pages/info/home?blogUid=' + blogUid,
				});
			},
			// 获取点赞列表
			getPraiseList: function() {
				let params = {}
				params.pageSize = 10;
				params.currentPage = 1;
				getPraiseListByUser(params).then(response => {
				  console.log("获取点赞列表", response)
				  if(response.code == "success") {
					this.praiseList = response.data.records;
				  }
				})
			},
		}
	}
</script>


<style>

</style>
