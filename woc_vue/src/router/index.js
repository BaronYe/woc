import Vue from 'vue';
import Router from 'vue-router';
import routes from "@router/routers";

const originalPush = Router.prototype.push;
Router.prototype.push = function(location) {
    return originalPush.call(this, location).catch(err => err)
}
Vue.use(Router);

export default new Router({
    routes
});
