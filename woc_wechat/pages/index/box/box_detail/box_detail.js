const app = getApp()
var myUrl = app.globalData.serverUrl
var id = wx.getStorageSync('id')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    card: [0, 1, 2, 3, 4, 5, 6, 7, 8],
    chooseSize: false,
    chooseSize1: false,
    animationData: '',
    radio1: '',
    currentTabIndex: null,
    balance: '',
    good: [],
    goodlist: [],
    paymentType: 1,
    orderId: ''
  },

  /**
 * 生命周期函数--监听页面加载
 */
  onLoad: function (options) {
    var that = this
    let boxid = options.id;
    var balance = wx.getStorageSync('balance')
    that.setData({
      balance: balance
    })
    wx.request({
      url: myUrl + '/api/box/getBoxById',
      data: {
        id: boxid
      },
      success(res) {
        console.log(res)
        that.setData({
          good: res.data.data
        })
      }
    })
    wx.request({
      url: myUrl + '/api/box/getBoxItem',
      data: {
        boxId: boxid
      },
      success(res) {
        that.setData({
          goodlist: res.data.data
        })
      }
    })
  },

  onTabItemTap: function (e) {
    console.log(e)
    this.setData({
      //拿到索引并改变动态
      currentTabIndex: e.target.dataset.index
    })
  },

  topay() {
    var that = this
    var paymentType = parseInt(that.data.paymentType)
    wx.request({
      url: myUrl + '/api/nsOrder/createOrder',
      method: 'POST',
      data: {
        userId: id,
        boxId: that.data.good.id,
        paymentType: paymentType,
        isTuangou: 0
      },
      success(res) {
        console.log(res.data)
        if (res.data.code == 400) {
          wx.showToast({
            title: '该盲盒已经卖光啦',
            icon: 'none'
          })
        } else {
          console.log(res.data.data.orderId)
          that.data.orderId = res.data.data.orderId
          wx.navigateTo({
            url: '../box_on/box_on',
            success: function (res) {
              // 通过eventChannel向被打开页面传送数据
              res.eventChannel.emit('orderId', { data: that.data.orderId })
              res.eventChannel.emit('box', { box: that.data.good })
            }
          })
        }
      }
    })
  },

  random() {
    var count = parseInt(Math.random(0, 8) * 10 - 1)
    console.log(count)
    this.setData({
      currentTabIndex: count
    })
  },
  radioChange(e) {
    console.log('radio发生change事件，携带value值为：', e.detail.value)
    this.setData({
      paymentType: e.detail.value
    })
  },
  // 显示遮罩层
  showshadow: function (e) {
    if (this.data.currentTabIndex == null) {
      wx.showToast({
        title: '请选择盲盒',
        icon: "error"
      })
    } else {
      if (this.data.chooseSize == false) {
        this.chooseSezi()
      } else {
        this.hideModal()
      }
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
  // 打开商品详情
  showshadow1: function (e) {
    if (this.data.chooseSize1 == false) {
      this.chooseSezi1()
    } else {
      this.hideModal()
    }
  },
  // 商品详情动画函数
  chooseSezi1: function (e) {
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
      chooseSize1: true
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
  hideModal1: function (e) {
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
        chooseSize1: false
      })
    }, 500)
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

  topin(){
    var that = this
    var paymentType = parseInt(that.data.paymentType)
    wx.request({
      url: myUrl + '/api/nsOrder/createOrder',
      method: 'POST',
      data: {
        userId: id,
        boxId: that.data.good.id,
        paymentType: paymentType,
        isTuangou: 1
      },
      success(res) {
        console.log(res.data)
        if (res.data.code == 400) {
          wx.showToast({
            title: '该盲盒已经卖光啦',
            icon: 'none'
          })
        } else {
          console.log(res.data.data.orderId)
          that.data.orderId = res.data.data.orderId
          wx.navigateTo({
            url: '../box_on/box_on',
            success: function (res) {
              // 通过eventChannel向被打开页面传送数据
              res.eventChannel.emit('orderId', { data: that.data.orderId })
              res.eventChannel.emit('box', { box: that.data.good })
            }
          })
        }
      }
    })
  },


  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },
})