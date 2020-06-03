<template>
  <div
    v-if="show"
    class="side-catalog"
  >
    <div class="side-catalog__title">
      <slot></slot>
    </div>
    <div
      v-for="(item) in catalogList"
      :key="item.ref"
      :style="{'padding-left': getTitleMargin(item.level)}"
      class="side-catalog__item"
      @click="anchorActive(item.ref)"
      :class="{
          'side-catalog__item--child': isChildren(item.level)
        }"
    >
      <i
        class="side-catalog__item-icon"
        :class="{
          'side-catalog__item-icon--active': active===item.ref,
          'side-catalog__item-icon--child': isChildren(item.level)
        }"
      />
      <span
        class="side-catalog__item-title"
        :class="[
          {'side-catalog__item-title--active': active===item.ref},
          `side-catalog__item--level${item.level || 1}`
        ]"
        :title="item.title"
      >{{ item.title }}</span>
    </div>
  </div>
</template>
<script>
import debounce from "lodash.debounce";
import throttle from "lodash.throttle";
export default {
  name: "SideCatalog",
  props: {
    refList: {
      type: Array,
      default() {
        return [
          // {
          //   title: 'name',
          //   ref: 'refname', //f
          //   level: 1,  //默认为1
          // },
        ];
      }
    },
    // 是否开启dom监听,dom有变化主动更新各个ref的offsetTop值
    openDomWatch: {
      type: Boolean,
      default: false
    },
    // 绑定scroll事件的dom的class
    // 该元素必须为定位元素或者最近的 table,td,th,body
    scrollElementSelector: {
      type: String,
      default: ""
    },
    containerElementSelector: {
      type: String,
      required: true
      // default: ""
    },
    headList: {
      type: Array,
      default() {
        return ["h2", "h3", "h4", "h5"];
      }
    }
  },
  data() {
    return {
      active: "",
      refTopMap: {},
      refTopList: [],
      catalogList: [],
      reverseCatalogList: [],
      isBeforeDestroy: false,
      observer: null,
      isClick: false,
      show: false
    };
  },
  computed: {
    scrollElement() {
      return this.scrollElementSelector
        ? document.querySelector(this.scrollElementSelector)
        : window;
    },
    scrollToEle() {
      return this.scrollElementSelector
        ? this.scrollElement
        : document.documentElement;
    }
  },
  async mounted() {
    await this.setOffsetParent();
    await this.setCatalogList();
    this.initActive();
    this.show = true;
    this.scrollElement.addEventListener(
      "scroll",
      throttle(this.scrollHandle, 50)
    );
    setTimeout(() => {
      this.setWatcher();
    }, 50);
  },
  beforeDestroy() {
    if (this.openDomWatch) {
      // beforeDestroy时,解绑dom监听之前,偶尔会触发observer监听的setCatalogList函数
      // 导致报错,需要用变量控制
      this.isBeforeDestroy = true;
      // 解绑dom监听
      this.observer.disconnect();
    }
    this.scrollElement.removeEventListener("scroll", this.scrollHandle);
  },
  methods: {
    // 点击title
    anchorActive(ref) {
      if (this.active === ref) return;
      // 点击title 会触发scroll事件,在内容高度不够的情况下点击的title和active的title会有出入
      // 所以点击的时候先return掉scroll事件
      this.isClick = true;
      this.scrollToEle.scrollTop = this.refTopMap[ref];
      this.active = ref;
      setTimeout(() => {
        this.isClick = false;
      }, 50);
      this.$emit("title-click", ref);
    },
    // 获取ref的dom
    getRefDom(_ref) {
      /**
       * 获取ref的dom元素有以下四种情况
       * 1. ref在循环中, ref是dom元素 => ref[0]
       * 2. ref在循环中, ref是vue实例 => ref[0].$el
       * 3. ref不在循环中, ref是dom元素 => ref
       * 4. ref不在循环中, ref不是vue实例 => ref.$el
       */
      const ref = this.$parent.$refs[_ref];
      if (Array.isArray(ref)) {
        return this.vueOrDom(ref[0]);
      }
      return this.vueOrDom(ref);
    },
    // ref 是vue还是dom
    vueOrDom(ref) {
      if (ref instanceof HTMLElement) return ref;
      if (ref._isVue) return ref.$el;
    },
    // 获取ref offsetTop数组
    setCatalogList() {
      if (this.isBeforeDestroy) return;
      this.catalogList = [];
      if (this.refList.length) {
        this.catalogForList();
      } else {
        this.catalogForDom();
      }
      this.reverseCatalogList = JSON.parse(
        JSON.stringify(this.catalogList)
      ).reverse();
      // 返回子组件个数
      if(this.catalogList.length > 0) {
        this.$emit("catalogSum", this.catalogList.length)
        console.log("setArray", this.catalogList)
      }
    },
    // scroll事件
    scrollHandle(e) {
      if (this.isClick) return;
      const scrollTop = this.scrollElementSelector
        ? e.target.scrollTop
        : document.documentElement.scrollTop;
      if (scrollTop === 0) {
        this.initActive();
        return;
      }
      this.reverseCatalogList.some(item => {
        if (scrollTop >= item.offsetTop) {
          this.active = item.ref;
          return true;
        }
        return false;
      });
    },
    initActive() {
      if(!this.catalogList.length) return;
      this.active = this.catalogList[0].ref;
    },
    getTitleMargin(level) {
      return level ? `${parseInt(level, 10) * 15}px` : "15px";
    },
    // 需要为scrollElement设置相对定位(offsetParent)
    // offsetParent(定位元素或者最近的 table,td,th,body)
    setOffsetParent() {
      if (!this.scrollElementSelector) return;
      const ele = document.querySelector(this.scrollElementSelector);
      if (ele.style.position) return;
      ele.style.position = "relative";
    },
    isChildren(level) {
      return level && level > 1;
    },
    setWatcher() {
      if (this.openDomWatch) {
        // 设置dom监听
        this.observer = new MutationObserver(debounce(this.setCatalogList, 200));
        this.observer.observe(
          document.querySelector(this.containerElementSelector),
          {
            childList: true,
            subtree: true,
            attributes: true
          }
        );
      }
    },
    catalogForList() {
      this.refList.forEach(item => {
          const offsetTop = this.getRefDom(item.ref).offsetTop;
          const title =  item.title ||  this.getRefDom(item.ref).innerText;
          this.catalogList.push({
            ref: item.ref,
            title,
            offsetTop,
            level: item.level
          });
          this.refTopMap[item.ref] = offsetTop;
        });
    },
    catalogForDom(){
      let headlevel = {};
        this.headList.forEach((item, index) => {
          headlevel[item] = index + 1;
        });
        const childrenList = Array.from(
          document.querySelectorAll(`${this.containerElementSelector}>*`)
        );
        childrenList.forEach((item, index) => {
          const nodeName = item.nodeName.toLowerCase();
          if (this.headList.includes(nodeName)) {
            this.catalogList.push({
              ref: `${item.nodeName}-${index}`,
              title: item.innerText,
              offsetTop: item.offsetTop,
              level: headlevel[nodeName]
            });
            this.refTopMap[`${item.nodeName}-${index}`] = item.offsetTop;
          }
        });
    }
  }
};
</script>
<style scoped lang="scss" src="./main.scss"></style>
