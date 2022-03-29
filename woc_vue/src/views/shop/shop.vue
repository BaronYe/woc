<template>
  <div class="container">
    <v-prompt :title="[
        '当前页面对门店的信息进行管理，可以添加门店，管理门店等。',
        '第一张图片将作为商品主图,支持同时上传多张图片',
        '支持jpg、gif、png格式上传或从图片空间中选择，建议使用尺寸375x200像素以上、大小不超过1M的正方形图片'
    ]">
    </v-prompt>
    <div class="d-flex ai-center">
      <el-button class="simon-button" @click="openDModelFn(0)">添加</el-button>
    </div>
    <div class="serach-box mt-20">
      <div @click.stop="isOpen = !isOpen" class="cp simon-colla-title">
        <span class="put-open">{{ isOpen ? '收起' : '展开' }}</span>
        <i :class="[isOpen?'el-icon-arrow-down':'el-icon-arrow-up']"></i>
      </div>
      <div class="ns-screen">
        <div class="simon-colla-content" :class="[isOpen ? 'simon_show' : '']">
          <el-form label-position="right" label-width="120px">
            <div class="d-flex ai-center">
              <el-form-item label="店铺名称：">
                <el-input style="width: 200px;" v-model="name" placeholder="请填写店铺名称"></el-input>
              </el-form-item>
              <el-form-item label="所属商圈：">
                <el-cascader
                    placeholder="全部"
                    :options="regionList"
                    v-model="ridx"
                    :props="{ value: 'id',label:'name',children:'items',checkStrictly: true }"
                    @change="handleChange"
                >
                </el-cascader>
              </el-form-item>
              <el-form-item label="营业状态：" prop="types">
                <el-select v-model="isClosed" placeholder="全部">
                  <el-option key="0" label="停业" value="0"></el-option>
                  <el-option key="1" label="营业中" value="1"></el-option>
                </el-select>
              </el-form-item>
            </div>
            <el-form-item>
              <el-button style="background-color: var(--orange);color: var(--white);border:none" @click="screenClick">筛选
              </el-button>
              <el-button @click="resetClick">重置</el-button>
            </el-form-item>
          </el-form>

        </div>
      </div>
    </div>
    <v-nav-menu :menu="menu" :menuId="menuId" @send="sendClick" />
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
            label="是否绑定商户">
          <template slot-scope="scope">
            <div v-if="scope.row.merchantState == '0'">没有绑定</div>
            <div v-else style="cursor: pointer;">
              <el-popover trigger="hover" placement="top">
                <p><span style="color: #999999;">商户名称: </span>{{ scope.row.merchant.name }}</p>
                <p><span style="color: #999999;">商户账号: </span>{{ scope.row.merchant.username }}</p>
                <div slot="reference" class="name-wrapper">
                  <div>商户“<span style="color: #409EFF;">{{scope.row.merchant.name}}</span>”绑定</div>
                </div>
              </el-popover>
            </div>
          </template>
        </el-table-column>
        <el-table-column
            prop="name"
            align="center"
            label="店铺名称">
        </el-table-column>
        <el-table-column
            align="center"
            label="所属商圈">
          <template slot-scope="scope">
            <div>{{ scope.row.region }}/{{ scope.row.circle }}</div>
          </template>
        </el-table-column>
        <el-table-column
            align="center"
            label="所属类型">
          <template slot-scope="scope">
            <div v-if="scope.row.types == '0'">吃喝类</div>
            <div v-else>游玩类</div>
          </template>
        </el-table-column>
        <el-table-column
            align="center"
            label="是否营业">
          <template slot-scope="scope">
            <div v-if="scope.row.isClosed == '1'">营业中</div>
            <div v-else>停业</div>
          </template>
        </el-table-column>

        <el-table-column
            prop="phone"
            align="center"
            label="店铺电话">
        </el-table-column>
        <el-table-column
            align="center"
            label="营业时间">
          <template slot-scope="scope">
            <div>{{ scope.row.startTime}}至{{ scope.row.endTime}}</div>
          </template>
        </el-table-column>
        <el-table-column
            align="center"
            label="店铺地址">
          <template slot-scope="scope">
            <el-popover trigger="hover" placement="top" style="cursor: pointer;">
              <p>{{ scope.row.address}}</p>
              <div slot="reference" class="name-wrapper">
                <el-button
                    type="text"
                    size="small">
                  查看
                </el-button>
              </div>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column
            prop="moods"
            align="center"
            label="人气">
        </el-table-column>
        <el-table-column
            align="center"
            width="120px"
            label="发现页展示">
          <template slot-scope="scope">
            <div v-if="scope.row.isFind == '1'">展示</div>
            <div v-else>隐藏</div>
          </template>
        </el-table-column>
        <el-table-column
            label="操作"
            align="center">
          <template slot-scope="scope">
            <el-button
                @click="openDModelFn(scope.row.id)"
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
    <v-shopSon ref="shopSon"></v-shopSon>
  </div>
</template>

<script>
import prompt from '@components/dist/prompt';
import shopSon from '@views/shop/shopSon';
import navMenu from '@components/dist/navMenu';

export default {
  name: 'shop',
  components: {
    'v-prompt': prompt,
    'v-nav-menu': navMenu,
    'v-shopSon': shopSon
  },
  data() {
    return {
      isOpen: true,
      name: '',
      page: 1,
      pagesize: 10,
      ridx: [],
      regionList: [],
      count: 0,
      dataList:[],
      menu:[
        {
          id:"-1",
          name:"全部"
        },{
          id:"0",
          name:"吃喝类"
        },{
          id:"1",
          name:"游玩类"
        },
      ],
      menuId:"-1",
      isClosed:""
    };
  },
  mounted() {
    this.getShopFn();
    this.getRegionAndCircleFn();
  },
  methods: {
    // 获取地区商圈
    getRegionAndCircleFn() {
      this.api.getRegionAndCircle().then(res => {
        this.regionList = res.data;
      });
    },
    // 地区管理分类选择
    handleChange(v) {
      this.ridx = v;
    },
    // 打开编辑莫太筐
    openDModelFn(id) {
      this.$refs['shopSon'].isShowDia(id, true);
    },
    // 分页
    pagingChange(p) {
      this.page = p;
      this.getShopFn();
    },
    // 每页条数
    handleSizeChange(val){
      this.page = 1;
      this.pagesize = val;
      this.getShopFn();
    },
    //多选
    handleSelectionChange(){

    },
    // 筛选
    screenClick(){
      this.page = 1;
      this.getShopFn();
    },
    // 重置
    resetClick(){
      this.ridx = [];
      this.name = "";
      this.isClosed = "";
      this.page = 1;
      this.getShopFn();
    },
    // menu的选择事件
    sendClick(e){
      this.menuId = e.id;
      this.page = 1;
      this.getShopFn();
    },
    // 获取店铺列表
    getShopFn() {
      let regionId = this.ridx[0] == undefined ? -1 : this.ridx[0];
      let circleId = this.ridx[1] == undefined ? -1 : this.ridx[1];

      this.api.getShop({
        page: this.page,
        pagesize: this.pagesize,
        name: this.name,
        regionId: regionId,
        circleId: circleId,
        types:this.menuId,
        isClosed:this.isClosed,
      }).then(res => {
        this.dataList = res.data;
        this.count = res.count;
      });
    },
    // 删除
    delClick(id){
      this.$confirm('此操作将删除该店铺, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.delShopById({id}).then(res=>{
          this.$message.success("删除成功");
          setTimeout(()=>{
            this.getShopFn();
          },1500)
        })
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
</style>
