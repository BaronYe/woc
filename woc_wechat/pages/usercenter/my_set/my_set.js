// pages/usercenter/my_set/my_set.js
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },

  out: function () {
    wx.clearStorageSync()
    wx.navigateBack({
      delta: 2,
    })
    wx.showToast({
      title: '您已注销登陆',
    })
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})