<template>
  <div class="container">
    <v-prompt :title="[
        '会员反馈展示所有会员反馈的信息',
    ]">
    </v-prompt>
    <v-nav-menu :menu="menu" :menuId="menuId" @send="sendClick"/>
    <template>
      <el-table
          :data="dataList"
          style="width: 100%;margin-top: 20px;">
        <el-table-column
            label="头像"
            width="100"
            align="center">
          <template slot-scope="item">
            <div v-if="item.row.users.avatar != null">
              <el-image
                  style="width: 33px; height:33px;"
                  :src="item.row.users.avatar"
                  :preview-src-list="[item.row.users.avatar]">
                <div slot="error" class="image-slot">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column
            prop="users.nickname"
            label="昵称"
            width="250"
            align="center">
        </el-table-column>
        <el-table-column
            prop="content"
            label="反馈内容"
            align="center">
        </el-table-column>
        <el-table-column
            label="状态"
            width="200"
            align="center">
          <template slot-scope="scope">
            <div v-for="(item,index) of menu" :key="index" v-if="scope.row.state == item.id">{{item.name}}</div>
          </template>
        </el-table-column>
        <el-table-column
            prop="createTime"
            label="创建时间"
            width="200"
            align="center">
        </el-table-column>
        <el-table-column
            label="操作"
            width="200"
            align="center">
          <template slot-scope="scope">
            <el-button
                @click.native.prevent="withClick(scope.row.id)"
                type="text"
                size="small">
              处理
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
      <div  style="margin-top: 20px;text-align: right;">
        <el-pagination
            background
            layout="total, prev, pager, next"
            :page-size="10"
            :total="count"
            :current-page.sync="page"
            @current-change="pageChange"
        ></el-pagination>
      </div>
    </template>
    <!-- 转账莫太框 -->
    <el-dialog
        :visible.sync="dialogVisible"
        top="40vh"
        :close-on-click-modal="false"
        width="300px">
      <div class="title">操作处理</div>
      <div class="box">
        <el-form label-position="right"  label-width="100px">
          <el-form-item label="处理结果：">
            <el-select v-model="state" style="width: 160px">
              <el-option
                  v-for="item in stateList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div style="margin-top: 20px;" class="simon-drawer__footer">
          <el-button style="padding: 10px 18px !important;" @click="dialogVisible=false">返 回</el-button>
          <el-button class="simon-button" @click="setClick()">确 定</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import prompt from '@components/dist/prompt';
import navMenu from '@components/dist/navMenu';

export default {
  name: 'feedbacks',
  components: {
    'v-prompt': prompt,
    'v-nav-menu': navMenu,
  },
  data() {
    return {
      menu: [
        {
          id: '',
          name: '全部'
        }, {
          id: '0',
          name: '待处理'
        }, {
          id: '1',
          name: '已处理'
        }, {
          id: '2',
          name: '忽略'
        }
      ],
      menuId: '',
      page:1,
      pagesize:10,
      dataList:[],
      count:0,
      form:null,
      dialogVisible:false,
      fid:0,
      stateList: [
       {
          id: '1',
          name: '已处理'
        }, {
          id: '2',
          name: '忽略'
        }
      ],
      state: '1',
    };
  },
  mounted() {
    this.getFeedbacksFn();
  },
  methods: {
    // menu选择
    sendClick(e){
      this.menuId = e.id;
      this.page = 1;
      this.getFeedbacksFn();
    },
    // 获取列表
    getFeedbacksFn(){
      this.api.getFeedbacks({
        page:this.page,
        pagesize:this.pagesize,
        state:this.menuId
      }).then(res=>{
        this.dataList = res.data;
        this.count = res.count;
      })
    },
    // 分页
    pageChange(val){
      this.page = val;
      this.getFeedbacksFn();
    },
    // 删除
    delClick(id) {
      this.$confirm('此操作将删除该反馈, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.delFeedbacksById({ id }).then(res => {
          this.$message.success('删除成功');
          setTimeout(() => {
            this.getFeedbacksFn();
          }, 1500);
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 处理
    withClick(id){
      this.dialogVisible = !this.dialogVisible;
      this.fid = id;
    },
    // 处理
    setClick(){
      this.api.setFeedbacks({
        id:this.fid,
        state:this.state,
      }).then(res=>{
        this.$message.success('操作成功');
        setTimeout(() => {
          this.dialogVisible = !this.dialogVisible;
          this.getFeedbacksFn();
        }, 1500);
      })
    },
  }
};
</script>

<style scoped lang="less">
/deep/ .el-pagination.is-background .el-pager li:not(.disabled).active {
  background-color: var(--orange);
}
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
  //height: 520px;
}
.simon-drawer__footer {
  //display: flex;
  margin-top: 40px;
  padding: 0 20px 20px ;
  text-align: right;
  button {
    flex: 1;
  }
}
/deep/.el-form-item--mini.el-form-item, .el-form-item--small.el-form-item{
  margin-top: 20px;
}
</style>
