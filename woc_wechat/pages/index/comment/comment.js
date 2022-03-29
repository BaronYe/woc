// pages/index/comment/comment.js
const app = getApp()
// import getDays from '../../utils/util'
const url=app.globalData.serverUrl
const id= wx.getStorageSync('id')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    //动态id
    details:{},
     dId:0,
     search_value:"",
       // 是否显示面板指示点
    indicatorDots: true,
    // 滑动方向是否为纵向
    vertical: false,
    // 自动切换
    autoplay: true,
    // 采用衔接滑动
    circular: true,
    // 自动切换时间间隔2s
    interval: 2000,
    // 滑动动画时长0.5s
    duration: 500,
    // 前边距，可用于露出前一项的一小部分，接受 px 和 rpx 值
    previousMargin: 0,
    // 后边距，可用于露出后一项的一小部分，接受 px 和 rpx 值
    nextMargin: 0,
    commentList:[],
    nowID:0,
    nowReplayCommentId:0,
  
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var me=this
    console.log(options.id)
    me.setData({
      dId:parseInt(options.id)
    })
    wx.getSystemInfo({
      success: function (res) {
        me.setData({
          scrollHeight: parseInt(res.windowHeight)+200
        })
      }
    });
    
    me.reflash()
    // me.getComment()
me.getComment()
// me.addComment()

  },
     getComment(){
    var me=this
     wx.request({
  url: url+'/api/comment/getComment',
  data:{
    dynamicId:me.data.dId,
    page:1,
    pagesize:10,
    userId:id
  },
 success:function(res){
    console.log(res)
    if(res.data.code==200){
      for( var index in res.data.data){
        res.data.data[index].isOpen=false
        if(res.data.data[index].commentCount>0){
          for(var i in res.data.data[index].commentList){
            if(res.data.data[index].commentList[i].beUserId!=res.data.data[index].userId){
              res.data.data[index].commentList[i].users.nickname=res.data.data[index].commentList[i].users.nickname+"->"+res.data.data[index].commentList[i].beUsers.nickname

            }
          }
        }

        // 获取子评论的名字
      //  if(res.data.data[index].commentCount>0){
      //   for(var i in res.data.data[index].commentList){
      //     if(res.data.data[index].commentList[i].beUserId!=res.data.data[index].userId){
      //     wx.request({
      //         url: url+'/api/user/getUserById',
      //         data:{
      //           id:res.data.data[index].commentList[i].beUserId
      //         },
      //         success:function(Res){
      //           me.fuck(Res)
      //           console.log(Res)
      //           console.log(Res.data.data.nickname)
      //           var tousernickname=Res.data.data.nickname
      //           console.log(res.data.data[index].commentCount)
      //          if(res.data.data[index].commentCount>0){
      //           var origin=res.data.data[index].commentList[i].users.nickname
      //           console.log(origin)
      //           console.log(tousernickname)
      //          res.data.data[index].commentList[i].users.nickname=origin+"->"+tousernickname
      //           console.log(i)
      //           console.log( res.data.data[index].commentList[i].users.nickname)
      //           me.setData({
      //             commentList:res.data.data
      //           })
      //          }
               
      //         }
      //       })
      //     }
      //   }
      //  }
      }
   
    }
    me.setData({
      commentList:res.data.data,
      isShow:false,
      search_value:""
    })
    
  }
})
  },
fuck(e){
  console.log(e)
},
  search(e){
    console.log(e.detail.value)
      var me=this
      me.setData({
        search_value:e.detail.value
      })
    
      },
  bindModel(e){
    console.log(e)
  },
  childComment(res){
   
  },
  dianzan(e){
    var me=this
    wx.request({
      url: url+'/api/comment/setCommentZan',
      method:'POST',
      data:{
        userId:id,
        dynamicId:me.data.dId,
        commenId:parseInt(e.currentTarget.id)

      },
      success:function(res){
        console.log(res)
        me.getComment()
      }
    })
   
  },
  open(e){
   var me= this
   var index=e.currentTarget.id
   if(me.data.commentList[index].commentList.length>0){
    me.setData({
      ['commentList['+index+'].isOpen']:true
    })
   }
  
  },
  close(e){
    var me= this
   var index=e.currentTarget.id
   me.setData({
     ['commentList['+index+'].isOpen']:false
   })
  },
  replayFather(e){
    var me=this
   var id=e.currentTarget.id
   me.setData({
     nowReplayCommentId:id,
     
   })

  },
  replaySon(e){
    var me=this
    var id=e.currentTarget.id
    var cid=e.currentTarget.dataset.tid
    console.log(e)
    me.setData({
      nowID:id,
      nowReplayCommentId:cid,
    
      
    })
  },
  publish(){
    var me=this
    wx.request({
      url: url+'/api/comment/setComment',
      method:'POST',
      data:{
        userId:id,
        dynamicId:me.data.dId,
        rid:parseInt(me.data.nowReplayCommentId),
        beUserId:parseInt(me.data.nowID),
        content:me.data.search_value,
        id:0
       
      },
      success:function(res){
        if(res.data.code==200){
          me.getComment()
          me.setData({
            search_value:"",
            nowReplayCommentId:0,
            nowID:id
         

          })


        }
      }
    })
  },
reflash(){
  var me =this
  var userId=0
  if(id!=null){
userId=id
  }
  wx.request({
    url: url+'/api/dynamic/getDynamicById',
    data:{
     id:me.data.dId,
     userId:userId
    },
    success:function(res){
      console.log(res)
     if(res.data.code==200){
     
         if(res.data.data.isFollow==0){
           res.data.data.isfoller=false
         }
         else{
           res.data.data.isfoller=true
         }
       
       me.setData({
       
           details:res.data.data,
           nowID:res.data.data.userId
        })
     }
    }
  })
},
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
  changeFoller(e){
    console.log(e)
   var me=this
    var other_id=e.currentTarget.id
   
 

    wx.request({
      url: url+'/api/userFollow/setFollow',
      method:'POST',
      data:{
        userId: id,
        followId:parseInt(other_id)
      },
      success:function(res){
      
if(res.data.code==200){
 me.reflash()
}
      }
    })
   
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