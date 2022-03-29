<template>
  <div>
    <el-drawer
        title="店铺管理"
        :visible.sync="dialogFormVisible"
        size="50%"
        :wrapperClosable="false"
        direction="rtl">
      <div>
        <div class="d-flex ai-center">
          <el-button class="simon-button" @click="addShopFn()">添加店铺</el-button>
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
                label="#">
            </el-table-column>
            <el-table-column
                prop="shopName"
                align="center"
                label="店铺名称">
            </el-table-column>
            <el-table-column
                prop="createTime"
                align="center"
                label="创建时间">
            </el-table-column>
            <el-table-column
                label="操作"
                align="center">
              <template slot-scope="scope">
                <el-button
                    @click="untieFn(scope.row.id,scope.row.shopId)"
                    type="text"
                    size="small">
                  解绑
                </el-button>
              </template>
            </el-table-column>
          </el-table>

        </template>
      </div>
    </el-drawer>

    <el-drawer
        title="店铺编辑"
        :visible.sync="dialogShopVisible"
        size="30%"
        :wrapperClosable="false"
        direction="rtl">
      <div class="simon-drawer__content">
        <el-form label-position="right" :model="form" :rules="rules" ref="goodBoxRef" v-if="form != null"
                 label-width="100px">
          <el-form-item label="选择店铺：" prop="shopName">
            <el-select v-model="form.shopName" style="width: 200px" no-data-text="加载中"  placeholder="请选择店铺" @click.native="toShopBox()"></el-select>
          </el-form-item>

        </el-form>
        <div class="simon-drawer__footer">
          <el-button @click="dialogShopVisible=false">取 消</el-button>
          <el-button class="simon-button" @click="setClick('goodBoxRef')">确 定</el-button>
        </div>
      </div>
    </el-drawer>
    <v-shop-box ref="shopBox" @send="doSend"></v-shop-box>
  </div>
</template>

<script>
import shopBox from '@components/dist/shopBox';

export default {
  name: 'shop',
  components:{
    'v-shop-box':shopBox
  },
  data() {
    return {
      dialogFormVisible: false,
      form: null,
      rules: {
        shopName: [{ required: true, message: '请选择店铺' }],
      },
      dialogShopVisible:false,
      merchantId:0,
      dataList:[]
    };
  },
  mounted() {

  },
  methods: {
    // 显示
    isShowDia(id, isVisible) {
      this.merchantId = id;
      this.dialogFormVisible = isVisible;
      this.getMerchantShopFn();
    },
    // 获取店铺
    getMerchantShopFn(){
      this.api.getMerchantShop({
        merchantId:this.merchantId
      }).then(res=>{
        this.dataList = res.data;
      })
    },
    // 添加店铺
    addShopFn(){
      this.form = {
        id:0,
        merchantId:this.merchantId,
        shopId:0,
        shopName:"",
      }
      this.dialogShopVisible = !this.dialogShopVisible;
    },
    // 选择店铺
    toShopBox(){
      this.$refs['shopBox'].isShowDia();
    },
    //获取到子组件传入的值
    doSend(e){
      this.form.shopName = e.name;
      this.form.shopId = e.id;
    },
    // 保存
    setClick(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.api.setMerchantShop({
            id:this.form.id,
            shopId:this.form.shopId,
            merchantId:this.form.merchantId,
          }).then(res=>{
            this.$message.success('保存成功');
            setTimeout(() => {
              this.dialogShopVisible = !this.dialogShopVisible;
              this.getMerchantShopFn();
            }, 1500);
          })
        }
      });
    },
    // 解绑
    untieFn(id,shopId){
      this.$confirm('此操作将解绑该店铺, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.delMerchantShopById({
          id:id,
          shopId:shopId,
        }).then(res=>{
          this.$message.success('操作成功');
          setTimeout(() => {
            this.getMerchantShopFn();
          }, 1500);
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    }
  }
};
</script>

<style scoped lang="less">
/deep/ .el-image-viewer__close {
  color: #fff;
  top: 100px;
  right: 100px;
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

/deep/ .el-pagination.is-background .el-pager li:not(.disabled).active {
  background-color: var(--orange);
}
</style>
