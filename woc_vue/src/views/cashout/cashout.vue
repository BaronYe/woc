<template>
  <div class="container">
    <v-prompt :title="[
        '会员提现展示所有会员提现的信息',
        '转账时，最好上传转账凭证和转账说明',
    ]">
    </v-prompt>
    <div class="serach-box mt-20">
      <div @click.stop="isOpen = !isOpen" class="cp simon-colla-title">
        <span class="put-open">{{ isOpen ? '收起' : '展开' }}</span>
        <i :class="[isOpen?'el-icon-arrow-down':'el-icon-arrow-up']"></i>
      </div>
      <div class="ns-screen">
        <div class="simon-colla-content" :class="[isOpen ? 'simon_show' : '']">
          <el-form label-position="right" label-width="120px">
            <div class="d-flex ai-center">
              <el-form-item label="提现码：">
                <el-input style="width: 200px;" v-model="codeName" placeholder="请输入提现码"></el-input>
              </el-form-item>
              <el-form-item label="状态：">
                <el-select v-model="state" placeholder="全部">
                  <el-option
                      v-for="item in stateList"
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
              <el-button @click="resetClick">重置</el-button>
            </el-form-item>
          </el-form>

        </div>
      </div>
    </div>
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
            align="center">
        </el-table-column>
        <el-table-column
            prop="cashOutCode"
            label="提现码"
            align="center">
        </el-table-column>
        <el-table-column
            label="申请提现金额"
            align="center">
          <template slot-scope="scope">
            <div>{{getTotalMoney(scope.row.price,scope.row.cashOutPoundage)}}</div>
          </template>
        </el-table-column>
        <el-table-column
            prop="cashOutPoundage"
            label="提现手续费"
            align="center">
        </el-table-column>
        <el-table-column
            prop="price"
            label="实际转账金额"
            align="center">
        </el-table-column>
        <el-table-column
            label="提现状态"
            align="center">
          <template slot-scope="scope">
            <div v-for="(item,index) of stateList" :key="index" v-if="scope.row.cashOutState == item.id">{{item.name}}</div>
          </template>
        </el-table-column>
        <el-table-column
            prop="createTime"
            label="申请时间"
            align="center">
        </el-table-column>
        <el-table-column
            label="操作"
            align="center">
          <template slot-scope="item">
            <el-button
                @click="toModelClick(item.row)"
                type="text"
                size="small">
              查看
            </el-button>
            <el-button
                @click="doModelClick(item.row)"
                type="text"
                v-if="item.row.cashOutState == '0'"
                size="small">
              转账
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
        top="10vh"
        :close-on-click-modal="false"
        width="40%">
      <div class="title">{{title}}</div>
      <div class="box">
        <el-form label-position="right" :model="rowObject"  v-if="rowObject != null"
                 label-width="200px">
          <el-form-item label="提现状态：">
            <div v-for="(item,index) of stateList" :key="index" v-if="rowObject.cashOutState == item.id">{{item.name}}</div>
          </el-form-item>
          <el-form-item label="提现码：">
            <div>{{rowObject.cashOutCode}}</div>
          </el-form-item>
          <el-form-item label="申请提现金额：">
            <div>{{getTotalMoney(rowObject.price,rowObject.cashOutPoundage)}}</div>
          </el-form-item>
          <el-form-item label="提现手续费：">
            <div>{{rowObject.cashOutPoundage}}</div>
          </el-form-item>
          <el-form-item label="提现到账金额：">
            <div>{{rowObject.price}}</div>
          </el-form-item>
          <el-form-item label="提现申请时间：">
            <div>{{rowObject.createTime}}</div>
          </el-form-item>
          <el-form-item label="转账凭证：">
            <el-col :span="12">
              <el-upload
                  class="avatar-uploader"
                  :action="`${this.siteinfo.siteroot}${this.siteinfo.project}/oss/upload`"
                  :show-file-list="false"
                  :disabled="types"
                  title="建议尺寸 375px*200px"
                  :on-success="imgsSuccess">
                <img v-if="rowObject.voucher" :src="rowObject.voucher" class="avatar"
                     alt="暂无">
                <i v-else class="el-icon-upload2 avatar-uploader-icon" style="font-size: 24px;line-height: 100px;"></i>
              </el-upload>
            </el-col>
          </el-form-item>
          <el-form-item label="转账凭证说明：">
            <el-input
                type="textarea"
                :rows="5"
                :disabled="types"
                resize="none"
                style="width: 450px;"
                v-model="rowObject.explains">
            </el-input>
          </el-form-item>
        </el-form>
        <div style="margin-left: 200px;margin-top: 20px;">
          <el-button style="padding: 10px 18px !important;" @click="dialogVisible=false">返 回</el-button>
          <el-button class="simon-button" @click="setClick()" v-if="!types">确 定</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import prompt from '@components/dist/prompt';

export default {
  name: 'cashout',
  components: {
    'v-prompt': prompt
  },
  data(){
    return{
      isOpen: true,
      codeName: '',
      state: '',
      stateList: [
        {
          id:"",
          name:"全部"
        },
        {
          id:"0",
          name:"待转账"
        },{
          id:"1",
          name:"已转账"
        }
      ],
      page:1,
      pagesize:10,
      dataList:[],
      count:0,
      dialogVisible:false,
      title:"提现信息",
      types:false,
      rowObject:null
    }
  },
  computed:{
    getTotalMoney:function(){
      return function(a,b){
        return (parseFloat(a) + parseFloat(b)).toFixed(2);
      }
    }
  },
  mounted() {
    this.getBalanceAndAdminFn();
  },
  methods:{
    //筛选
    screenClick(){
      this.page = 1;
      this.getBalanceAndAdminFn();
    },
    // 重置
    resetClick() {
      this.codeName = '';
      this.state = '';
      this.page = 1;
      this.getBalanceAndAdminFn();
    },
    // 获取提现列表
    getBalanceAndAdminFn(){
      this.api.getBalanceAndAdmin({
        page:this.page,
        pagesize:this.pagesize,
        codeName:this.codeName,
        state:this.state
      }).then(res=>{
        this.dataList = res.data;
        this.count = res.count;
      })
    },
    // 分页
    pageChange(p){
      this.page = p;
      this.getBalanceAndAdminFn();
    },
    // 查看
    toModelClick(row){
      this.types = true;
      this.rowObject = JSON.parse(JSON.stringify(row));
      this.dialogVisible = !this.dialogVisible;
    },
    // 转账
    doModelClick(row){
      this.types = false;
      this.rowObject = JSON.parse(JSON.stringify(row));
      this.dialogVisible = !this.dialogVisible;
    },
    // 上传凭证
    imgsSuccess(res){
      this.rowObject.voucher = res.data;
    },
    // 确认转账
    setClick(){
      this.api.setUserBalance({
        id:this.rowObject.id,
        voucher:this.rowObject.voucher,
        explains:this.rowObject.explains,
      }).then(res=>{
        this.$message.success('操作成功');
        setTimeout(() => {
          this.dialogVisible = !this.dialogVisible;
          this.getBalanceAndAdminFn();
        }, 1500);
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
  padding-bottom: 30px;
}
.layui-show{
  display: block !important;
}
.ns-text-color {
  color: #FF6A00 !important;
}

/deep/ .el-form{
  margin-top: 16px;
  margin-bottom: 0;
}
/deep/ .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item{
  margin-bottom: 10px;
}
/deep/ .el-upload--text {
  width: 100%;
  height: 100%;
  border: none;
}

.avatar-uploader {
  width: 188px;
  height: 100px;
  line-height: 100px;
  border: 1px #ddd dashed;
  margin-left: 5px;
}

.avatar {
  width: 188px;
  height: 100px;
  display: block;
}
</style>
