const app = getApp()
var myUrl = app.globalData.serverUrl
Page({

  /**
   * 页面的初始数据
   */
  data: {
    tab: ['全部', '优惠劵', '实体', '手办'],
    id: null,
    currentTabIndex: 0,
    goods: []
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
      url: myUrl + '/api/integralGoods/getIntegralGoodsAndWechat',
      data: {
        page: 1,
        pagesize: 5,
        cid: -1,
        types: 0,
        sort: "asc"
      },
      success: function (res) {
        console.log(res)
        let goods = that.data.goods
        for (let i = 0; i < res.data.data.length; i++) {
          goods.push(res.data.data[i])
        }
        console.log(goods)
        that.setData({
          goods: goods
        })
      }
    })
  },

  todetail(e) {
    let goodid = e.currentTarget.dataset.index
    console.log(goodid)
    wx.navigateTo({
      url: '../jifeng_shop/shop_detail/shop_detail?id='+goodid,
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