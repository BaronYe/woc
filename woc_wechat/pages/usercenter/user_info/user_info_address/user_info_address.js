const app = getApp()
var myUrl = app.globalData.serverUrl
var uid = wx.getStorageSync('id')
Page({
  data: {
    addressInfo: null
  },
  chooseAddress() {
    wx.chooseAddress({
      success: (res) => {
        this.setData({
          addressInfo: res
        })
      },
      fail: function (err) {
        console.log(err)
      }
    })
  },
  saveAddress() {
    var addressInfo = this.data.addressInfo
    if (this.data.addressInfo) {
      wx.request({
        url: myUrl + '/api/address/setAddress',
        method: "POST",
        data: {
          id: 0,
          userId: uid,
          name: addressInfo.userName,
          mobile: addressInfo.telNumber,
          province: addressInfo.provinceName,
          city: addressInfo.cityName,
          region: addressInfo.countyName,
          detailed: addressInfo.detailInfo,
          postalCode: addressInfo.postalCode,
          isDefault: 0+""
        },
        success: res=>{
          wx.showToast({
            title: '地址保存成功',
          })
        }
      })
    } else {
      wx.showToast({
        title: '地址为空',
      })
    }
  }
})