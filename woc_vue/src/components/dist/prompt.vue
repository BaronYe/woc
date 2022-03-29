<template>
  <div class="ns-tips">
    <div class="layui-colla-item">
      <h2 class="layui-colla-title" @click="isShow = !isShow">
        操作提示
        <i :class="[isShow ? 'el-icon-arrow-down':'el-icon-arrow-up']"></i>
      </h2>
      <ul class="layui-colla-content" :class="[isShow ? 'layui-show':'']">
        <li v-for="item of title">{{item}}</li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
name: "prompt",
  props:["title"],
  data(){
  return{
    isShow: true
    }
  }
}
</script>

<style scoped lang="less">
.ns-tips {
  padding: 10px 20px;
  background-color: var(--white);
  border: 1px solid #f1f1f1;
  border-radius: 4px;
  margin-bottom: 20px;
  cursor: pointer;
  h2{
    font-weight: 400;
    font-size: 16px;
    margin-bottom: 4px;
  }
  .layui-colla-item{
    &:first-child {
      border-top: none;
    }
  }
  .layui-colla-content {
    padding-left: 30px;
    border: none;
    color: var(--999);
    display: none;
  }
  .layui-show {
    display: block !important;
  }
   li {
    line-height: 25px;
     font-size: 13px;
    list-style: initial;
  }
}

</style>
