<template>
  <div class="container">
    <v-prompt :title="[
        '点击添加优惠券按钮可以添加优惠券，点击详情按钮可以查看优惠券详情',
        '进行中的优惠券需先关闭才可进行删除操作',
        '点击删除按钮会删除会员领取的优惠券，请谨慎操作',
        '时间超过优惠券设置的结束时间或有效期限时，优惠券自动关闭',
        '手动关闭优惠券后，用户将不能领取该优惠券，但是已经领取的优惠券（未到期）仍然可以使用']">
    </v-prompt>
    <div class="d-flex ai-center">
      <el-button class="simon-button" @click="openDModelFn(0)">添加优惠券</el-button>
    </div>
    <div class="serach-box mt-20">
      <div @click.stop="isOpen = !isOpen" class="cp simon-colla-title">
        <span class="put-open">{{isOpen?'收起':'展开'}}</span>
        <i :class="[isOpen?'el-icon-arrow-down':'el-icon-arrow-up']"></i>
      </div>
      <div class="ns-screen">
        <div class="simon-colla-content" :class="[isOpen ? 'simon_show' : '']">
          <el-form label-position="right"  label-width="140px">
            <div class="d-flex ai-center">
              <el-form-item label="优惠券名称：" >
                <el-input style="width: 200px;" v-model="name" placeholder="请输入优惠券名称"></el-input>
              </el-form-item>
              <el-form-item label="优惠券类型：" >
                <el-select v-model="cTypes" placeholder="全部">
                  <el-option
                      v-for="item in coassifyList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </div>
            <div class="d-flex ai-center">
              <el-form-item label="优惠券形式：" >
                <el-select v-model="shopStatus" placeholder="全部">
                  <el-option
                      v-for="item in shopList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="是否开启限时抢："  v-if="shopStatus == '1'">
                <el-select v-model="isRob" placeholder="全部">
                  <el-option
                      v-for="item in robList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </div>
            <el-form-item label="优惠券时间段：" >
              <el-date-picker
                  v-model="timeList"
                  type="datetimerange"
                  format="yyyy 年 MM 月 dd 日 HH:mm"
                  value-format="yyyy-MM-dd HH:mm"
                  range-separator="至"
                  style="width: 520px;"
                  start-placeholder="优惠券发放日期"
                  end-placeholder="优惠券截止日期">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button style="background-color: var(--orange);color: var(--white);border:none" @click="screenClick">筛选</el-button>
              <el-button @click="resetClick()">重置</el-button>
            </el-form-item>
          </el-form>

        </div>
      </div>
    </div>
    <v-nav-menu :menu="menu" :menuId="menuId" @send="sendClick"/>
    <!-- 列表 -->
    <template>
      <el-table
          :data="dataList"
          style="width: 100%;margin-top: 20px;">
        <el-table-column
            label="优惠券名称"
            align="center"
            prop="title">
        </el-table-column>
        <el-table-column
            label="优惠券类型"
            align="center">
          <template slot-scope="scope">
            <div>{{scope.row.types == '0' ?'满减':'折扣'}}</div>
          </template>
        </el-table-column>
        <el-table-column
            label="优惠券金额/折扣"
            align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.types == '0' ">¥{{scope.row.couponPrice}}</div>
            <div v-else>{{scope.row.discount}}折</div>
          </template>
        </el-table-column>
        <el-table-column
            label="优惠券数量/已领取"
            align="center">
          <template slot-scope="scope">
            <div>{{scope.row.totalCount}} / {{scope.row.toCount}}</div>
          </template>
        </el-table-column>
        <el-table-column
            label="发放时间"
            align="center"
            prop="startTime">
        </el-table-column>
        <el-table-column
            label="截止时间"
            align="center"
            prop="endTime">
        </el-table-column>
        <el-table-column
            label="状态"
            align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.couponStatus == '0'">已失效</div>
            <div v-else-if="scope.row.couponStatus == '2'">未开始</div>
            <div v-else>进行中</div>
          </template>
        </el-table-column>
        <el-table-column
            label="有效期限"
            width="200"
            align="left">
          <template slot-scope="scope">
            <div v-if="scope.row.timeStatus == '0' ">失效期：{{scope.row.couponTime}}</div>
            <div v-else>领取后，{{scope.row.couponDays}}天有效</div>
          </template>
        </el-table-column>
        <el-table-column
            label="操作"
            width="200"
            align="left">
          <template slot-scope="scope">
            <el-button
                @click.native.prevent="getUserCouponToCouponIdFn(scope.row.id)"
                type="text"
                size="small">
              领取记录
            </el-button>
            <el-button
                @click.native.prevent="editClick(scope.row.id)"
                type="text"
                size="small">
              编辑
            </el-button>
            <el-button
                @click.native.prevent="offClick(scope.row.id)"
                type="text"
                v-if="scope.row.couponStatus != '0'"
                size="small">
              关闭
            </el-button>
            <el-button
                @click.native.prevent="delClick(scope.row.id)"
                type="text"
                v-if="scope.row.couponStatus == '0'"
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
            :total="count-0"
            :current-page.sync="page"
            :page-sizes="[10, 20, 30, 40,50]"
            @current-change="pagingChange"
            @size-change="handleSizeChange"
        ></el-pagination>
      </div>
    </template>
    <v-model ref="vmodel"></v-model>
    <el-drawer
        title="优惠券领取记录"
        :visible.sync="dialogFormVisible"
        size="50%"
        :wrapperClosable="false"
        direction="rtl">
      <template>
        <el-table
            :data="couponList"
            style="width: 100%">
          <el-table-column
              label="头像"
              align="center"
              width="100">
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
              label="用户名称"
              align="center">
          </el-table-column>
          <el-table-column
              prop="couponCode"
              label="券码"
              align="center">
          </el-table-column>
          <el-table-column
              prop="startTime"
              label="领取时间"
              align="center">
          </el-table-column>
          <el-table-column
              prop="endTime"
              label="截止时间"
              align="center">
          </el-table-column>
          <el-table-column
              label="状态"
              align="center">
            <template slot-scope="scope">
              <div v-if="scope.row.state == '0'">未使用</div>
              <div v-else-if="scope.row.state == '1'">已使用</div>
              <div v-else-if="scope.row.state == '2'">已过期</div>
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
              :page-size="cPagesize"
              :total="cCount-0"
              :current-page.sync="cPage"
              :page-sizes="[10, 20, 30, 40,50]"
              @current-change="cPagingChange"
              @size-change="cHandleSizeChange"
          ></el-pagination>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script>
import prompt from '@components/dist/prompt';
import model from '@views/coupon/model';
import navMenu from '@components/dist/navMenu';

export default {
  name: 'coupon',
  components: {
    'v-prompt': prompt,
    'v-model':model,
    'v-nav-menu': navMenu
  },
  data(){
    return{
      isOpen:true,
      name:"",
      coassifyList:[
        {
          id: '',
          name: '全部'
        },
        {
          id: '0',
          name: '满减'
        }, {
          id: '1',
          name: '折扣'
        }
      ],
      cTypes:"",
      page:1,
      pagesize:10,
      dataList:[],
      count:0,
      timeList:[],
      menu: [
        {
          id: '',
          name: '全部'
        }, {
          id: '2',
          name: '未开始'
        }, {
          id: '1',
          name: '进行中'
        }, {
          id: '0',
          name: '已失效'
        }
      ],
      menuId: '',
      shopList:[
        {
          id: '',
          name: '全部'
        },
        {
          id: '0',
          name: '通用券'
        }, {
          id: '1',
          name: '专用券'
        }
      ],
      shopStatus:"",
      robList:[
        {
          id: '',
          name: '全部'
        },
        {
          id: '0',
          name: '关闭'
        }, {
          id: '1',
          name: '开启'
        }
      ],
      isRob:"",
      dialogFormVisible: false,
      cPage:1,
      cPagesize:10,
      couponList:[],
      cCount:0,
    }
  },
  mounted() {
    this.getCouponFn();
  },
  methods:{
    // 筛选
    screenClick(){
      this.page = 1;
      this.getCouponFn();
    },
    // 重置
    resetClick(){
      this.name = "";
      this.shopStatus = "";
      this.isRob = "";
      this.timeList = [];
      this.cTypes = "";
    },
    // 打开编辑莫太筐
    openDModelFn(id) {
      this.$refs['vmodel'].isShowDia(id, true);
    },
    // 编辑
    editClick(id){
      this.$refs['vmodel'].isShowDia(id, true);
    },
    // 分页
    pagingChange(val){
      this.page = val;
      this.getCouponFn();
    },
    // 每页个数
    handleSizeChange(val){
      this.pagesize = val;
      this.page = 1;
      this.getCouponFn();
    },
    // menu选择
    sendClick(e) {
      this.menuId = e.id;
      this.page = 1;
      this.getCouponFn();
    },
    // 获取优惠券列表
    getCouponFn(){
      this.api.getCoupon({
        page:this.page,
        pagesize:this.pagesize,
        name:this.name,
        types:this.cTypes,
        state:this.menuId,
        shopStatus:this.shopStatus,
        isRob:this.isRob,
        startTime:this.timeList[0] == undefined ? '' :this.timeList[0],
        endTime:this.timeList[1] == undefined ? '' :this.timeList[1],
      }).then(res=>{
        this.dataList = res.data;
        this.count = res.count;
      })
    },
    // 删除
    delClick(id) {
      this.$confirm('此操作将删除该优惠券, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.delCouponById({ id }).then(res => {
          this.$message.success('删除成功');
          setTimeout(() => {
            this.getCouponFn();
          }, 1500);
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 关闭优惠券
    offClick(id){
      this.$confirm('此操作将关闭该优惠券, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.offCouponById({ id }).then(res => {
          this.$message.success('关闭成功');
          setTimeout(() => {
            this.getCouponFn();
          }, 1500);
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 打开莫太筐
    getUserCouponToCouponIdFn(id){
      this.couponId = id;
      this.getUserCouponToCouponId();
      this.dialogFormVisible = !this.dialogFormVisible;
    },
    //领取记录
    getUserCouponToCouponId(){
      this.api.getUserCouponToCouponId({
        couponId:this.couponId,
        page:this.cPage,
        pagesize:this.cPagesize,
      }).then(res=>{
        this.couponList = res.data;
        this.cCount = res.count;
      })
    },
    // 分页
    cPagingChange(val){
      this.cPage = val;
      this.getUserCouponToCouponId();
    },
    // 每页个数
    cHandleSizeChange(val){
      this.cPagesize = val;
      this.cPage = 1;
      this.getUserCouponToCouponId();
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
.ns-screen{
  border: 0;
  background-color: #fff;
  border-radius: 5px;
  min-height: 45px;
}
.serach-box{
  position: relative;
  .simon-colla-content{
    padding: 20px 0 10px;
    border: 1px solid #f1f1f1;
    display: none;
  }
}
.simon_show{
  display: block !important;
}
.simon-colla-title{
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
  i{
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
/deep/ .el-pagination.is-background .el-pager li:not(.disabled).active{
  background-color:var(--orange);
}
</style>
