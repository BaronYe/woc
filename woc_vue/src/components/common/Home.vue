<template>
  <div class="wrapper">
    <v-head></v-head>
    <v-sidebar></v-sidebar>
    <div class="content-box" :class="[collapse ? 'content-collapse':'']">
      <vTags></vTags>
      <div class="content">
        <transition name="fade-transform" mode="out-in">
          <router-view></router-view>
        </transition>
        <el-backtop target=".content"></el-backtop>
      </div>
    </div>
  </div>
</template>

<script>
import vHead from './Header.vue';
import vSidebar from './Sidebar.vue';
import vTags from './Tags.vue';
import bus from './bus';

export default {
  data() {
    return {
      tagsList: [],
      collapse: true
    };
  },
  components: {
    vHead,
    vSidebar,
    vTags
  },
  created() {
    bus.$on('collapse-content', msg => {
      this.collapse = msg;
    });
  }
};
</script>
<style>
/*fade-transform*/
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all 0.2s;
}

.fade-transform-enter {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

.content-collapse {
  left: 0;
}
</style>
