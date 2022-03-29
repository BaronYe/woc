// pages/index/coupon/coupon_page.js
const app = getApp()
const url=app.globalData.serverUrl
const id= wx.getStorageSync('id')
import dateFormat from '../../../utils/util'

import drawQrcode from '../../../utils/weapp.qrcode.esm.js'
import base64 from '../../../utils/base64'

Page({

  /**
   * 页面的初始数据
   */
  data: {
    special_get:false,
    type:0,
    bodyType:0,
    special_coupon_here_data:[],
    special_coupon_play_data:[],
    coupon_data:[],
    special_coupon:[],
    text:"每周一 00：00更新",
    test_image:"https://gimg2.baidu.com/image_search/src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20181119%2Fb087e852763b49bfba2b85559f6cb605.png&refer=http%3A%2F%2F5b0988e595225.cdn.sohucs.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1628914094&t=add6c5c3f837431296e66ea757ee836a",
    text_image2:"https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fi2.chuimg.com%2Fe9d4ad1617814a8391ca612595300e34_1124w_1124h.jpg%3FimageView2%2F2%2Fw%2F600%2Finterlace%2F1%2Fq%2F90&refer=http%3A%2F%2Fi2.chuimg.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1628914972&t=7dbfbfcca90def0ad1eead81612e6f20",

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    // console.log('注册成功123abc!@#','base64加密后：',base64.encode('注册成功123abc!@#'))
    // console.log(base64.encode('注册成功123abc!@#'),'base64解密后：',base64.decode(base64.encode('注册成功123abc!@#')))


    var me=this
  
me.common()
me.getSpecial()


wx.request({
  url: url+"/api/coupon/getCouponDoRob",
  data:{
    userId:id
  },
  success:function(res){
    console.log(res)
    for (var index in res.data.data) {
      res.data.data[index].image= res.data.data[index].shop.banner[0]
    }
    me.setData({
      special_coupon:res.data.data
    })
  
  }
})

  },
  getSpecial(){
    var me=this
    wx.request({
      url: url+"/api/coupon/getCouponDoTyps",
      data:{
        page:0,
        pagesize:5,
        types:me.data.bodyType,
        userId:id
      },
      success:function(res){
        console.log(res.data.data)
        me.setData({
          special_coupon_here_data:res.data.data
        })
      }
    })
  },
  set_special_get(e){
    var me=this
    console.log(e.currentTarget.id)
   wx.request({
     url: url+"/api/userCoupon/doUserCoupon",
     data:{
      userId:id,
      couponId:e.currentTarget.id
     },
     success:function(res){
     if(res.code==200){
      me.setData({
        special_get:true
      })
      wx.showToast({
        title: '领取成功',
      })
     }else{
      wx.showToast({
        title: '领取失败',
      })
    }

  
     }
   })


   
  },
  common(){
    var me=this

    wx.request({
      url: url+"/api/coupon/getCouponDoSpecial",
      // 加上个id
      data:{
        page:1,
        pagesize:5,
        userId:id
      },
      success:function(res){
        console.log(res)
        me.setData({
          coupon_data:res.data.data
        })
      
      }
    })
  }
  ,
  ercode(){

  },
  getZero(){
    this.setData({
      type:0
    })
  },
  getbodyTypeZero(){
    var me=this
   
    me.setData({
      bodyType:0
    })
    me.getSpecial()

  },
  getbodyTypeOne(){
    var me=this
  
    me.setData({
      bodyType:1
    })
    var me=this
    wx.request({
      url: url+"/api/coupon/getCouponDoTyps",
      data:{
        page:0,
        pagesize:5,
        types:me.data.bodyType,
        userId:id
      },
      success:function(res){
        console.log(res.data.data)
        me.setData({
          special_coupon_play_data:res.data.data
        })
      }
    })
  },
  getOne(){
    this.setData({
      type:1
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
  //  this.coupon=this.selectComponent("#coupon")
  },
  myuse(e){
    var couponId=base64.encode(e.currentTarget.id)
    console.log(couponId)
    var useId=base64.encode(id)

    wx.navigateTo({
      url: '../ercode/ercode?couponId='+couponId+'&userId'+useId,
    })
  },
  onMyget(e){
    console.log(e)
    var couponId=e.currentTarget.id
   
    console.log(id)
    wx.request({
      url: url+"/api/userCoupon/doUserCoupon",
      data:{
        userId:id,
        couponId:couponId

      },
      success:function(res){
        if(res.statusCode==200){
          wx.showToast({
            title: '领取成功',
          })
        }else{
          wx.showToast({
            title: '领取失败',
          })
        }
      }
    })


  },
  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    var me=this
   me.common()
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