<template>
  <div class="container">
    <v-prompt :title="[
        '客服二维码建议使用尺寸200x200像素以上、大小不超过1M的图片']">
    </v-prompt>
    <el-form label-position="right" :model="configs" label-width="200px" v-if="configs != null">
      <div class="layui-card-header">客服配置</div>
      <el-form-item label="客服二维码：">
        <el-col :span="12">
          <el-upload
              class="avatar-uploader"
              :action="`${this.siteinfo.siteroot}${this.siteinfo.project}/oss/upload`"
              :show-file-list="false"
              title="建议尺寸 375px*200px"
              :on-success="imgsSuccess">
            <img v-if="configs.wechatCode" :src="configs.wechatCode" class="avatar"alt="暂无">
            <i v-else class="el-icon-upload2 avatar-uploader-icon" style="font-size: 24px;line-height: 100px;"></i>
          </el-upload>
        </el-col>
      </el-form-item>
      <div class="layui-card-header">提现配置</div>
      <el-form-item label="提现手续费比率：">
        <el-input-number v-model="configs.cashOutPoundage" style="width: 140px" controls-position="right"
                         :min="0"></el-input-number>
        <span class="layui-form-mid">%</span>
      </el-form-item>
      <div class="ns-word-aux">
        <p>比率必须为0-100的整数</p>
      </div>
      <el-form-item label="最低提现额度：">
        <el-input-number v-model="configs.cashOutMoney" style="width: 140px" controls-position="right"
                         :min="0"></el-input-number>
      </el-form-item>
      <div class="layui-card-header">积分配置</div>
      <el-form-item label="积分自定义名称：">
        <el-input v-model="configs.integralName" style="width: 140px" ></el-input>
      </el-form-item>
      <el-form-item label="多少积分抵扣1元：">
        <el-input-number v-model="configs.integralDeduction" style="width: 140px" controls-position="right"
                         :min="1"></el-input-number>
      </el-form-item>
      <div class="ns-word-aux">
        <p>积分抵扣数量必须大于1</p>
      </div>
      <el-form-item label="注册获得积分：">
        <el-input-number v-model="configs.registerIntegralNum" style="width: 140px" controls-position="right"
                         :min="0"></el-input-number>
      </el-form-item>
      <el-form-item label="每天登陆获得积分：">
        <el-input-number v-model="configs.loginIntegralNum" style="width: 140px" controls-position="right"
                         :min="0"></el-input-number>
      </el-form-item>
      <el-form-item label="消费1元等多少积分：">
        <el-input-number v-model="configs.consumeIntegralNum" :step="0.1" style="width: 140px" controls-position="right"
                         :min="0"></el-input-number>
      </el-form-item>
      <el-form-item label="完善信息获得积分：">
        <el-input-number v-model="configs.perfectIntegralNum" style="width: 140px" controls-position="right"
                         :min="0"></el-input-number>
      </el-form-item>
      <el-form-item label="每天发布动态获得积分：">
        <el-input-number v-model="configs.dynamicIntegralNum" style="width: 140px" controls-position="right"
                         :min="0"></el-input-number>
      </el-form-item>
      <el-form-item label="邀请好友获得积分：">
        <el-input-number v-model="configs.inviteIntegralNum" style="width: 140px" controls-position="right"
                         :min="0"></el-input-number>
      </el-form-item>
      <el-form-item label="分享小程序获得积分：">
        <el-input-number v-model="configs.shareIntegralNum" style="width: 140px" controls-position="right"
                         :min="0"></el-input-number>
      </el-form-item>
      <el-button class="simon-button sb-btn" @click="onSubmit()">保 存</el-button>
    </el-form>
  </div>
</template>

<script>
import prompt from '@components/dist/prompt';
export default {
  name: "configs",
  components:{
    'v-prompt': prompt,
  },
  data(){
    return{
      configs:null
    }
  },
  mounted() {
    this.getConfigs();
  },
  methods:{
    // 获取小程序信息
    getConfigs() {
      this.api.getConfigs({}).then(res => {
        this.configs = res.data;
      });
    },
    // 上图片
    imgsSuccess(res){
      this.configs.wechatCode = res.data;
    },
    //保存
    onSubmit() {
      let configs = JSON.parse(JSON.stringify(this.configs));
      configs.consumeIntegralNum =  configs.consumeIntegralNum.toString();
      this.api.setConfigs(configs).then(res => {
        this.$message.success("保存成功");
        setTimeout(()=>{
          this.getConfigs();
        },1500)
      });
    }
  }
}
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
/deep/ .el-upload--text {
  width: 100%;
  height: 100%;
  border: none;
}

.avatar-uploader {
  width: 100px;
  height: 100px;
  line-height: 100px;
  border: 1px #ddd dashed;
  margin-left: 5px;
}

.avatar {
  width: 100px;
  height: 100px;
  display: block;
}
.layui-form-mid {
  display: inline-block;
  height: 34px;
  line-height: 34px;
  padding: 0 !important;
  margin-left: 10px;
}
.ns-word-aux{
  margin-left: 200px;
  display: block;
  margin-top: 5px;
  color: #B2B2B2;
  font-size: 12px;
  line-height: 1;
  position: relative;
  top: -10px;
}
.sb-btn{
  margin: 20px 0 0 100px;
}
</style>
