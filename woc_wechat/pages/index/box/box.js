const app = getApp()
var myUrl = app.globalData.serverUrl
Page({

  /**
   * 页面的初始数据
   */
  data: {
    id: null,
    currentTabIndex: 0,
    leibie: [],
    new: [],
    pao: [],
    ri: [],
    guo: []
  },

  //Tab选项函数
  onTabItemTap: function (e) {
    this.setData({
      //拿到索引并改变动态
      currentTabIndex: e.target.dataset.index
    })
  },

  onLoad: function (options) {
    var that = this
    // 查询盲盒分类
    wx.request({
      url: myUrl + '/api/box/getBoxClassify',
      success(res) {
        that.setData({
          leibie: res.data.data
        })
      }
    })
    //查询新品推荐盲盒
    wx.request({
      url: myUrl + '/api/box/getBox',
      data: {
        page: 1,
        pagesize: 20,
        cid: 4,
        name: "",
        isShelves: "1"
      },
      success(res) {
        that.setData({
          new: res.data.data
        })
      }
    })
    //查询泡泡玛特盲盒
    wx.request({
      url: myUrl + '/api/box/getBox',
      data: {
        page: 1,
        pagesize: 20,
        cid: 5,
        name: "",
        isShelves: "1"
      },
      success(res) {
        that.setData({
          pao: res.data.data
        })
      }
    })
    //查询热血日漫盲盒
    wx.request({
      url: myUrl + '/api/box/getBox',
      data: {
        page: 1,
        pagesize: 20,
        cid: 6,
        name: "",
        isShelves: "1"
      },
      success(res) {
        that.setData({
          ri: res.data.data
        })
      }
    })
    //查询国潮IP盲盒
    wx.request({
      url: myUrl + '/api/box/getBox',
      data: {
        page: 1,
        pagesize: 20,
        cid: 7,
        name: "",
        isShelves: "1"
      },
      success(res) {
        that.setData({
          guo: res.data.data
        })
      }
    })
  },

  toboxdetail(e) {
    let boxid = e.currentTarget.dataset.index
    wx.navigateTo({
      url: '../box/box_detail/box_detail?id=' + boxid,
    })
  },
  onUnload: function () {
    wx.navigateBack({
      delta: 1,
    })
  },
  toshop(){
    wx.navigateTo({
      url: '../box_shop/box_shop',
    })
  }
})