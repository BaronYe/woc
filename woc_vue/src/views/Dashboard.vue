<template>
  <div style="padding: 0 30px;">
    <div class="layui-card-header">
      <div>
        <span class="ns-card-title">实时概况</span>
        <span class="ns-card-sub">更新时间：{{ nowDate }}</span>
      </div>
    </div>
    <div class="layui-card-body">
      <div class="ns-survey-detail-con">
        <div class="ns-survey-detail-aco">
          <span>今日订单数</span>
        </div>
        <p class="ns-survey-detail-num ns-text-color">{{ home.todayOrderCount }}</p>
        <div class="ns-survey-detail-split"></div>
        <div class="ns-survey-detail-total">
          <span>订单总数</span>
          <span>{{ home.orderCount }}</span>
        </div>
      </div>
      <div class="ns-survey-detail-con">
        <div class="ns-survey-detail-aco">
          <span>今日新增会员数</span>
        </div>
        <p class="ns-survey-detail-num ns-text-color">{{ home.todayUserCount }}</p>
        <div class="ns-survey-detail-split"></div>
        <div class="ns-survey-detail-total">
          <span>会员总数</span>
          <span>{{ home.userCount }}</span>
        </div>
      </div>
      <div class="ns-survey-detail-con">
        <div class="ns-survey-detail-aco">
          <span>今日新增店铺数</span>
        </div>
        <p class="ns-survey-detail-num ns-text-color">{{ home.todayShopCount }}</p>
        <div class="ns-survey-detail-split"></div>
        <div class="ns-survey-detail-total">
          <span>店铺总数</span>
          <span>{{ home.shopCount }}</span>
        </div>
      </div>
      <div class="ns-survey-detail-con">
        <div class="ns-survey-detail-aco">
          <span>今日新增盲盒数</span>
        </div>
        <p class="ns-survey-detail-num ns-text-color">{{ home.todayBoxCount }}</p>
        <div class="ns-survey-detail-split"></div>
        <div class="ns-survey-detail-total">
          <span>盲盒总数</span>
          <span>{{ home.boxCount }}</span>
        </div>
      </div>
    </div>
    <div class="statistics-wrap">
      <div class="item">
        <div class="flex-box" style="background: rgba(255, 125, 68, 0.1);">
          <h5 class="title">待付款订单</h5>
          <div class="num">{{ home.paymentCount }}</div>
        </div>
        <div class="flex-box" style="background: rgba(255, 69, 68, 0.1);">
          <h5 class="title">待发货订单</h5>
          <div class="num">{{ home.sendCount }}</div>
        </div>
      </div>
      <div class="item">
        <div class="flex-box" style="background: rgba(80, 130, 255, 0.1);">
          <h5 class="title">待收货订单</h5>
          <div class="num">{{ home.shouCount }}</div>
        </div>
        <div class="flex-box" style="background: rgba(255, 125, 68, 0.1);">
          <h5 class="title">拼单中订单</h5>
          <div class="num">{{ home.groupCount }}</div>
        </div>
      </div>
      <div class="item">
        <div class="flex-box" style="background: rgba(60, 188, 66, 0.1);margin-right:0;">
          <h5 class="title">销售额(元)</h5>
          <div class="num">{{ home.money }}</div>
        </div>
<!--        <div class="flex-box">-->

<!--        </div>-->
      </div>
    </div>
    <div style="display: flex;">
      <schart class="wrapper" canvasId="myCanvas" :options="options"></schart>
    </div>
  </div>
</template>

<script>
import Schart from "vue-schart"; // 导入Schart组件
export default {

  data() {
    return {
      home: '',
      nowDate: '',
      options: {},
    };
  },
  components: {
    Schart,
  },
  mounted() {
    this.formatDate();
    this.doHomeFn();
    this.getNsOrderCount();
  },
  methods: {
    // 获取数据
    doHomeFn() {
      this.api.doHome().then(res => {
        this.home = res.data;
      });
    },
    // 获取时间
    formatDate() {
      let date = new Date();
      let year = date.getFullYear(); // 年
      let month = date.getMonth() + 1; // 月
      let day = date.getDate(); // 日
      let week = date.getDay(); // 星期
      let weekArr = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
      let hour = date.getHours(); // 时
      hour = hour < 10 ? '0' + hour : hour; // 如果只有一位，则前面补零
      let minute = date.getMinutes(); // 分
      minute = minute < 10 ? '0' + minute : minute; // 如果只有一位，则前面补零
      let second = date.getSeconds(); // 秒
      second = second < 10 ? '0' + second : second; // 如果只有一位，则前面补零
      this.nowDate = `${year}/${month}/${day} ${hour}:${minute}:${second} ${weekArr[week]}`;
    },
    // 7天订单数
    getNsOrderCount(){
      this.api.getNsOrderCount().then(res=>{
        let data = res.data;
        let a = [];
        let b = [];
        data.forEach((v,k)=>{
          a.push(v.item);
          b.push(v.value);
        })

        this.options = {
          type: "line",
          title: {
            text: "最近七天订单数（个）",
          },
          labels: a.reverse(),
          // labels: ["周一", "周二", "周三", "周四", "周五", "周六", "周日"],
          datasets: [
            {
              label: "订单数",
              data: b.reverse(),
              // data: [234, 278, 270, 190, 230,199,20],
            }
          ],
        };


      })
    }
  }
};
</script>

<style lang="less" scoped>
.layui-card-header {
  position: relative;
  color: #333;
  border-radius: 2px 2px 0 0;
  font-size: 14px;
  border: 0;
  height: auto;
  line-height: 16px;
  padding: 5px 0 15px 0;

  .ns-card-title {
    font-weight: 500;
    font-size: 14px;
  }

  .ns-card-sub {
    margin-left: 12px;
    color: #B7B8B7;
    font-size: 12px;
  }
}

.layui-card-body {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;

  .ns-survey-detail-con {
    display: inline-block;
    width: calc((100% - 90px) / 4);
    margin-right: 30px;
    margin-bottom: 30px;
    background-color: #FFF;
    padding: 0 20px;
    box-sizing: border-box;
    position: relative;

    &:last-child {
      margin-right: 0;
    }

    .ns-survey-detail-aco {
      font-size: 16px;
      line-height: 18px;
      margin-top: 20px;
      font-weight: 400;
    }

    .ns-survey-detail-num {
      font-size: 36px;
      font-weight: 400;
      margin-top: 30px;
    }

    .ns-text-color {
      color: #FF6A00 !important;
    }

    .ns-survey-detail-split {
      width: 100%;
      height: 1px;
      background-color: #FFFFFF;
      margin-top: 25px;
    }

    .ns-survey-detail-total {
      display: flex;
      justify-content: space-between;
      margin: 18px 0 15px;
      font-size: 14px;
      line-height: 16px;
    }
  }
}

.statistics-wrap {
  display: flex;

  .item {
    width: calc((100% - 90px) / 4);
    margin-right: 30px;
    margin-bottom: 30px;
    display: flex;

    .flex-box {
      flex: 1;
      background: #f5f5f5;
      height: 120px;
      display: flex;
      justify-content: center;
      align-items: center;
      flex-direction: column;
      cursor: pointer;

      &:first-child {
        margin-right: 30px;
      }

      .title {
        font-size: 14px;
      }

      .num {
        font-size: 28px;
        font-weight: 400;
        color: #FF8143;
        margin-top: 10px;
      }
    }
  }
}
.wrapper {
  width: 100%;
  //width: calc((100% - 30px) / 2);
  height: 600px;
}
</style>
