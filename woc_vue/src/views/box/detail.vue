<template>
  <div>
    <el-drawer
        :title="title"
        :visible.sync="dialogFormVisible"
        size="30%"
        :wrapperClosable="false"
        direction="rtl">
      <div class="simon-drawer__content">
        <el-form label-position="right" :model="form" :rules="rules" ref="activityRef" v-if="form != null"
                 :label-width="formLabelWidth">
          <el-form-item label="图片：" prop="cover">
            <el-col :span="12">
              <el-upload
                  class="avatar-uploader"
                  :action="`${this.siteinfo.siteroot}${this.siteinfo.project}/oss/upload`"
                  :show-file-list="false"
                  title="建议尺寸 375px*200px"
                  :on-success="imgsSuccess">
                <img v-if="form.cover" :src="form.cover" class="avatar" title="建议尺寸 375px*200px"
                     alt="暂无">
                <i v-else class="el-icon-upload2 avatar-uploader-icon" style="font-size: 24px;line-height: 100px;"
                   title="建议尺寸 375px*200px"></i>
              </el-upload>
            </el-col>
          </el-form-item>
          <el-form-item label="盲盒名称：" prop="boxName">
            <el-input v-model="form.boxName" style="width: 200px" placeholder="请填写盲盒名称"></el-input>
          </el-form-item>
          <el-form-item label="盲盒分类：" prop="cid">
            <el-select v-model="form.cid" placeholder="全部" style="width:200px;">
              <el-option
                  v-for="item in coassifyList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="价格：" prop="price">
            <el-input v-model="form.price" type="number" style="width:200px;" placeholder="请填写价格"></el-input>
          </el-form-item>
          <el-form-item label="拼单价格：" prop="spellPrice">
            <el-input v-model="form.spellPrice" type="number" style="width:200px;" placeholder="请填写拼单价格"></el-input>
          </el-form-item>
          <el-form-item label="数量：" prop="counts">
            <el-input v-model="form.counts" type="number" style="width:200px;" placeholder="请填写数量"></el-input>
          </el-form-item>
          <el-form-item label="标签：">
            <el-input v-model="form.label" style="width:200px;" placeholder="请填写标签"></el-input>
          </el-form-item>
          <el-form-item label="是否上架：">
            <el-radio v-model="form.isShelves" label="0">放入仓库</el-radio>
            <el-radio v-model="form.isShelves" label="1">立即上架</el-radio>
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
export default {
  name: 'detail',
  data() {
    return {
      title: '盲盒编辑',
      dialogFormVisible: false,
      id: 0,
      form: null,
      formLabelWidth: '120px',
      rules: {
        boxName: [{ required: true, message: '请填写盲盒名称' }],
        cover: [{ required: true, message: '请上传主图' }],
        cid: [{ required: true, message: '请选择分类' }],
        price: [{ required: true, message: '请填写价格' }],
        spellPrice: [{ required: true, message: '请填写拼单价格' }],
      },
      editorOption: {
        placeholder: '请在这里添加活动描述',
        theme: 'snow'
      },
      coassifyList:[],

    };
  },
  mounted() {
    this.getClassifyList();
  },
  methods: {
    // 显示
    isShowDia(id, isVisible) {
      this.id = id;
      this.dialogFormVisible = isVisible;
      if (id == 0) {
        this.form = {
          boxName: '',
          id: 0,
          cid: "",
          cover: '',
          price: '',
          spellPrice: '',
          label: '',
          counts:0,
          isShelves:"0"
        };
      }else{
        this.toDetail();
      }
    },
    // 获取分类
    getClassifyList() {
      this.api.getBoxClassify().then(res => {
        this.coassifyList = res.data;
      });
    },
    //主图
    imgsSuccess(res) {
      this.form.cover=res.data;
    },

    // 活动内容
    onEditorChange(editor) {
      this.form.text = editor.html;
    },
    // 保存
    setClick(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.api.setBox({
            id:this.form.id,
            boxName:this.form.boxName,
            cid:parseInt(this.form.cid),
            price:this.form.price,
            spellPrice:this.form.spellPrice,
            cover:this.form.cover,
            label:this.form.label,
            counts:parseInt(this.form.counts),
            isShelves:this.form.isShelves,
          }).then(res=>{
            this.$message.success('保存成功');
            setTimeout(() => {
              this.dialogFormVisible = !this.dialogFormVisible;
              this.$parent.getBoxFn();
            }, 1500);
          })
        }
      });
    },
    //获取活动详情
    toDetail(){
      this.api.getBoxById({
        id:this.id
      }).then(res=>{
        this.form = res.data;
      })
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

/deep/ .el-tabs__item.is-active{
  color: var(--orange);
}
/deep/.el-tabs__active-bar{
  background-color: var(--orange);
}
/deep/.el-tabs__item:hover{
  color: var(--orange);
}
.avatar-uploader-banner {
  width: 82px;
  height: 82px;
  line-height: 82px;
  border: 1px #ccc dashed;
  border-radius: 4px;

}
.banner {
  width: 82px;
  height: 82px;
  position: relative;
  margin-left: 10px;
  margin-bottom: 10px;
  border-radius: 4px;
}

.banner-div {
  position: absolute;
  width: 82px;
  height: 82px;
  background: rgba(0, 0, 0, .6);
  text-align: center;
  line-height: 82px;
  display: none;
  border-radius: 4px;
}

.banner:hover .banner-div {
  display: block;
}

.banner-div i {
  color: #fff;
}

.banner-img {
  width: 82px;
  height: 82px;
}
/deep/.el-radio__label{
  font-size: 14px;
  font-weight: 400;
  color: #bbbbbb;
}
/deep/.el-radio__input.is-checked+.el-radio__label{
  color: var(--orange);
}
/deep/.el-radio__input.is-checked .el-radio__inner{
  border-color: var(--orange);
  background:var(--orange);
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
