// pages/search/search.js
const app = getApp()
const url = app.globalData.serverUrl
const id = wx.getStorageSync('id')

Page({

  /**
   * 页面的初始数据
   */
  data: {
    sign_shop: true,
    sign_activity: false,
    sign_user: false,
    shop: [],
    activity: [],
    user: [],
    search_value: ""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var me = this

    // wx.request({
    //   url: url + '/api/serach/doSerach',
    //   data: {
    //     types: 2,
    //     page: 1,
    //     pagesize: 5,
    //     serach: ""
    //   },
    //   success: function (res) {
    //     console.log(res.data.data)
    //     me.setData({
    //       followUser: res.data.data
    //     })
    //   }
    // })
  },
  cancle(e) {
    // 取消关注
    console.log(e)
    wx.request({
      url: url + '/api/userFollow/setFollow',
      method: 'POST',
      data: {
        userId: id,
        followId: parseInt(e.currentTarget.id)
      },
      success: function (res) {

        if (res.data.code == 200) {

        }
      }
    })

  },
  search(e) {
    console.log(e.detail.value)
    var me = this
    me.setData({
      search_value: e.detail.value
    })

  },
  change_shop() {
    this.setData({
      sign_shop: true,
      sign_activity: false,
      sign_user: false
    })
  },
  change_activity() {
    this.setData({
      sign_shop: false,
      sign_activity: true,
      sign_user: false
    })
  },
  change_user() {
    this.setData({
      sign_shop: false,
      sign_activity: false,
      sign_user: true
    })
  },
  query() {
    var me = this
    var value = this.data.search_value
    var type
    if (this.data.sign_shop == true) {
      type = 0
    }
    if (this.data.sign_activity == true) {
      type = 1
    }
    if (this.data.sign_user == true) {
      type = 2
    }
    var url = app.globalData.serverUrl
    // console.log(value)
    // console.log(type)
    wx.request({
      url: url + "/api/serach/doSerach",
      data: {
        types: type,
        page: 1,
        pagesize: 5,
        serach: value
      },
      success: function (res) {
        console.log(res.data.data)

        if (type == 0) {
          for (var index in res.data.data) {
            res.data.data[index].image = res.data.data[index].banner[0]
          }
          me.setData({
            shop: res.data.data
          })
        }
        if (type == 1) {
          me.setData({
            activity: res.data.data
          })
        }
        if (type == 2) {
          me.setData({
            user: res.data.data
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