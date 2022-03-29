const app = getApp()
var myUrl = app.globalData.serverUrl
Page({
    data: {
        inputShowed: false,
        inputVal: "",
        search:'',
        goods: []
    },
    onLoad() {
        var that = this
        that.setData({
            search: this.search.bind(this)
        })
        wx.request({
            url: myUrl + '/api/nsOrderSell/getSellShop',
            data: {
                page: 1,
                pagesize: 20,
                cid: -1,
                state: 0,
                serachName: ""
            },
            success(res) {
                console.log(res)
                that.setData({
                    goods: res.data.data
                })
            }
        })
    },
    todetail(e) {
        let goodid = e.currentTarget.dataset.index
        console.log(goodid)
        wx.navigateTo({
            url: '../box_shop/box_shop_detail/box_shop_detail?id=' + goodid,
        })
    },
    search: function (value) {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                resolve([{ text: '搜索结果', value: 1 }, { text: '搜索结果2', value: 2 }])
            }, 200)
        })
    },
    selectResult: function (e) {
        console.log(e)
        console.log('select result', e.detail)
    },
    tobox() {
        wx.navigateTo({
            url: '../box/box',
        })
    },
    onUnload: function () {
        wx.navigateBack({
            delta: 2,
        })
    },
});