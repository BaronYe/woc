<template>
  <div class="container">
    <v-prompt :title="[
        '轮播管理展示所以轮播相关信息',
        '添加轮播时，可选择跳转公众号链接还是活动详情',
        '支持jpg、gif、png格式上传或从图片空间中选择，建议使用尺寸375x200像素以上、大小不超过1M的图片',
    ]">
    </v-prompt>
    <div class="d-flex ai-center">
      <el-button class="simon-button" @click="addEssay(0)">添加轮播图</el-button>
    </div>

    <template>
      <el-table
          :data="bannerList"
          style="width: 100%;margin-top: 20px;">
        <el-table-column
            prop="id"
            label="#"
            align="center">
        </el-table-column>
        <el-table-column
            label="图片"
            align="center">
          <template slot-scope="item">
            <div v-if="item.row.img != null">
              <el-image
                  style="width: 94px; height:50px;"
                  :src="item.row.img"
                  :preview-src-list="[item.row.img]">
                <div slot="error" class="image-slot">
                  <i class="el-icon-picture-outline"></i>
                </div>
              </el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column
            label="类型"
            align="center">
          <template slot-scope="item">
            <div>{{item.row.type === '0' ? '跳转公众号链接' : '跳转活动详情'}}</div>
          </template>
        </el-table-column>
        <el-table-column
            label="链接/活动ID"
            align="center">
          <template slot-scope="item">
            <el-link :href="item.row.link" target="_blank" v-if="item.row.type === '0'">{{item.row.link}}</el-link>
            <el-link v-if="item.row.type === '1'" target="_blank">{{item.row.activityId}}</el-link>
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
            <el-button type="text" size="small" @click="editBanner(item.row)">编辑</el-button>
            <el-button type="text" size="small" @click="delBannerById(item.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
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
          <el-form :model="form" :rules="rules" ref="registerRef" v-if="form != null">
            <el-form-item label="图片（375*200）" prop="img" :label-width="formLabelWidth">
              <el-col :span="12">
                <el-upload
                    class="avatar-uploader"
                    :action="`${this.siteinfo.siteroot}${this.siteinfo.project}/oss/upload`"
                    :show-file-list="false"
                    title="建议尺寸 375px*200px"
                    :on-success="handleAvatarSuccess">
                  <img v-if="form.img" :src="form.img" class="avatar" title="建议尺寸 375px*200px" alt="暂无">
                  <i v-else class="el-icon-plus avatar-uploader-icon" title="建议尺寸 375px*200px"></i>
                </el-upload>
              </el-col>
            </el-form-item>
            <el-form-item label="类型" :label-width="formLabelWidth">
              <el-select v-model="form.type" placeholder="请选择类型">
                <el-option label="跳转公众号链接" value="0"></el-option>
                <el-option label="跳转活动详情" value="1"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="链接" v-if="form.type === '0'" prop="link" :label-width="formLabelWidth">
              <el-input v-model="form.link" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="活动ID" v-if="form.type === '1'" prop="activityId" :label-width="formLabelWidth">
              <el-input v-model="form.activityId" autocomplete="off"></el-input>
            </el-form-item>
          </el-form>
          <div class="simon-drawer__footer">
            <el-button @click="dialogFormVisible=false">取 消</el-button>
            <el-button class="simon-button" @click="setBanner('registerRef')" >确 定</el-button>
          </div>
        </div>
      </el-drawer>
    </template>
  </div>
</template>

<script>
import prompt from '@components/dist/prompt';

export default {
  name: 'banner',
  components:{
    'v-prompt': prompt,
  },
  data() {
    return {
      bannerList: [],
      title: '添加轮播图',
      dialogFormVisible: false,
      form: null,
      formLabelWidth: '80px',
      rules: {
        img: [{ required: true, message: '请上传图片' }],
        link: [{ required: true, message: '请输入公众号链接' }],
        activityId: [{ required: true, message: '请输入活动ID' }],
      }
    };
  },
  mounted() {
    this.getBanner();
  },
  methods: {
    // 获取信息
    getBanner() {
      this.api.getBanner().then(res => {
        this.bannerList = res.data;
      });
    },
    // 添加
    addEssay() {
      this.title = '添加轮播图';
      this.form = {
        img: '',
        link: '',
        id: 0,
        type: '0',
        activityId: null
      };
      this.dialogFormVisible = true;
    },
    // 上传图片成功的回调
    handleAvatarSuccess(res) {
      this.form.img = res.data;
    },
    // 保存
    setBanner(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          if (this.form.type === '1') {
            this.form.activityId = parseInt(this.form.activityId);
          }
          this.api.setBanner(this.form).then(res => {
            this.$message({
              type: 'success',
              message: '操作成功'
            });
            setTimeout(()=>{
              this.dialogFormVisible = false;
              this.getBanner();
            },1500)
          });
        }
      });

    },
    // 编辑
    editBanner(item) {
      this.title = '修改轮播图';
      this.form = Object.assign({}, item);
      this.dialogFormVisible = true;
    },
    // 删除
    delBannerById(id) {
      this.$confirm('此操作将删除该信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.delBannerById({ id }).then(res => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
          setTimeout(()=>{
            this.getBanner();
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
  padding:  20px;
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
</style>
