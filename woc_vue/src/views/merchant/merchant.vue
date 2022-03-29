<template>
  <div class="container">
    <v-prompt :title="[
        '当前页面对商户的信息进行管理，可以添加商户，管理商户等',
    ]">
    </v-prompt>
    <div class="d-flex ai-center">
      <el-button class="simon-button" @click="addModelFn()">添加商户</el-button>
      <div class="ml-auto">
        <el-input placeholder="请输入商户名称" v-model="name">
          <el-button slot="append" icon="el-icon-search" @click="serachClick()"></el-button>
        </el-input>
      </div>
    </div>
    <!--列表-->
    <template>
      <el-table
          :data="dataList"
          style="width: 100%;margin-top: 20px;">
        <el-table-column
            label="#"
            align="center"
            prop="id">
        </el-table-column>
        <el-table-column
            label="商户名称"
            align="center"
            prop="name">
        </el-table-column>
        <el-table-column
            label="商户账号"
            align="center"
            prop="username">
        </el-table-column>
        <el-table-column
            label="状态"
            align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.isDisable == '0'">正常</div>
            <div v-if="scope.row.isDisable == '1'">禁用</div>
          </template>
        </el-table-column>
        <el-table-column
            label="创建时间"
            align="center"
            prop="createTime">
        </el-table-column>
        <el-table-column
            label="店铺"
            align="center">
          <template slot-scope="scope">
            <el-button
                @click="mentModelFn(scope.row.id)"
                type="text"
                size="small">
              管理
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
            label="操作"
            width="200"
            align="center">
          <template slot-scope="scope">
            <el-button
                @click="setDisableFn(scope.row.id,'1')"
                type="text"
                v-if="scope.row.isDisable == '0'"
                size="small">
              封禁
            </el-button>
            <el-button
                @click="setDisableFn(scope.row.id,'0')"
                type="text"
                v-if="scope.row.isDisable == '1'"
                size="small">
              解封
            </el-button>
            <el-button
                @click="editModelFn(scope.row.id)"
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
    <!-- 分页 -->
    <template>
      <div style="margin-top: 20px;text-align: right;">
        <el-pagination
            background
            layout="total, prev, pager, next"
            :page-size="pagesize"
            :total="count"
            :current-page.sync="page"
            @current-change="pagingChange"
        ></el-pagination>
      </div>
    </template>
    <!--弹窗-->
    <template>
      <el-drawer
          title="商户编辑"
          :visible.sync="dialogFormVisible"
          size="30%"
          :wrapperClosable="false"
          direction="rtl">
        <div class="simon-drawer__content">
          <el-form label-position="right" :model="form" :rules="rules" ref="formRef" v-if="form != null"
                   label-width="110px">
            <el-form-item label="商户名称：" prop="name">
              <el-input v-model="form.name" style="width: 200px" placeholder="请填写商户名称"></el-input>
            </el-form-item>
            <el-form-item label="商户账号：" prop="username">
              <el-input v-model="form.username" style="width: 200px" placeholder="请填写商户账号"></el-input>
            </el-form-item>
            <el-form-item label="商户密码：" prop="password">
              <el-input v-model="form.password" type="password" style="width: 200px" placeholder="请填写商户密码"></el-input>
            </el-form-item>
          </el-form>
          <div class="simon-drawer__footer">
            <el-button @click="dialogFormVisible=false">取 消</el-button>
            <el-button class="simon-button" @click="setClick('formRef')">确 定</el-button>
          </div>
        </div>
      </el-drawer>
    </template>
    <v-shop ref="vShop" ></v-shop>
  </div>
</template>

<script>
import prompt from '@components/dist/prompt';
import shop from '@views/merchant/shop';

export default {
  name: 'merchant',
  components: {
    'v-prompt': prompt,
    'v-shop':shop
  },
  data(){
    return{
      form:null,
      dialogFormVisible:false,
      rules:{
        username: [{ required: true, message: '请填写商户账号' }],
        password: [{ required: true, message: '请填写商户密码' }],
        name: [{ required: true, message: '请填写商户名称' }],
      },
      page:1,
      pagesize:10,
      name:"",
      dataList:[],
      count:0
    }
  },
  mounted() {
    this.getMerchantFn();
  },
  methods:{
    //查询商户
    getMerchantFn(){
      this.api.getMerchant({
        page:this.page,
        pagesize:this.pagesize,
        name:this.name,
      }).then(res=>{
        this.dataList = res.data;
        this.count = res.count;
      })
    },
    // 搜索
    serachClick(){
      this.page = 1;
      this.getMerchantFn();
    },
    //分页
    pagingChange(p){
      this.page = p;
      this.getMerchantFn();
    },
    // 添加
    addModelFn(){
      this.form = {
        id:0,
        username:"",
        password:"",
        name:"",
      };
      this.dialogFormVisible = !this.dialogFormVisible
    },
    // 编辑
    editModelFn(id){
      this.api.getMerchantById({id}).then(res=>{
        this.form = {
          id:res.data.id,
          username:res.data.username,
          password:res.data.password,
          name:res.data.name,
        };
        this.dialogFormVisible = !this.dialogFormVisible
      })
    },
    // 封禁与解封
    setDisableFn(id,isDisable){
      let title = "";
      if (isDisable == "1") {
        title = '此操作将禁用该商户, 是否继续?';
      }else{
        title = '此操作将解封该商户, 是否继续?';
      }
      this.$confirm(title, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.setDisable({
          id:id,
          isDisable:isDisable
        }).then(res=>{
          this.$message.success('操作成功');
          setTimeout(() => {
            this.getMerchantFn();
          }, 1500);
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
          this.api.setMerchant(this.form).then(res=>{
            this.$message.success('保存成功');
            setTimeout(() => {
              this.dialogFormVisible = !this.dialogFormVisible;
              this.getMerchantFn();
            }, 1500);
          })
        }
      });
    },
    // 删除
    delClick(id){
      this.$confirm('此操作将删除该商户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.delMerchantById({id}).then(res=>{
          this.$message.success("删除成功");
          setTimeout(()=>{
            this.getMerchantFn();
          },1500)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 管理
    mentModelFn(id){
      this.$refs['vShop'].isShowDia(id,true);
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
/deep/ .el-pagination.is-background .el-pager li:not(.disabled).active{
  background-color:var(--orange);
}
</style>
