// pages/message/zan/zan.js
const app = getApp()
var myUrl = app.globalData.serverUrl
var id = wx.getStorageSync('id')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    id: null,
    currentTabIndex: 0,
    //店铺
    shop: [{
      'name': "三星咖选·派瑞辞休闲聚馆",
      'image': "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fbjcache.leju.com%2Fzxjiaju%2Fzx_pic%2F20161118%2Fde%2F96%2Fd96e483ca1f45a406ad2151c4ed46a63.jpeg&refer=http%3A%2F%2Fbjcache.leju.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1627876519&t=a60a6d9a88c31b83ed976ccd6cf3937a",
      'price': 50,
      'fromTime': "07:30",
      'toTime': "9:30",
      'address': "西湖景区·中西餐厅",
      'fire': 4
    }, {
      'name': "三星咖选·派瑞辞休闲聚馆",
      'image': "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fbjcache.leju.com%2Fzxjiaju%2Fzx_pic%2F20161118%2Fde%2F96%2Fd96e483ca1f45a406ad2151c4ed46a63.jpeg&refer=http%3A%2F%2Fbjcache.leju.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1627876519&t=a60a6d9a88c31b83ed976ccd6cf3937a",
      'price': 50,
      'fromTime': "07:30",
      'toTime': "9:30",
      'address': "西湖景区·中西餐厅",
      'fire': 4
    }],
    //活动
    action: [
      {
        'image': 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fbjcache.leju.com%2Fzxjiaju%2Fzx_pic%2F20161118%2Fde%2F96%2Fd96e483ca1f45a406ad2151c4ed46a63.jpeg&refer=http%3A%2F%2Fbjcache.leju.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1627876519&t=a60a6d9a88c31b83ed976ccd6cf3937a',
        'title': '应约姐姐 较低的',
        'time': '三天后结束'
      }, {
        'image': 'https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fbjcache.leju.com%2Fzxjiaju%2Fzx_pic%2F20161118%2Fde%2F96%2Fd96e483ca1f45a406ad2151c4ed46a63.jpeg&refer=http%3A%2F%2Fbjcache.leju.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1627876519&t=a60a6d9a88c31b83ed976ccd6cf3937a',
        'title': '应约姐姐 较低的',
        'time': '三天后结束'
      }
    ]
  },

  //Tab选项函数
  onTabItemTap: function (e) {
    this.setData({
      //拿到索引并改变动态
      currentTabIndex: e.target.dataset.index
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this
    wx.request({
      url: myUrl + '/api/shop/getShopCollectByUid',
      data: {
        userId: id,
        page: 1,
        pagesize: 20
      },
      success(res) {
        console.log(res.data.data)
      }
    })
    wx.request({
      url: myUrl + '/api/activity/getActivityCollectByUid',
      data: {
        userId: id,
        page: 1,
        pagesize: 20
      },
      success(res) {
        console.log(res.data.data)
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