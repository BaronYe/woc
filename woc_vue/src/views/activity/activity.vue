<template>
  <div class="container">
    <v-prompt :title="[
        '活动管理展示活动相关信息',
        '时间超过活动的结束时间时，活动自动结束']">
    </v-prompt>

    <div class="d-flex ai-center">
      <el-button class="simon-button" @click="openDModelFn(0)">添加</el-button>
      <div class="ml-auto">
        <el-input placeholder="请输入活动名称" v-model="name">
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
            label="标题"
            align="center"
            prop="activityName">
        </el-table-column>
        <el-table-column
            label="开始时间"
            align="center"
            prop="activityStartTime">
        </el-table-column>
        <el-table-column
            label="截止时间"
            align="center"
            prop="activityEndTime">
        </el-table-column>
        <el-table-column
            label="状态"
            align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.state == '2'">已结束</div>
            <div v-if="scope.row.state == '1'">进行中</div>
            <div v-if="scope.row.state == '0'">未开始</div>
          </template>
        </el-table-column>
        <el-table-column
            label="地址"
            align="center"
            prop="activityAddress">
        </el-table-column>
        <el-table-column
            label="联系方式"
            align="center"
            prop="activityPhone">
        </el-table-column>
        <el-table-column
            label="是否显示"
            align="center">
          <template slot-scope="scope">
            <div v-if="scope.row.activityShow == '0'">隐藏</div>
            <div v-if="scope.row.activityShow == '1'">显示</div>
          </template>
        </el-table-column>
        <el-table-column
            label="操作"
            width="200"
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
            layout="total, prev, pager, next"
            :page-size="pagesize"
            :total="count-0"
            :current-page.sync="page"
            @current-change="pagingChange"
        ></el-pagination>
      </div>
    </template>
    <v-activitySon ref="activitySon"></v-activitySon>
  </div>
</template>

<script>
import activitySon from '@views/activity/activitySon';
import prompt from '@components/dist/prompt';

export default {
  name: 'activity',
  components: {
    'v-activitySon': activitySon,
    'v-prompt': prompt
  },
  data() {
    return {
      page: 1,
      pagesize: 10,
      activityShow: '',
      name: '',
      dataList: [],
      count: 0
    };
  },
  mounted() {
    this.getActivityFn();
  },
  methods: {
    //添加
    openDModelFn(id) {
      this.$refs['activitySon'].isShowDia(id, true);
    },
    // 获取活动列表
    getActivityFn() {
      this.api.getActivity({
        page: this.page,
        pagesize: this.pagesize,
        activityShow: this.activityShow == '' ? '-1' : this.activityShow,
        name: this.name
      }).then(res => {
        this.dataList = res.data;
        this.count = res.count;
      });
    },
    // 分页
    pagingChange(v){
      this.page = v;
      this.getActivityFn();
    },
    // 搜索
    serachClick(){
      this.page = 1;
      this.getActivityFn();
    },
    // 删除
    delClick(id){
      this.$confirm('此操作将删除该活动, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.delActivityById({id}).then(res=>{
          this.$message.success("删除成功");
          setTimeout(()=>{
            this.getActivityFn();
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
/deep/ .el-pagination.is-background .el-pager li:not(.disabled).active{
  background-color:var(--orange);
}
</style>
