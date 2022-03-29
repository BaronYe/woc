<template>
  <div class="container">
    <v-prompt :title="[
        '系统通知页是针对管理员给用户发送相关信息通知'
     ]">
    </v-prompt>
    <div class="d-flex ai-center">
      <el-button class="simon-button" @click="addEssay(0)">添加通知</el-button>
    </div>
    <template>
      <el-table
          :data="dataList"
          style="width: 100%;margin-top: 20px;">
        <el-table-column
            prop="id"
            label="#"
            align="center">
        </el-table-column>
        <el-table-column
            label="发布者头像"
            align="center">
          <template slot-scope="item">
            <div v-if="item.row.cover != null">
              <el-image
                  style="width: 40px; height:40px;"
                  :src="item.row.cover"
                  :preview-src-list="[item.row.cover]">
              </el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column
            label="发布者名称"
            prop="name"
            align="center">
        </el-table-column>
        <el-table-column
            label="标题"
            align="center">
          <template slot-scope="item">
            <div>{{item.row.title}}</div>
          </template>
        </el-table-column>
        <el-table-column
            label="内容"
            align="center">
          <template slot-scope="item">
            <div style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 2;overflow: hidden;">{{item.row.content}}</div>
          </template>
        </el-table-column>
        <el-table-column
            prop="createTime"
            align="center"
            label="创建时间">
        </el-table-column>
        <el-table-column
            label="操作"
            align="center">
          <template slot-scope="item">
            <el-button type="text" size="small" @click="editNotices(item.row)">编辑</el-button>
            <el-button type="text" size="small" @click="delNoticesById(item.row.id)">删除</el-button>
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
    <!-- 详情 -->
    <template>
      <el-drawer
          :title="title"
          :visible.sync="dialogFormVisible"
          size="30%"
          :wrapperClosable="false"
          direction="rtl">
        <div class="simon-drawer__content">
          <el-form :model="form" label-position="right" :label-width="formLabelWidth" :rules="rules" ref="formRef"
                   v-if="form != null">
            <el-form-item label="标题" prop="title">
              <el-input v-model="form.title" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="内容" prop="content">
              <el-input type="textarea" resize="none" :rows="5" v-model="form.content" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="发布者头像" prop="cover">
              <el-col :span="12">
                <el-upload
                    class="avatar-uploader"
                    :action="`${this.siteinfo.siteroot}${this.siteinfo.project}/oss/upload`"
                    :show-file-list="false"
                    title="建议尺寸 100*100"
                    :on-success="imgSuccess">
                  <img v-if="form.cover" :src="form.cover" class="avatar" alt="暂无">
                  <i v-else class="el-icon-upload2 avatar-uploader-icon" style="font-size: 20px;line-height: 82px;"></i>
                </el-upload>
              </el-col>
            </el-form-item>
            <el-form-item label="发布者名称" prop="name">
              <el-input v-model="form.name" autocomplete="off"></el-input>
            </el-form-item>
          </el-form>
          <div class="simon-drawer__footer">
            <el-button @click="dialogFormVisible=false">取 消</el-button>
            <el-button class="simon-button" @click="setNotices('formRef')">确 定</el-button>
          </div>
        </div>
      </el-drawer>
    </template>
  </div>
</template>

<script>
import prompt from '@components/dist/prompt';

export default {
  name: 'notices',
  components: {
    'v-prompt': prompt
  },
  data() {
    return {
      title: '添加',
      dialogFormVisible: false,
      form: null,
      formLabelWidth: '120px',
      rules: {
        title: [{ required: true, message: '请输入标题' }],
        content: [{ required: true, message: '请输入内容' }],
        cover: [{ required: true, message: '请上传发布者头像' }],
        name: [{ required: true, message: '请输入发布者名称' }]
      },
      page:1,
      pagesize:10,
      count:0,
      dataList:[]
    };
  },
  mounted() {
    this.getNotices();
  },
  methods: {
    // 添加
    addEssay() {
      this.title = '添加';
      this.form = {
        title: '',
        content: '',
        id: 0,
        cover: '',
        name: ''
      };
      this.dialogFormVisible = true;
    },
    // 上传头像
    imgSuccess(res) {
      this.form.cover = res.data;
    },
    // 保存
    setNotices(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.api.setNotices(this.form).then(res => {
            this.$message({
              type: 'success',
              message: '操作成功'
            });
            setTimeout(()=>{
              this.dialogFormVisible = false;
              this.getNotices();
            },1500)
          });
        }
      });
    },
    // 获取通知
    getNotices(){
      this.api.getNotices({
        page:this.page,
        pagesize:this.pagesize
      }).then(res=>{
        this.dataList = res.data;
        this.count = res.count;
      })
    },
    // 分页
    pagingChange(p) {
      this.page = p;
      this.getNotices();
    },
    // 每页条数
    handleSizeChange(val){
      this.page = 1;
      this.pagesize = val;
      this.getNotices();
    },
    // 编辑
    editNotices(item){
      this.title = '修改';
      this.form = Object.assign({}, item);
      this.dialogFormVisible = true;
    },
    // 删除
    delNoticesById(id) {
      this.$confirm('此操作将删除该信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.delNoticesById({ id }).then(res => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
          setTimeout(()=>{
            this.getNotices();
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
/deep/ .el-image-viewer__close {
  color: #fff;
  top: 100px;
  right: 100px;
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
  border: 1px #eee dashed;
  margin-left: 5px;
}

.avatar {
  width: 188px;
  height: 100px;
  display: block;
}

.simon-drawer__content {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 20px;
}

.simon-drawer__content form {
  flex: 1;
}

.simon-drawer__footer {
  display: flex;
}

.simon-drawer__footer button {
  flex: 1;
}

.avatar-uploader {
  width: 82px;
  height: 82px;
  line-height: 82px;
  border: 1px #ddd dashed;
  margin-left: 5px;
}

.avatar {
  width: 82px;
  height: 82px;
  display: block;
}
/deep/.el-radio__input.is-checked+.el-radio__label{
  color: var(--orange);
}
/deep/.el-radio__input.is-checked .el-radio__inner{
  border-color: var(--orange);
  background:var(--orange);
}
/deep/ .el-pagination.is-background .el-pager li:not(.disabled).active {
  background-color: var(--orange);
}
</style>
