<template>
  <div>
    <dl :class="[isOpen ? 'isopen' : '']" class="simon-anim-upbit simon-anim">
      <dd
        v-for="(item, index) of items"
        :key="index"
        :class="[itemName == item.name ? 'simon-this' : '']"
        @click="doSelect(item)"
      >
        {{ item.name }}
      </dd>
    </dl>
  </div>
</template>
<script>
export default {
  props: ["items","itemName","isOpen"],
  methods: {
    doSelect(item) {
        // this.device = item.name;
        // this.$emit('input', this.device);
        this.$parent.selectName =  item.name;
        this.$parent.isOpen = !this.isOpen;
        this.$emit('send',item)
    },
  },
};
</script>

<style scoped>
.isopen {
  display: block;
}
dl {
  display: none;
  position: absolute;
  left: 0;
  top: 42px;
  padding: 5px 0;
  z-index: 899;
  min-width: 100%;
  border: 1px solid #d2d2d2;
  max-height: 300px;
  overflow-y: auto;
  background-color: #fff;
  border-radius: 2px;
  box-shadow: 0 2px 4px rgb(0 0 0 / 12%);
  box-sizing: border-box;
}
dd {
  padding: 0 10px;
  line-height: 36px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  text-align: left;
  color: #666;
  cursor: pointer;
}
.simon-this {
  background-color: #ff6a00;
  color: #fff;
}
@keyframes simon-upbit {
  from {
    transform: translate3d(0, 30px, 0);
    opacity: 0.3;
  }

  to {
    transform: translate3d(0, 0, 0);
    opacity: 1;
  }
}

.simon-anim-upbit {
  -webkit-animation-name: simon-upbit;
  animation-name: simon-upbit;
}
.simon-anim {
  -webkit-animation-duration: 0.3s;
  animation-duration: 0.3s;
  -webkit-animation-fill-mode: both;
  animation-fill-mode: both;
}
</style>