<template>
  <div id="container" style="margin-left:100px; width: 100%; height: 300px;"></div>
<!--  <div id="container" style="margin-left:100px; width: 1000px; height: 500px;"></div>-->
</template>

<script>
  import { getBlogContributeCount } from "@/api/index";
  import echarts from "echarts";

  export default {
    mounted() {
      this.initDate();
      // this.initChart();
    },
    data() {
      return {
        contributeDate: [],
        blogContributeCount: [],
      }
    },
    created() {

    },
    methods: {
      initDate: function() {
        console.log("测试数据", this.getVirtulData(2016));
        getBlogContributeCount().then(response => {
          console.log("我来啦", response.data.blogContributeCount);
          if(response.code == "success") {

            var contributeDate = response.data.contributeDate;

            var blogContributeCount = response.data.blogContributeCount;

            this.contributeDate = response.data.contributeDate;

            this.blogContributeCount = response.data.blogContributeCount;

            let chart = echarts.init(document.getElementById('container'))

            let option = {
              //设置背景
              // backgroundColor: '#d0d0d0',

              title: {
                top: 30,
                text: '文章贡献度',
                subtext: '一年内博客提交的数量',
                left: 'center',
                textStyle: {
                  color: '#000'
                }
              },
              tooltip : {
                trigger: 'item'
              },
              legend: {
                top: '30',
                left: '100',
                data:['文章数', 'Top 12'],
                textStyle: {
                  // 设置字体颜色
                  color: '#000'
                }
              },
              calendar: [{
                top: 100,
                left: 'center',
                range: ['2018-12-25', '2019-12-25'],
                splitLine: {
                  show: true,
                  lineStyle: {
                    // 设置月份分割线的颜色
                    color: '#D3D3D3',
                    width: 4,
                    type: 'solid'
                  }
                },
                yearLabel: {show: false},
                dayLabel: {
                  nameMap: ["周一","周二","周三","周四","周五","周六","周日"], // 设置中文显示
                  textStyle: {
                    // 设置周显示颜色
                    color: '#000'
                  }
                },
                monthLabel: {
                  nameMap: 'cn', // 设置中文显示
                  textStyle: {
                    // 设置月显示颜色
                    color: '#000'
                  }
                },
                itemStyle: {
                  normal: {
                    // 设置背景颜色
                    color: '#FFFAF0',
                    borderWidth: 1,
                    // 设置方块分割线段颜色
                    borderColor: '#D3D3D3'
                  }
                }
              }],
              series : [
                {
                  name: '文章数',
                  type: 'scatter',
                  coordinateSystem: 'calendar',
                  data: blogContributeCount,
                  // 根据值设置原点大小
                  symbolSize: function (val) {
                    return val[1] / 0.3;
                  },
                  itemStyle: {
                    normal: {
                      // 设置圆点颜色
                      color: '#FF69B4'
                    }
                  }
                }
              ]
            };


            // let option = {
            //   title: {
            //     top: 30,
            //     left: 'center',
            //     text: '文章贡献度'
            //   },
            //   tooltip: {},
            //   visualMap: {
            //     min: 0,
            //     max: 5,
            //     type: 'piecewise',
            //     orient: 'horizontal',
            //     left: 'center',
            //     top: 65,
            //     textStyle: {
            //       color: '#000'
            //     }
            //   },
            //   calendar: {
            //     top: 120,
            //     left: 30,
            //     right: 30,
            //     cellSize: ['auto', 13],
            //     range: ['2018-12-25', '2019-12-25'],
            //     itemStyle: {
            //       normal: {borderWidth: 0.5}
            //     },
            //     yearLabel: {show: false},
            //     dayLabel: {
            //       nameMap: ["周一","周二","周三","周四","周五","周六","周日"], // 设置中文显示
            //     },
            //     monthLabel: {
            //       nameMap: 'cn' // 设置中文显示
            //     }
            //   },
            //   series: {
            //     type: 'heatmap',
            //     coordinateSystem: 'calendar',
            //     data: blogContributeCount,
            //   },
            // };

            chart.setOption(option);

          }

        });
      },
      getVirtulData(year) {
        year = year || '2017';
        var date = +echarts.number.parseDate(year + '-01-01');
        var end = +echarts.number.parseDate((+year + 1) + '-01-01');
        var dayTime = 3600 * 24 * 1000;
        var data = [];
        for (var time = date; time < end; time += dayTime) {
          data.push([
            echarts.format.formatTime('yyyy-MM-dd', time),
            Math.floor(Math.random() * 10000)
          ]);
        }
        return data;
      },

      initChart() {

        // let chart = echarts.init(document.getElementById('container'))
        //
        // let option = {
        //   title: {
        //     top: 30,
        //     left: 'center',
        //     text: '文章贡献度'
        //   },
        //   tooltip: {},
        //   visualMap: {
        //     min: 0,
        //     max: 10,
        //     type: 'piecewise',
        //     orient: 'horizontal',
        //     left: 'center',
        //     top: 65,
        //     textStyle: {
        //       color: '#000'
        //     }
        //   },
        //   calendar: {
        //     top: 120,
        //     left: 30,
        //     right: 30,
        //     cellSize: ['auto', 13],
        //     range: ["2018-12-25", "2019-12-25"],
        //     itemStyle: {
        //       normal: {borderWidth: 0.5}
        //     },
        //     yearLabel: {show: false},
        //     dayLabel: {
        //       nameMap: ["周一","周二","周三","周四","周五","周六","周日"], // 设置中文显示
        //       firstDay: 0 // 从周一开始
        //     },
        //     monthLabel: {
        //       nameMap: 'cn' // 设置中文显示
        //     }
        //   },
        //   series: {
        //     type: 'heatmap',
        //     coordinateSystem: 'calendar',
        //     data: this.getVirtulData(2016)
        //   }
        // };

        chart.setOption(option);

        // console.log(this.getVirtulData(2016));

      }
    }
  }

</script>
