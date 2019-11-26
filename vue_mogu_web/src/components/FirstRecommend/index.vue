<template>
  <div class="banner">
    <div class="carousel-wrap" id="carousel"  @mouseenter="inDiv" @mouseleave="outDiv">
      <span v-show="isShow" class="left" @click="leftChange"><</span>
      <span v-show="isShow" class="right" @click="rightChange">></span>
      <transition-group tag="ul" class="slide-ul" name="list">
        <li
          v-for="(list,index) in slideList"
          :key="index"
          v-show="index===currentIndex"
          @mouseenter="stop"
          @mouseleave="go"
        >
          <a href="javascript:void(0);">
            <img
              v-if="list.photoList"
              :src="PICTURE_HOST + list.photoList[0]"
              :alt="list.title"
              @click="goToInfo(list.uid)"
            >
          </a>
          <div class="carousel-title">
            <span>{{list.title}}</span>
          </div>
        </li>
      </transition-group>
      <div class="carousel-items">
        <span
          v-for="(item,index) in slideList.length"
          :key="item.uid"
          :class="{'active':index===currentIndex}"
          @mouseover="change(index)"
        ></span>
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
      PICTURE_HOST: process.env.PICTURE_HOST,
      isShow: false, //控制左右滑动按钮是否显示
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
    inDiv: function() {
      this.isShow = true;
    },
    outDiv: function() {
      this.isShow = false;
    },
    leftChange: function() {
      var currentIndex = this.currentIndex - 1;
      if (currentIndex < 0) {
        this.currentIndex = this.slideList.length - 1;
      } else {
        this.currentIndex = currentIndex;
      }

      console.log(this.currentIndex);
    },
    rightChange: function() {
      var currentIndex = this.currentIndex + 1;
      if (currentIndex >= this.slideList.length) {
        this.currentIndex = 0;
      } else {
        this.currentIndex = currentIndex;
      }
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
      let routeData = this.$router.resolve({
        path: "/info",
        query: { blogUid: uid }
      });
      window.open(routeData.href, "_blank");
    }
  }
};
</script>

<style>
.carousel-wrap {
  position: relative;
}

.left:hover {
  color: rgba(90, 88, 88, 0.8);
}
.right:hover {
  color: rgba(90, 88, 88, 0.9);
}

.left {
  font-size: 80px;
  color: rgba(223, 219, 219, 0.8);
  position: absolute;
  z-index: 99999;
  cursor: pointer;
  top: 28%;
}

.right {
  font-size: 80px;
  color: rgba(223, 219, 219, 0.8);
  position: absolute;
  z-index: 99999;
  cursor: pointer;
  top: 28%;
  right: 0%;
}

.carousel-wrap {
  height: 453px;
  width: 100%;
  overflow: hidden;
  background-color: #fff;
}

.carousel-title span {
  color: white;
  font-size: 22px;
  display: inline-block;
}

@media only screen and (max-width: 1100px) {
  .left {
    width: 80px;
    height: 80px;
    font-size: 80px;
    top: 27%;
  }

  .right {
    width: 80px;
    height: 80px;
    font-size: 80px;
    top: 27%;
    right: 0%;
  }
  .carousel-wrap {
    height: 380px;
    width: 100%;
    overflow: hidden;
    background-color: #fff;
  }

  .carousel-title span {
    color: white;
    font-size: 20px;
    display: inline-block;
  }
}

@media only screen and (max-width: 900px) {
  .carousel-wrap {
    height: 300px;
    width: 100%;
    overflow: hidden;
    background-color: #fff;
  }

  .carousel-title span {
    color: white;
    font-size: 18px;
    display: inline-block;
  }
}

@media only screen and (max-width: 700px) {
  .left {
    width: 60px;
    height: 60px;
    font-size: 60px;
    top: 27%;
  }

  .right {
    width: 60px;
    height: 60px;
    font-size: 60px;
    top: 27%;
    right: 0%;
  }
  .carousel-wrap {
    height: 250px;
    width: 100%;
    overflow: hidden;
    background-color: #fff;
  }

  .carousel-title span {
    color: white;
    font-size: 16px;
    display: inline-block;
  }
}

@media only screen and (max-width: 500px) {
  .left {
    width: 50px;
    height: 50px;
    font-size: 50px;
    top: 27%;
  }

  .right {
    width: 50px;
    height: 50px;
    font-size: 50px;
    top: 27%;
    right: 0%;
  }

  .carousel-wrap {
    height: 200px;
    width: 100%;
    overflow: hidden;
    background-color: #fff;
  }
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
  height: auto;
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

.carousel-items {
  position: absolute;
  z-index: 10;
  bottom: 20px;
  width: 100%;
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
