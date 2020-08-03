<template name="basics">
	<view>

		<view class="fixed">
			<cu-custom :isBack="true" bgColor="bg-shadeTop text-white">
				<block slot="content"> 友情链接 </block>
			</cu-custom>
		</view>
		
		<view class="cu-chat">
			<view class="cu-item" v-for="item in linkList" :key="item.uid">
				<image class="cu-avatar radius lg" src="https://ossweb-img.qq.com/images/lol/web201310/skin/big143004.jpg"></image>
				
				<view class="main">
					<view class="content shadow bg-green">
						<text>{{item.title}}</text>
					</view>
				</view>
				<view class="date "> {{item.summary}}</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {getLink} from "../../api/index.js"
	export default {
		name: "link",
		data() {
			return {
				linkList: []
			}
		},
		created() {
			this.getLinkList()
		},
		methods: {
			getLinkList() {
				var params = {}
				getLink(params).then(res =>{
					console.log(res)
					if(res.code == "success") {
						this.linkList = res.data;
					}
				})
			}
		}
	}
</script>


<style scoped>

</style>
