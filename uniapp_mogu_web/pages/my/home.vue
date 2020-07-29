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
				<view class="text-xxl text-orange">{{visitTotal}}</view>
				<view class="margin-top-sm">
					<text class="cuIcon-attentionfill"></text> View
				</view>
			</view>

			<view class="flex flex-sub flex-direction solid-right">
				<view class="text-xxl text-blue">{{starCount}}</view>
				<view class="margin-top-sm">
					<text class="cuIcon-favorfill"></text> Star</view>
			</view>
			
			<view class="flex flex-sub flex-direction">
				<view class="text-xxl text-green">{{forksCount}}</view>
				<view class="margin-top-sm">
					<text class="cuIcon-fork"></text> Fork</view>
			</view>
			
		</view>
		<view class="cu-list menu card-menu margin-top-xl margin-bottom-xl shadow-lg radius">
			<view class="cu-item arrow">
				<view class="content" bindtap="CopyLink" data-link="https://github.com/weilanwl/ColorUI">
					<text class="cuIcon-github text-grey"></text>
					<text class="text-grey">联系方式</text>
				</view>
			</view>

			<view class="cu-item arrow">
				<navigator class="content" url="/pages/about/log/log" hover-class="none">
					<text class="cuIcon-formfill text-green"></text>
					<text class="text-grey">我的评论</text>
				</navigator>
			</view>

			<view class="cu-item arrow">
				<navigator class="content" url="/pages/about/log/log" hover-class="none">
					<text class="cuIcon-formfill text-green"></text>
					<text class="text-grey">我的回复</text>
				</navigator>
			</view>

			<view class="cu-item arrow">
				<navigator class="content" url="/pages/about/log/log" hover-class="none">
					<text class="cuIcon-formfill text-green"></text>
					<text class="text-grey">我的点赞</text>
				</navigator>
			</view>

			<view class="cu-item arrow">
				<view class="content" bindtap="showQrcode">
					<text class="cuIcon-appreciatefill text-red"></text>
					<text class="text-grey">赞赏支持</text>
				</view>
			</view>

			<view class="cu-item arrow">
				<button class="cu-btn content" open-type="feedback">
					<text class="cuIcon-writefill text-cyan"></text>
					<text class="text-grey">意见反馈</text>
				</button>
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
