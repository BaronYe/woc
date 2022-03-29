<template>
  <div class="container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="积分分类" name="classify">
        <v-prompt :title="[
        '添加积分商品的时候需要选择对应的积分分类,用户可以根据积积分分类搜索积分商品。',
        '积分商品属性是前台搜索分类查询积分商品之后可以通过商品的属性进行进一步搜索。']">
        </v-prompt>
        <div class="d-flex ai-center">
          <el-button class="simon-button" @click="addClassify()">添加积分分类</el-button>
        </div>
        <!-- 列表 -->
        <template>
          <el-table
              :data="coassifyList"
              style="width: 100%;margin-top: 20px;">
            <el-table-column
                label="分类名称"
                align="left"
                prop="name">
            </el-table-column>
            <el-table-column
                label="操作"
                width="200"
                align="center">
              <template slot-scope="scope">
                <el-button
                    @click.native.prevent="editClick(scope.row)"
                    type="text"
                    size="small">
                  编辑
                </el-button>
                <el-button
                    @click.native.prevent="delClick(scope.row.id)"
                    type="text"
                    size="small">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </template>
        <!-- 弹窗 -->
        <template>
          <el-dialog
              title="添加积分分类"
              :visible.sync="dialogVisible"
              top="30vh"
              width="20%">
            <div>
              <el-form label-position="right" :model="form" :rules="rules" ref="classifyRef" v-if="form != null"
                       :label-width="formLabelWidth">
                <el-form-item label="分类名称：" prop="name">
                  <el-input style="width: 200px;" v-model="form.name" placeholder="请填写分类名称"></el-input>
                </el-form-item>
                <div style="margin: 40px auto 0">
                  <el-button class="simon-button" style="width:100px;display: block;margin:auto;"
                             @click="setClick('classifyRef')">保存
                  </el-button>
                </div>
              </el-form>
            </div>
          </el-dialog>
        </template>
      </el-tab-pane>
      <el-tab-pane label="积分商品" name="goods">
        <v-prompt :title="[
        '积分商品是对积分商城中所以商品的管理，编辑',
        ]">
        </v-prompt>
        <div class="d-flex ai-center">
          <el-button class="simon-button" @click="addGoods()">添加积分商品</el-button>
        </div>
        <!--商品的弹窗-->
        <template>
          <el-drawer
              title="积分商品的编辑"
              :visible.sync="dialogFormVisible"
              size="30%"
              :wrapperClosable="false"
              direction="rtl">
            <div class="simon-drawer__content">
              <el-form label-position="right" :model="goods" :rules="rulesGoods" ref="goodsRef" v-if="goods != null"
                       :label-width="formLabelWidth">

                <el-form-item label="商品标题：" prop="title">
                  <el-input v-model="goods.title" style="width: 200px" placeholder="请填写商品标题"></el-input>
                </el-form-item>
                <el-form-item label="商品介绍：">
                  <el-input v-model="goods.content" style="width: 200px" placeholder="请填写商品介绍"></el-input>
                </el-form-item>
                <el-form-item label="商品分类：" prop="cid">
                  <el-select v-model="goods.cid" placeholder="全部" style="width:200px;">
                    <el-option
                        v-for="item in coassifyList"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="积分数量：" prop="totalCount">
                  <el-input-number v-model="goods.totalCount"  style="width:200px;" :min="1" placeholder="请填写价格"></el-input-number>
                </el-form-item>
                <el-form-item label="积分限购数量：" >
                  <el-input-number v-model="goods.isPurchasing"  style="width:200px;" :min="0" placeholder="请填写积分限购数量"></el-input-number>
                </el-form-item>
                <div class="ns-word-aux" >
                  <p>数量为“0“则不限购</p>
                </div>
                <el-form-item label="选择商品：" prop="iName">
                  <el-select v-model="goods.iName" style="width: 300px" no-data-text="加载中"  placeholder="请选择商品" @click.native="toGoodBox()"></el-select>
                </el-form-item>
                <el-form-item label="是否上架：">
                  <el-radio v-model="goods.isShelves" label="0">放入仓库</el-radio>
                  <el-radio v-model="goods.isShelves" label="1">立即上架</el-radio>
                </el-form-item>

              </el-form>
              <div class="simon-drawer__footer">
                <el-button @click="dialogFormVisible=false">取 消</el-button>
                <el-button class="simon-button" @click="setGoodsClick('goodsRef')">确 定</el-button>
              </div>
            </div>
          </el-drawer>
        </template>
        <div class="serach-box mt-20">
          <div @click.stop="isOpen = !isOpen" class="cp simon-colla-title">
            <span class="put-open">{{ isOpen ? '收起' : '展开' }}</span>
            <i :class="[isOpen?'el-icon-arrow-down':'el-icon-arrow-up']"></i>
          </div>
          <div class="ns-screen">
            <div class="simon-colla-content" :class="[isOpen ? 'simon_show' : '']">
              <el-form label-position="right" label-width="120px">
                <div class="d-flex ai-center">
                  <el-form-item label="商品标题：">
                    <el-input style="width: 200px;" v-model="name" placeholder="请填写商品标题"></el-input>
                  </el-form-item>
                  <el-form-item label="商品分类：">
                    <el-select v-model="cid" placeholder="全部">
                      <el-option
                          v-for="item in coassifyList"
                          :key="item.id"
                          :label="item.name"
                          :value="item.id">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </div>
                <el-form-item>
                  <el-button style="background-color: var(--orange);color: var(--white);border:none" @click="screenClick">
                    筛选
                  </el-button>
                  <el-button @click="resetClick()">重置</el-button>
                </el-form-item>
              </el-form>

            </div>
          </div>
        </div>
        <v-nav-menu :menu="menu" :menuId="menuId" @send="sendClick"/>

        <div class="d-flex ai-center mt-20">
          <button class="s-button" @click="batchItemFn('0')">批量上架</button>
          <button class="s-button" @click="batchItemFn('1')">批量下架</button>
          <button class="s-button" @click="batchItemFn('2')">批量删除</button>
        </div>
        <!--列表-->
        <template>
          <el-table
              ref="multipleTable"
              :data="dataList"
              tooltip-effect="dark"
              style="width: 100%;margin-top: 20px;"
              @selection-change="handleSelectionChange">
            <el-table-column
                type="selection"
                width="55">
            </el-table-column>
            <el-table-column
                prop="id"
                align="center"
                width="120px"
                label="#">
            </el-table-column>
            <el-table-column
                prop="title"
                align="center"
                label="商品标题">
            </el-table-column>
            <el-table-column
                align="center"
                label="商品分类">
              <template slot-scope="scope">
                <div v-for="(item,index) of coassifyList" :key="index" v-if="scope.row.cid == item.id">{{ item.name }}</div>
              </template>
            </el-table-column>
            <el-table-column
                prop="totalCount"
                align="center"
                label="积分数量">
            </el-table-column>
            <el-table-column
                align="center"
                label="商品状态">
              <template slot-scope="scope">
                <div>{{ scope.row.isShelves == '0' ? '仓库中' : '销售中' }}</div>
              </template>
            </el-table-column>
            <el-table-column
                align="center"
                width="120px"
                label="商品主图">
              <template slot-scope="scope">
                <el-image :src="scope.row.item.imgs[0]" :preview-src-list="[scope.row.item.imgs[0]]" style="width: 40px;height: 40px;"></el-image>
              </template>
            </el-table-column>
            <el-table-column
                prop="item.title"
                align="center"
                label="商品名称">
            </el-table-column>
            <el-table-column
                prop="item.price"
                align="center"
                label="商品价格">
            </el-table-column>
            <el-table-column
                prop="sales"
                align="center"
                label="销量">
            </el-table-column>
            <el-table-column
                label="操作"
                align="center">
              <template slot-scope="scope">
                <el-button
                    @click="editGoodClick(scope.row.id)"
                    type="text"
                    size="small">
                  编辑
                </el-button>
                <el-button
                    @click.native.prevent="delGoodClick(scope.row.id)"
                    type="text"
                    size="small">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>

        </template>
        <!-- 分页 -->
        <template>
          <div style="margin-top: 20px;text-align: right;">
            <el-pagination
                background
                layout="total, sizes,prev, pager, next"
                :page-size="pagesize"
                :total="count"
                :current-page.sync="page"
                :page-sizes="[10, 20, 30, 40,50]"
                @current-change="pagingChange"
                @size-change="handleSizeChange"
            ></el-pagination>
          </div>
        </template>
        <v-good-box ref="goodBox" @send="doSend"></v-good-box>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import prompt from '@components/dist/prompt';
import goodBox from '@components/dist/goodBox';
import navMenu from '@components/dist/navMenu';

export default {
  name: 'integral',
  components:{
    'v-prompt': prompt,
    'v-good-box':goodBox,
    'v-nav-menu': navMenu,
  },
  data(){
    return{
      activeName:"classify",
      dialogVisible: false,
      form: null,
      formLabelWidth: '120px',
      options: [],
      rules: {
        name: [{ required: true, message: '请填写分类名称' }]
      },
      coassifyList:[],
      dialogFormVisible:false,
      goods:null,
      rulesGoods:{
        title: [{ required: true, message: '请填写商品名称' }],
        cid: [{ required: true, message: '请填写商品分类' }],
        iName: [{ required: true, message: '请选择商品' }],
      },
      cid:"",
      page: 1,
      pagesize: 10,
      dataList: [],
      count: 0,
      menu: [
        {
          id: '-1',
          name: '全部'
        }, {
          id: '0',
          name: '仓库中'
        }, {
          id: '1',
          name: '销售中'
        }
      ],
      menuId: '-1',
      name:"",
      isOpen:true,
      idx:[]
    }
  },
  mounted() {
    this.getClassifyList();
  },
  methods:{
    handleClick(tab){
      if (tab.index == 0){
        this.getClassifyList();
      }else{
        this.getGoodsList();
      }
    },
    // 添加
    addClassify() {
      this.form = {
        name: '',
        id: 0
      };
      this.dialogVisible = !this.dialogVisible;
    },
    //修改
    editClick(item){
      this.form = {
        name: item.name,
        id: item.id
      };
      this.dialogVisible = !this.dialogVisible;
    },
    // 删除
    delClick(id){
      this.$confirm('此操作将删除该分类, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.delIntegralClassifyById({id}).then(res=>{
          this.$message.success("删除成功");
          setTimeout(()=>{
            this.getClassifyList();
          },1500)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 保存
    setClick(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.api.setIntegralClassify({
            id: this.form.id,
            name: this.form.name,
          }).then(res => {
            this.$message.success("保存成功");
            setTimeout(()=>{
              this.dialogVisible = !this.dialogVisible;
              this.form = {
                name: '',
                id: 0
              };
              this.getClassifyList();
            },1500)
          });
        }
      });
    },
    // 获取分类
    getClassifyList(){
      this.api.getIntegralClassify().then(res=>{
        this.coassifyList = res.data;
      })
    },
    // 添加积分商品
    addGoods(){
      this.dialogFormVisible = !this.dialogFormVisible;
      this.goods = {
        id:0,
        cid:"",
        totalCount:0,
        isPurchasing:0,
        title:"",
        content:"",
        itemId:"",
        isShelves:"0",
        iName:""
      }
    },
    // 选择商品
    toGoodBox(){
      this.$refs['goodBox'].isShowDia();
    },
    //获取到子组件传入的值
    doSend(e){
      this.goods.iName = e.title;
      this.goods.itemId = e.id;
    },
    // 保存商品
    setGoodsClick(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.api.setIntegralGoods({
            id: this.goods.id,
            cid: parseInt(this.goods.cid),
            itemId: parseInt(this.goods.itemId),
            totalCount: parseInt(this.goods.totalCount),
            isPurchasing: parseInt(this.goods.isPurchasing),
            isShelves:this.goods.isShelves,
            title:this.goods.title,
            content:this.goods.content,
          }).then(res => {
            this.$message.success("保存成功");
            setTimeout(()=>{
              this.dialogFormVisible = !this.dialogFormVisible;
              this.goods = {
                id:0,
                cid:"",
                totalCount:0,
                title:"",
                content:"",
                itemId:"",
                isShelves:"0",
                isPurchasing:0,
                iName:""
              }
              this.getGoodsList();
            },1500)
          });
        }
      });
    },
    // 获取商品列表
    getGoodsList(){
      this.api.getIntegralGoods({
        page: this.page,
        pagesize: this.pagesize,
        name: this.name,
        cid: this.cid == '' ? -1 : this.cid,
        isShelves: this.menuId
      }).then(res => {
        this.dataList = res.data;
        this.count = res.count;
      });
    },
    // menu选择
    sendClick(e) {
      this.menuId = e.id;
      this.page = 1;
      this.getGoodsList();
    },
    // 分页
    pagingChange(p) {
      this.page = p;
      this.getGoodsList();
    },
    // 每页条数
    handleSizeChange(val){
      this.page = 1;
      this.pagesize = val;
      this.getGoodsList();
    },
    // 筛选
    screenClick() {
      this.page = 1;
      this.getGoodsList();
    },
    // 重置
    resetClick() {
      this.cid = '';
      this.name = '';
      this.page = 1;
      this.getGoodsList();
    },
    // 删除商品
    delGoodClick(id){
      this.$confirm('此操作将删除该商品, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.delIntegralGoodsById({id}).then(res=>{
          this.$message.success("删除成功");
          setTimeout(()=>{
            this.getGoodsList();
          },1500)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 编辑商品详情
    editGoodClick(id){
      this.api.getIntegralGoodsById({id}).then(res=>{
        this.goods = res.data;
        this.dialogFormVisible = !this.dialogFormVisible;
      })
    },
    // 列表多选框
    handleSelectionChange(val) {
      let arr = [];
      val.forEach(item => {
        arr.push(item.id);
      });
      this.idx = arr;
    },
    // 批量上下架
    batchItemFn(types) {
      if (this.idx.length === 0) {
        return this.$message.warning('请选择要操作的数据');
      }
      this.$confirm('是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.batchIntegral({
          types:types,
          idx:this.idx.join(',')
        }).then(res=>{
          this.$message.success("操作成功");
          setTimeout(()=>{
            this.getGoodsList();
          },1500)
        })
      })
    },
  }
};
</script>

<style scoped lang="less">
/deep/ .el-image-viewer__close {
  color: #fff;
  top: 100px;
  right: 100px;
}
/deep/ .el-tabs__item.is-active{
  color: var(--orange);
}
/deep/.el-tabs__active-bar{
  background-color: var(--orange);
}
/deep/.el-tabs__item:hover{
  color: var(--orange);
}
.simon-drawer__content {
  display: flex;
  flex-direction: column;
  height: 100%;

  form {
    flex: 1;
  }

  .simon-drawer__footer {
    display: flex;
    margin-top: 40px;
    padding-bottom: 10px;

    button {
      flex: 1;
    }
  }
}

/deep/ .el-drawer__body {
  overflow-y: auto;
  overflow-x: hidden;
  padding: 20px 20px 10px;
}
/deep/.el-radio__label{
  font-size: 14px;
  font-weight: 400;
  color: #bbbbbb;
}
/deep/.el-radio__input.is-checked+.el-radio__label{
  color: var(--orange);
}
/deep/.el-radio__input.is-checked .el-radio__inner{
  border-color: var(--orange);
  background:var(--orange);
}
/deep/ .el-pagination.is-background .el-pager li:not(.disabled).active {
  background-color: var(--orange);
}
.ns-screen {
  border: 0;
  background-color: #fff;
  border-radius: 5px;
  min-height: 45px;
}

.serach-box {
  position: relative;

  .simon-colla-content {
    padding: 20px 0 10px;
    border: 1px solid #f1f1f1;
    display: none;
  }
}

.simon_show {
  display: block !important;
}

.simon-colla-title {
  position: initial;
  height: 0;
  padding: 0 15px 0 35px;
  color: #333;
  cursor: pointer;
  font-size: 14px;
  overflow: hidden;

  .put-open {
    position: absolute;
    right: 40px;
    padding: 5px;
    color: #3D88FB;
    cursor: pointer;
    top: 5px;
  }

  i {
    color: #3D88FB !important;
    position: absolute;
    left: auto;
    transform: translateX(-50%);
    right: 10px;
    z-index: 2;
    top: 8px;
    padding: 5px;
  }
}

.ns-word-aux{
  margin-left: 120px;
  display: block;
  margin-top: 5px;
  color: #B2B2B2;
  font-size: 12px;
  line-height: 1;
  position: relative;
  top: -10px;
}
</style>
