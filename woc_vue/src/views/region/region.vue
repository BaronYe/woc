<template>
  <div class="container">
    <v-prompt :title="[
        '当前页面对地区商圈的信息进行管理，可以添加地区商圈，管理地区商圈等。',
        '如果直接删除地区，那么地区下的商圈也会消失，请谨慎操作。',
        '添加店铺的时候需要选择对应的地区商圈,用户可以根据地区商圈搜索店铺。'
    ]">
    </v-prompt>
    <div class="d-flex ai-center">
      <el-button class="simon-button" @click="addRegion">添加地区</el-button>
    </div>
    <!-- 列表 -->
    <template>
      <el-table
          :data="regionList"
          style="width: 100%;margin-top: 20px;">
        <el-table-column
            label="#"
            align="center"
            width="150px"
            prop="id">
        </el-table-column>
        <el-table-column
            align="center"
            width="150px"
            label="地区底图">
          <template slot-scope="scope">
            <el-image :src="scope.row.img"
                      :preview-src-list="[scope.row.img]"
                      style="width: 30px;height: 30px;"></el-image>
          </template>
        </el-table-column>
        <el-table-column
            label="是否显示"
            align="center"
            width="200px"
            prop="name">
          <template slot-scope="scope">
            <div>{{scope.row.isShow == '0' ? '隐藏':'显示'}}</div>
          </template>
        </el-table-column>
        <el-table-column
            label="地区名称"
            align="left"
            prop="name">
        </el-table-column>
        <el-table-column
            label="商圈"
            width="200px"
            align="center">
          <template slot-scope="scope">
            <el-button
                @click.native.prevent="openDModelFn(scope.row.id,scope.row.name)"
                type="text"
                size="small">
              编辑
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
            label="操作"
            width="200px"
            align="center">
          <template slot-scope="scope">
            <el-button
                @click.native.prevent="editClick(scope.row)"
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
    <!-- 弹窗 -->
    <template>
      <el-drawer
          title="地区编辑"
          :visible.sync="dialogFormVisible"
          size="30%"
          :wrapperClosable="false"
          direction="rtl">
        <div class="simon-drawer__content">
          <el-form label-position="right" :model="form" :rules="rules" ref="formRef" v-if="form != null"
                   label-width="100px">
            <el-form-item label="图片（375*200）" prop="img">
              <el-col :span="12">
                <el-upload
                    class="avatar-uploader"
                    :action="`${this.siteinfo.siteroot}${this.siteinfo.project}/oss/upload`"
                    :show-file-list="false"
                    title="建议尺寸 100*100"
                    :on-success="imgSuccess">
                  <img v-if="form.img" :src="form.img" class="avatar" alt="暂无">
                  <i v-else class="el-icon-upload2 avatar-uploader-icon" style="font-size: 20px;line-height: 82px;"></i>
                </el-upload>
              </el-col>
            </el-form-item>
            <el-form-item label="地区名称" prop="name">
              <el-input v-model="form.name" placeholder="请填写地区名称"></el-input>
            </el-form-item>
            <el-form-item label="是否显示" prop="isShow">
              <el-radio v-model="form.isShow" label="0">隐藏</el-radio>
              <el-radio v-model="form.isShow" label="1">显示</el-radio>
            </el-form-item>
          </el-form>
          <div class="simon-drawer__footer">
            <el-button @click="dialogFormVisible=false">取 消</el-button>
            <el-button class="simon-button" @click="setClick('formRef')">确 定</el-button>
          </div>
        </div>
      </el-drawer>
    </template>

    <v-circleSon ref="circleSon"></v-circleSon>
  </div>
</template>

<script>
import prompt from '@components/dist/prompt';
import circleSon from '@views/region/circleSon';

export default {
  name: 'region',
  components: {
    'v-prompt': prompt,
    'v-circleSon': circleSon,
  },
  data(){
    return{
      form:null,
      dialogFormVisible:false,
      rules:{
        name: [{ required: true, message: '请填写地区名称' }],
        img: [{ required: true, message: '请上传地区底图' }],
        isShow: [{ required: true, message: '请选择是否显示' }],
      },
      regionList:[]
    }
  },
  mounted() {
    this.getRegionFn();
  },
  methods:{
    //添加地区
    addRegion(){
      this.form = {
        id:0,
        name:"",
        img:"",
        isShow:"1"
      }
      this.dialogFormVisible = true;
    },
    //编辑
    editClick(item){
      this.form = JSON.parse(JSON.stringify(item));
      this.dialogFormVisible = true;
    },
    // 添加图片
    imgSuccess(res){
      this.form.img = res.data;
    },
    // 保存
    setClick(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.api.setRegion(this.form).then(res=>{
            this.$message.success('保存成功');
            setTimeout(() => {
              this.dialogFormVisible = !this.dialogFormVisible;
              this.getRegionFn();
            }, 1500);
          })
        }
      });
    },
    // 删除
    delClick(id){
      this.$confirm('此操作将删除该活动, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.delRegionById({id}).then(res=>{
          this.$message.success("删除成功");
          setTimeout(()=>{
            this.getRegionFn();
          },1500)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 查询
    getRegionFn(){
      this.api.getRegion().then(res=>{
        this.regionList = res.data;
      })
    },
    //添加
    openDModelFn(id,title) {
      this.$refs['circleSon'].isShowDia(id, true,title);
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
/deep/ .el-upload--text {
  width: 100%;
  height: 100%;
  border: none;
}
/deep/ .el-drawer__body {
  overflow-y: auto;
  overflow-x: hidden;
  padding: 20px 20px 10px;
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
</style>
