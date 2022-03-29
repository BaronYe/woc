// pages/find/article/article.js
const app = getApp()
const url = app.globalData.serverUrl
const id = wx.getStorageSync('id')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    articleID: 0,
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var me = this
    console.log(options)
    me.setData({
      articleID: options.id
    })
    me.reflash()
  },
  reflash() {
    var me = this
    wx.request({
      url: url + '/api/activity/getActivityById',
      data: {
        id: me.data.articleID,
        userId: id
      },
      success: function (res) {
        var data = res.data.data
        data.activityDesc = data.activityDesc.replace(/width\s*:\s*[0-9]+px/g, 'width:100%');
        data.activityDesc = data.activityDesc.replace(/<([\/]?)(center)((:?\s*)(:?[^>]*)(:?\s*))>/g, '<$1div$3>');//替换center标签
        data.activityDesc = data.activityDesc.replace(/\<img/gi, '<img class="rich-img" ');//正则给img标签增加class
        //或者这样直接添加修改style
        data.activityDesc = data.activityDesc.replace(/style\s*?=\s*?([‘"])[\s\S]*?\1/ig, 'style="width:100%;height:auto;display: block;margin:auto"');
        data.activityDesc = data.activityDesc.replace(/\<p/gi, '<P class="rich-p" ');//正则给p标签增加class
        if (res.statusCode == 200) {
          me.setData({
            article: data
          })
        }
      }
    })
  },
  onclickTrue() {
    var me = this
    wx.request({
      url: url + '/api/activity/setActivityCollect',
      method: 'POST',

      data: {
        userId: id,
        activityId: parseInt(me.data.articleID)

      },
      success: function (res) {
        if (res.statusCode == 200) {
          me.reflash()
        }
      }
    })
  },
  onclickFalse() {
    var me = this
    wx.request({
      url: url + '/api/activity/setActivityCollect',
      method: 'POST',

      data: {
        userId: id,
        activityId: parseInt(me.data.articleID)

      },

      success: function (res) {
        if (res.statusCode == 200) {
          me.reflash()
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