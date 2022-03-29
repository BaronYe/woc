<template>
  <div class="container">
    <v-prompt :title="['请谨慎执行该操作，变更该配置会导致之前的登录失效，用户需要重新登录','必须与微信小程序平台配置一样']"></v-prompt>
    <el-form ref="form" :model="applets" v-if="applets != null" label-width="100px" class="form">
      <el-form-item label="appId">
        <el-col :span="10">
          <el-input v-model="applets.appId"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item label="appSecret">
        <el-col :span="10">
          <el-input v-model="applets.appSecret"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item label="payId">
        <el-col :span="10">
          <el-input v-model="applets.payId"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item label="paySecret">
        <el-col :span="10">
          <el-input v-model="applets.paySecret"></el-input>
        </el-col>
      </el-form-item>
      <el-form-item>
        <el-button class="simon-button" @click="onSubmit">保 存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import prompt from '@components/dist/prompt';

export default {
  name: 'applets',
  components: {
    'v-prompt': prompt
  },
  data() {
    return {
      applets: null
    };
  },
  mounted() {
    this.getApplets();
  },
  methods: {
    // 获取小程序信息
    getApplets() {
      this.api.getApplets({}).then(res => {
        this.applets = res.data;
      });
    },
    onSubmit() {
      this.api.setApplets(this.applets).then(res => {
        this.$message.success(res.msg);
      });
    }
  }
};
</script>

<style scoped>

</style>
