const app = getApp()
var myUrl = app.globalData.serverUrl
var userid = wx.getStorageSync('id')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    aid: '',
    address: {},
    region: [],
    switch: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    console.log(options)
    let aid = options.aid
    wx.request({
      url: myUrl + '/api/address/getAddressByid',
      data: {
        id: aid
      },
      success(res) {
        console.log(res.data.data)
        that.setData({
          address: res.data.data,
          aid: aid
        })
        if (res.data.data.isDefault == 0) {
          that.setData({
            switch: false
          })
        } else {
          that.setData({
            switch: true
          })
        }
      }
    })
  },
  bindRegionChange: function (e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      region: e.detail.value,
      'address.province': e.detail.value[0],
      'address.city': e.detail.value[1],
      'address.region': e.detail.value[2]
    })
  },
  switchChange(e) {
    var that = this
    console.log(e.detail.value)
    that.setData({
      switch: e.detail.value
    })
    if (e.detail.value == true) {
      that.setData({
        'address.isDefault': 1
      })
    } else {
      that.setData({
        'address.isDefault': 0
      })
    }
  },
  editname(e) {
    var that = this
    console.log(e.detail.value)
    that.setData({
      'address.name': e.detail.value
    })
  },
  editmobile(e) {
    var that = this
    console.log(e.detail.value)
    that.setData({
      'address.mobile': e.detail.value
    })
  },
  editdetail(e) {
    var that = this
    console.log(e.detail.value)
    that.setData({
      'address.detailed': e.detail.value
    })
  },
  toedit() {
    var that = this
    var address = that.data.address
    wx.request({
      url: myUrl + '/api/address/setAddress',
      method: 'POST',
      data: {
        id: parseInt(that.data.aid),
        userid: userid,
        name: address.name,
        mobile: address.mobile,
        province: address.province,
        city: address.city,
        region: address.region,
        detailed: address.detailed,
        postalCOde: address.postalCOde,
        isDefault: address.isDefault
      },
      success(res) {
        console.log(res)
        if (res.data.code == 200) {
          wx.showToast({
            title: '修改成功',
          })
          wx.navigateBack({
            delta: 0,
          })
        } else {
          wx.showToast({
            title: '修改失败',
            icon: 'error'
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