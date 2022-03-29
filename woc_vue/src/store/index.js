/**
  * @Description: 配置全局变量
  * @author 宋新勇
  */
import Vue from 'vue';
import Vuex from 'vuex'
Vue.use(Vuex);

//引入vuex 数据持久化插件
import createPersistedState from "vuex-persistedstate"

export default new Vuex.Store({
	state:{
		munIdx:0,
		token:"",
		admin:"",
	},
	mutations:{
		// munidx的修改
		COMMIT_MUNIDX(state, obj) {
			state.munIdx = obj.munIdx;
		},
		// token
		COMMIT_TOKEN(state, obj) {
			state.token = obj.token;
		},
		// admin
		COMMIT_ADMIN(state, obj) {
			state.admin = obj.admin;
		},
	},
	plugins: [createPersistedState({
		storage: window.sessionStorage,
		reducer(val) {
			return {
				token: val.token,
				admin: val.admin,
				munIdx:val.munIdx,
			}
		}
	})]
})
