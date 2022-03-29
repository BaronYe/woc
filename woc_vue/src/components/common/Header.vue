<template>
  <div class="header" :class="{'content-collapse':collapse,'content-no-collapse':!collapse}">

    <div class="layui-logo" @click="collapseChage">
      <img src="@assets/img/logo.png">
    </div>
    <ul class="layui-nav layui-layout-left">
      <li v-for="(item,index) of munList" :key="index" class="layui-nav-item">
        <a :class="[munIdx == index ? 'active':'']" @click="toRouter(item,index)">
          <span>{{ item.name }}</span>
        </a>
      </li>
    </ul>
    <div class="header-right">
      <div class="header-user-con">
        <!-- 全屏显示 -->
        <div class="btn-fullscreen" @click="handleFullScreen">
          <el-tooltip effect="dark" :content="fullscreen?`取消全屏`:`全屏`" placement="bottom">
            <i class="el-icon-rank"></i>
          </el-tooltip>
        </div>
        <!-- 用户名下拉菜单 -->
        <el-dropdown class="user-name" trigger="click" @command="handleCommand">
                    <span class="el-dropdown-link">
                        {{ name }}
                        <i class="el-icon-caret-bottom"></i>
                    </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="info">个人信息</el-dropdown-item>
            <el-dropdown-item divided command="password">修改密码</el-dropdown-item>
            <el-dropdown-item divided command="loginout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <el-dialog title="修改密码" :visible.sync="dialogFormVisible" width="35%">
      <el-form :model="form">
        <el-form-item label="修改密码" :label-width="formLabelWidth">
          <el-input v-model="form.pas" autocomplete="off" type="password"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" :label-width="formLabelWidth">
          <el-input v-model="form.newPas" autocomplete="off" type="password"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="confrimAdmin()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import bus from '../common/bus';
import { mapState } from 'vuex';

export default {
  data() {
    return {
      collapse: true,
      fullscreen: false,
      name: '管理员',
      message: 2,
      dialogFormVisible: false,
      form: {
        pas: '',
        newPas: ''
      },
      formLabelWidth: '100px', content: '',
      munList: [],
    };
  },
  computed: {
    ...mapState({
      munIdx: (obj) => obj.munIdx,
    }),

  },
  methods: {
    // 用户名下拉菜单选择事件
    handleCommand(command) {
      if (command === 'loginout') {
        sessionStorage.removeItem('token');
        this.$router.push('/login');
      } else if (command === 'info') {
        this.$router.push({
          path: '/userinfo'
        });
      } else if (command === 'password') {
        this.dialogFormVisible = true;
      }
    },
    // 侧边栏折叠
    collapseChage(collapse) {
      bus.$emit('collapse',collapse );
    },
    // 全屏事件
    handleFullScreen() {
      let element = document.documentElement;
      if (this.fullscreen) {
        if (document.exitFullscreen) {
          document.exitFullscreen();
        } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen();
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen();
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen();
        }
      } else {
        if (element.requestFullscreen) {
          element.requestFullscreen();
        } else if (element.webkitRequestFullScreen) {
          element.webkitRequestFullScreen();
        } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen();
        } else if (element.msRequestFullscreen) {
          // IE11
          element.msRequestFullscreen();
        }
      }
      this.fullscreen = !this.fullscreen;
    },
    // 修改密码
    confrimAdmin() {
      if (this.form.pas === '' || this.form.newPas === '') {
        this.$message({
          type: 'error',
          message: '密码不能为空!'
        });
        return;
      }
      if (this.form.pas === this.form.newPas) {
        this.api.setAdmin({
          id: JSON.parse(sessionStorage.getItem('admin')).id,
          password: this.form.pas
        }).then(res => {
          this.$message({
            type: 'success',
            message: '修改成功'
          });
          this.dialogFormVisible = false;
        });
        return;
      }
      this.$message({
        type: 'error',
        message: '密码不一致!'
      });
    },
    // 菜单栏的点击事件
    toRouter(item,index) {
      this.$store.commit('COMMIT_MUNIDX', {
        munIdx: index
      });
      if (item.link != "" || item.subs.length == 0){
        this.collapseChage(true);
        this.$router.push(item.link);
        bus.$emit('tags',item.name );
      }else{
        this.collapseChage(false);
        bus.$emit('subs',item.subs );
        let path = this.$route.path;
        let idx = 0;
        item.subs.forEach((v,k) =>{
          if (v.link == path){
            idx = k
          }
        })
        this.$router.push(item.subs[idx].link);
      }
    },
    /**
     * 获取菜单栏
     */
    getMenuBar(){
      this.api.getMenuBar().then(res=>{
        let data = res.data;
        this.munList = data;
        this.toRouter(data[this.munIdx],this.munIdx)
      })
    }
  },
  mounted() {
    this.getMenuBar();
  }
};
</script>
<style scoped lang="less">
.header {
  position: relative;
  box-sizing: border-box;
  height: 70px;
  font-size: 22px;
  color: #000;
  -webkit-transition: left .3s ease-in-out;
  transition: left .3s ease-in-out;
  display: flex;
  background: #fff;
  border-bottom: 1px #f1f1f1 solid;

}

.layui-logo {
  position: absolute;
  left: 0;
  top: 0;
  width: 185px;
  height: 100%;
  line-height: 70px;
  text-align: center;
  color: #009688;
  font-size: 16px;
}

.collapse-btn {
  padding: 0 21px;
  cursor: pointer;
  line-height: 70px;
  display: block;
}

.collapse-btn:hover {
  background: #fff;
}

.content-name {
  display: block;
  line-height: 70px;
  width: 100%;
  font-size: 15px;
  color: #666;
  font-weight: 400;
  font-palette: dark;
  font-style: italic;
}


.header-right {
  position: fixed;
  right: 30px;
}

.header-user-con {
  display: flex;
  height: 70px;
  align-items: center;
}

.btn-fullscreen {
  transform: rotate(45deg);
  margin-right: 5px;
  font-size: 24px;
}

.btn-bell,
.btn-fullscreen {
  position: relative;
  width: 30px;
  height: 30px;
  text-align: center;
  border-radius: 15px;
  cursor: pointer;
}

.btn-bell-badge {
  position: absolute;
  right: 0;
  top: -2px;
  width: 8px;
  height: 8px;
  border-radius: 4px;
  background: #f56c6c;
  color: #fff;
}

.btn-bell .el-icon-bell {
  color: #fff;
}

.user-name {
  margin-left: 10px;
}

.user-avator {
  margin-left: 20px;
}

.user-avator img {
  display: block;
  width: 36px;
  height: 36px;
  border-radius: 50%;
}

.el-dropdown-link {
  color: #000;
  cursor: pointer;
}

.el-dropdown-menu__item {
  text-align: center;
}

@media screen and (max-width: 500px) {
  .collapse-btn, .content-name {
    display: none;
  }
}

.layui-layout-admin .layui-header .layui-nav {
  background: 0 0;
}

.layui-nav {
  position: relative;
  padding: 0 20px;
  color: #333333;
  border-radius: 2px;
  box-sizing: border-box;
  font-size: 14px;
  background-color: #ffffff !important;
}

.layui-layout-left {
  position: absolute !important;
  left: 185px;
  top: 0;
}

.layui-nav .layui-nav-item {
  position: relative;
  display: inline-block;
  *display: inline;
  *zoom: 1;
  vertical-align: middle;
  line-height: 69px;

  a {
    display: block;
    padding: 0 20px;
    color: #333;
    transition: all .3s;
    -webkit-transition: all .3s;
    cursor: pointer;

    &:hover {
      span {
        border-bottom: 3px solid var(--orange);
        border-radius: 1.5px;
        box-sizing: border-box;
      }
    }

    span {
      display: inline-block;
      height: 40px;
      line-height: 40px;
      box-sizing: border-box;
    }
  }

  .active span {
    border-bottom: 3px solid var(--orange);
    border-radius: 1.5px;
    box-sizing: border-box;
  }

}

</style>
