// index.js
// 获取应用实例
const app = getApp()
const url = app.globalData.serverUrl
const id = wx.getStorageSync('id')

Page({
  data: {
    test: [],
    user_information: [],
    isfoller: false,
    isrecommend: true,
    ishere: false,
    motto: 'Hello World',
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    canIUseGetUserProfile: false,
    canIUseOpenData: wx.canIUse('open-data.type.userAvatarUrl') && wx.canIUse('open-data.type.userNickName'),
    images: [],
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
  // 事件处理函数
  bindViewTap() {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },
  zan(e) {
    //加个2秒倒计时
    var me = this
    wx.request({
      url: url + '/api/dynamic/setDynamicZan',
      method: 'POST',
      data: {
        dynamicId: parseInt(e.currentTarget.id),
        userId: id
      },
      success: function (res) {
        if (res.data.code == 200) {
          me.reflash()
        }
        console.log(res)
      }
    })
  },
  onLoad() {
    var me = this
    var url = app.globalData.serverUrl
    wx.request({
      url: url + '/api/banner/getBanner',
      success: function (res) {
        if (res.data.code == 200) {
          me.setData({
            images: res.data.data
          })
        }
        console.log(res)
      }
    })
    me.reflash()
  },
  redirect() {
    wx.navigateTo({
      url: '../search/search',
    })
  },
  reflash() {
    var me = this
    var type
    if (me.data.isfoller) {
      type = 0
    } else if (me.data.isrecommend) {
      type = 1
    }
    else {
      type = 2
    }
    wx.request({
      url: url + "/api/dynamic/getHomeDynaic",
      method: "GET",
      data: {
        page: 1,
        pagesize: 5,
        userId: id,
        types: type,
        lat: "5",
        lng: "54"
      },
      success: function (res) {
        if (res.data.code == 200) {

          for (var index in res.data.data) {
            if (res.data.data[index].isFollow == 1) {
              res.data.data[index].isfoller = true
            }
            else {
              res.data.data[index].isfoller = false
            }
          }

          me.setData({
            user_information: res.data.data
          })
        }
      }
    })
  },
  goTOCoupon() {
    wx.navigateTo({
      url: './coupon/coupon_page',
    })
  },
  setFoller() {

    var me = this
    me.setData({
      isfoller: true,
      isrecommend: false,
      ishere: false
    })
    me.reflash()
  },
  setRecommend() {
    var me = this
    me.setData({
      isfoller: false,
      isrecommend: true,
      ishere: false
    })
    me.reflash()
  },
  setHere() {
    var me = this
    me.setData({
      isfoller: false,
      isrecommend: false,
      ishere: true
    })
  },
  tobox() {

    wx.navigateTo({
      url: '../index/box/box',
    })
  },
  getUserProfile(e) {
    // 推荐使用wx.getUserProfile获取用户信息，开发者每次通过该接口获取用户个人信息均需用户确认，开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
    wx.getUserProfile({
      desc: '展示用户信息', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
      success: (res) => {
        console.log(res)
        this.setData({
          userInfo: res.userInfo,
          hasUserInfo: true
        })
      }
    })
  },

  goto_Comment(e) {
    console.log(e)
    wx.navigateTo({
      url: '/pages/index/comment/comment?id=' + e.currentTarget.id,
    })
  },
  changeFoller(e) {
    console.log(e)
    var me = this
    var other_id = e.currentTarget.id



    wx.request({
      url: url + '/api/userFollow/setFollow',
      method: 'POST',
      data: {
        userId: id,
        followId: parseInt(other_id)
      },
      success: function (res) {

        if (res.data.code == 200) {
          me.reflash()
        }
      }
    })

  },
  goTONearBy() {
    wx.navigateTo({
      url: '/pages/index/nearby/nearby',
    })
  },
  goTOFunuy() {
    wx.navigateTo({
      url: '/pages/index/funny/funny',
    })
  },
  goToSearch() {
    wx.navigateTo({
      url: '/pages/index/map/map',
    })
  },
  onShow() {
    this.onLoad()
  },
  delete(e) {
    console.log(e)
    let rid = e.currentTarget.dataset.index
    wx.request({
      url: url + '/api/dynamic/getDynamicById',
      data: {
        id: rid,
        userId: id
      },
      success(res) {
        console.log(res.data)
      }
    })
  }

})
