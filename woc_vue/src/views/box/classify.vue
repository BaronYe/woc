<template>
  <div class="container">
    <v-prompt :title="[
        '添加盲盒的时候需要选择对应的盲盒分类,用户可以根据盲盒分类搜索盲盒商品。',
        '盲盒属性是前台搜索分类查询盲盒之后可以通过商品的属性进行进一步搜索。']">
    </v-prompt>
    <div class="d-flex ai-center">
      <el-button class="simon-button" @click="addClassify()">添加盲盒分类</el-button>
    </div>
    <!-- 列表 -->
    <template>
      <el-table
          :data="coassifyList"
          style="width: 100%;margin-top: 20px;">
        <el-table-column
            label="分类名称"
            align="left"
            prop="name">
        </el-table-column>
        <el-table-column
            label="操作"
            width="200"
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
      <el-dialog
          title="添加盲盒分类"
          :visible.sync="dialogVisible"
          top="30vh"
          width="390px">
        <div>
          <el-form label-position="right" :model="form" :rules="rules" ref="classifyRef" v-if="form != null"
                   :label-width="formLabelWidth">
            <el-form-item label="分类名称：" prop="name">
              <el-input style="width: 245px;" v-model="form.name" placeholder="请填写分类名称"></el-input>
            </el-form-item>
            <div style="margin: 40px auto 0">
              <el-button class="simon-button" style="width:100px;display: block;margin:auto;"
                         @click="setClick('classifyRef')">保存
              </el-button>
            </div>
          </el-form>
        </div>
      </el-dialog>
    </template>
  </div>
</template>

<script>
import prompt from '@components/dist/prompt';
export default {
name: "classify",
  components: {
    'v-prompt': prompt
  },data() {
    return {
      dialogVisible: false,
      form: null,
      formLabelWidth: '100px',
      options: [],
      rules: {
        name: [{ required: true, message: '请填写分类名称' }]
      },
      coassifyList:[]
    };
  },
  mounted() {
    this.getClassifyList();
  },
  methods: {
    // 添加
    addClassify() {
      this.form = {
        name: '',
        id: 0
      };
      this.dialogVisible = !this.dialogVisible;
    },
    //修改
    editClick(item){
      this.form = {
        name: item.name,
        id: item.id
      };
      this.dialogVisible = !this.dialogVisible;
    },
    // 删除
    delClick(id){
      this.$confirm('此操作将删除该分类, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.delBoxClassifyById({id}).then(res=>{
          this.$message.success("删除成功");
          setTimeout(()=>{
            this.getClassifyList();
          },1500)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },

    // 保存
    setClick(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.api.setBoxClassify({
            id: this.form.id,
            name: this.form.name,
          }).then(res => {

            this.$message.success("保存成功");
            setTimeout(()=>{
              this.dialogVisible = !this.dialogVisible;
              this.form = {
                name: '',
                id: 0
              };
              this.getClassifyList();
            },1500)
          });
        }
      });
    },
    // 获取分类
    getClassifyList(){
      this.api.getBoxClassify().then(res=>{
        this.coassifyList = res.data;
      })
    },

  }
};
</script>

<style scoped>

</style>
