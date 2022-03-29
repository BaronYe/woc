const app = getApp()
var myUrl = app.globalData.serverUrl

Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo: {},
    dongtai: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (option) {
    var that = this
    // console.log(option.query)
    const eventChannel = this.getOpenerEventChannel()
    eventChannel.on('userInfo', function (data) {
      console.log(data)
      that.setuserinfo(data.data)
    })
    var id = wx.getStorageSync('id')
    wx.request({
      url: myUrl + '/api/dynamic/getDynamicByUserId',
      data: {
        userId: id,
        page: 1,
        pagesize: 5
      },
      success: function (res) {
        console.log(res.data.data[0])
        let dongtai = that.data.dongtai
        for (let i = 0; i < res.data.data.length; i++) {
          dongtai.push(res.data.data[i])
        }
        that.setData({
          dongtai: dongtai
        })
        console.log(dongtai)
      }
    })
  },

  setuserinfo(e) {
    this.setData({
      userInfo: e
    })
  },

  dianzan() {
    wx.request({
      url: myUrl + '/api/dynamic/setDynamicZan',
      method: 'POST',
      data: {
        dynamicId: 8,
        userId: 1
      },
      success: function (res) {
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