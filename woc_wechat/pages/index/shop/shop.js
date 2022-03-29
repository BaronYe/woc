// pages/index/shop/shop.js
const app = getApp()
// import getDays from '../../utils/util'
const url = app.globalData.serverUrl
const id = wx.getStorageSync('id')
import base64 from '../../../utils/base64'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    shop: {},
    desc: '',
    shopID: 0,
    ifget: false,
    // 是否显示面板指示点
    indicatorDots: true,
    // 滑动方向是否为纵向
    vertical: false,
    // 自动切换
    autoplay: true,
    // 采用衔接滑动
    circular: true,
    // 自动切换时间间隔2s
    interval: 2000,
    // 滑动动画时长0.5s
    duration: 500,
    // 前边距，可用于露出前一项的一小部分，接受 px 和 rpx 值
    previousMargin: 0,
    // 后边距，可用于露出后一项的一小部分，接受 px 和 rpx 值
    nextMargin: 0,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var me = this
    console.log(options)
    me.setData({
      shopID: parseInt(options.id)
    })
    me.refalsh()

  },
  refalsh() {
    var me = this
    wx.request({
      url: url + '/api/shop/getShopById',
      data: {
        id: me.data.shopID,
        userId: id
      },
      success: function (res) {
        console.log(res)
        res.data.data.coupon = res.data.data.couponList[0]
        res.data.data.shopDesc = res.data.data.shopDesc.replace(/width\s*:\s*[0-9]+px/g, 'width:100%');
        res.data.data.shopDesc = res.data.data.shopDesc.replace(/<([\/]?)(center)((:?\s*)(:?[^>]*)(:?\s*))>/g, '<$1div$3>');//替换center标签
        res.data.data.shopDesc = res.data.data.shopDesc.replace(/\<img/gi, '<img class="rich-img" ');//正则给img标签增加class
        //或者这样直接添加修改style
        res.data.data.shopDesc = res.data.data.shopDesc.replace(/style\s*?=\s*?([‘"])[\s\S]*?\1/ig, 'style="width:100%;height:auto;display: block;margin:auto"');
        res.data.data.shopDesc = res.data.data.shopDesc.replace(/\<p/gi, '<P class="rich-p" ');//正则给p标签增加class
        me.setData({
          shop: res.data.data,
          desc: res.data.data.shopDesc
        })
      }
    })
  },
  myuse(e) {
    var couponId = base64.encode(e.currentTarget.id)
    console.log(couponId)
    var useId = base64.encode(id)

    wx.navigateTo({
      url: '../ercode/ercode?couponId=' + couponId + '&userId' + useId,
    })
  },
  get(e) {
    wx.request({
      url: url + '/api/userCoupon/doUserCoupon',
      data: {
        userId: id,
        couponId: e.currentTarget.id,

      },
      success: function (res) {
        if (res.data.code == 200) {
          wx.showToast({
            title: '领取成功',
          })
        } else {
          wx.showToast({
            title: '领取失败',
          })
        }
      }
    })
  },
  onclickTrue() {
    var me = this
    wx.request({
      url: url + '/api/shop/setShopCollect',
      method: 'POST',
      data: {
        userId: id,
        shopId: me.data.shopID
      },
      success: function (res) {
        if (res.statusCode == 200) {
          me.refalsh()
        }
      }
    })
  },
  onclickFalse() {
    var me = this
    wx.request({
      url: url + '/api/shop/setShopCollect',
      method: 'POST',
      data: {
        userId: id,
        shopId: me.data.shopID
      },
      success: function (res) {
        if (res.statusCode == 200) {
          me.refalsh()
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