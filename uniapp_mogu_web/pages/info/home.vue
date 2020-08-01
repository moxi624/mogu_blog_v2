<template name="basics">
	<view>

		<view class="fixed">
			<cu-custom :isBack="true" bgColor="bg-shadeTop text-white">
				<block slot="content" >{{blogData.title}}</block>
			</cu-custom>
		</view>
		
		<scroll-view scroll-y class="DrawerPage">
<!-- 		    <view class="flex-sub text-center margin-sm">
		      <view class="text-xxxl" v-if="blogData.title != null">
		        <text class="text-black text-bold" style="font-size: 20px;">{{blogData.title}}</text>
		      </view>
		    </view> -->
		
			<view class="text-gray text-sm flex justify-start">
				<view class="text-gray text-sm" v-for="(tag, index) in blogData.tagList" :key="tag.uid" style="margin-left: 20px;">
					<view v-if="index%3==0" class="cu-tag bg-red light sm round">{{tag.content}}</view>
					<view v-if="index%3==1" class="cu-tag bg-green light sm round">{{tag.content}}</view>
					<view v-if="index%3==2" class="cu-tag bg-brown light sm round">{{tag.content}}</view>			
				</view>
			</view>
		
		
		    <view class="cf">
		        <view class="margin-sm fr">
		            <view class="cu-capsule round">
		                <view class="cu-tag bg-red sm">
		                    <text class="cuIcon-likefill"></text>
		                </view>
		                <view class="cu-tag line-red sm">
		                {{blogData.collectCount}}
		                </view>
		            </view>
		            <view class="cu-capsule round">
		                <view class="cu-tag bg-orange sm">
		                    <text class="cuIcon-hotfill"></text>
		                </view>
		                <view class="cu-tag line-orange sm">
		                {{blogData.clickCount}}
		                </view>
		            </view>
		            <view class="cu-capsule round">
		                <view class="cu-tag  bg-blue sm">
		                    <text class="cuIcon-timefill"></text>
		                </view>
		                <view class="cu-tag line-blue sm">
		                {{blogData.createTime}}
		                </view>
		            </view>
		            <text class="cu-capsule">&nbsp;</text>
		        </view>
		    </view>
		
			<jyf-parser class="ck-content margin-sm" :html="blogData.content"></jyf-parser>
			
			<!--以下是评论内容-->
			<view class="cu-list menu-avatar comment solids-top">
				<view class="cu-item" style="margin-top: 10rpx">
					<view class="cu-avatar round" style="background-image:url(https://ossweb-img.qq.com/images/lol/img/champion/Morgana.png);"></view>
					<view class="content">
						<view class="text-grey">莫甘娜</view>
						<view class="text-gray text-content text-df" style="margin-top: 10rpx;">
							凯尔，你被自己的光芒变的盲目。
						</view>
						<view class="margin-top-sm flex justify-between">
							<view class="text-gray text-df">2018年12月4日</view>
							<view>
								<text class="cuIcon-appreciatefill text-red"></text>
								<text class="cuIcon-messagefill text-gray margin-left-sm"></text>
							</view>
						</view>
					</view>
				</view>
				
				<view class="cu-item" style="margin-top: 10rpx">
					<view class="cu-avatar round" style="background-image:url(https://ossweb-img.qq.com/images/lol/img/champion/Morgana.png);"></view>
					<view class="content">
						<view class="text-grey">莫甘娜</view>
						<view class="text-gray text-content text-df" style="margin-top: 10rpx;">
							凯尔，你被自己的光芒变的盲目。
						</view>
						<view class="margin-top-sm flex justify-between">
							<view class="text-gray text-df">2018年12月4日</view>
							<view>
								<text class="cuIcon-appreciatefill text-red"></text>
								<text class="cuIcon-messagefill text-gray margin-left-sm"></text>
							</view>
						</view>
					</view>
				</view>
			</view>
			<view class="cu-tabbar-height"></view>
		</scroll-view>
	</view>
</template>

<script>
	import jyfParser from "../../components/jyf-parser/jyf-parser";
	import {getBlogByUid} from "../../api/blogContent.js";
	export default {
		name: "basics",
		data() {
			return {
				blogUid: null,
				highlightStyle: "dracula",
				linenums: false,
				showLoading: false,
				decode: true,
				blogData : {},
			}
		},
		components: {
			jyfParser
		},
		onLoad(option) {
			this.blogUid = option.blogUid
			console.log("传递过来的博客uid", option.blogUid)
			this.getBlogInfo()
		},
		methods: {
			getBlogInfo() {
				var that = this
				let params = {}
				params.uid = this.blogUid;
				getBlogByUid(params).then(res =>{
					console.log(res)
					if(res.code == "success") {
						that.blogData = res.data;	
					}
				})
			},
		}
	}
</script>


<style>
/* @import '../../static/css/ckeditor.css'; */

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
