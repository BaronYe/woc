<template>
  <div>
    <el-dialog
        :visible.sync="dialogVisible"
        top="10vh"
        :close-on-click-modal="false"
        width="60%">
      <div class="title">商品选择</div>
      <div class="d-flex box">
        <div class="left">
          <div class="layui-colla-item">
            <h2 class="layui-colla-title" :class="[cid == -1 ? 'ns-text-color':'']" @click="doCidCLick(-1)">
              全部分类
            </h2>
          </div>
          <div v-for="(item,index) of classIfyList" :key="index" class="layui-colla-item">
            <h2 class="layui-colla-title" :class="[cid == item.id ? 'ns-text-color':'']" @click="doCidCLick(item.id)">
              <i :class="[cid == item.id ? 'el-icon-caret-bottom ns-text-color':'el-icon-caret-right']" class="layui-colla-icon"></i>
              {{item.name}}</h2>
            <div :class="[cid == item.id ? 'layui-show':'',sid == v.id ?'ns-text-color':'']" class="layui-colla-content classification-item" v-for="(v,k) of item.items" @click="doSifCLick(v.id)">{{v.name}}</div>
          </div>
        </div>
        <div class="right">
          <div class="ns-single-filter-box">
            <el-input placeholder="请输入商品名称" style="width: 200px;" v-model="name">
              <el-button slot="append" icon="el-icon-search" @click="serachClick()"></el-button>
            </el-input>
          </div>
          <!--列表-->
          <template>
            <el-table
                ref="multipleTable"
                :data="dataList"
                tooltip-effect="dark"
                style="width: 100%;margin-top: 20px;">
              <el-table-column
                  prop="id"
                  align="center"
                  width="120px"
                  label="#">
              </el-table-column>
              <el-table-column
                  align="center"
                  width="120px"
                  label="商品主图">
                <template slot-scope="scope">
                  <el-image :src="scope.row.imgs[0]" :preview-src-list="[scope.row.imgs[0]]" style="width: 40px;height: 40px;"></el-image>
                </template>
              </el-table-column>
              <el-table-column
                  align="center"
                  label="商品名称">
                <template slot-scope="scope">
                  <div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp:2;overflow: hidden;">{{scope.row.title}}</div>
                </template>
              </el-table-column>
              <el-table-column
                  align="center"
                  label="商品分类">
                <template slot-scope="scope">
                  <div>{{scope.row.cName}}/{{scope.row.sName}}</div>
                </template>
              </el-table-column>
              <el-table-column
                  prop="price"
                  align="center"
                  label="商品价格">
              </el-table-column>
              <el-table-column
                  label="操作"
                  width="200"
                  align="center">
                <template slot-scope="scope">
                  <el-button
                      @click="toFuCLick(scope.row)"
                      type="text"
                      size="small">
                    确定
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

          </template>
          <!-- 分页 -->
          <template>
            <div style="margin-top: 30px;text-align: right;">
              <el-pagination
                  background
                  layout="total,prev, pager, next"
                  :page-size="pagesize"
                  :total="count-0"
                  :current-page.sync="page"
                  @current-change="pagingChange"
              ></el-pagination>
            </div>
          </template>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'goodBox',
  data(){
    return{
      dialogVisible:false,
      classIfyList:[],
      cid:-1,
      sid:-1,
      name:"",
      page:1,
      pagesize:5,
      dataList:[],
      count:0
    }
  },
  mounted() {
    this.getClassifysFn();
    this.getItemFn();
  },
  methods:{
    isShowDia(){
      this.dialogVisible = !this.dialogVisible;
    },
    // 获取一级分类
    getClassifysFn() {
      this.api.getClassifyList().then(res => {
        this.classIfyList = res.data;
      });
    },
    // 一级类目的选择
    doCidCLick(id){
      this.cid = id;
      this.sid = -1;
      this.page = 1;
      this.getItemFn();
    },
    // 二级类目的选择
    doSifCLick(id){
      this.sid = id;
      this.page = 1;
      this.getItemFn();
    },
    // 搜索
    serachClick(){
      this.page = 1;
      this.getItemFn();
    },
    pagingChange(p){
      this.page = p;
      this.getItemFn();
    },
    // 查询商品列表
    getItemFn(){
      this.api.getItem({
        page:this.page,
        pagesize:this.pagesize,
        name:this.name,
        isShelves: "1",
        cid:this.cid,
        sonCid:this.sid,
      }).then(res=>{
        this.dataList = res.data;
        this.count = res.count;
      })
    },
    //向父组件传值
    toFuCLick(item){
      this.$emit('send',item);
      this.isShowDia();
    }
  }
};
</script>

<style scoped lang="less">
/deep/.el-dialog__header{
  padding: 0;
}
/deep/.el-dialog__body{
  padding: 0;
}
/deep/.el-dialog__headerbtn{
  top: 15px;
}
.title{
  padding: 0 80px 0 20px;
  height: 42px;
  line-height: 42px;
  border-bottom: 1px solid #eee;
  font-size: 14px;
  color: #333;
  overflow: hidden;
  background-color: #F8F8F8;
  border-radius: 2px 2px 0 0;
}
.box{
  height: 520px;
  .left{
    width: 135px;
    margin: 20px;
    padding-right: 10px;
    border-right: 1px solid #f2f2f2;
    box-sizing: border-box;
    overflow-y: auto;
    .layui-colla-item{
      border: 0;
      font-size: 12px;
      cursor: pointer;
      .layui-colla-title{
        position: relative;
        padding: 0 15px 0  15px;
        color: #333;
        cursor: pointer;
        overflow: hidden;
        height: 32px;
        background-color: #fff;
        border: 0;
        line-height: 32px;
        font-size: 12px;
        font-weight: 400;
        .layui-colla-icon{
          position: absolute;
          left: 0;
          top: 7px;
        }
      }
      .layui-colla-content {
        display: none;
        padding: 0;
        line-height: 22px;
        color: #666;
      }
      .classification-item{
        padding-left: 26px;
        border: 0;
      }
    }
  }
  .right{
    flex: 1;
    margin-top: 20px;
    margin-right: 20px;
    .ns-single-filter-box {
      display: flex;
      justify-content: space-between;
      padding: 0;
      background-color: #fff;
    }
  }
}
.layui-show{
  display: block !important;
}
.ns-text-color {
  color: #FF6A00 !important;
}
</style>
