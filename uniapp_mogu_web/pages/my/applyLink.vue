<template name="basics">
	<view>
		
		<nav-bar home :bgColor="['#f37402','#0f0']" bgColorAngle="90" :backState="1000" fontColor="#000" title="友链申请"></nav-bar>
		
		<view class="box">
			<view class="cu-bar">
				<view class="action border-title">
					<text class="text-xl text-bold text-blue">申请须知</text>
					<text class="bg-gradual-blue" style="width:3rem"></text>
				</view>
			</view>
		</view>
		
		<view>
			<view class="action" style="margin-top: 10px;">
				<text class="cuIcon-emoji text-red" style="font-size: 18px; margin-left: 15px; margin-right: 5px;"></text> 请确定贵站可以稳定运营
			</view>
			<view class="action" style="margin-top: 10px;">
				<text class="cuIcon-emoji text-red" style="font-size: 18px; margin-left: 15px; margin-right: 5px;"></text> 原创博客优先，技术类博客优先
			</view>
			<view class="action" style="margin-top: 10px;">
				<text class="cuIcon-emoji text-red" style="font-size: 18px; margin-left: 15px; margin-right: 5px;"></text> 申请前请先添加下方蘑菇博客友链
			</view>
			<view class="action" style="margin-top: 10px;" >
				<text class="cuIcon-emoji text-red" style="font-size: 18px; margin-left: 15px; margin-right: 5px;"></text> 欢迎各位小伙伴一起互换友链~
			</view> 
		</view>
		
		<view class="box">
			<view class="cu-bar">
				<view class="action border-title">
					<text class="text-xl text-bold text-blue">蘑菇博客</text>
					<text class="bg-gradual-blue" style="width:3rem"></text>
				</view>
			</view>
		</view>
		
		<view>
			<view class="action" style="margin-top: 10px;">
				<text class="cuIcon-edit text-red" style="font-size: 18px; margin-left: 15px; margin-right: 5px;"></text> 蘑菇博客
			</view>
			<view class="action" style="margin-top: 10px;">
				<text class="cuIcon-pic text-red" style="font-size: 18px; margin-left: 15px; margin-right: 5px;"></text> http://image.moguit.cn/favicon.png
			</view>
			<view class="action" style="margin-top: 10px;">
				<text class="cuIcon-calendar text-red" style="font-size: 18px; margin-left: 15px; margin-right: 5px;"></text> 蘑菇博客 - 专注于技术分享的博客平台
			</view>
			<view class="action" style="margin-top: 10px;" >
				<text class="cuIcon-weblock text-red" style="font-size: 18px; margin-left: 15px; margin-right: 5px;"></text> http://www.moguit.cn
			</view> 
		</view>
		
		<form>
			<view class="cu-form-group margin-top">
				<view class="title text-black">网站名称</view>
				<input placeholder="请输入网站名称" v-model="blogLink.title"></input>
			</view>
			<view class="cu-form-group">
				<view class="title text-black">网站简介</view>
				<input placeholder="请输入网站简介" v-model="blogLink.summary"></input>
			</view>
			<view class="cu-form-group">
				<view class="title text-black">网站URL</view>
				<input placeholder="请输入网站URL" v-model="blogLink.url"></input>
			</view>
			<view class="cu-form-group">
				<view class="title text-black">网站LOGO</view>
				<input placeholder="请输入网站LOGO" v-model="blogLink.logo"></input>
			</view>
		</form>
		
		<button class="cu-btn block bg-black margin-sm lg" @click="submit"> 提交</button>

	</view>
</template>

<script>
	import {replyBlogLink} from "../../api/user.js"
	import {IsURL} from "../../utils/webUtils.js";
	export default {
		name: "applyLink",
		data() {
			return {
				blogLink: {
					title: "",
					summary: "",
					url: "",
					logo: "",
				}
			}
		},
		created() {
			
		},
		methods: {
			submit() {
				console.log("提交表单", this.blogLink)
				if(this.blogLink.title == "" || this.blogLink.summary == "" || this.blogLink.url == "") {
					uni.showToast({
						icon: "none",
						title: "请先完善表单~",
					})
					return;
				}
				if(!IsURL(this.blogLink.url)) {
					uni.showToast({
						icon: "none",
						title: "请输入正确的URL~",
					})
					return;
				}
				
				replyBlogLink(this.blogLink).then(res =>{
					console.log(res)
					if(res.code == this.$ECode.SUCCESS) {
						uni.showToast({
							icon: "success",
							title: res.data,
						})
					}
				})
			}
		}
	}
</script>


<style scoped>

</style>
