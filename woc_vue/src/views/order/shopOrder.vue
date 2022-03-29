<template>
  <div class="container">
    <v-prompt :title="[
        '当前页面盲盒商城订单信息进行管理',
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
              <el-form-item label="订单编号：">
                <el-input style="width: 200px;" v-model="orderNo" placeholder="请填写订单编号"></el-input>
              </el-form-item>
              <el-form-item label="支付类型：">
                <el-select v-model="paymentType" placeholder="全部">
                  <el-option
                      v-for="item in paymentTypeList"
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
    <v-nav-menu :menu="menu" :menuId="orderStatus" @send="sendClick"/>
    <!--列表-->
    <template>
      <el-table
          ref="multipleTable"
          :data="dataList"
          tooltip-effect="dark"
          style="width: 100%;margin-top: 20px;">
        <el-table-column
            prop="orderNo"
            align="center"
            width="150px"
            label="订单编号">
        </el-table-column>
        <el-table-column
            align="center"
            width="150px"
            label="购买用户">
          <template slot-scope="scope">
            <div style="display: flex;align-items: center;justify-content: center">
              <el-image :src="scope.row.user.avatar" :preview-src-list="[scope.row.user.avatar]"
                        style="width: 30px;height: 30px;border-radius: 100%;margin-right: 6px;"></el-image>
              <div>{{ scope.row.user.nickname }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column
            align="center"
            width="100px"
            label="盲盒主图">
          <template slot-scope="scope">
            <el-image :src="scope.row.boxPicture" :preview-src-list="[scope.row.boxPicture]"
                      style="width: 30px;height: 30px;"></el-image>
          </template>
        </el-table-column>
        <el-table-column
            prop="boxName"
            align="center"
            label="盲盒名称">
        </el-table-column>
        <el-table-column
            label="商品信息"
            align="center">
          <template slot-scope="scope">
            <div style="cursor: pointer;" v-if="scope.row.item != ''">
              <el-popover trigger="hover" placement="top">
                <p>商品名称：<span style="color: #20a0ff">{{ scope.row.item.iName }}</span></p>
                <p>商品规格：<span style="color: #20a0ff">{{ scope.row.item.spesc }}</span></p>
                <div slot="reference" class="name-wrapper">
                  <div><span style="color: #409EFF;">查看</span></div>
                </div>
              </el-popover>
            </div>
          </template>
        </el-table-column>
        <el-table-column
            align="center"
            label="支付类型">
          <template slot-scope="scope">
            <div v-if="scope.row.paymentType === 0">余额支付</div>
            <div v-else-if="scope.row.paymentType === 1">微信支付</div>
          </template>
        </el-table-column>
        <el-table-column
            align="center"
            label="订单金额">
          <template slot-scope="scope">
            <div>¥{{ scope.row.orderMoney }} 元</div>
          </template>
        </el-table-column>
        <el-table-column
            align="center"
            label="订单状态">
          <template slot-scope="scope">
            <div v-for="(item,index) of menu" :key="index" v-if="scope.row.orderStatus == item.id">{{ item.name }}</div>
          </template>
        </el-table-column>
        <el-table-column
            align="center"
            label="地址">
          <template slot-scope="scope">
            <div style="cursor: pointer;">
              <el-popover trigger="hover" placement="top">
                <div>
                  <p><span style="color: #999999;">姓名: </span> <span>{{scope.row.address.receiverName}}</span> </p>
                  <p><span style="color: #999999;">电话: </span> <span>{{scope.row.address.receiverMobile}}</span> </p>
                  <p><span style="color: #999999;">地址: </span> <span>{{scope.row.address.receiverProvince}}</span>
                    <span>{{scope.row.address.receiverCity}}</span>
                    <span>{{scope.row.address.receiverRegion}}</span>
                    <span>{{scope.row.address.receiverDetailed}}</span>
                  </p>
                  <p><span style="color: #999999;">邮编: </span><span>{{scope.row.address.receiverZip}}</span> </p>
                </div>
                <div slot="reference" class="name-wrapper" >
                  <div style="color: #20a0ff;margin-left: 10px;">查看</div>
                </div>
              </el-popover>
            </div>
          </template>
        </el-table-column>
        <el-table-column
            prop="createTime"
            align="center"
            label="创建时间">
        </el-table-column>
        <el-table-column
            label="操作"
            width="200"
            align="center">
          <template slot-scope="scope">
            <el-button
                @click="comDialogClick(scope.row.id)"
                v-if="scope.row.orderStatus === 1 "
                type="text"
                size="small">
              发货
            </el-button>
            <el-button
                @click="takeClick(scope.row.id)"
                v-if="scope.row.orderStatus === 2"
                type="text"
                size="small">
              确认收货
            </el-button>
            <el-button
                @click.native.prevent="delNsOderById(scope.row.id)"
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
    <!--发货-->
    <template>
      <el-drawer
          title="填写快递单号"
          :visible.sync="comDialog"
          direction="rtl"
          size="30%">
        <div class="simon-drawer__content">
          <el-form :model="form" :rules="rules" ref="registerRef" v-if="form != null">
            <el-form-item label="快递公司" prop="com" :label-width="formLabelWidth">
              <el-select v-model="form.com" placeholder="请选择快递公司">
                <el-option v-for="item of comList" :key="item.value" :label="item.name" :value="item.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="快递单号" prop="name" :label-width="formLabelWidth">
              <el-input v-model="form.name" autocomplete="off"></el-input>
            </el-form-item>
          </el-form>
          <div class="simon-drawer__footer">
            <el-button @click="comDialog=false">取 消</el-button>
            <el-button class="simon-button" @click="comfirmOrderClick('registerRef')">确 定</el-button>
          </div>
        </div>
      </el-drawer>
    </template>

  </div>
</template>

<script>
import prompt from '@components/dist/prompt';
import navMenu from '@components/dist/navMenu';
import comList from './com';
export default {
  name: 'blindOrder',
  components: {
    'v-prompt': prompt,
    'v-nav-menu': navMenu
  },
  data() {
    return {
      page: 1,
      pagesize: 10,
      userId: 0,
      orderNo: '',
      paymentType: -1,
      orderStatus: -1,
      dataList: [],
      count: 0,
      menu: [
        {
          id: -1,
          name: '全部'
        }, {
          id: 0,
          name: '待付款'
        }, {
          id: 1,
          name: '待发货'
        }, {
          id: 2,
          name: '待收货'
        }, {
          id: 3,
          name: '已完成'
        }
      ],
      isOpen: true,
      paymentTypeList:[
        {
          id: -1,
          name: '全部'
        }, {
          id: 0,
          name: '余额'
        }, {
          id: 1,
          name: '微信'
        }
      ],


      comList: comList,
      comDialog:false,
      form: null,
      formLabelWidth: '100px',
      rules:{
        name: [{ required: true, message: '快递单号不能为空' }],
        com: [{ required: true, message: '快递公司不能为空' }]
      },
      orderId:0
    };
  },
  mounted() {
    this.getSellOrderFn();
  },
  methods: {
    // 分页
    pagingChange(p) {
      this.page = p;
      this.getSellOrderFn();
    },
    // 每页条数
    handleSizeChange(val) {
      this.page = 1;
      this.pagesize = val;
      this.getSellOrderFn();
    },
    // menu选择
    sendClick(e) {
      this.orderStatus = e.id;
      this.page = 1;
      this.getSellOrderFn();
    },
    // 筛选
    screenClick() {
      this.page = 1;
      this.getSellOrderFn();
    },
    // 重置
    resetClick() {
      this.orderNo = '';
      this.paymentType = -1;
      this.orderStatus = -1;
      this.page = 1;
      this.getSellOrderFn();
    },
    // 获取订单列表
    getSellOrderFn() {
      this.api.getSellOrder({
        page: this.page,
        pagesize: this.pagesize,
        userId: this.userId,
        orderNo: this.orderNo,
        paymentType: this.paymentType,
        orderStatus: this.orderStatus,
      }).then(res => {
        this.dataList = res.data;
        this.count = res.count;
      });
    },
    // 填写快递单号
    comDialogClick(id){
      this.orderId = id;
      this.comDialog = true;
      this.form = {
        name:"",
        com:""
      }
    },
    // 确认发货
    comfirmOrderClick(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.api.setSellOrder({
            id:this.orderId,
            shippingCom:this.form.com,
            shippingExpress:this.form.name,
            shippingStatus:1,
            orderStatus:2,
          }).then(res=>{
            this.$message.success(res.msg);
            setTimeout(_=>{
              this.comDialog = false;
              this.getSellOrderFn();
              this.form = {
                name:"",
                com:""
              }
            },1500)
          })
        }
      });
    },
    // 确认收货
    takeClick(id){
      this.$confirm('确认收货, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.setSellOrder({
          id:id,
          orderStatus:3,
        }).then(res=>{
          this.$message.success(res.msg);
          setTimeout(_=>{
            this.getSellOrderFn();
          },1500)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 删除订单
    delNsOderById(id) {
      this.$confirm('此操作将删除该订单, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.delSellOrderById({ id }).then(res => {
          this.$message.success(res.msg);
          setTimeout(_=>{
            this.getSellOrderFn();
          },1500)
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
  }
};
</script>

<style scoped lang="less">
/deep/ .el-pagination.is-background .el-pager li:not(.disabled).active {
  background-color: var(--orange);
}

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
/deep/ .el-drawer__body {
  overflow-y: auto;
  overflow-x: hidden;
  padding: 20px 20px 10px;
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
/deep/.el-radio__input.is-checked+.el-radio__label{
  color: var(--orange);
}
/deep/.el-radio__input.is-checked .el-radio__inner{
  border-color: var(--orange);
  background:var(--orange);
}
</style>
