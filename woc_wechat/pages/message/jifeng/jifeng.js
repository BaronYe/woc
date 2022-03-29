const app = getApp()
var myUrl = app.globalData.serverUrl
var id = wx.getStorageSync('id')
var point = wx.getStorageSync('point')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    list: [],
    point: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    that.setData({
      point: point
    })
  },

  getall: function () {
    wx.request({
      url: myUrl + '/api/userPoint/receivePoint',
      data: {
        userId: id
      },
      success(res) {
        console.log(res)
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
    var that = this
    wx.request({
      url: myUrl + '/api/userPoint/getUserPoint',
      data: {
        page: 1,
        pagesize: 40,
        userId: id,
        sign: -1
      },
      success(res) {
        console.log(res.data.data)
        that.setData({
          list: res.data.data
        })
      }
    })
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