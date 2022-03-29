const app = getApp()
var myUrl = app.globalData.serverUrl
var id = wx.getStorageSync('id')
Page({
  data: {
    type: 'box',
    orderId: '',
    good: '',
    box: '',
    animationMain: null,//正面
    animationBack: null,//背面
    chooseSize: false,
    animationData: '',
    sellname: '',
    sellprice: '',
    sellmain: ''
  },
  onLoad: function (options) {
    var that = this
    const eventChannel = this.getOpenerEventChannel()
    // 监听acceptDataFromOpenerPage事件，获取上一页面通过eventChannel传送到当前页面的数据
    eventChannel.on('box', function (box) {
      console.log(box)
      that.setData({
        box: box.box
      })
    })
    eventChannel.on('orderId', function (data) {
      console.log(data)
      that.data.orderId = data.data
    })

    wx.request({
      url: myUrl + '/api/nsOrder/noBoxItem',
      data: {
        orderId: that.data.orderId
      },
      success(res) {
        console.log(res)
        that.setData({
          good: res.data.data
        })
        if (that.data.sellname == '') {
          that.setData({
            sellname: res.data.data.title
          })
        }
      }
    })
  },
  toAddress() {
    var that = this
    wx.navigateTo({
      url: '../../../address/address_list/address_list?type=' + "box",
      success(res) {
        res.eventChannel.emit('type', { type: that.data.type })
        res.eventChannel.emit('orderId', { data: that.data.orderId })
      }
    })
  },
  onReady() {
    this.animation_main = wx.createAnimation({
      duration: 1500,
      timingFunction: 'linear'
    })
    this.animation_back = wx.createAnimation({
      duration: 1500,
      timingFunction: 'linear'
    })
    this.animation_main.rotateY(180).step()
    this.animation_back.rotateY(0).step()
    this.setData({
      animationMain: this.animation_main.export(),
      animationBack: this.animation_back.export(),
    })
  },
  nameinput(e) {
    this.setData({
      sellname: e.detail.value
    })
  },
  priceinput(e) {
    this.setData({
      sellprice: e.detail.value
    })
  },
  maininput(e) {
    this.setData({
      sellmain: e.detail.value
    })
  },
  tosell() {
    var that = this
    if (that.data.sellprice == '') {
      wx.showToast({
        title: '请填写售价！',
      })
    } else {
      wx.request({
        url: myUrl + '/api/nsOrderSell/creareSellShop',
        method: 'POST',
        data: {
          sellUserId: id,
          orderId: that.data.orderId,
          title: that.data.sellname,
          price: that.data.sellprice,
          content: that.data.sellmain
        },
        success(res) {
          console.log(res)
          wx.navigateTo({
            url: '../../box_shop/box_shop',
          })
        }
      })
    }
  },
  // 显示遮罩层
  showshadow: function (e) {
    if (this.data.chooseSize == false) {
      this.chooseSezi()
    } else {
      this.hideModal()
    }
  },
  // 就选它动画函数
  chooseSezi: function (e) {
    // 用that取代this，防止不必要的情况发生
    var that = this;
    // 创建一个动画实例
    var animation = wx.createAnimation({
      // 动画持续时间
      duration: 200,
      // 定义动画效果，当前是匀速
      timingFunction: 'linear'
    })
    // 将该变量赋值给当前动画
    that.animation = animation
    // 先在y轴偏移，然后用step()完成一个动画
    animation.translateY(1000).step()
    // 用setData改变当前动画
    that.setData({
      // 通过export()方法导出数据
      animationData: animation.export(),
      // 改变view里面的Wx：if
      chooseSize: true
    })
    // 设置setTimeout来改变y轴偏移量，实现有感觉的滑动 滑动时间
    setTimeout(function () {
      animation.translateY(0).step()
      that.setData({
        animationData: animation.export(),
        clearcart: false
      })
    }, 100)
  },
  // 隐藏
  hideModal: function (e) {
    var that = this;
    var animation = wx.createAnimation({
      duration: 500,
      timingFunction: 'linear'
    })
    that.animation = animation
    animation.translateY(700).step()
    that.setData({
      animationData: animation.export()
    })
    setTimeout(function () {
      animation.translateY(0).step()
      that.setData({
        animationData: animation.export(),
        chooseSize: false
      })
    }, 500)
  },
  onUnload: function () {
    wx.navigateBack({
      delta: 1,
    })
  },
})