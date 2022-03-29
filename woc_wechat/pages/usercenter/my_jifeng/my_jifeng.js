const app = getApp()
var myUrl = app.globalData.serverUrl
var id = wx.getStorageSync('id')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    detail: []
  },

  toshop() {
    wx.navigateTo({
      url: '../my_jifeng/jifeng_shop/jifeng_shop',
    })
  },

  toorder(){
    wx.navigateTo({
      url: '../my_jifeng/jifeng_order/jifeng_order',
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    wx.request({
      url: myUrl + '/api/userPoint/getUserPoint',
      data: {
        page: 1,
        pagesize: 50,
        userId: id
      },
      success(res) {
        console.log(res.data.data)
        var detail = that.data.detail
        for (let i = 0; i < res.data.data.length; i++) {
          detail.push(res.data.data[i])
        }
        that.setData({
          detail: detail
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