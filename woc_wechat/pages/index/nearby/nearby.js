const app = getApp()
var myUrl = app.globalData.serverUrl
Page({

  /**
   * 页面的初始数据
   */
  data: {
    information: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    wx.request({
      url: myUrl + '/api/shop/getShopDoWechat',
      data: {
        page: 1,
        pagesize: 20,
        regionId: -1,
        circleId: -1,
        types: "0"
      },
      success(res) {
        console.log(res.data.data)
        that.setData({
          information: res.data.data
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
  gotoDetail(e) {
    console.log(e.currentTarget.dataset.index)
    let shopid = e.currentTarget.dataset.index
    wx.navigateTo({
      url: '../shop/shop?id=' + shopid
    })

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