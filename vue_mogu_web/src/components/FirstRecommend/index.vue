<template>

  <div class="banner">    
    <div class="carousel-wrap" id="carousel">
        <transition-group tag="ul" class='slide-ul' name="list">
          <li v-for="(list,index) in slideList" :key="index" v-show="index===currentIndex" @mouseenter="stop" @mouseleave="go">
            <a href="javascript:void(0);"  >
              <img v-if="list.photoList" :src="list.photoList[0]" :alt="list.title" @click="goToInfo(list.uid)">
            </a>
            <div class="carousel-title">
              <span>{{list.title}}</span>
            </div>
          </li>
        </transition-group>
        <div class="carousel-items">
          <span v-for="(item,index) in slideList.length" :key="item.uid" :class="{'active':index===currentIndex}" @mouseover="change(index)"></span>
        </div>
    </div>	
  </div>	
</template>

<script>
import { getBlogByLevel } from "../../api/index";
export default {
  name: "FirstRecommend",
  data() {
    return {
      slideList: [],
      currentIndex: 0,
      timer: "",
      firstData: [] //；一级推荐数据
    };
  },
  created() {
    this.$nextTick(() => {
      this.timer = setInterval(() => {
        this.autoPlay();
      }, 4000);
    });

    var params = new URLSearchParams();
    params.append("level", 1);
    getBlogByLevel(params).then(response => {
      console.log("一级推荐", response);
      this.slideList = response.data.records;
    });
  },
  methods: {
    go() {
      this.timer = setInterval(() => {
        this.autoPlay();
      }, 4000);
    },
    stop() {
      clearInterval(this.timer);
      this.timer = null;
    },
    change(index) {
      this.currentIndex = index;
    },
    autoPlay() {
      this.currentIndex++;
      if (this.currentIndex > this.slideList.length - 1) {
        this.currentIndex = 0;
      }
    },
    //跳转到文章详情
    goToInfo(uid) {
      console.log("跳转到文章详情");
      this.$router.push({ path: "/info", query: { blogUid: uid } });
    }
  }
};
</script>

<style>
.carousel-wrap {
  height: 453px;
  width: 100%;
  overflow: hidden;
  background-color: #fff;
}

.slide-ul {
  position: relative;
  width: 100%;
  height: 100%;
}

.slide-ul li {
  position: absolute;
  width: 100%;
  height: 100%;
}

img {
  width: 100%;
  height: 100%;
}
.carousel-title {
  position: absolute;
  z-index: 10;
  bottom: 40px;
  height: 40px;
  width: 100%;
  line-height: 40px;
  text-align: center;
  background: rgba(0, 0, 0, 0.3);
}
.carousel-title span {
  color: white;
  font-size: 22px;
  display: inline-block;
}
.carousel-items {
  position: absolute;
  z-index: 10;
  bottom: 20px;
  width: 66%;
  margin: 0 auto;
  text-align: center;
  font-size: 0;
}

.carousel-items span {
  display: inline-block;
  height: 6px;
  width: 30px;
  margin: 0 3px;
  background-color: #b2b2b2;
  cursor: pointer;
}
.carousel-items .active {
  background-color: orange;
}

.list-enter-to {
  transition: all 1s ease;
  transform: translateX(0);
}

.list-leave-active {
  transition: all 1s ease;
  transform: translateX(-100%);
}

.list-enter {
  transform: translateX(100%);
}

.list-leave {
  transform: translateX(0);
}
</style>
