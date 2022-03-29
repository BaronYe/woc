const app = getApp()
var myUrl = app.globalData.serverUrl
Page({
  formSubmit: function (e) {
    if (e.detail.value.input) {
      console.log(e.detail.value.input)
      var userDesc = e.detail.value.input
      var uid = wx.getStorageSync('id')
      wx.request({
        url: myUrl + '/api/user/setUser',
        method: "POST",
        data: {
          id: uid,
          userDesc: userDesc
        },
        success: res => {
          wx.showToast({
            title: '保存成功',
          })
        }
      })
      wx.navigateBack({
        delta: 2,
      })
    } else {
      wx.showToast({
        title: '输入不能为空',
      })
    }
  },
  /**
   * 页面的初始数据
   */
  data: {
    userInfo: {}
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    const eventChannel = this.getOpenerEventChannel()
    eventChannel.on('userinfo', function (data) {
      console.log(data)
      that.setuserinfo(data.data)
    })
  },

  setuserinfo(e) {
    this.setData({
      userInfo: e
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