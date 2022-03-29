<template>
  <div class="menu-box">
    <div v-for="(item,index) of menuList" :key="index" class="mb-item"
         :class="[menuId == item.id ? 'is-this':'']"
         @click="menuClick(item)"
    >{{item.name}}</div>
  </div>
</template>

<script>
export default {
  name: 'navMenu',
  props: ['menu',"menuId"],
  data() {
    return {
      menuList:this.menu,
      mIndex:this.menuId
    };
  }, methods: {
    menuClick(item){
        this.$parent.menuId = item.id;
        this.$emit("send",item)
    }
  }
};
</script>

<style scoped lang="less">
.menu-box{
  //display: flex;
  //align-items: center;
  height: 41px;
  position: relative;
  left: 0;
  white-space: nowrap;
  font-size: 0;
  transition: all .2s;
  -webkit-transition: all .2s;
  margin-top: 20px;
  .mb-item{
    display: inline-block;
    *display: inline;
    *zoom: 1;
    vertical-align: middle;
    font-size: 14px;
    transition: all .2s;
    -webkit-transition: all .2s;
    position: relative;
    line-height: 40px;
    min-width: 65px;
    padding: 0 15px;
    text-align: center;
    cursor: pointer;
    background-color: #FFFFFF;
    border-top: 1px solid #EEEEEE;
    border-bottom: 1px solid #EEEEEE;
    border-left: 1px solid #EEEEEE;
    &:last-child {
      border-right: 1px solid #EEEEEE;
    }
    &:first-child {
      border-left: 1px solid #EEEEEE;
    }
  }
  .is-this{
    background: #EEEEEE;
    border-radius: 2px;
  }
}
</style>
