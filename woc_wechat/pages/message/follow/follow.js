// pages/message/zan/zan.js
const app = getApp()
var myUrl = app.globalData.serverUrl
Page({

  /**
   * 页面的初始数据
   */
  data: {
    id: null,
    currentTabIndex: 0,
    //关注
    follow: [],
    //粉丝
    followed: []
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
    this.getfollow()
    this.getfollowed()
  },

  getfollow: function () {
    var that = this
    var id = wx.getStorageSync('id')
    wx.request({
      url: myUrl + '/api/userFollow/getFollow',
      data: {
        userId: id,
        types: 0,
        page: 1,
        pagesize: 5
      },
      success: function (res) {
        console.log(res)
        let follow = that.data.follow
        for (let i = 0; i < res.data.data.length; i++) {
          follow.push(res.data.data[i])
        }
        that.setData({
          follow: follow
        })
      }
    })
  },

  getfollowed: function () {
    var that = this
    var id = wx.getStorageSync('id')
    wx.request({
      url: myUrl + '/api/userFollow/getFollow',
      data: {
        userId: id,
        types: 1,
        page: 1,
        pagesize: 5
      },
      success: function (res) {
        console.log(res)
        let followed = that.data.followed
        for (let i = 0; i < res.data.data.length; i++) {
          followed.push(res.data.data[i])
        }
        that.setData({
          followed: followed
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