<template>
  <div>
    <el-drawer
        :title="title"
        :visible.sync="dialogFormVisible"
        size="40%"
        :wrapperClosable="false"
        direction="rtl">
      <div class="simon-drawer__content">
        <el-form label-position="left" :model="form" :rules="rules" ref="activityRef" v-if="form != null"
                 :label-width="formLabelWidth">
          <el-form-item label="图片（375*200）" prop="activityImages">
            <el-col :span="12">
              <el-upload
                  class="avatar-uploader"
                  :action="`${this.siteinfo.siteroot}${this.siteinfo.project}/oss/upload`"
                  :show-file-list="false"
                  title="建议尺寸 375px*200px"
                  :on-success="imgsSuccess">
                <img v-if="form.activityImages" :src="form.activityImages" class="avatar" title="建议尺寸 375px*200px"
                     alt="暂无">
                <i v-else class="el-icon-upload2 avatar-uploader-icon" style="font-size: 24px;line-height: 100px;"
                   title="建议尺寸 375px*200px"></i>
              </el-upload>
            </el-col>
          </el-form-item>
          <el-form-item label="标题" prop="activityName">
            <el-input v-model="form.activityName" placeholder="请填写活动的标题"></el-input>
          </el-form-item>
          <div class="d-flex">
            <el-form-item label="标签" prop="label" style="width: 45%;">
              <el-input v-model="form.label" placeholder="请填写活动的标签"></el-input>
            </el-form-item>
            <el-form-item label="人均" prop="perCapita" style="width: 45%;margin-left: auto">
              <el-input v-model="form.perCapita" placeholder="请填写活动的人均"></el-input>
            </el-form-item>
          </div>
          <el-form-item label="地址" prop="activityAddress">
            <el-input v-model="form.activityAddress" placeholder="请填写活动的地址"></el-input>
          </el-form-item>
          <div class="d-flex">
            <el-form-item label="电话" prop="activityPhone" style="width: 45%;">
              <el-input v-model="form.activityPhone" placeholder="请填写联系电话"></el-input>
            </el-form-item>
            <el-form-item label="是否显示" style="width: 45%;margin-left: auto;">
              <el-switch
                  v-model="form.activityShow"
                  active-color="#FF6200"
                  inactive-color="#CCCCCC"
                  active-value="1"
                  inactive-value="0">
              </el-switch>
            </el-form-item>
          </div>
          <div style="display: flex;">
            <el-form-item label="开始日期" prop="activityStartTime" style="width: 40%;">
              <el-date-picker
                  v-model="form.activityStartTime"
                  type="date"
                  placeholder="选择日期"
                  format="yyyy 年 MM 月 dd 日"
                  value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
            <el-form-item label="截止日期" prop="activityEndTime" style="width: 45%;margin-left: auto;">
              <el-date-picker
                  v-model="form.activityEndTime"
                  type="date"
                  placeholder="选择结束日期"
                  format="yyyy 年 MM 月 dd 日"
                  value-format="yyyy-MM-dd">
              </el-date-picker>
            </el-form-item>
          </div>
          <el-form-item label="活动描述" prop="activityDesc">
            <quill-editor style="margin-top: 10px;" ref="myTextEditor" v-model="form.activityDesc"
                          :options="editorOption"
                          @change="onEditorChange($event)"></quill-editor>
          </el-form-item>

        </el-form>
        <div class="simon-drawer__footer">
          <el-button @click="dialogFormVisible=false">取 消</el-button>
          <el-button class="simon-button" @click="setClick('activityRef')">确 定</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import 'quill/dist/quill.core.css';
import 'quill/dist/quill.snow.css';
import 'quill/dist/quill.bubble.css';
import { quillEditor } from 'vue-quill-editor';

export default {
  name: 'activitySon',
  components: {
    quillEditor
  },
  data() {
    return {
      title: '活动编辑',
      dialogFormVisible: false,
      id: 0,
      form: null,
      formLabelWidth: '100px',
      rules: {
        activityName: [{ required: true, message: '请填写活动标题' }],
        activityImages: [{ required: true, message: '请上传活动图片' }],
        activityAddress: [{ required: true, message: '请填写活动地址' }],
        activityPhone: [{ required: true, message: '请填写活动电话' }],
        activityEndTime: [{ required: true, message: '请填写活动截止时间' }],
        activityStartTime: [{ required: true, message: '请填写活动开始时间' }]
      },
      editorOption: {
        placeholder: '请在这里添加活动描述',
        theme: 'snow'
      }
    };
  },
  mounted() {
  },
  methods: {
    // 显示
    isShowDia(id, isVisible) {
      this.id = id;
      this.dialogFormVisible = isVisible;
      if (id == 0) {
        this.form = {
          activityName: '',
          id: 0,
          activityImages: '',
          activityDesc: '',
          activityAddress: '',
          activityPhone: '',
          activityEndTime: '',
          activityStartTime: '',
          activityShow: '0',
          perCapita: '',
          label: ''
        };
      }else{
        this.toDetail();
      }
    },
    // 图片上传
    imgsSuccess(res) {
      this.form.activityImages = res.data;
    },
    // 活动内容
    onEditorChange(editor) {
      this.form.activityDesc = editor.html;
    },
    // 保存
    setClick(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.api.setActivity(this.form).then(res => {
            this.$message.success('保存成功');
            setTimeout(() => {
              this.dialogFormVisible = !this.dialogFormVisible;
              this.$parent.getActivityFn();
            }, 1500);
          });
        }
      });
    },
    //获取活动详情
    toDetail(){
      this.api.getActivityById({
        id:this.id
      }).then(res=>{
        this.form = res.data;
      })
    }
  }
};
</script>

<style scoped lang="less">
/deep/ .el-image-viewer__close {
  color: #fff;
  top: 100px;
  right: 100px;
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

/deep/ .el-drawer__body {
  overflow-y: auto;
  overflow-x: hidden;
  padding: 20px 20px 10px;
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
