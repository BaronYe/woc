const app = getApp()
var myUrl = app.globalData.serverUrl
var userid = wx.getStorageSync('id')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    type: '',
    adlist: [],
    good: [],
    orderId: '',
    addressId: '',
    show: false,
    animationData: '',
    addresid: ''
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    const eventChannel = this.getOpenerEventChannel()
    // 监听acceptDataFromOpenerPage事件，获取上一页面通过eventChannel传送到当前页面的数据
    eventChannel.on('type', function (type) {
      console.log(type)
      that.setData({
        type: type.type
      })
    })
    eventChannel.on('orderId', function (data) {
      console.log(data)
      that.setData({
        orderId: data.data
      })
    })
    eventChannel.on('good', function (good) {
      console.log(good)
      that.setData({
        good: good.good
      })
    })
  },

  radioChange(e) {
    console.log('radio发生change事件，携带value值为：', e.detail.value)
    this.setData({
      addressId: e.detail.value
    })
  },

  add() {
    wx.chooseAddress({
      success: (res) => {
        console.log(res)
        wx.request({
          url: myUrl + '/api/address/setAddress',
          method: "POST",
          data: {
            id: 0,
            userId: userid,
            name: res.userName,
            mobile: res.telNumber,
            province: res.provinceName,
            city: res.cityName,
            region: res.countyName,
            detailed: res.detailInfo,
            postalCode: res.postalCode,
            isDefault: 0
          },
          success: res => {
            wx.showToast({
              title: '地址保存成功',
            })
          }
        })
      },
      fail: function (err) {
        console.log(err)
      }
    })
  },

  detail(e) {
    let addresid = e.currentTarget.dataset.index
    this.data.addresid = addresid
    this.setData({
      show: true
    })
  },

  hideModal: function (e) {
    var that = this;
    var animation = wx.createAnimation({
      duration: 500,
      timingFunction: 'linear'
    })
    that.animation = animation
    animation.translateY(700).step()
    that.setData({
      animationData: animation.export()
    })
    setTimeout(function () {
      animation.translateY(0).step()
      that.setData({
        animationData: animation.export(),
        show: false
      })
    }, 500)
  },

  send() {
    var that = this
    let orderId = that.data.orderId
    let type = that.data.type
    if (type == "box") {
      wx.request({
        url: myUrl + '/api/nsOrder/setMail',
        data: {
          orderId: this.data.orderId,
          addressId: this.data.addressId
        },
        success(res) {
          console.log(res)
          wx.navigateTo({
            url: '../../index/box/box_order/box_order',
            success(res) {
              res.eventChannel.emit('orderId', { data: orderId })
            }
          })
        }
      })
    } if (type == "boxshop") {
      var pages = getCurrentPages();   //当前页面
      var prevPage = pages[pages.length - 2];   //上一页面
      prevPage.setData({
        //直接给上一个页面赋值
        addresschose: that.data.addressId,
      });
      wx.navigateBack({
        //返回
        delta: 1
      })
      //   wx.navigateTo({
      //     url: '../../index/box_shop/box_shop_detail/box_shop_detail?type=' + "boxon",
      //     success(res) {
      //       res.eventChannel.emit('type', { type: "boxon" })
      //       res.eventChannel.emit('good', { good: that.data.good })
      //       res.eventChannel.emit('addressid', { addresid: that.data.addressId })
      //     }
      //   })
    }
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
    var that = this
    wx.request({
      url: myUrl + '/api/address/getAddressByUid',
      data: {
        userId: userid
      },
      success(res) {
        console.log(res)
        that.setData({
          adlist: res.data.data,
          addressId: res.data.data[0].id
        })
      }
    })
  },

  delete() {
    var that = this
    wx.request({
      url: myUrl + '/api/address/delAddressById',
      data: {
        id: this.data.addresid
      },
      success(res) {
        console.log(res)
        wx.showToast({
          title: '删除成功',
        })
        that.setData({
          show: false
        })
        wx.request({
          url: myUrl + '/api/address/getAddressByUid',
          data: {
            userId: userid
          },
          success(res) {
            console.log(res)
            that.setData({
              adlist: res.data.data
            })
          }
        })
      }
    })
  },

  edit(e) {
    console.log(e)
    let aid = e.currentTarget.dataset.index
    wx.navigateTo({
      url: '../address_form/address_form?aid=' + aid,
    })
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