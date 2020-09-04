<template>
	<view>
		<view
			:class="[{ header_fixed: navFixed, header_absolute: type == 'transparent', header_shadow: navShadow, header_colorWhite: isWhite }, themeBgColorName]"
			:style="[navBgColor ? { backgroundImage: navBgColor } : {}, { paddingTop: statusBarHeight + 'px', color: navFontColor, opacity: transparentValue }]"
		>
			<view class="header_content">
				<view class="header_left_box">
					<slot name="left">
						<view class="header_left_info" :class="{ header_btnMongol: isTwoBtn, header_colorWhite_btnMongol: isWhite && isTwoBtn }" v-if="back || $slots.left || home">
							<view class="header_left_back" :class="{ header_btnMongol_left_back: isTwoBtn }" v-if="back && !firstPage" @click="onBackPage">
								<image class="header_icon" v-if="isWhite" src="/static/zhouWei-navBar/icon_back_white.png" mode="aspectFit"></image>
								<image class="header_icon" v-else src="/static/zhouWei-navBar/icon_back_black.png" mode="aspectFit"></image>
							</view>
							<text class="header_left_line" :class="{ header_colorWhite_left_line: isWhite }" v-if="isTwoBtn"></text>
							<view class="header_left_home" :class="{ header_btnMongol_left_home: isTwoBtn }" v-if="(firstPage && back) || home" @click="onBackHome">
								<image class="header_icon" v-if="isWhite" src="/static/zhouWei-navBar/icon_home_white.png" mode="aspectFit"></image>
								<image class="header_icon" v-else src="/static/zhouWei-navBar/icon_home_black.png" mode="aspectFit"></image>
							</view>
						</view>
					</slot>
					<view class="header_title" v-if="!titleCenter && ($slots.default || navTitle)" :style="{ color: navFontColor }">
						<slot>
							<text :style="{ color: navFontColor }">{{ navTitle }}</text>
						</slot>
					</view>
				</view>
				<view class="header_title header_title_center" v-if="titleCenter && ($slots.default || navTitle)" :style="{ color: navFontColor }">
					<slot>
						<text :style="{ color: navFontColor }">{{ navTitle }}</text>
					</slot>
				</view>
				<view class="header_right_info"><slot name="right"></slot></view>
			</view>
		</view>
		<view
			class="header_transparentFixed header_fixed"
			v-if="type == 'transparentFixed'"
			:style="{ paddingTop: statusBarHeight + 'px', color: navTransparentFixedFontColor, opacity: 1 - transparentValue, zIndex: transparentValue < 0.3 ? 100 : 90 }"
		>
			<view class="header_content">
				<view class="header_left_box">
					<slot name="transparentFixedLeft">
						<view
							class="header_left_info header_transparentFixed_left_info"
							:class="{ header_transparentFixed_colorWhite_left_info: isWhite }"
							v-if="back || $slots.left || home"
						>
							<view class="header_left_back" :class="{ header_btnMongol_left_back: isTwoBtn }" v-if="back && !firstPage" @click="onBackPage">
								<image class="header_icon" v-if="isWhite" src="/static/zhouWei-navBar/icon_back_white.png" mode="aspectFit"></image>
								<image class="header_icon" v-else src="/static/zhouWei-navBar/icon_back_black.png" mode="aspectFit"></image>
							</view>
							<text class="header_left_line" v-if="isTwoBtn"></text>
							<view class="header_left_home" :class="{ header_btnMongol_left_home: isTwoBtn }" v-if="(firstPage && back) || home" @click="onBackHome">
								<image class="header_icon" v-if="isWhite" src="/static/zhouWei-navBar/icon_home_white.png" mode="aspectFit"></image>
								<image class="header_icon" v-else src="/static/zhouWei-navBar/icon_home_black.png" mode="aspectFit"></image>
							</view>
						</view>
					</slot>
					<view class="header_title" v-if="!titleCenter && (navTitle || $slots.transparentFixed)" :style="{ color: navTransparentFixedFontColor }">
						<slot name="transparentFixed">
							<text :style="{ color: navTransparentFixedFontColor }">{{ navTitle }}</text>
						</slot>
					</view>
				</view>
				<view class="header_title header_title_center" v-if="titleCenter && (navTitle || $slots.transparentFixed)" :style="{ color: navTransparentFixedFontColor }">
					<slot name="transparentFixed">
						<text :style="{ color: navTransparentFixedFontColor }">{{ navTitle }}</text>
					</slot>
				</view>
				<view class="header_right_info"><slot name="transparentFixedRight"></slot></view>
			</view>
		</view>
		<view v-if="type == 'fixed'" :style="{ paddingTop: statusBarHeight + 'px' }"><view class="header_station"></view></view>
	</view>
</template>
<script>
// 主页页面的页面路径
// 关联功能：打开的页面只有一个的时候右上角自动显示返回首页按钮，下面这个数组是排除显示返回首页的页面。
// 主页使用场景：小程序分享出去的页面，用户点击开是分享页面，很多情况下是没有返回首页按钮的
const mainPagePath = ['pages/home/home', 'pages/my/my', 'pages/demo/common', 'pages/template/common', 'pages/apiDemo/common'];
//返回首页的地址
const homePath = '/pages/index/index';
//白色表达值
const whiteList = ['#FFF', '#FFFFFF', 'white', 'rgb(255,255,255)', 'rgba(255,255,255,1)'];
export default {
	props: {
		//是否显示返回按钮
		// 1000 显示返回按钮
		// 2000 不显示返回按钮
		// 3000 自定义返回按钮方法，点击返回箭头后会发送一个backClick事件
		backState: {
			default: function() {
				return 1000;
			}
		},
		//是否显示返回首页按钮
		home: {
			type: Boolean,
			default: function() {
				return false;
			}
		},
		//导航背景色，支持渐变
		bgColor: {
			type: [String, Array],
			default: function() {
				return '#FFFFFF';
			}
		},
		// 导航背景色渐变角度
		bgColorAngle: {
			type: [String, Number],
			default: function() {
				return 90;
			}
		},
		//导航字体颜色，字体颜色为白色的时候会把手机状态栏设置为白色，否则为黑色
		fontColor: {
			type: String,
			default: function() {
				return '#000000';
			}
		},
		//标题是否居中
		titleCenter: {
			type: Boolean,
			default: function() {
				return true;
			}
		},
		//标题
		title: {
			type: String,
			default: function() {
				return '';
			}
		},
		//类型 fixed为固定 默认
		// ordinary 普通的 不固定
		// transparent 透明不固定的
		//transparentFixed  透明固定的
		type: {
			type: String,
			default: function() {
				return 'fixed';
			}
		},
		//透明固定的时候字体颜色
		transparentFixedFontColor: {
			type: String,
			default: function() {
				return '#000000';
			}
		},
		// 屏幕滑动距离顶部距离(透明固定导航比传)
		scrollTop: {
			type: Number,
			default: function() {
				return 0;
			}
		},
		// 是否显示阴影
		shadow: {
			type: Boolean,
			default: function() {
				return true;
			}
		}
	},
	data() {
		return {
			//当前页面是否是第一个页面
			firstPage: false,
			//透明度值
			transparentValue: 1,
			//标题
			navTitle: '',
			//字体色
			navFontColor: '#000000',
			//背景色
			navBgColor: '',
			//透明底字体色
			navTransparentFixedFontColor: '#000000',
			// 是否使用
			themeBgColor: false,
			// 导航栏高度
			statusBarHeight: 0,
			// 上次显示的导航栏颜色
			lastFrontColor: '',
			themeBgColorName: ''
		};
	},
	computed: {
		back() {
			return this.backState == 1000 || this.backState == 3000;
		},
		//导航固定
		navFixed() {
			if (this.type == 'transparentFixed' || this.type == 'fixed') {
				return true;
			} else {
				return false;
			}
		},
		//导航底部线是否显示
		navShadow() {
			if (this.bgColor && typeof this.bgColor == 'string') {
				return this.shadow && this.type !== 'transparent' && whiteList.includes(this.bgColor);
			} else {
				return false;
			}
		},
		//导航字体是否是白色颜色
		isWhite() {
			return whiteList.includes(this.navFontColor);
		},
		//右上角是否有两个按钮
		isTwoBtn() {
			return (this.backState == 1000 || this.backState == 3000) && this.home && !this.firstPage;
		}
	},
	watch: {
		title(val) {
			this.navTitle = val;
		},
		fontColor(val) {
			this.navFontColor = val;
			this.settingColor();
		},
		bgColor(val) {
			this.getNavBgColor(val);
		},
		transparentFixedFontColor(val) {
			this.navTransparentFixedFontColor = val;
		},
		scrollTop(val) {
			this.pageScroll({
				scrollTop: val
			});
		}
	},
	//第一次加载
	created() {
		this.navTitle = this.title;
		this.navFontColor = this.fontColor;
		this.getNavBgColor(this.bgColor);
		this.navTransparentFixedFontColor = this.transparentFixedFontColor;
		//获取手机状态栏高度
		this.statusBarHeight = uni.getSystemInfoSync()['statusBarHeight'];
		const _this = this;
		this.pageScroll({
			scrollTop: this.scrollTop
		});
		//获取所有页面
		let currentPages = getCurrentPages();
		let pageLen = currentPages.length;
		//判断是否是第一个页面，如果是有设置back为true的页面，将不显示返回箭头，而显示返回首页按钮
		if (pageLen == 1 && !mainPagePath.includes(currentPages[0].route)) {
			this.firstPage = true;
		}
	},
	//方法
	methods: {
		//返回上一页面
		onBackPage() {
			if (this.backState == 3000) {
				this.$emit('backClick');
			} else {
				uni.navigateBack();
			}
		},
		//返回首页
		onBackHome() {
			uni.redirectTo({
				url: homePath
			});
		},
		pageScroll(e) {
			if (this.type == 'transparentFixed') {
				if (e.scrollTop && e.scrollTop > 0) {
					if (e.scrollTop > 180) {
						this.transparentValue = 1;
					} else {
						this.transparentValue = e.scrollTop / 180;
					}
				} else {
					this.transparentValue = 0;
				}
				this.settingColor();
			}
		},
		// 获取导航背景颜色
		getNavBgColor(val) {
			if (val.indexOf('#') === -1) {
				this.themeBgColorName = val;
				this.themeBgColor = true;
				this.navBgColor = '';
			} else if (this.type == 'transparent') {
				this.themeBgColor = false;
				this.navBgColor = '';
			} else {
				if (typeof val == 'string') {
					this.navBgColor = 'linear-gradient(90deg,' + val + ',' + val + ')';
				} else if (Array.isArray(val) && val.length >= 2) {
					let navBgColor = 'linear-gradient(' + this.bgColorAngle + 'deg';
					val.forEach(item => {
						if (typeof item == 'string') {
							navBgColor += ',' + item;
						} else if (typeof item == 'object') {
							navBgColor += ',' + item.color + ' ' + item.scale;
						}
					});
					navBgColor += ')';
					this.navBgColor = navBgColor;
				}
			}
		},
		//设置手机状态栏颜色
		settingColor() {
			let navColor = this.navFontColor;
			if (this.type == 'transparentFixed' && this.transparentValue <= 0.5) {
				navColor = this.navTransparentFixedFontColor;
			}
			let frontColor = '#000000';
			if (whiteList.includes(navColor)) {
				frontColor = '#ffffff';
			}
			if (this.lastFrontColor == frontColor) {
				return;
			}
			setTimeout(() => {
				this.lastFrontColor = frontColor;
				// 改变手机状态栏颜色
				uni.setNavigationBarColor({
					frontColor: frontColor,
					backgroundColor: '#FFFFFF'
				});
			}, 150);
		}
	}
};
</script>
<style lang="scss">
.header_content {
	/* #ifndef APP-NVUE */
	display: flex;
	/* #endif */
	/* #ifdef MP */
	padding-right: 190rpx;
	box-sizing: border-box;
	/* #endif */
	width: 750rpx;
	align-items: flex-end;
	justify-content: space-between;
	flex-direction: row;
	height: 88rpx;
	position: relative;
}

.header_station {
	height: 88rpx;
}

.header_shadow {
	// border-style: solid;
	// border-width: 2rpx;
	// border-color: #f5f5f5;
	box-shadow: 0 0 6rpx 0 #ddd;
}

.header_fixed {
	position: fixed;
	top: 0;
	left: 0;
	width: 750rpx;
	z-index: 99;
}

.header_absolute {
	position: absolute;
	top: 0;
	left: 0;
	z-index: 99;
	width: 750rpx;
	background-color: transparent !important;
}

.header_left_box {
	/* #ifndef APP-PLUS-NVUE */
	display: flex;
	/* #endif */
	flex-direction: row;
	align-items: center;
	height: 88rpx;
	flex: 1;
}

.header_left_line {
	height: 30rpx;
	width: 2rpx;
	background-color: rgba(255, 255, 255, 0.4);
}

.header_left_back {
	width: 56rpx;
	height: 100%;
	/* #ifndef APP-PLUS-NVUE */
	display: flex;
	/* #endif */
	align-items: center;
	justify-content: center;
}

.header_icon {
	width: 30rpx;
	height: 30rpx;
}

.header_left_home {
	width: 56rpx;
	height: 100%;
	/* #ifndef APP-PLUS-NVUE */
	display: flex;
	/* #endif */
	align-items: center;
	justify-content: center;
}

.header_left_info {
	/* #ifndef APP-PLUS-NVUE */
	display: flex;
	/* #endif */
	flex-direction: row;
	align-items: center;
	height: 56rpx;
	margin-left: 16rpx;
}

.header_title {
	height: 88rpx;
	font-size: 32rpx;
	padding-left: 30rpx;
	padding-right: 30rpx;
	font-weight: 700;
	text-overflow: ellipsis;
	/* #ifndef APP-PLUS-NVUE */
	white-space: nowrap;
	display: flex;
	overflow: hidden;
	/* #endif */
	/* #ifdef APP-PLUS-NVUE */
	lines: 1;
	/* #endif */
	flex-direction: row;
	align-items: center;
	justify-content: center;
	/* #ifdef MP */
	max-width: calc(100vw - 160rpx);
	/* #endif */
}

.header_title_center {
	position: absolute;
	bottom: 0rpx;
	left: 375rpx;
	transform: translateX(-50%);
}

.header_right_info {
	height: 88rpx;
	/* #ifndef APP-PLUS-NVUE */
	display: flex;
	flex-shrink: 0;
	/* #endif */
	flex-direction: row;
	align-items: center;
}

.header_btnMongol {
	border-radius: 33rpx;
	border-style: solid;
	border-width: 2rpx;
	border-color: rgba(0, 0, 0, 0.1);
	background-color: rgba(255, 255, 255, 0.7);
	/* #ifndef APP-PLUS-NVUE */
	box-sizing: border-box;
	/* #endif */
}

.header_btnMongol_left_back,
.header_btnMongol_left_home {
	width: 70rpx;
}

.header_transparentFixed {
	border-bottom-width: 0;
	background-color: transparent;
	background-image: transparent;
}

.header_transparentFixed_left_info {
	border-style: solid;
	border-width: 2rpx;
	border-color: rgba(0, 0, 0, 0.1);
	background-color: rgba(255, 255, 255, 0.7);
	border-radius: 33rpx;
	/* #ifndef APP-PLUS-NVUE */
	box-sizing: border-box;
	/* #endif */
}

.header_transparentFixed_colorWhite_left_info {
	border-style: solid;
	border-width: 2rpx;
	border-color: rgba(255, 255, 255, 0.3);
	background-color: rgba(0, 0, 0, 0.2);
}

//颜色白色
.header_colorWhite_btnMongol {
	border-style: solid;
	border-width: 2rpx;
	border-color: rgba(255, 255, 255, 0.3);
	background-color: rgba(0, 0, 0, 0.2);
}

.header_colorWhite_left_line {
	background-color: rgba(255, 255, 255, 0.3);
}
</style>
