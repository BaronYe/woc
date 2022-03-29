<template>
  <div class="container">
    <v-prompt :title="[
        '动态管理是对用户发布的动态进行管理，如有违规信息，进行删除等操作',
     ]">
    </v-prompt>
    <div class="d-flex ai-center">
      <div class="ml-auto">
        <el-input placeholder="请输入搜索值" v-model="title">
          <el-button slot="append" icon="el-icon-search" @click="serachClick()"></el-button>
        </el-input>
      </div>
    </div>
    <!-- 列表 -->
    <template>
      <el-table
          :data="dataList"
          style="width: 100%;margin-top: 20px;">
        <el-table-column
            label="#"
            align="center"
            width="100"
            prop="id">
        </el-table-column>
        <el-table-column
            align="center"
            width="150px"
            label="发布用户">
          <template slot-scope="scope">
            <div style="display: flex;align-items: center;justify-content: center">
              <el-image :src="scope.row.users.avatar" :preview-src-list="[scope.row.users.avatar]"
                        style="width: 30px;height: 30px;border-radius: 100%;margin-right: 6px;"></el-image>
              <div>{{ scope.row.users.nickname }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column
            label="发布内容"
            align="center"
            prop="content">
        </el-table-column>
        <el-table-column
            label="发布地址"
            align="center"
            prop="address">
        </el-table-column>
        <el-table-column
            align="center"
            width="150"
            label="发布图片">
          <template slot-scope="scope">
            <div style="display: flex;align-items: center;justify-content: center">
              <el-image :src="scope.row.imgs[0]" :preview-src-list="scope.row.imgs"
                        style="width: 40px;height: 40px;margin-right: 6px;"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column
            label="创建时间"
            align="center"
            width="200"
            prop="createTime">
        </el-table-column>
        <el-table-column
            label="点赞数量"
            align="center"
            width="100"
            prop="zanCount">
        </el-table-column>
        <el-table-column
            label="收藏数量"
            width="100"
            align="center"
            prop="commentCount">
        </el-table-column>
        <el-table-column
            label="操作"
            width="150"
            align="center">
          <template slot-scope="scope">
            <el-button
                @click="doCommentFn(scope.row.id)"
                type="text"
                size="small">
              评论
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
    <!--评论列表-->
    <template>
      <el-drawer
          title="评论列表"
          :visible.sync="dialog"
          direction="rtl"
          size="50%">
        <div class="simon-drawer__content">
          <!-- 列表 -->
          <template>
            <el-table
                :data="commentList"
                style="width: 100%;margin-top: 20px;">
              <el-table-column
                  label="#"
                  align="center"
                  width="100"
                  prop="id">
              </el-table-column>
              <el-table-column
                  align="center"
                  width="150px"
                  label="评论用户">
                <template slot-scope="scope">
                  <div style="display: flex;align-items: center;justify-content: center">
                    <el-image :src="scope.row.users.avatar" :preview-src-list="[scope.row.users.avatar]"
                              style="width: 30px;height: 30px;border-radius: 100%;margin-right: 6px;"></el-image>
                    <div>{{ scope.row.users.nickname }}</div>
                  </div>
                </template>
              </el-table-column>
              <el-table-column
                  label="内容"
                  align="center"
                  prop="content">
              </el-table-column>
              <el-table-column
                  label="评论对象"
                  align="center">
                <template slot-scope="scope">
                  <div v-if="scope.row.rid == 0">评论动态</div>
                  <div v-else>回复: <span style="color: #20a0ff;">{{scope.row.beTitle}}</span> </div>
                </template>
              </el-table-column>
              <el-table-column
                  label="创建时间"
                  align="center"
                  width="200"
                  prop="createTime">
              </el-table-column>
              <el-table-column
                  label="操作"
                  width="150"
                  align="center">
                <template slot-scope="scope">
                  <el-button
                      @click.native.prevent="delCommentByIdFn(scope.row.id)"
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
                  :page-size="cPagesize"
                  :total="cCount"
                  :current-page.sync="cPage"
                  @current-change="cPagingChange"
              ></el-pagination>
            </div>
          </template>
        </div>
      </el-drawer>
    </template>
  </div>
</template>

<script>
import prompt from '@components/dist/prompt';
import Vue from 'vue';

export default {
  name: 'dynamic',
  data(){
    return{
      page:1,
      pagesize:10,
      title:"",
      userId:0,
      dataList:[],
      count:0,
      dialog:false,
      dynamicId:0,
      cPage:1,
      cPagesize:10,
      commentList:[],
      cCount:0
    }
  },
  components: {
    'v-prompt': prompt,
  },
  mounted() {
    var that = this
    that.getDynamicFn();
    console.log(this.dataList)
    console.log(this.count)
  },
  methods: {
    // 获取动态
    getDynamicFn(){
      var that = this;
      this.api.getDynamic({
        page:this.page,
        pagesize:this.pagesize,
        title:this.title,
        userId:this.userId,
      }).then(res=>{
        console.log(res)
        that.dataList = res.data;
        that.count = res.count;
      })
    },
    // 搜索
    serachClick(){
      this.page = 1;
      this.getDynamicFn();
    },
    // 分页
    pagingChange(val){
      this.page = val;
      this.getDynamicFn();
    },
    // 删除
    delClick(id){
      this.$confirm('此操作将删除该动态, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.delDynamicById({ id:id }).then(res => {
          this.$message.success(res.msg);
          setTimeout(_=>{
            this.getDynamicFn();
          },1500)
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 评论
    doCommentFn(id){
      this.dynamicId = id;
      this.dialog = true;
      this.getCommentAndAdminFn();
    },
    // 获取评论
    getCommentAndAdminFn(){
      this.api.getCommentAndAdmin({
        dynamicId:this.dynamicId,
        page:this.cPage,
        pagesize:this.cPagesize
      }).then(res=>{
        this.commentList = res.data;
        this.cCount = res.count;
      })
    },
    // 评论分页
    cPagingChange(val){
      this.cPage = val;
      this.getCommentAndAdminFn();
    },
    // 删除评论
    delCommentByIdFn(id){
      this.$confirm('此操作将删除该评论, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.delCommentById({ id:id }).then(res => {
          this.$message.success(res.msg);
          setTimeout(_=>{
            this.getCommentAndAdminFn();
          },1500)
        });
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
/deep/ .el-pagination.is-background .el-pager li:not(.disabled).active{
  background-color:var(--orange);
}
/deep/ .el-image-viewer__close {
  color: #fff;
  top: 100px;
  right: 100px;
}
</style>
