// pages/usercenter/user_info/user_info.js
const app = getApp()
var myUrl = app.globalData.serverUrl
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo: {
      gender: '',
      avatar: ''
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (option) {
    var that = this
    // console.log(option.query)
    const eventChannel = this.getOpenerEventChannel()
    // eventChannel.emit('acceptDataFromOpenedPage', { data: 'test' });
    // eventChannel.emit('someEvent', { data: 'test' });
    // 监听acceptDataFromOpenerPage事件，获取上一页面通过eventChannel传送到当前页面的数据
    eventChannel.on('userinfo', function (data) {
      console.log(data)
      if (data.data.gender = 1) {
        data.data.gender = '男'
      } else {
        data.data.gender = '女'
      }
      that.setuserinfo(data.data)
    })
  },

  change_image() {
    wx.chooseImage({
      count: 0,
      success: res => {
        console.log(res.tempFilePaths)
        console.log(this.data.userInfo.id)
        this.setData({
          'userInfo.avatar': res.tempFilePaths[0]
        })
        wx.uploadFile({
          filePath: res.tempFilePaths[0],
          name: 'file',
          url: myUrl + '/api/oss/upload',
          success: function (res) {
            var img = JSON.parse(res.data)
            console.log(img.data)
            var id = wx.getStorageSync('id')
            wx.request({
              url: myUrl + '/api/user/setUser',
              method: "POST",
              data: {
                id: id,
                avatar: img.data
              }
            })
          }
        })
      }
    })
  },

  tonickname() {
    wx.navigateTo({
      url: '../user_info/user_info_nickname/user_info_nickname',
    })
  },

  todes() {
    wx.navigateTo({
      url: '../user_info/user_info_des/user_info_des',
      success: res => {
        console.log(res)
        res.eventChannel.emit('userinfo', { data: this.data.userInfo })
      }
    })
  },

  setuserinfo(e) {
    this.setData({
      userInfo: e
    })
  },

  bindDateChange: function (e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      'userInfo.birthday': e.detail.value
    })
    wx.request({
      url: myUrl + '/api/user/setUser',
      method: "POST",
      data: {
        id: this.data.userInfo.id,
        birthday: e.detail.value
      }
    })
  },

  toaddress() {
    wx.navigateTo({
      url: '../user_info/user_info_address/user_info_address'
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