<template>
  <div class="container">
    <v-prompt :title="[
        '话题管理是对话题列表进行修改，添加，删除等',
     ]">
    </v-prompt>
    <div class="d-flex ai-center">
      <el-button class="simon-button" @click="addClassify()">添加话题</el-button>
    </div>
    <!-- 列表 -->
    <template>
      <el-table
          :data="dataList"
          style="width: 100%;margin-top: 20px;">
        <el-table-column
            label="话题标题"
            align="left"
            prop="title">
        </el-table-column>
        <el-table-column
            label="话题状态"
            align="center"
            width="100px"
            prop="title">
          <template slot-scope="scope">
            <div v-for="(item,index) of options" v-if="item.id == scope.row.isShow">{{item.name}}</div>
          </template>
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
          title="添加话题"
          :visible.sync="dialogVisible"
          top="30vh"
          width="20%">
        <div>
          <el-form label-position="right" :model="form" :rules="rules" ref="classifyRef" v-if="form != null"
                   :label-width="formLabelWidth">
            <el-form-item label="话题标题：" prop="title">
              <el-input style="width: 245px;" v-model="form.title" placeholder="请填写分类名称"></el-input>
            </el-form-item>
            <el-form-item label="话题状态："  prop="isShow">
              <el-select v-model="form.isShow" placeholder="请选择" style="width: 245px;">
                <el-option
                    v-for="item in options"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
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
  name: 'topics',
  components: {
    'v-prompt': prompt
  },
  data() {
    return {
      dialogVisible: false,
      form: null,
      formLabelWidth: '100px',
      options: [
        {
          id:0,
          name:"隐藏"
        },{
          id:1,
          name:"显示"
        }
      ],
      rules: {
        title: [{ required: true, message: '请填写话题标题' }],
        isShow: [{ required: true, message: '请选择话题状态' }],
      },
      dataList:[]
    };
  },
  mounted() {
    this.getTopicsList();
  },
  methods: {
    // 添加
    addClassify() {
      this.form = {
        title: '',
        isShow: 1,
        id: 0
      };
      this.dialogVisible = !this.dialogVisible;
    },
    //修改
    editClick(item){
      this.form = JSON.parse(JSON.stringify(item));
      this.dialogVisible = !this.dialogVisible;
    },
    // 删除
    delClick(id){
      this.$confirm('此操作将删除该分类, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.delTopics({id}).then(res=>{
          this.$message.success("删除成功");
          setTimeout(()=>{
            this.getTopicsList();
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
          this.api.setTopics(this.form).then(res => {
            this.form = {
              title: '',
              isShow: 1,
              id: 0
            };
            this.$message.success("保存成功");
            setTimeout(()=>{
              this.dialogVisible = !this.dialogVisible;
              this.getTopicsList();
            },1500)
          });
        }
      });
    },
    // 获取分类以及子类
    getTopicsList(){
      this.api.getTopics().then(res=>{
        this.dataList = res.data;
      })
    },

  }
};
</script>

<style scoped lang="less">
</style>
