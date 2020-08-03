<template name="basics">
	<view>

		<view class="fixed">
			<cu-custom :isBack="true" bgColor="bg-shadeTop text-white">
				<block slot="content"> 赞赏支持 </block>
			</cu-custom>
		</view>
		
		<view class="box">
			<view class="cu-bar">
				<view class="action border-title">
					<text class="text-xl text-bold text-blue">支付宝</text>
					<text class="bg-gradual-blue" style="width:3rem"></text>
				</view>
			</view>
		</view>
		
		<view  style="margin: 0 auto; text-align: center;">
			<image class="payPhoto" :src="webConfig.aliPayPhoto" @tap="ViewImage" :data-url="webConfig.aliPayPhoto"></image>
		</view>
		
		
		<view class="box">
			<view class="cu-bar">
				<view class="action border-title">
					<text class="text-xl text-bold text-blue">微信</text>
					<text class="bg-gradual-blue" style="width:3rem"></text>
				</view>
			</view>
		</view>
		
		<view  style="margin: 0 auto; text-align: center;">
			<image class="payPhoto" :src="webConfig.weixinPayPhoto"  @tap="ViewImage" :data-url="webConfig.weixinPayPhoto"></image>
		</view>
		
	</view>
</template>

<script>
	import {getWebConfig} from "../../api/about.js";
	export default {
		name: "myAppreciate",
		data() {
			return {
				webConfig: {},
			}
		},
		created() {
			this.getWebConfigData()
		},
		methods: {
			getWebConfigData() {
				var that = this
				let params = {}
				getWebConfig(params).then(res =>{
					console.log("获取联系方式", res)
					if(res.code == "success") {
						that.webConfig = res.data;	
					}
				})
			},
			ViewImage(e) {
				console.log("显示图片")
				let urls = [this.webConfig.weixinPayPhoto, this.webConfig.aliPayPhoto]
				uni.previewImage({
					indicator: "default",
					urls: urls,
					current: e.currentTarget.dataset.url
				});
			},
		}
	}
</script>


<style scoped>
.payPhoto {
	width: 250px;
	height: 250px;
}
</style>
