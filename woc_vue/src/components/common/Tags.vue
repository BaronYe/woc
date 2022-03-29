<template>
  <div class="tags" v-if="!collapse">
    <div>{{content}}</div>
  </div>
</template>

<script>
import bus from './bus';

export default {
  data() {
    return {
      content: "",
      collapse:true
    };
  },
  methods: {

  },
  computed: {

  },
  watch: {
    $route(newValue, oldValue) {
      this.content = newValue.meta.content || newValue.meta.title;
    }
  },
  created() {
    bus.$on('collapse-tags', msg => {
      this.collapse = msg;
    });
    this.content = this.$route.meta.content || this.$route.meta.title;
  }
};

</script>


<style>
.tags {
  position: relative;
  height: 44px;
  line-height: 44px;
  overflow: hidden;
  background: #fff;
  overflow: hidden;
  padding: 0 18px;
  font-size: 14px;
  color: #666;
}

</style>
