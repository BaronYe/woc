<template>
  <div>
    <el-drawer
        :title="title"
        :visible.sync="dialogFormVisible"
        size="50%"
        :wrapperClosable="false"
        direction="rtl">
      <div class="simon-drawer__content">
        <div class="d-flex ai-center">
          <el-button class="simon-button" @click="addBoxItem()">添加</el-button>
        </div>
        <!--列表-->
        <template>
          <el-table
              ref="multipleTable"
              :data="dataList"
              tooltip-effect="dark"
              style="width: 100%;margin-top: 20px;">
            <el-table-column
                prop="id"
                align="center"
                width="120px"
                label="#">
            </el-table-column>
            <el-table-column
                prop="iName"
                align="center"
                label="商品名称">
            </el-table-column>
            <el-table-column
                align="center"
                label="商品概率">
              <template slot-scope="scope">
                <div>{{scope.row.probability}}%</div>
              </template>
            </el-table-column>
            <el-table-column
                align="center"
                label="商品规格">
              <template slot-scope="scope">
                <div >{{scope.row.specsName}}</div>
              </template>
            </el-table-column>
            <el-table-column
                label="操作"
                width="200"
                align="center">
              <template slot-scope="scope">
                <el-button
                    @click="editClick(scope.row)"
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
      </div>
    </el-drawer>

    <template>
      <el-drawer
          title="商品的编辑"
          :visible.sync="dialogItemVisible"
          size="30%"
          :wrapperClosable="false"
          direction="rtl">
        <div class="simon-drawer__content">
          <el-form label-position="right" :model="form" :rules="rules" ref="goodBoxRef" v-if="form != null"
                   label-width="100px">
            <el-form-item label="选择商品：" prop="iName">
              <el-select v-model="form.iName" style="width: 200px" no-data-text="加载中"  placeholder="请选择商品" @click.native="toGoodBox()"></el-select>
            </el-form-item>
            <el-form-item label="选择规格：" prop="spescId" v-if="specsList.length > 0 || form.spescId > 0">
              <el-select v-model="form.spescId" style="width: 200px"  placeholder="请选择规格" >
                <el-option
                    v-for="item in specsList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="商品概率：" prop="probability">
              <el-input-number v-model="form.probability" style="width: 200px" placeholder="请填写商品概率"></el-input-number>
            </el-form-item>
          </el-form>
          <div class="simon-drawer__footer">
            <el-button @click="dialogItemVisible=false">取 消</el-button>
            <el-button class="simon-button" @click="setClick('goodBoxRef')">确 定</el-button>
          </div>
        </div>
      </el-drawer>
    </template>
    <v-good-box ref="goodBox" @send="doSend"></v-good-box>
  </div>
</template>

<script>
import goodBox from '@components/dist/goodBox';

export default {
  name: 'goods',
  components:{
    'v-good-box':goodBox
  },
  data() {
    return {
      title: '',
      dialogFormVisible: false,
      dialogItemVisible: false,
      form:null,
      rules:{
        iName: [{ required: true, message: '请选择商品' }],
        spescId: [{ required: true, message: '请选择规格' }],
        probability: [{ required: true, message: '请填写商品概率' }],
      },
      dataList:[],
      specsList:[]
    };
  },
  mounted() {

  },
  methods: {
    // 显示
    isShowDia(id, isVisible, boxName) {
      this.id = id;
      this.dialogFormVisible = isVisible;
      this.title = boxName + '下的商品';
      this.getBoxItemFn();
    },
    // 获取盲盒商品
    getBoxItemFn(){
      this.api.getBoxItem({
        boxId:this.id
      }).then(res=>{
        this.dataList = res.data;
      })
    },
    // 添加
    addBoxItem() {
      this.form = {
        id: 0,
        boxId: this.id,
        itemId: '',
        iName: '',
        spescId:'',
        probability: '',
      };
      this.dialogItemVisible = !this.dialogItemVisible;
    },
    // 编辑
    editClick(item){
      this.form = JSON.parse(JSON.stringify(item));
      this.dialogItemVisible = !this.dialogItemVisible;
      this.getSpecsFn(item.itemId);
    },
    // 选择商品
    toGoodBox(){
      this.$refs['goodBox'].isShowDia();
    },
    //获取到子组件传入的值
    doSend(e){
      this.form.iName = e.title;
      this.form.itemId = e.id;
      this.getSpecsFn();
    },
    // 查看规格
    getSpecsFn(){
      this.api.getSpecs({
        itemId:this.form.itemId
      }).then(res=>{
        this.specsList = res.data;
      })
    },
    // 保存
    setClick(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.api.setBoxItem({
            id:this.form.id,
            boxId:this.form.boxId,
            itemId:this.form.itemId,
            spescId: parseInt(this.form.spescId),
            probability:this.form.probability + "",
          }).then(res=>{
            this.$message.success('保存成功');
            setTimeout(() => {
              this.dialogItemVisible = !this.dialogItemVisible;
              this.getBoxItemFn();
            }, 1500);
          })
        }
      });
    },
    // 删除
    delClick(id) {
      this.$confirm('此操作将删除该商品, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.delBoxItemById({ id }).then(res => {
          this.$message.success('删除成功');
          setTimeout(() => {
            this.getBoxItemFn();
          }, 1500);
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

/deep/ .el-tabs__item.is-active {
  color: var(--orange);
}

/deep/ .el-tabs__active-bar {
  background-color: var(--orange);
}

/deep/ .el-tabs__item:hover {
  color: var(--orange);
}

.avatar-uploader-banner {
  width: 82px;
  height: 82px;
  line-height: 82px;
  border: 1px #ccc dashed;
  border-radius: 4px;

}
</style>
