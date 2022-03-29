<template>
  <div class="container">
    <v-prompt :title="[
        '当前页面对用户的信息进行管理，可以查看用户的地址，个人简介等。',
    ]">
    </v-prompt>
    <template>
      <el-table
          :data="userList"
          style="width: 100%">
        <el-table-column
            label="头像"
            align="center"
            width="100">
          <template slot-scope="item">
            <div v-if="item.row.avatar != null">
              <el-image
                  style="width: 33px; height:33px;"
                  :src="item.row.avatar"
                  :preview-src-list="[item.row.avatar]">
                <div slot="error" class="image-slot">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column
            prop="id"
            align="center"
            width="100"
            label="#">
        </el-table-column>
        <el-table-column
            prop="nickname"
            label="昵称"
            align="center">
        </el-table-column>
        <el-table-column
            label="性别"
            align="center">
          <template slot-scope="item">
            <div v-if="item.row.gender === '1'">男</div>
            <div v-else-if="item.row.gender === '0'">女</div>
            <div v-else>未知</div>
          </template>
        </el-table-column>
        <el-table-column
            prop="birthday"
            label="生日"
            align="center">
        </el-table-column>
        <el-table-column
            label="余额"
            align="center">
          <template slot-scope="item">
            <div style="color: #f02d2d;">¥{{item.row.balance}}</div>
          </template>
        </el-table-column>
        <el-table-column
            prop="point"
            label="积分"
            align="center">
        </el-table-column>
        <el-table-column
            label="地址"
            align="center">
          <template slot-scope="scope">
            <div style="cursor: pointer;">
              <el-popover trigger="hover" placement="top">
                <p>{{ scope.row.localAddress }}</p>
                <div slot="reference" class="name-wrapper">
                  <div><span style="color: #409EFF;">查看</span></div>
                </div>
              </el-popover>
            </div>
          </template>
        </el-table-column>
        <el-table-column
            label="简介"
            align="center">
          <template slot-scope="scope">
            <div style="cursor: pointer;">
              <el-popover trigger="hover" placement="top">
                <p>{{ scope.row.userDesc }}</p>
                <div slot="reference" class="name-wrapper">
                  <div><span style="color: #409EFF;">查看</span></div>
                </div>
              </el-popover>
            </div>
          </template>
        </el-table-column>
        <el-table-column
            prop="createTime"
            label="创建时间"
            align="center">
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
  </div>
</template>

<script>
import prompt from '@components/dist/prompt';

export default {
  name: 'user',
  components: {
    'v-prompt': prompt,
  },
  data() {
    return {
      page: 1,
      pagesize:10,
      userList: [],
      count: 0,
      form: {
        integral: ''
      },
      user: null,
      formLabelWidth: '120px',
      dialogFormVisible: false
    };
  },
  mounted() {
    this.getUserList();
  },
  methods: {
    // 获取用户列表
    getUserList() {
      this.api.getUserList({
        page: this.page,
        pagesize: this.pagesize,
      }).then(res => {
        this.userList = res.data;
        this.count = res.count;
      });
    },
    // 分页
    pagingChange(page) {
      this.page = page;
      this.getUserList();
    },
    // 个数
    handleSizeChange(val){
      this.pagesize = val;
      this.page = 1;
      this.getUserList();
    },

  }
};
</script>

<style scoped>
/deep/ .el-image-viewer__close {
  color: #fff;
  top: 100px;
  right: 100px;
}
/deep/ .el-pagination.is-background .el-pager li:not(.disabled).active {
  background-color: #FF6200 !important;
}
</style>
