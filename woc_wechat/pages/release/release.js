const app = getApp()
var myUrl = app.globalData.serverUrl
Page({

  /**
   * 页面的初始数据
   */
  data: {
    img_url: [],
    content: '',
    hideAdd: 0
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },
  input: function (e) {
    this.setData({
      content: e.detail.value
    })
  },
  chooseimage: function () {
    var that = this;
    wx.chooseImage({
      count: 9, // 默认9  
      sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有  
      sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有  
      success: function (res) {

        if (res.tempFilePaths.length > 0) {

          //图如果满了9张，不显示加图
          if (res.tempFilePaths.length == 9) {
            that.setData({
              hideAdd: 1
            })
          } else {
            that.setData({
              hideAdd: 0
            })
          }
          //把每次选择的图push进数组
          let img_url = that.data.img_url;
          for (let i = 0; i < res.tempFilePaths.length; i++) {
            console.log(res.tempFilePaths[i])
            img_url.push(res.tempFilePaths[i])
          }
          that.setData({
            img_url: img_url
          })

        }

      }
    })
  },
  //发布按钮事件
  send: function () {
    var that = this;
    var user_id = wx.getStorageSync('id');
    if (that.data.img_url == []) {
      wx.showToast({
        title: '至少发布一张图片',
        icon: 'error'
      })
    } else {
      wx.showLoading({
        title: '上传中',
      })
      that.img_upload()
    }
  },
  //图片上传
  img_upload: function () {
    let that = this;
    let img_url = that.data.img_url;
    let img_url_ok = []
    //由于图片只能一张一张地上传，所以用循环
    for (let i = 0; i < img_url.length; i++) {
      wx.uploadFile({
        //路径填你上传图片方法的地址
        url: myUrl + '/api/oss/upload',
        filePath: img_url[i],
        name: 'file',
        success: function (res) {
          console.log('上传成功');
          //把上传成功的图片的地址放入数组中
          var img = JSON.parse(res.data)
          img_url_ok.push(img.data)
          //如果全部传完，则可以将图片路径保存到数据库
          if (img_url_ok.length == img_url.length) {
            var userid = wx.getStorageSync('id');
            var content = that.data.content;
            var img_url_on = img_url_ok.join(',')
            console.log(img_url_on)
            wx.request({
              url: myUrl + '/api/dynamic/setDynamic',
              method: 'POST',
              data: {
                id: 0,
                userId: userid,
                imgs: img_url_on,
                content: content,
              },
              success: function (res) {
                console.log(res)
                if (res.data.code == 200) {
                  wx.hideLoading()
                  wx.showModal({
                    title: '已发布',
                    showCancel: false,
                    success: function (res) {
                      if (res.confirm) {
                        wx.switchTab({
                          url: '/pages/index/index',
                        })
                      }
                    }
                  })
                }
              }
            })
          }
        },
        fail: function (res) {
          console.log('上传失败')
        }
      })
    }
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