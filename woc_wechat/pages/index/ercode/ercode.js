// pages/index/ercode/ercode.js
import drawQrcode from '../../../utils/weapp.qrcode.esm.js'
import base64 from '.././../../utils/base64'
const app = getApp()
const url=app.globalData.serverUrl

Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
  console.log(base64.decode(options.couponId))

  drawQrcode({
    width: 200,
    height: 200,
    canvasId: 'myQrcode',
    // ctx: wx.createCanvasContext('myQrcode'),
    text: options.couponId,
    // v1.0.0+版本支持在二维码上绘制图片
    // image: {
    //   imageResource: '../../images/icon.png',
    //   dx: 70,
    //   dy: 70,
    //   dWidth: 60,
    //   dHeight: 60
    // }
  })

  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
  scan(){


    
    wx.scanCode({
      onlyFromCamera: true,
      success (res) {
        console.log(res.result)
        if(res.data.code==400){
          wx.showToast({
            title: '该优惠前已经失效',
          })
        }else{
          var id=base64.decode(res.result)
        wx.request({
          url: url+'/api/userCoupon/useUserCoupon',
          data:{
            id:parseInt(id)
          },
          success:function(res){
            console.log(res)
            wx.showToast({
              title: '使用成功',
            })
          }
        })
        }

        
      }
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