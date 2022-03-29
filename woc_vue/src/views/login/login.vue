<template>
  <div class="login-container">
    <vue-particles
        color="#fff"
        :particlesNumber="100"
        :moveSpeed="1.5"
        :lineOpacity="0.5"
        class="bg"
    ></vue-particles>
    <div class="login-main">
      <div class="login-tab">
        <el-row>
          <el-col
              :span="24"
              style="height: 50px;"
              :class="['active']">
            <span class="tab-text">管理员登录</span>
          </el-col>
        </el-row>
      </div>
      <div class="login-form">
        <el-form
            :model="dataForm"
            :rules="dataRule"
            ref="dataForm"
            @keyup.enter.native="dataFormSubmit()"
            status-icon
        >
          <el-form-item prop="account">
            <el-input v-model="dataForm.account" placeholder="请输入帐号"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="dataForm.password" type="password" placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item prop="verifyCode">
            <el-row :gutter="20">
              <el-col :span="14">
                <el-input v-model="dataForm.verifyCode" placeholder="请输入验证码"></el-input>
              </el-col>
              <el-col :span="10" class="login-captcha">
                <div class="login-code" @click="refreshCode">
                  <!--验证码组件-->
                  <s-identify :identifyCode="identifyCode"></s-identify>
                </div>
              </el-col>
            </el-row>
          </el-form-item>
          <el-form-item>
            <el-button
                class="login-btn-submit"
                type="primary"
                style="background:#17b3a3;border:0;height: 40px;font-size: 16px;"
                @click="dataFormSubmit()"
            >登录
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script>
import SIdentify from './SIdentify';
import { messages } from '@assets/js/common.js';
import md5 from 'js-md5';

export default {
  name: 'login',
  data() {
    const validateCode = (rule, value, callback) => {
      if (this.identifyCode !== value) {
        this.dataForm.verifyCode = '';
        this.refreshCode();
        callback(new Error('请输入正确的验证码'));
      } else {
        callback();
      }
    };
    return {
      identifyCode: '',
      identifyCodes: '1234567890',
      verifyCode: '', //验证码
      dataForm: {
        account: '',
        password: '',
        verifyCode: ''
      },
      dataRule: {
        account: [{ required: true, message: '帐号不能为空', trigger: 'blur' }],
        password: [
          { required: true, message: '密码不能为空', trigger: 'blur' }
        ],
        verifyCode: [
          { required: true, message: '验证码不正确', trigger: 'blur' },
          { validator: validateCode, message: '验证码不正确', trigger: 'blur' }
        ]
      }
    };
  },

  components: { SIdentify },
  created() {
    this.refreshCode();
  },
  mounted() {
    this.identifyCode = '';
    this.makeCode(this.identifyCodes, 4);
  },
  methods: {
    //生成验证码第三步
    randomNum(min, max) {
      return Math.floor(Math.random() * (max - min) + min);
    },
    //生成验证码第一步
    refreshCode() {
      this.identifyCode = '';
      this.makeCode(this.identifyCodes, 4);
    },
    //生成验证码第二步
    makeCode(o, l) {
      for (let i = 0; i < l; i++) {
        this.identifyCode += this.identifyCodes[
            this.randomNum(0, this.identifyCodes.length)
            ];
      }
    },
    //登录
    dataFormSubmit() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          this.api.login({
            account: this.dataForm.account,
            password: md5(this.dataForm.password)
          }).then(res => {
            sessionStorage.setItem('token', res.data.token);
            sessionStorage.setItem('admin', JSON.stringify(res.data.admin));
            messages('success', '登录成功');
            this.$router.push({
              path: '/dashboard'
            });
          });
        }
      });
    },


  }
};
</script>
<style lang="less" scoped>
/deep/ .el-form-item__error {
  color: #ff3300;
}

.bg {
  position: fixed;
  z-index: -1;
  width: 100%;
  height: 100%;
}

.login-container {
  background-image: url(../../assets/02.jpg);
  background-size: cover;
  width: 100%;
  height: 100%;
  position: fixed;
  display: table;

  .login-form {
    display: block;
    vertical-align: middle;
    text-align: center;
    color: white;
    font-size: 18px;

    .aa {
      margin: auto;
      float: none;
    }

    h3 {
      line-height: 60px;
      margin-left: 100px;
    }

    .acount {
      text-align: left;
    }
  }
}

.login-main {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 380px;
  border-radius: 5px;
  background: #fff;
  overflow: hidden;
}

@media screen and (max-width: 500px) {
  .login-main {
    width: 94%;
  }
}

.login-tab {
  height: 50px;
  line-height: 47px;
  text-align: center;
  margin-bottom: 20px;
  border-bottom: 1px solid #d8d8d8;
  color: #ffffff;
}

.active {
  border-bottom: 2px solid #17b3a3;
  color: #17b3a3;
}

.login-form {
  padding: 20px;
}

.tab-text {
  font-size: 16px;
}

.tab-choose {
  color: #17b3a3;
}

.login-title {
  font-size: 16px;
}

.login-captcha {
  overflow: hidden;

  > img {
    width: 100%;
    cursor: pointer;
  }
}

.login-btn-submit {
  width: 100%;
  margin-top: 38px;
}
</style>

