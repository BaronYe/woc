// pages/component/coupon.js
Component({
  options: {
    multipleSlots: true // 在组件定义时的选项中启用多slot支持
  },
  /**
   * 组件的属性列表
   */
  properties: {

    price:{
      type: Number,
      value: 30
    },
    max:{
      type: Number,
      value: 100
    },
    startTime:{
      type: String,
      value: "2017.03.10"
    },
    endTime:{
      type: String,
      value: "2017.03.30"
    },
    ifget:{
      type:Number,
      value:-1
    }

  },

  /**
   * 组件的初始数据
   */
  data: {
    //改为0 数字
    ifget:-1
  },

  /**
   * 组件的方法列表
   */
  methods: {
set(){
  //增加状态
  this.setData({
    ifget:0
  })
},

    get(){
      var myEventDetail = {} // detail对象，提供给事件监听函数
      var myEventOption = {} // 触发事件的选项
      this.triggerEvent('myevent', myEventDetail, myEventOption)
      //变为0 ，-1， 1
      this.setData({
        ifget:0
      })
    },
    use(){
      var myEventDetail = {} // detail对象，提供给事件监听函数
      var myEventOption = {} // 触发事件的选项
      this.triggerEvent('myuse', myEventDetail, myEventOption)
    }
  }
})
