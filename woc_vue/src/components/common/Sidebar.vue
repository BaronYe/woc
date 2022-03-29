<template>
  <div class="sidebar">
    <el-menu
        class="sidebar-el-menu"
        :default-active="onRoutes"
        background-color="#fff"
        text-color="#333333"
        active-text-color="#FF6A00"
        unique-opened
        router
    >
      <template v-for="item in subs">
        <template>
          <el-menu-item :index="item.link" :key="item.link">
            <i :class="item.icon"></i>
            <span slot="title">{{ item.name }}</span>
          </el-menu-item>
        </template>
      </template>
    </el-menu>
  </div>
</template>

<script>
import bus from '../common/bus';
export default {
  data() {
    return {
      collapse: true,
      subs:[]
    };
  },
  computed: {
    onRoutes() {
      return this.$route.path;
      // return this.$route.path.replace('/', '');
    }
  },
  created() {
    // 通过 Event Bus 进行组件间通信，来折叠侧边栏
    bus.$on('collapse', msg => {
      this.collapse = msg;
      bus.$emit('collapse-content', msg);
      bus.$emit('collapse-tags', msg);
    });
    bus.$on('subs', msg => {
      this.subs = msg;
    });
  }
};
</script>

<style scoped>

.sidebar {
  display: block;
  position: absolute;
  left: 0;
  top: 70px;
  bottom: 0;
  overflow-y: scroll;
  border-right: 1px #f1f1f1 solid;
}
/deep/.el-menu-item{
  border-left: 3px var(--white) solid;
}
.is-active {
  background-color: var(--orangeLight) !important;
  color: var(--orange) !important;
  box-sizing: border-box;
  position: relative;
  border-left: 3px var(--orange) solid;
}

.logo {
  height: 70px;
  padding: 0 0 0 20px;
  line-height: 70px;
  color: #fff;
  font-size: 18px;
  font-weight: 400;
  font-palette: dark;
  font-style: italic;
  display: flex;
  align-items: center;
}

.logo-name {
  width: 100%;
  display: block;
  height: 100%;
  overflow: hidden;
}

.logo > img {
  margin-right: 10px;
}

.sidebar::-webkit-scrollbar {
  width: 0;
}

.sidebar-el-menu:not(.el-menu--collapse) {
  width: 184px;
  height: 100%;
  /*height: calc(100% - 70px);*/
}

.el-menu:last-child {
  /*height: calc(100% - 70px);*/
}
.sidebar-el-menu{
  width: 0;
}
.sidebar-el-menu-name:not(.el-menu--collapse) {
  width: 184px;
}

.sidebar-el-menu-name > ul {
  height: 70px;
}

.sidebar-el-menu > ul {
  height: 100%;
}
</style>
