// pages/find/find.js
const app = getApp()
// import getDays from '../../utils/util'
const url = app.globalData.serverUrl
const id = wx.getStorageSync('id')
const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return `${[year, month, day].map(formatNumber).join('/')} ${[hour, minute, second].map(formatNumber).join(':')}`
}
const getDays = (endTime) => {
  var nowTime = formatTime(new Date());
  nowTime = Date.parse(nowTime);
  var ee = Date.parse(endTime);
  var days = parseInt((ee - nowTime) / (1000 * 60 * 60 * 24));
  if (days <= 0) {
    days = Math.abs(days) + 1;
  }
  return days;

}
const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : `0${n}`
}

Page({
  /**
   * 页面的初始数据
   */
  data: {
    good_place: [],
    activity: [],
    information: []
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var dat = getDays("2021-07-20")
    console.log(dat)
    console.log(options)
    var me = this
    var url = app.globalData.serverUrl
    // 好去处排行
    wx.request({
      url: url + "/api/shop/rankingShop",
      data: {
        page: 1,
        pagesize: 5,
        regionId: 0,
        circleId: 0
      },
      success: function (res) {
        console.log(res)
        for (var index in res.data.data) {
          res.data.data[index].img = res.data.data[index].banner[0]
          res.data.data[index].rank = parseInt(index) + 1
        }
        me.setData({
          good_place: res.data.data
        })
      }
    })
    // 获取店铺列表
    wx.request({
      url: url + '/api/shop/getShopDoWechat',
      data: {
        page: 1,
        pagesize: 5,
        regionId: -1,
        circleId: -1,
        types: "-1"
      },
      success: function (res) {
        me.setData({
          information: res.data.data
        })
        console.log(res)
      }
    })
    // 查询活动列表
    wx.request({
      url: url + '/api/activity/getActivity',
      data: {
        page: 1,
        pagesize: 5,
        activityShow: -1,
        name: ""
      },
      success: function (res) {
        var size = res.data.data.length
        console.log(res)
        var temp = []
        var activity = me.data.activity
        for (var index in res.data.data) {
          res.data.data[index].time = getDays(res.data.data[index].activityEndTime)
          temp.push(res.data.data[index])
          console.log(temp[index])
          if (temp.length == 3) {
            activity.push(temp)
            temp = []
          }
          console.log(parseInt(index))
          console.log(size)
          if (parseInt(index) == size - 1) {
            activity.push(temp)
          }
        }
        me.setData({
          activity: activity
        })
        console.log(res)
      }
    })
  },
  getDetals(e) {
    wx.navigateTo({
      url: '../index/shop/shop?id=' + e.currentTarget.id,
    })
  },
  gotoDetail(e) {
    console.log(e)
    var parent = e.currentTarget.dataset.parent
    var child = e.currentTarget.dataset.child
    wx.navigateTo({
      url: './article/article?id=' + this.data.activity[parent][child].id,
    })
  },
  goToSearch() {
    wx.navigateTo({
      url: '/pages/index/map/map',
    })
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
  redirect() {
    wx.navigateTo({
      url: '../search/search',
    })
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
  },


  onTabClick(e) {
    const index = e.detail.index
    this.setData({
      activeTab: index
    })
  },

  onChange(e) {
    const index = e.detail.index
    this.setData({
      activeTab: index
    })
  },
  handleClick(e) {
    wx.navigateTo({
      url: './webview',
    })
  }
})