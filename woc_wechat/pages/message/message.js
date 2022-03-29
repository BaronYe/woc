Page({
  data: {
  },
  onLoad() {
  },
  tozan() {
    wx.navigateTo({
      url: '../message/zan/zan'
    })
  },
  tofollow() {
    wx.navigateTo({
      url: '../message/follow/follow',
    })
  },
  topinglun() {
    wx.navigateTo({
      url: '../message/pinglun/pinglun',
    })
  },
  toxitong() {
    wx.navigateTo({
      url: '../message/xitong/xitong',
    })
  },
  toshangping() {
    wx.navigateTo({
      url: '../message/shangping/shangping',
    })
  },
  tojifeng() {
    wx.navigateTo({
      url: '../message/jifeng/jifeng',
    })
  }
})