const app = getApp()
var myUrl = app.globalData.serverUrl
Page({

  /**
   * 页面的初始数据
   */
  data: {
    goods: []
  },

  to_order() {
    var that = this
    wx.navigateTo({
      url: '../shop_order/shop_order',
      success: function (res) {
        res.eventChannel.emit('good', { data: that.data.goods })
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    console.log(options)
    let goodid = options.id;
    console.log(goodid)
    wx.request({
      url: myUrl + '/api/integralGoods/getIntegralGoodsById',
      data: {
        id: goodid
      },
      success(res) {
        console.log(res.data.data)
        that.setData({
          goods: res.data.data
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
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})