const app = getApp()
var myUrl = app.globalData.serverUrl
var id = wx.getStorageSync('id')
Page({
  data: {
    userInfo: {},
    hasUserInfo: false,
    canIUseGetUserProfile: false,
    openid: '',
    session_key: ''
  },
  onShow() {
    this.login()
    if (wx.getUserProfile) {
      wx.login({
        success: (res) => {
          wx.request({
            url: myUrl + '/api/user/getOpenId',
            data: {
              code: res.code
            },
            success: res => {
              this.setData({
                canIUseGetUserProfile: true,
                openid: res.data.data.openId,
                session_key: res.data.data.sessionKey
              })
              wx.setStorageSync('openid', this.data.openid)
              wx.setStorageSync('session_key', this.data.session_key)
            }
          })
        }
      })
    }
  },
  onLoad() {
    wx.startLocationUpdateBackground({
      success: (res) => {
        wx.getLocation({
          type: 'wgs84',
          success(res) {
            const latitude = res.latitude
            const longitude = res.longitude
            const speed = res.speed
            const accuracy = res.accuracy
            console.log(res)
          }
        })
        console.log(res)
      },
    })
  },
  getUserProfile(e) {
    // 推荐使用wx.getUserProfile获取用户信息，开发者每次通过该接口获取用户个人信息均需用户确认
    // 开发者妥善保管用户快速填写的头像昵称，避免重复弹窗
    wx.getUserProfile({
      desc: '用于完善会员资料', // 声明获取用户个人信息后的用途，后续会展示在弹窗中，请谨慎填写
      success: (res) => {
        console.log(this.data.openid)
        var myUrl = app.globalData.serverUrl
        console.log(res)
        this.setData({
          hasUserInfo: true,
          // userInfo: res.userInfo
        })
        //注册用户
        wx.request({
          url: myUrl + '/api/user/login',
          method: 'POST',
          data: {
            avatar: res.userInfo.avatarUrl,
            nickname: res.userInfo.nickName,
            gender: res.userInfo.gender + "",
            openId: this.data.openid + "",
            inviterId: 0
          },
          success: res => {
            console.log(res.data)
            this.setData({
              userInfo: res.data.data
            })
            wx.setStorageSync('id', res.data.data.id)
          }
        })
      }
    })
  },
  getUserInfo(e) {
    // 不推荐使用getUserInfo获取用户信息，预计自2021年4月13日起，getUserInfo将不再弹出弹窗，并直接返回匿名的用户个人信息
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  },
  // 获取用户信息
  login() {
    var that = this
    var myUrl = app.globalData.serverUrl
    wx.getStorage({
      key: 'id',
      success(res) {
        console.log(res.data)
        const id = res.data
        wx.request({
          url: myUrl + '/api/user/getUserById',
          data: {
            id: id
          },
          success: res => {
            console.log(res.data)
            that.setData({
              userInfo: res.data.data,
              hasUserInfo: true
            })
            wx.setStorageSync('balance', that.data.userInfo.balance)
            wx.setStorageSync('point', that.data.userInfo.point)
          }
        })
      }
    })
  },
  toUserinfo() {
    wx.navigateTo({
      url: '../usercenter/user_info/user_info',
      success: res => {
        console.log(res)
        res.eventChannel.emit('userinfo', { data: this.data.userInfo })
      }
    })
  },
  tomoney() {
    wx.navigateTo({
      url: '../usercenter/my_money/my_money',
      success: res => {
        res.eventChannel.emit('money', { data: this.data.userInfo.balance })
      }
    })
  },
  torelease() {
    wx.navigateTo({
      url: '../usercenter/my_release/my_release',
      success: res => {
        res.eventChannel.emit('userInfo', { data: this.data.userInfo })
      }
    })
  },
  to_order() {
    wx.navigateTo({
      url: '../usercenter/my_order/my_order',
    })
  },
  tocollect() {
    wx.navigateTo({
      url: '../usercenter/my_collect/my_collect'
    })
  },
  toset() {
    wx.navigateTo({
      url: '../usercenter/my_set/my_set',
    })
  },
  tojifeng() {
    wx.navigateTo({
      url: '../usercenter/my_jifeng/my_jifeng',
    })
  },
  tocoup() {
    wx.request({
      url: myUrl + '/api/coupon/getCouponDoTyps',
      data: {
        userId: id,
        page: 1,
        pagesize: 20,
        types: "1"
      },
      success(res) {
        console.log(res.data)
      }
    })
  }
})