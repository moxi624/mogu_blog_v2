<template name="basics">
	<view>

		<nav-bar home :bgColor="['#f37402','#0f0']" bgColorAngle="90" :backState="1000" fontColor="#000" title="友情链接"></nav-bar>
		
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
					if(res.code == this.$ECode.SUCCESS) {
						this.linkList = res.data;
					}
				})
			}
		}
	}
</script>


<style scoped>

</style>
