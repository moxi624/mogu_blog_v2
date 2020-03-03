<template>
  <div class="banner">
    <el-carousel class="bannerBox" indicator-position="outside" >
      <el-carousel-item  v-for="(list,index) in slideList" :key="index">
            <img
              style="width:100%; height:100%; display:block;cursor:pointer;"
              v-if="list.photoList"
              :src="PICTURE_HOST + list.photoList[0]"
              :alt="list.title"
              @click="goToInfo(list.uid)">
            <div class="carousel-title" @click="goToInfo(list.uid)">
              <span>{{list.title}}</span>
            </div>
      </el-carousel-item>
    </el-carousel>
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
    var params = new URLSearchParams();
    params.append("level", 1);
    params.append("useSort", 1);
    getBlogByLevel(params).then(response => {
      this.slideList = response.data.records;
    });
  },
  methods: {
    //跳转到文章详情
    goToInfo(uid) {
      let routeData = this.$router.resolve({
        path: "/info",
        query: { blogUid: uid }
      });
      window.open(routeData.href, '_blank');
    }
  }
};
</script>

<style>
  .el-carousel__container {
    height: 450px !important;;
  }
.carousel-title span {
  color: white;
  font-size: 22px;
  display: inline-block;
}

@media only screen and (max-width: 1200px) {
  .el-carousel__container {
    height: 360px !important;;
  }
}
  @media only screen and (max-width: 1000px) {
    .el-carousel__container {
      height: 340px !important;;
    }
  }

  @media only screen and (max-width: 960px) {
    .el-carousel__container {
      height: 280px !important;;
    }
  }

  @media only screen and (max-width: 500px) {
    .el-carousel__container {
      height: 200px !important;;
    }
  }

/*  .right {*/
/*    width: 60px;*/
/*    height: 60px;*/
/*    font-size: 60px;*/
/*    top: 27%;*/
/*    right: 0%;*/
/*  }*/
/*  .carousel-wrap {*/
/*    height: 250px;*/
/*    width: 100%;*/
/*    overflow: hidden;*/
/*    background-color: #fff;*/
/*  }*/

/*  .carousel-title span {*/
/*    color: white;*/
/*    font-size: 16px;*/
/*    display: inline-block;*/
/*  }*/
/*}*/

/*@media only screen and (max-width: 500px) {*/
/*  .left {*/
/*    width: 50px;*/
/*    height: 50px;*/
/*    font-size: 50px;*/
/*    top: 27%;*/
/*  }*/

/*  .right {*/
/*    width: 50px;*/
/*    height: 50px;*/
/*    font-size: 50px;*/
/*    top: 27%;*/
/*    right: 0%;*/
/*  }*/

/*  .carousel-wrap {*/
/*    height: 200px;*/
/*    width: 100%;*/
/*    overflow: hidden;*/
/*    background-color: #fff;*/
/*  }*/
/*}*/

/*.slide-ul {*/
/*  position: relative;*/
/*  width: 100%;*/
/*  height: 100%;*/
/*}*/

/*.slide-ul li {*/
/*  position: absolute;*/
/*  width: 100%;*/
/*  height: 100%;*/
/*}*/

/*img {*/
/*  width: 100%;*/
/*  height: auto;*/
/*}*/
.carousel-title {
  cursor: pointer;
  position: absolute;
  z-index: 10;
  bottom: 40px;
  height: 40px;
  width: 100%;
  line-height: 40px;
  text-align: center;
  background: rgba(0, 0, 0, 0.3);
}

/*.carousel-items {*/
/*  position: absolute;*/
/*  z-index: 10;*/
/*  bottom: 20px;*/
/*  width: 100%;*/
/*  margin: 0 auto;*/
/*  text-align: center;*/
/*  font-size: 0;*/
/*}*/

/*.carousel-items span {*/
/*  display: inline-block;*/
/*  height: 6px;*/
/*  width: 30px;*/
/*  margin: 0 3px;*/
/*  background-color: #b2b2b2;*/
/*  cursor: pointer;*/
/*}*/
/*.carousel-items .active {*/
/*  background-color: orange;*/
/*}*/

/*.list-enter-to {*/
/*  transition: all 1s ease;*/
/*  transform: translateX(0);*/
/*}*/

/*.list-leave-active {*/
/*  transition: all 1s ease;*/
/*  transform: translateX(-100%);*/
/*}*/

/*.list-enter {*/
/*  transform: translateX(100%);*/
/*}*/

/*.list-leave {*/
/*  transform: translateX(0);*/
/*}*/

.el-carousel__item h3 {
  color: #475669;
  font-size: 18px;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}
</style>
