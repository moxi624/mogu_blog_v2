<template name="basics">
	<scroll-view scroll-y class="scrollPage">
		<view class="UCenter-bg">
			<image src="/static/logo.png" class="png" mode="widthFix"></image>
			<view class="text-xl" style="margin-top: 5px;">
				蘑菇博客
			</view>
			<image src="https://raw.githubusercontent.com/weilanwl/ColorUI/master/demo/images/wave.gif" mode="scaleToFill" class="gif-wave"></image>
		</view>
		<view class="padding flex text-center text-grey bg-white shadow-warp">

			<view class="flex flex-sub flex-direction solid-right">
				<view class="margin-top-sm text-xxl">
					<text class="cuIcon-messagefill" style="color: #00B0E8;"></text>
				</view>
				<view class="text-grey">留言</view>
			</view>

			<view class="flex flex-sub flex-direction solid-right">
				<view class="margin-top-sm text-xxl">
					<text class="cuIcon-link" style="color: #ffaa00;"></text>
				</view>
				<view class="text-grey">友链</view>
			</view>
			
			<view class="flex flex-sub flex-direction solid-right">
				<view class="margin-top-sm text-xxl">
					<text class="cuIcon-tagfill" style="color: #00ff00;"></text>
				</view>
				<view class="text-grey">标签</view>
			</view>
			
			<view class="flex flex-sub flex-direction solid-right">
				<view class="margin-top-sm text-xxl">
					<text class="cuIcon-sort" style="color: #ff0000;"></text>
				</view>
				<view class="text-grey">分类</view>
			</view>
			
		</view>
		<view class="cu-list menu card-menu margin-top-xl margin-bottom-xl shadow-lg radius">

			<view class="cu-item arrow">
				<navigator class="content" url="/pages/my/about" hover-class="none">
					<text class="cuIcon-github text-grey"></text>
					<text class="text-grey">关于我</text>
				</navigator>
			</view>
			

			<view class="cu-item arrow">
				<navigator class="content" url="/pages/my/myComment" hover-class="none">
					<text class="cuIcon-comment text-black"></text>
					<text class="text-grey">我的评论</text>
				</navigator>
			</view>

			<view class="cu-item arrow">
				<navigator class="content" url="/pages/my/myReply" hover-class="none">
					<text class="cuIcon-notice text-green"></text>
					<text class="text-grey">我的回复</text>
				</navigator>
			</view>

			<view class="cu-item arrow">
				<navigator class="content" url="/pages/my/myPraise" hover-class="none">
					<text class="cuIcon-favor text-yellow"></text>
					<text class="text-grey">我的点赞</text>
				</navigator>
			</view>
			
			<view class="cu-item arrow">
				<navigator class="content" url="/pages/my/applyLink" hover-class="none">
					<text class="cuIcon-link text-blue"></text>
					<text class="text-grey">友链申请</text>
				</navigator>
			</view>
			
			<view class="cu-item arrow">
				<navigator class="content" url="/pages/my/applyLink" hover-class="none">
					<text class="cuIcon-appreciatefill text-red"></text>
					<text class="text-grey">赞赏支持</text>
				</navigator>
			</view>
			
			<view class="cu-item arrow">
				<navigator class="content" url="/pages/my/myFeedback" hover-class="none">
					<text class="cuIcon-writefill text-brown"></text>
					<text class="text-grey">意见反馈</text>
				</navigator>
			</view>

		</view>
		<view class="cu-tabbar-height"></view>
	</scroll-view>
</template>

<script>
	export default {
		name: "basics",
		data() {
			return {
				starCount: 15,
				forksCount: 20,
				visitTotal: 25
			}
		}
	}
</script>

<style>
	.page {
		height: 100vh;
	}

	.UCenter-bg {
		background-image: url(https://image.weilanwl.com/color2.0/index.jpg);
		background-size: cover;
		height: 400rpx;
		display: flex;
		justify-content: center;
		padding-top: 40rpx;
		overflow: hidden;
		position: relative;
		flex-direction: column;
		align-items: center;
		color: #fff;
		font-weight: 300;
		text-shadow: 0 0 3px rgba(0, 0, 0, 0.3);
	}

	.UCenter-bg text {
		opacity: 0.8;
	}

	.UCenter-bg image {
		width: 200rpx;
		height: 200rpx;
	}

	.UCenter-bg .gif-wave {
		position: absolute;
		width: 100%;
		bottom: 0;
		left: 0;
		z-index: 99;
		mix-blend-mode: screen;
		height: 100rpx;
	}

	map,
	.mapBox {
		left: 0;
		z-index: 99;
		mix-blend-mode: screen;
		height: 100rpx;
	}

	map,
	.mapBox {
		width: 750rpx;
		height: 300rpx;
	}
</style>
