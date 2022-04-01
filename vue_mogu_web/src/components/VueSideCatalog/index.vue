<template>
  <div
    class="side-catalog"
    :style="{height}"
  >
    <div
      v-if="title"
      class="side-catalog__title"
    >
      {{title}}
    </div>
    <div class="side-catalog__list">
      <div>
        <i
          v-if="lineShow"
          class="side-catalog__list-line"
          :style="lineLeft!==undefined?{left:lineLeft+'px'}:''"
        ></i>
        <div
          v-for="(item) in topList"
          :key="item.ref"
          :style="[
          {'padding-left': iconLeft ? '0px' : getTitleMargin(item.level)},
          active===item.ref ? {color: activeColor}: ''
        ]"
          class="side-catalog__list-item"
          @click="activeAnchor(item.ref)"
          :class="{
            'side-catalog__list-item--active': active===item.ref,
            'side-catalog__list-item--child': isChildren(item.level)
          }"
        >
          <!-- 每行插槽 -->
          <slot
            name="row"
            v-bind:level="item.level"
            v-bind:isActive="active===item.ref"
            v-bind:title="item.title"
          >
            <!-- 每行icon插槽 -->
            <slot
              name="default"
              v-bind:level="item.level"
              v-bind:isActive="active===item.ref"
            >
              <i
                class="side-catalog__list-item-icon"
                :class="{
              'side-catalog__list-item-icon--child': isChildren(item.level)
            }"
                :style="active===item.ref ? {color: activeColor}: ''"
              />
            </slot>
            <span
              class="side-catalog__list-item-title"
              :class="[
            `side-catalog__list-item-title--level${item.level || 1}`
          ]"
              :title="item.title"
              :style="[
            active===item.ref ? {color: activeColor}: '',
            {'padding-left': iconLeft ?  getTitleMargin(item.level) : ''}
            ]"
            >{{ item.title }}</span>
          </slot>
        </div>
      </div>

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
      watch: {
        type: Boolean,
        default: false
      },
      // 绑定scroll事件的dom的class
      // 该元素必须为定位元素或者最近的 table,td,th,body
      innerScroll: {
        type: Boolean,
        default: false
      },
      htmlContent: {
        type: String,
        default: '',
      },
      container: {
        type: String,
        required: true
        // default: ""
      },
      levelList: {
        type: Array,
        default() {
          return ["h2", "h3", "h4", "h5"];
        }
      },
      activeColor: {
        type: String,
        default: "#006bff"
      },
      height: {
        type: String,
        default: ""
      },
      levelGap: {
        type: Number,
        default: 20
      },
      iconLeft: {
        type: Boolean,
        default: false
      },
      title: {
        type: String,
        default: ""
      },
      lineLeft: {
        type: Number,
        default: 22
      },
      lineShow: {
        type: Boolean,
        default: true
      }
    },
    data() {
      return {
        active: "",
        refTopMap: {},
        topList: [],
        reverseTopList: [],
        isBeforeDestroy: false,
        observer: null,
        itemClicking: false,
        debounceIntoView: null,
        throttleScroll: null,
      };
    },
    computed: {
      scrollElement() {
        return this.innerScroll ? document.querySelector(this.container) : window;
      },
      scrollToEle() {
        return this.innerScroll ? this.scrollElement : document.documentElement;
      }
    },
    watch: {
      //这样使用watch时有一个特点，就是当值第一次绑定的时候，不会执行监听函数，只有值发生改变才会执行。如果我们需要在最初绑定值的时候也执行函数，则就需要用到immediate属性。
      // 比如当父组件向子组件动态传值时，子组件props首次获取到父组件传来的默认值时，也需要执行函数，此时就需要将immediate设为true
      htmlContent: {
        handler: function (val, oldVal) {
          this.debounceIntoView = debounce(this.activeIntoView, 250);
          this.throttleScroll = throttle(this.scrollHandle, 200);
          this.setOffsetParent();
          this.setTopList();
          this.initActive();
          this.scrollElement.addEventListener("scroll", this.throttleScroll);
          if (!this.watch) return;
          // 等待dom渲染完成之后监听
          setTimeout(() => {
            this.setWatcher();
          }, 200);
        },
        immediate: true,
      }
    },
    async mounted() {
      this.debounceIntoView = debounce(this.activeIntoView, 250);
      this.throttleScroll = throttle(this.scrollHandle, 200);
      await this.setOffsetParent();
      await this.setTopList();
      this.initActive();
      this.scrollElement.addEventListener("scroll", this.throttleScroll);
      if (!this.watch) return;
      // 等待dom渲染完成之后监听
      setTimeout(() => {
        this.setWatcher();
      }, 200);
    },
    beforeDestroy() {
      if (!this.watch) return;
      // beforeDestroy时,解绑dom监听之前,会触发observer监听的setTopList函数
      // 导致报错,需要用变量控制
      this.isBeforeDestroy = true;
      // 解绑dom监听
      this.observer.disconnect();

      this.scrollElement.removeEventListener("scroll", this.throttleScroll);
    },
    methods: {
      // 点击title
      activeAnchor(ref) {
        if (this.active === ref) return;
        // 点击title 会触发scroll事件,在内容高度不够的情况下点击的title和active的title会有出入
        // 所以点击的时候先return掉scroll事件
        this.itemClicking = true;
        this.scrollToEle.scrollTop = this.refTopMap[ref];
        this.active = ref;
        this.debounceIntoView();
        // 等待页面滚动完成
        setTimeout(() => {
          this.itemClicking = false;
        }, 150);
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
      setTopList() {
        if (this.isBeforeDestroy) return;
        this.topList = [];
        if (this.refList.length) {
          this.topForList();
        } else {
          this.topForDom();
        }
        this.reverseTopList = JSON.parse(
          JSON.stringify(this.topList)
        ).reverse();
        // this.scrollHeight = this.scrollToEle.scrollHeight;
      },
      // scroll事件
      scrollHandle() {
        // 点击title的滚动不触发
        if (this.itemClicking) return;
        const { scrollTop, clientHeight, scrollHeight } = this.scrollToEle;
        // 到达顶部
        if (scrollTop === 0) {
          this.initActive();
          return;
        }
        // 到达底部
        if (scrollTop + clientHeight >= scrollHeight) {
          this.initActive(true);
          return;
        }
        this.reverseTopList.some(item => {
          if (scrollTop >= item.offsetTop) {
            this.active = item.ref;
            this.debounceIntoView();
            return true;
          }
          return false;
        });
      },
      initActive(last) {
        if (!this.topList.length) return;
        const index = last ? this.topList.length - 1 : 0;
        this.active = this.topList[index].ref;
        this.debounceIntoView(true);
      },
      activeIntoView(edge) {
        // 等active元素改变后
        this.$nextTick(() => {
          const activeEl = document.querySelector(
            ".side-catalog__list-item--active"
          );
          if (!activeEl) return;
          // 顶部或者底部 scrollIntoView为smooth时无效
          activeEl.scrollIntoView({
            block: "center",
            behavior: edge ? "auto" : "smooth"
          });
        });
      },
      getTitleMargin(level) {
        return level
          ? `${parseInt(level, 10) * this.levelGap}px`
          : this.levelGap + "px";
      },
      // 需要为scrollElement设置相对定位(offsetParent)
      // offsetParent(定位元素或者最近的 table,td,th,body)
      setOffsetParent() {
        if (!this.innerScroll) return;
        const ele = document.querySelector(this.container);
        if (ele.style.position) return;
        ele.style.position = "relative";
      },
      isChildren(level) {
        return level && level > 1;
      },
      setWatcher() {
        // 设置dom监听
        this.observer = new MutationObserver(debounce(this.setTopList, 700));
        this.observer.observe(document.querySelector(this.container), {
          childList: true,
          subtree: true,
          attributes: true
        });
      },
      // 根据refList获取catalogList
      topForList() {
        this.refList.forEach(item => {
          const offsetTop = this.getRefDom(item.ref).offsetTop;
          const title = item.title || this.getRefDom(item.ref).innerText;
          this.topList.push({
            ref: item.ref,
            title,
            offsetTop,
            level: item.level
          });
          this.refTopMap[item.ref] = offsetTop;
        });
      },
      // 根据levelList获取catalogList
      topForDom() {
        let headlevel = {};
        this.levelList.forEach((item, index) => {
          headlevel[item] = index + 1;
        });
        const childrenList = Array.from(
          document.querySelectorAll(`${this.container}>*`)
        );
        childrenList.forEach((item, index) => {
          const nodeName = item.nodeName.toLowerCase();
          if (this.levelList.includes(nodeName)) {
            this.topList.push({
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
<style lang="scss" src="./main.scss"></style>
