<template>
  <div>
    <el-drawer
        :title="title"
        :visible.sync="dialogFormVisible"
        size="60%"
        :wrapperClosable="false"
        direction="rtl">
      <div class="simon-drawer__content">
        <el-form label-position="right" :model="form" :rules="rules" ref="formRef" v-if="form != null"
                 label-width="100px">
          <div class="layui-card-header">基础配置</div>
          <el-form-item label="店铺名称：" prop="name">
            <el-input v-model="form.name" style="width: 300px" placeholder="请填写店铺名称"></el-input>
          </el-form-item>
          <el-form-item label="所属商圈：" prop="regions">
            <el-cascader
                placeholder="请选择所属商圈"
                :options="regionList"
                v-model="form.regions"
                :props="{ value: 'id',label:'name',children:'items' }"
                @change="handleChange"
            >
            </el-cascader>
          </el-form-item>
          <el-form-item label="所属类型：" prop="types">
            <el-select v-model="form.types" placeholder="请选择所属类型">
              <el-option key="0" label="吃喝类" value="0"></el-option>
              <el-option key="1" label="游玩类" value="1"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="店铺电话：" prop="phone">
            <el-input v-model="form.phone" style="width: 193px" type="number" placeholder="请填写店铺电话"></el-input>
          </el-form-item>
          <el-form-item label="店铺主图（375*200）" prop="banner">
            <div style="display: flex;flex-wrap: wrap;flex: 1;">
              <el-upload
                  class="avatar-uploader-banner"
                  :show-file-list="false"
                  :action="`${this.siteinfo.siteroot}${this.siteinfo.project}/oss/upload`"
                  :on-success="imgsSuccess">
                <i class="el-icon-plus"></i>
              </el-upload>
              <div v-for="(item,index) of form.banner" class="banner">
                <div class="banner-div">
                  <i class="el-icon-delete" @click="delimgs(index)"></i>
                </div>
                <img width="100%" :src="item" alt="" class="banner-img">
              </div>
            </div>
          </el-form-item>
          <div class="d-flex ai-center">
            <el-form-item label="开始时间" prop="startTime">
              <el-time-picker
                  v-model="form.startTime"
                  value-format="HH:mm"
                  format="HH:mm"
                  placeholder="店铺营业开始时间">
              </el-time-picker>
            </el-form-item>
            <el-form-item label="截止时间" prop="endTime" style="margin-left: 50px;">
              <el-time-picker
                  v-model="form.endTime"
                  value-format="HH:mm"
                  format="HH:mm"
                  placeholder="店铺营业截止时间">
              </el-time-picker>
            </el-form-item>
          </div>
          <el-form-item label="发现页展示">
            <el-radio v-model="form.isFind" label="0">隐藏</el-radio>
            <el-radio v-model="form.isFind" label="1">展示</el-radio>
          </el-form-item>
          <el-form-item label="是否停业">
            <el-radio v-model="form.isClosed" label="0">停业</el-radio>
            <el-radio v-model="form.isClosed" label="1">营业</el-radio>
          </el-form-item>
          <el-form-item label="店铺人气：" prop="moods">
            <el-input-number v-model="form.moods" :min="0" style="width: 193px;" label="请填写店铺人气"></el-input-number>
          </el-form-item>
          <el-form-item label="人均" >
            <el-input v-model="form.perCapita" style="width: 193px" placeholder="请填写店铺的人均"></el-input>
          </el-form-item>
          <div class="layui-card-header">地址配置</div>
          <div class="d-flex">
            <div style="width: 50%;">
              <el-form-item label="店铺地址：" prop="address">
                <el-input v-model="form.address" style="width: 350px" placeholder="请填写店铺地址"></el-input>
              </el-form-item>
              <el-form-item label="店铺经度：" prop="longitude">
                <el-input v-model="form.longitude" style="width: 200px" placeholder="请填写店铺经度"></el-input>
              </el-form-item>
              <el-form-item label="店铺纬度：" prop="latitude">
                <el-input v-model="form.latitude" style="width: 200px" placeholder="请填写店铺纬度"></el-input>
              </el-form-item>
            </div>
            <div style="width: 50%;">
              <v-map @toMap="toMap"></v-map>
            </div>
          </div>
          <div class="layui-card-header">店铺描述</div>
          <div style="">
            <quill-editor style="margin-top: 10px;" ref="myTextEditor" v-model="form.shopDesc"
                          :options="editorOption"
                          @change="onEditorChange($event)"></quill-editor>
          </div>
        </el-form>
        <div class="simon-drawer__footer">
          <el-button @click="dialogFormVisible=false">取 消</el-button>
          <el-button class="simon-button" @click="setClick('formRef')">确 定</el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import map from '@components/dist/map';
import { quillEditor } from 'vue-quill-editor';
import 'quill/dist/quill.core.css';
import 'quill/dist/quill.snow.css';
import 'quill/dist/quill.bubble.css';

export default {
  name: 'shopSon',
  components: {
    'v-map': map,
    quillEditor
  },
  data() {
    return {
      title: '店铺编辑',
      dialogFormVisible: false,
      form: null,
      rules: {
        name: [{ required: true, message: '请填写店铺名称' }],
        regions: [{ required: true, message: '请选择商圈' }],
        types: [{ required: true, message: '请选择类型' }],
        phone: [{ required: true, message: '请填写电话' }],
        banner: [{ required: true, message: '请上传店铺主图' }],
        startTime: [{ required: true, message: '请填写店铺营业开始时间' }],
        endTime: [{ required: true, message: '请填写店铺营业截止时间' }],
        address: [{ required: true, message: '请填写店铺店址' }],
        longitude: [{ required: true, message: '请填写店铺经度' }],
        latitude: [{ required: true, message: '请填写店铺纬度' }]
      },
      regionList: [],
      editorOption: {
        placeholder: '请在这里添加店铺描述',
        theme: 'snow'
      }
    };
  },
  mounted() {
    this.getRegionAndCircleFn();
  },
  methods: {
    // 父调子
    isShowDia(id, dialog) {
      this.id = id;
      if (this.id == 0) {
        this.form = {
          name: '',
          regions: [],
          phone: '',
          shopDesc: '',
          address: '',
          longitude: '',
          latitude: '',
          banner: [],
          startTime: '',
          endTime: '',
          isFind: '0',
          isClosed: '1',
          types: '',
          perCapita: '',
          moods: 0,
          id: 0
        };
      } else {
        this.getShopByIdFn();
      }
      this.dialogFormVisible = dialog;

    },
    // 获取详情
    getShopByIdFn(){
      this.api.getShopById({
        id:this.id
      }).then(res=>{
        this.form = res.data;
        this.form.regions = [res.data.regionId,res.data.circleId]
      })
    },
    // 获取地区商圈
    getRegionAndCircleFn() {
      this.api.getRegionAndCircle().then(res => {
        this.regionList = res.data;
      });
    },
    // 地区管理分类选择
    handleChange(v) {
      this.form.regions = v;
    },
    // 活动内容
    onEditorChange(editor) {
      this.form.shopDesc = editor.html;
    },//商品主图
    imgsSuccess(res) {
      this.form.banner.push(res.data);
    },
    // 删除图片
    delimgs(e) {
      this.$confirm('此操作将删除该文件图片, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.form.banner.splice(e, 1);
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    // 获取经纬度
    toMap(v) {
      this.form.latitude = v.latLng.lat;
      this.form.longitude = v.latLng.lng;
      this.form.address = v.address;
    },
    // 保存
    setClick(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.api.setShop({
            name: this.form.name,
            phone: this.form.phone,
            shopDesc: this.form.shopDesc,
            address: this.form.address,
            longitude: this.form.longitude + '',
            latitude: this.form.latitude + '',
            banner: this.form.banner.join(','),
            startTime: this.form.startTime,
            endTime: this.form.endTime,
            isFind: this.form.isFind,
            isClosed: this.form.isClosed,
            types: this.form.types,
            moods: this.form.moods,
            id: this.form.id,
            regionId: this.form.regions[0],
            circleId: this.form.regions[1],
            perCapita:this.form.perCapita + ""
          }).then(res => {
            this.$message.success('保存成功');
            setTimeout(() => {
              this.dialogFormVisible = !this.dialogFormVisible;
              this.$parent.getShopFn();
            }, 1500);
          });
        }
      });
    }
  }
};
</script>

<style scoped lang="less">
.layui-card-header {
  position: relative;
  height: 42px;
  line-height: 42px;
  padding: 0 10px;
  border-bottom: 1px solid #f6f6f6;
  color: #333;
  border-radius: 2px 2px 0 0;
  font-size: 14px;
  margin-bottom: 20px;
  font-weight: 600;
}
.layui-card-header::before {
  content: "";
  display: inline-block;
  width: 3px;
  height: 14px;
  background-color: #FF6A00;
  position: absolute;
  left: 0;
  top: 50%;
  border-radius: 5px;
  transform: translateY(-50%);
}
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

/deep/ .el-radio__label {
  font-size: 14px;
  font-weight: 400;
  color: #bbbbbb;
}

/deep/ .el-radio__input.is-checked + .el-radio__label {
  color: var(--orange);
}

/deep/ .el-radio__input.is-checked .el-radio__inner {
  border-color: var(--orange);
  background: var(--orange);
}
</style>
