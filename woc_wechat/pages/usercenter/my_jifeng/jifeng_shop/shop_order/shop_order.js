const app = getApp()
var myUrl = app.globalData.serverUrl
var userid = wx.getStorageSync('id')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    good: [],
    address: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    const eventChannel = this.getOpenerEventChannel()
    eventChannel.on('good', function (data) {
      console.log(data.data)
      that.setData({
        good: data.data
      })
    })
    wx.request({
      url: myUrl + '/api/address/getAddressByUid',
      data: {
        userId: userid
      }, success(res) {
        console.log(res)
        that.setData({
          address: res.data.data[0]
        })
      }
    })
  },

  todingdan() {
    var that = this
    var goodid = that.data.good.id
    var addressid = that.data.address.id
    wx.request({
      url: myUrl + '/api/nsIntegralOrder/createIntegralOrder',
      method:'POST',
      data: {
        integralGoodsId: goodid,
        userId: userid,
        addressId: addressid
      },
      success(res) {
        console.log(res)
        if(res.data.code = 400){
          wx.showToast({
            title: '积分不足',
          })
        }else{
          wx.showToast({
            title: '兑换成功',
          })
          wx.navigateBack({
            delta: 5,
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