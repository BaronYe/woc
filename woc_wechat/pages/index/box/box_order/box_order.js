const app = getApp()
var myUrl = app.globalData.serverUrl
Page({

  /**
   * 页面的初始数据
   */
  data: {
    orderId: '',
    order: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    const eventChannel = this.getOpenerEventChannel()
    // 监听acceptDataFromOpenerPage事件，获取上一页面通过eventChannel传送到当前页面的数据
    eventChannel.on('orderId', function (data) {
      console.log(data)
      that.data.orderId = data.data
    })
    wx.request({
      url: myUrl + '/api/nsOrder/getOrderById',
      data: {
        orderId: that.data.orderId
      },
      success(res) {
        console.log(res.data.data)
        that.setData({
          order: res.data.data
        })
      }
    })

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {
    wx.redirectTo({
      url: '/pages/index/box/box'
    })
  }
})