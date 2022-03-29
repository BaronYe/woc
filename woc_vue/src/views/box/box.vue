<template>
  <div class="container">
    <v-prompt :title="[
        '盲盒管理展示所有盲盒相关信息',
        '添加盲盒时，请先保存盲盒再对盲盒下的商品的进行编辑添加',
        '支持jpg、gif、png格式上传或从图片空间中选择，建议使用尺寸375x200像素以上、大小不超过1M的图片',
    ]">
    </v-prompt>
    <div class="d-flex ai-center">
      <el-button class="simon-button" @click="openDModelFn(0)">添加盲盒</el-button>
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
              <el-form-item label="盲盒名称：">
                <el-input style="width: 200px;" v-model="name" placeholder="请填写盲盒名称"></el-input>
              </el-form-item>
              <el-form-item label="盲盒分类：">
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
      <button class="s-button" @click="batchBoxFn('0')">批量上架</button>
      <button class="s-button" @click="batchBoxFn('1')">批量下架</button>
      <button class="s-button" @click="batchBoxFn('2')">批量删除</button>
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
            align="center"
            width="120px"
            label="盲盒主图">
          <template slot-scope="scope">
            <el-image :src="scope.row.cover" :preview-src-list="[scope.row.cover]" style="width: 40px;height: 40px;"></el-image>
          </template>
        </el-table-column>
        <el-table-column
            prop="boxName"
            align="center"
            label="盲盒名称">
        </el-table-column>
        <el-table-column
            align="center"
            label="盲盒分类">
          <template slot-scope="scope">
            <div v-for="(item,index) of coassifyList" :key="index" v-if="scope.row.cid == item.id">{{ item.name }}</div>
          </template>
        </el-table-column>
        <el-table-column
            prop="label"
            align="center"
            label="标签">
        </el-table-column>
        <el-table-column
            align="center"
            label="盲盒价格/拼单价">
          <template slot-scope="scope">
            <div>{{scope.row.price}}元 / {{scope.row.spellPrice}}元</div>
          </template>
        </el-table-column>
        <el-table-column
            align="center"
            label="盲盒状态">
          <template slot-scope="scope">
            <div>{{ scope.row.isShelves == '0' ? '仓库中' : '销售中' }}</div>
          </template>
        </el-table-column>
        <el-table-column
            prop="counts"
            align="center"
            label="剩余数量">
        </el-table-column>
        <el-table-column
            prop="sales"
            align="center"
            label="销量">
        </el-table-column>
        <el-table-column
            label="操作"
            width="200"
            align="center">
          <template slot-scope="scope">
            <el-button
                @click="openGoodFn(scope.row.id,scope.row.boxName)"
                type="text"
                size="small">
              商品
            </el-button>
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
    <v-detail ref="boxDetail"></v-detail>
    <v-goods ref="goodsDetail"></v-goods>
  </div>
</template>

<script>
import prompt from '@components/dist/prompt';
import detail from '@views/box/detail';
import navMenu from '@components/dist/navMenu';
import goods from '@views/box/goods';

export default {
  name: 'box',
  components: {
    'v-prompt': prompt,
    'v-nav-menu': navMenu,
    'v-detail': detail,
    'v-goods':goods
  },
  data() {
    return {
      isOpen: true,
      name: '',
      cid: '',
      coassifyList: [],
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
      idx:[]
    };
  },
  mounted() {
    this.getClassifyList();
    this.getBoxFn();
  },
  methods: {
    // 获取分类
    getClassifyList() {
      this.api.getBoxClassify().then(res => {
        this.coassifyList = res.data;
      });
    },
    // 筛选
    screenClick() {
      this.page = 1;
      this.getBoxFn();
    },
    // 重置
    resetClick() {
      this.cid = '';
      this.name = '';
      this.page = 1;
      this.getBoxFn();
    },
    // 获取盲盒
    getBoxFn() {
      this.api.getBox({
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
    // 分页
    pagingChange(p) {
      this.page = p;
      this.getBoxFn();
    },
    // 每页条数
    handleSizeChange(val){
      this.page = 1;
      this.pagesize = val;
      this.getBoxFn();
    },
    // menu选择
    sendClick(e) {
      this.menuId = e.id;
      this.page = 1;
      this.getBoxFn();
    },
    // 批量选择
    handleSelectionChange(val) {
      let arr = [];
      val.forEach(item => {
        arr.push(item.id);
      });
      this.idx = arr;
    },
    // 批量上下架
    batchBoxFn(types) {
      if (this.idx.length === 0) {
        return this.$message.warning('请选择要操作的数据');
      }
      this.$confirm('是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.batchBox({
          types:types,
          idx:this.idx.join(',')
        }).then(res=>{
          this.$message.success("操作成功");
          setTimeout(()=>{
            this.getBoxFn();
          },1500)
        })
      })
    },
    // 打开编辑莫太筐
    openDModelFn(id) {
      this.$refs['boxDetail'].isShowDia(id, true);
    },
    // 删除
    delClick(id) {
      this.$confirm('此操作将删除该盲盒, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.delBoxById({ id }).then(res => {
          this.$message.success('删除成功');
          setTimeout(() => {
            this.getBoxFn();
          }, 1500);
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 盲个下的商品
    openGoodFn(id,boxName){
      this.$refs['goodsDetail'].isShowDia(id, true,boxName);
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
</style>
