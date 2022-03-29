const app = getApp()
var myUrl = app.globalData.serverUrl
var id = wx.getStorageSync('id')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    sellid: null,
    good: [],
    address: [],
    addressid: '',
    type: 'boxshop',
    paymentType: 1,
    addressBack: false
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    console.log(options.id)
    let sellid = parseInt(options.id)
    that.setData({
      sellid: sellid
    })
    wx.request({
      url: myUrl + '/api/nsOrderSell/getSellShopById',
      data: {
        id: options.id
      },
      success(res) {
        console.log(res)
        that.setData({
          good: res.data.data
        })
      }
    })
    if (that.data.addressBack == false) {
      wx.request({
        url: myUrl + '/api/address/getAddressByUid',
        data: {
          userId: id
        },
        success(res) {
          console.log(res)
          that.setData({
            address: res.data.data[0]
          })
        }
      })
    } if (that.data.addressBack == true) {
      wx.request({
        url: myUrl + '/api/address/getAddressByid',
        data: {
          id: that.data.addressid
        },
        success(res) {
          that.setData({
            address: res.data.data
          })
        }
      })
    }

  },
  radioChange(e) {
    console.log('radio发生change事件，携带value值为：', e.detail.value)
    this.setData({
      paymentType: e.detail.value
    })
  },

  todingdan() {
    var that = this
    wx.request({
      url: myUrl + '/api/nsOrderSellOrder/createSellOrder',
      method: 'POST',
      data: {
        sellId: that.data.sellid,
        userId: id,
        paymentType: parseInt(that.data.paymentType),
        addressId: that.data.address.id
      },
      success(res) {
        console.log(res.data)
        if (res.data.code == 200) {
          var orderId = res.data.data.orderId
          console.log(orderId)
          wx.request({
            url: myUrl + '/api/nsOrderSellOrder/toSellOrderPay',
            data: {
              id: orderId
            },
            success(res) {
              wx.navigateBack({
                delta: 4,
              })
              wx.showToast({
                title: '购买成功',
              })
            }
          })
        } else if (res.data.code == 400) {
          wx.showToast({
            title: res.data.msg,
            icon: 'error'
          })
        }
      }
    })
  },
  toaddress() {
    var that = this
    wx.navigateTo({
      url: '../../../address/address_list/address_list?type=' + "boxshop",
      success(res) {
        res.eventChannel.emit('type', { type: that.data.type })
        res.eventChannel.emit('good', { good: that.data.good })
      }
    })
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    var that = this
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    let pages = getCurrentPages();

    let currPage = pages[pages.length - 1];

    if (currPage.data.addresschose) {

      this.setData({

        //将携带的参数赋值

        addressid: currPage.data.addresschose,

        addressBack: true

      });

      console.log(this.data.address, '地址')

    }
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
    wx.navigateBack({
      delta: 2,
    })
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