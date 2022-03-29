const app = getApp()
var myUrl = app.globalData.serverUrl
Page({
    data: {
        inputShowed: false,
        inputVal: ""
    },
    showInput: function () {
        this.setData({
            inputShowed: true
        });
    },
    hideInput: function () {
        this.setData({
            inputVal: "",
            inputShowed: false
        });
    },
    clearInput: function () {
        this.setData({
            inputVal: ""
        });
    },
    inputTyping: function (e) {
        console.log(e.detail.value)
        var that = this
        this.setData({
            inputVal: e.detail.value
        });
        wx.request({
            url: myUrl + '/api/nsOrderSell/getSellShop',
            data: {
                page: 1,
                pagesize: 5,
                cid: -1,
                state: -1,
                serachName: that.data.inputVal
            },
            success(res) {
                console.log(res)
            }
        })
    }
});