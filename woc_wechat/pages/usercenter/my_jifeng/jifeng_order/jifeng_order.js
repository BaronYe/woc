const app = getApp()
var myUrl = app.globalData.serverUrl
var id = wx.getStorageSync('id')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    list: [],
    fahuo: [],
    shouhuo: [],
    success: [],
    id: null,
    currentTabIndex: 0,
  },

  //Tab选项函数
  onTabItemTap: function (e) {
    this.setData({
      //拿到索引并改变动态
      currentTabIndex: e.target.dataset.index
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    wx.request({
      url: myUrl + '/api/nsIntegralOrder/getIntegralOrder',
      data: {
        page: 1,
        pagesize: 10,
        userId: id,
        orderNo: "",
        orderStatus: -1
      },
      success(res) {
        console.log(res.data.data)
        var list = that.data.list
        var fahuo = that.data.fahuo
        var shouhuo = that.data.shouhuo
        var success = that.data.success
        for (let i = 0; i < res.data.data.length; i++) {
          list.push(res.data.data[i])
          if (res.data.data[i].orderStatus == 1) {
            fahuo.push(res.data.data[i])
          } if (res.data.data[i].orderStatus == 2) {
            shouhuo.push(res.data.data[i])
          } if (res.data.data[i].orderStatus == 3) {
            success.push(res.data.data[i])
          }
        }
        that.setData({
          list: list,
          fahuo: fahuo,
          shouhuo: shouhuo,
          success: success
        })
      }
    })
  },

  todetail(e) {
    let orderid = e.currentTarget.dataset.index
    console.log(orderid)
    // wx.navigateTo({
    //   url: '../jifeng_detail/jifeng_detail',
    // })
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