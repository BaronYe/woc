import Vue from 'vue';
import App from './App.vue';
import router from './router';
import "@assets/css/main.css";
import "@assets/css/simon.css";
// import '@assets/css/reset.css'
// import '@assets/css/layui.css'
// import '@assets/css/common.css'
// 引入ElementUI
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI, {
    size: 'small'
});
// 引入vant
import Vant from 'vant';
import 'vant/lib/index.css';
Vue.use(Vant);
// 引入粒子效果
import VueParticles from 'vue-particles'
Vue.use(VueParticles)

import './assets/css/icon.css';
import 'babel-polyfill';
// 引入less style 样式写法
import less from 'less'
Vue.use(less)

import api from './api/api'
Vue.prototype.api=api;
import siteinfo from './api/siteinfo'
Vue.prototype.siteinfo=siteinfo;

import store from '@store/index.js';
// Vue.prototype.store = store;



Vue.config.productionTip = false;
//使用钩子函数对路由进行权限跳转
router.beforeEach((to, from, next) => {
    document.title = `${to.meta.title}`;
    const role = sessionStorage.getItem('token');
    if (!role && to.path !== '/login') {
        next('/login');
    }
    document.body.scrollTop =0;
    document.documentElement.scrollTop = 0;
    next();
});

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app');
