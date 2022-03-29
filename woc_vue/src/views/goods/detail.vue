<template>
  <div>
    <el-drawer
        :title="title"
        :visible.sync="dialogFormVisible"
        size="60%"
        :wrapperClosable="false"
        direction="rtl">
         <div class="simon-drawer__content" >
           <el-tabs v-model="activeName" @tab-click="handleClick">
             <el-tab-pane label="基础配置" name="first">
               <el-form label-position="right" :model="form" :rules="rules" ref="activityRef" v-if="form != null"
                        :label-width="formLabelWidth">
                 <el-form-item label="商品名称" prop="title">
                   <el-input v-model="form.title" style="width: 300px" placeholder="请填写商品名称"></el-input>
                 </el-form-item>
                 <el-form-item label="商品分类" prop="classifys">
                   <el-cascader
                       placeholder="请选择商品分类"
                       :options="coassifyList"
                       v-model="form.classifys"
                       :props="{ value: 'id',label:'name',children:'items' }"
                       @change="handleChange"
                   >
                   </el-cascader>
                 </el-form-item>
                 <el-form-item label="商品主图（375*375）" prop="imgs">
                   <div style="display: flex;flex-wrap: wrap;flex: 1;">
                     <el-upload
                         class="avatar-uploader-banner"
                         :show-file-list="false"
                         :action="`${this.siteinfo.siteroot}${this.siteinfo.project}/oss/upload`"
                         :on-success="imgsSuccess">
                       <i class="el-icon-plus"></i>
                     </el-upload>
                     <div v-for="(item,index) of form.imgs" class="banner">
                       <div class="banner-div">
                         <i class="el-icon-delete" @click="delimgs(index)"></i>
                       </div>
                       <img width="100%" :src="item" alt="" class="banner-img">
                     </div>
                   </div>
                 </el-form-item>
                 <div class="d-flex">
                   <el-form-item label="价格" prop="price">
                     <el-input v-model="form.price" type="number" style="width:200px;" placeholder="请填写商品价格"></el-input>
                   </el-form-item>
                   <el-form-item label="原价" prop="specialPrice" style="margin-left: 50px;">
                     <el-input v-model="form.specialPrice" type="number" style="width:200px;" placeholder="请填写商品特价"></el-input>
                   </el-form-item>
                 </div>
                 <el-form-item label="排序" >
                   <el-input v-model="form.isDesc" type="number" style="width:200px;" placeholder="请填写商品排序"></el-input>
                 </el-form-item>
                 <el-form-item label="是否上架">
                   <el-radio v-model="form.isShelves" label="0">放入仓库</el-radio>
                   <el-radio v-model="form.isShelves" label="1">立即上架</el-radio>
                 </el-form-item>
<!--                 <el-form-item label="是否上架积分商城">-->
<!--                   <el-radio v-model="form.isIntegral" label="0">关闭</el-radio>-->
<!--                   <el-radio v-model="form.isIntegral" label="1">开启</el-radio>-->
<!--                 </el-form-item>-->
<!--                 <el-form-item label="积分数量：" v-if="form.isIntegral == '1'">-->
<!--                   <el-input-number v-model="form.integralNum"  style="width: 140px" controls-position="right" :min="0" ></el-input-number>-->
<!--                 </el-form-item>-->
<!--                 <el-form-item label="每人兑换数量：" v-if="form.isIntegral == '1'">-->
<!--                   <el-input-number v-model="form.exchangeNum"  style="width: 140px" controls-position="right" :min="0" ></el-input-number>-->
<!--                 </el-form-item>-->
                 <el-form-item label="商品描述" prop="text">
                   <quill-editor style="margin-top: 10px;" ref="myTextEditor" v-model="form.text"
                                 :options="editorOption"
                                 @change="onEditorChange($event)"></quill-editor>
                 </el-form-item>

               </el-form>
               <div class="simon-drawer__footer">
                 <el-button @click="dialogFormVisible=false">取 消</el-button>
                 <el-button class="simon-button" @click="setClick('activityRef')">确 定</el-button>
               </div>
             </el-tab-pane>
             <el-tab-pane label="商品规格" name="third" :disabled="id == 0">
               <div class="d-flex ai-center">
                 <el-button class="simon-button" @click="addSpecs()">添加</el-button>
               </div>
               <!--规格列表-->
               <template>
                 <el-table
                     :data="specsList"
                     style="width: 100%;margin-top: 20px;">
                   <el-table-column
                       align="center"
                       label="商品图">
                     <template slot-scope="scope">
                       <el-image :src="scope.row.img" style="width: 40px;height: 40px;"></el-image>
                     </template>
                   </el-table-column>
                   <el-table-column
                       prop="name"
                       align="center"
                       label="规格名称">
                   </el-table-column>
                   <el-table-column
                       prop="price"
                       align="center"
                       label="价格">
                   </el-table-column>
                   <el-table-column
                       prop="linePrice"
                       align="center"
                       label="原价">
                   </el-table-column>
<!--                   <el-table-column-->
<!--                       prop="integralNum"-->
<!--                       align="center"-->
<!--                       label="积分数量">-->
<!--                   </el-table-column>-->
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
                           @click.native.prevent="delSpecsClick(scope.row.id)"
                           type="text"
                           size="small">
                         删除
                       </el-button>
                     </template>
                   </el-table-column>
                 </el-table>
               </template>

             </el-tab-pane>
           </el-tabs>
         </div>
    </el-drawer>
    <!-- 弹窗 -->
    <template>
      <el-drawer
          title="添加商品规格"
          :visible.sync="dialogVisible"
          direction="rtl"
          :wrapperClosable="false"
          size="30%">
        <div class="simon-drawer__content">
          <el-form label-position="right" :model="specs" :rules="rulesSpecs" ref="specsRef" v-if="specs != null"
                   label-width="100px">
            <el-form-item label="图片（100*100）" prop="img">
              <el-col :span="12">
                <el-upload
                    class="avatar-uploader"
                    :action="`${this.siteinfo.siteroot}${this.siteinfo.project}/oss/upload`"
                    :show-file-list="false"
                    title="建议尺寸 100*100"
                    :on-success="imgSuccess">
                  <img v-if="specs.img" :src="specs.img" class="avatar" alt="暂无">
                  <i v-else class="el-icon-upload2 avatar-uploader-icon" style="font-size: 20px;line-height: 82px;"></i>
                </el-upload>
              </el-col>
            </el-form-item>
            <el-form-item label="规格名称" prop="name">
              <el-input v-model="specs.name" placeholder="请填写规格名称"></el-input>
            </el-form-item>
            <el-form-item label="价格" prop="price">
              <el-input v-model="specs.price" type="number" placeholder="请填写价格"></el-input>
            </el-form-item>
            <el-form-item label="原价" prop="linePrice">
              <el-input v-model="specs.linePrice" type="number" placeholder="请填写原价"></el-input>
            </el-form-item>
<!--            <el-form-item label="积分数量：" >-->
<!--              <el-input-number v-model="specs.integralNum"  style="width: 140px" controls-position="right" :min="0" ></el-input-number>-->
<!--            </el-form-item>-->
          </el-form>
          <div class="simon-drawer__footer">
            <el-button @click="dialogVisible=false">取 消</el-button>
            <el-button class="simon-button" @click="setSpecsClick('specsRef')">确 定</el-button>
          </div>
        </div>
      </el-drawer>
    </template>
  </div>
</template>

<script>
import 'quill/dist/quill.core.css';
import 'quill/dist/quill.snow.css';
import 'quill/dist/quill.bubble.css';
import { quillEditor } from 'vue-quill-editor';

export default {
  name: 'detail',
  components: {
    quillEditor
  },
  data() {
    return {
      title: '活动编辑',
      dialogFormVisible: false,
      id: 0,
      form: null,
      formLabelWidth: '150px',
      rules: {
        title: [{ required: true, message: '请填写商品名称' }],
        imgs: [{ required: true, message: '请上传商品主图' }],
        specialPrice: [{ required: true, message: '请填写商品特价' }],
        price: [{ required: true, message: '请填写商品价格' }],
        classifys: [{ required: true, message: '请选择商品分类' }]
      },
      editorOption: {
        placeholder: '请在这里添加活动描述',
        theme: 'snow'
      },
      activeName: 'first',
      coassifyList:[],
      dialogVisible:false,
      specs:null,
      rulesSpecs:{
        name: [{ required: true, message: '请填写规格名称' }],
        img: [{ required: true, message: '请上传商品图' }],
        price: [{ required: true, message: '请填写商品价格' }],
        linePrice: [{ required: true, message: '请填写商品原价' }]
      },
      specsList:[]
    };
  },
  mounted() {

    this.getClassifyListFn();
  },
  methods: {
    // 显示
    isShowDia(id, isVisible) {
      this.id = id;
      this.dialogFormVisible = isVisible;
      this.activeName = "first";
      if (id == 0) {
        this.form = {
          title: '',
          id: 0,
          imgs: [],
          specialPrice: '',
          price: '',
          text: '',
          classifys:[],
          isDesc:0,
          isShelves:"0",

        };
      }else{
        this.toDetail();
      }
    },

    // nav栏的点击选择
    handleClick(tab) {
      if (tab.index == '1'){
        this.getspecsFn();
      }
    },
    // 获取分类
    getClassifyListFn(){
      this.api.getClassifyList().then(res=>{
        this.coassifyList = res.data;
      })
    },
    //商品分类的选择
    handleChange(v){
      this.form.classifys = v;
    },
    //商品主图
    imgsSuccess(res) {
      this.form.imgs.push(res.data);
    },
    //规格商品图
    imgSuccess(res) {
      this.specs.img = res.data;
    },
    // 删除图片
    delimgs(e) {
      this.$confirm('此操作将删除该文件图片, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.form.imgs.splice(e, 1);
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 活动内容
    onEditorChange(editor) {
      this.form.text = editor.html;
    },
    // 保存
    setClick(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.api.setItem({
            id:this.form.id,
            title:this.form.title,
            imgs:this.form.imgs.join(","),
            specialPrice:this.form.specialPrice,
            price:this.form.price,
            text:this.form.text,
            isDesc:parseInt(this.form.isDesc),
            isShelves:this.form.isShelves,
            cid:this.form.classifys[0],
            sonCid:this.form.classifys[1],
          }).then(res=>{
            this.$message.success('保存成功');
            setTimeout(() => {
              this.dialogFormVisible = !this.dialogFormVisible;
              this.$parent.getItemFn();
            }, 1500);
          })
        }
      });
    },
    //获取活动详情
    toDetail(){
      this.api.getItemById({
        id:this.id
      }).then(res=>{
        this.form = res.data;
        this.form.classifys = [res.data.cid,res.data.sonCid]
      })
    },
    // 规格添加
    addSpecs(){
      this.specs = {
        img:"",
        name:"",
        id:0,
        itemId:this.id,
        price:"",
        linePrice:""
      }
      this.dialogVisible = !this.dialogVisible;
    },
    // 编辑规格
    editClick(item){
      this.specs = item;
      this.dialogVisible = !this.dialogVisible;
    },
    // 规格保存
    setSpecsClick(formName){
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.api.setSpecs(this.specs).then(res=>{
            this.$message.success('保存成功');
            setTimeout(() => {
              this.dialogVisible = !this.dialogVisible;
              this.getspecsFn();
            }, 1500);
          })
        }
      });
    },
    // 获取规格
    getspecsFn(){
      this.api.getSpecs({
        itemId:this.id
      }).then(res=>{
        this.specsList = res.data;
      })
    },
    // 删除规格
    delSpecsClick(id){
      this.$confirm('此操作将删除该活动, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.api.delSpecsById({id}).then(res=>{
          this.$message.success("删除成功");
          setTimeout(()=>{
            this.getspecsFn();
          },1500)
        })
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
.aaa{
  overflow-y: auto;
  overflow-x: hidden;
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
</style>
