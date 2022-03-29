import axios from "axios";
import router from "../router/index";
import {
    Loading
} from "element-ui";
import { messages } from '../assets/js/common.js'
axios.defaults.timeout = 60000;
axios.defaults.baseURL = process.env.VUE_APP_LOGOUT_URL;
axios.defaults.headers.post["Content-Type"] =
    "application/x-www-form-urlencoded;charset=UTF-8";
let loading = null;
/*
 *请求前拦截
 *用于处理需要请求前的操作
 */
axios.interceptors.request.use(
    config => {
        // loading = Loading.service({
        //     text: "正在加载中......",
        //     fullscreen: true
        // });
        if (sessionStorage.getItem('token')) {
            config.headers["Authorization"] = sessionStorage.getItem('token');
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);
/*
 *请求响应拦截
 *用于处理数据返回后的操作
 */
axios.interceptors.response.use(
    response => {
        return new Promise((resolve, reject) => {
            //请求成功后关闭加载框
            if (loading) {
                loading.close();
            }
            const res = response.data;
            if (res.code === 200) {
                resolve(res)
            }else if (res.code === 401){
                messages("warning", res.msg);
                sessionStorage.removeItem('token');
                setTimeout(() => {
                    router.replace({
                        path: "/login",
                    });
                }, 1000);
            }else {
                reject(res)
                messages("error", response.data.msg);
            }
        })
    },
    error => {
        //请求成功后关闭加载框
        if (loading) {
            loading.close();
        }
        //断网处理或者请求超时
        if (!error.response) {
            //请求超时
            if (error.message.includes("timeout")) {
                messages("error", "请求超时，请检查互联网连接");
            } else {
                //断网，可以展示断网组件
                messages("error", "请检查网络是否已连接");
            }
            return;
        }
        const status = error.response.status;
        switch (status) {
            case 500:
                messages("error", "服务器内部错误");
                break;
            case 404:
                messages(
                    "error",
                    "未找到远程服务器"
                );
                break;
            case 401:
                messages("warning", "用户登陆过期，请重新登陆");
                setTimeout(() => {
                    router.replace({
                        path: "/login",
                    });
                }, 1000);
                break;
            case 400:
                messages("error", "数据异常");
                break;
            default:
                messages("error", error.response.data.message);
        }
        return Promise.reject(error);
    }
);
/*
 *get方法，对应get请求
 *@param {String} url [请求的url地址]
 *@param {Object} params [请求时候携带的参数]
 */
export function get(url, params) {
    return new Promise((resolve, reject) => {
        axios
            .get(url, {
                params
            })
            .then(res => {
                resolve(res);
            })
            .catch(err => {
                reject(err);
            });
    });
}
/*
 *post方法，对应post请求
 *@param {String} url [请求的url地址]
 *@param {Object} params [请求时候携带的参数]
 */
export function post(url, params) {
    return new Promise((resolve, reject) => {
        axios
            .post(url, params)
            .then(res => {
                resolve(res);
            })
            .catch(err => {
                reject(err);
            });
    });
}
/*
 *put方法，对应put请求
 *@param {String} url [请求的url地址]
 *@param {Object} params [请求时候携带的参数]
 */
export function put(url, params) {
    return new Promise((resolve, reject) => {
        axios
            .put(url, params)
            .then(res => {
                resolve(res);
            })
            .catch(err => {
                reject(err);
            });
    });
}
/*
 *delete方法，对应delete请求
 *@param {String} url [请求的url地址]
 *@param {Object} params [请求时候携带的参数]
 */
export function Delete(url, params) {
    return new Promise((resolve, reject) => {
        axios
            .delete(url, {
                params
            })
            .then(res => {
                resolve(res);
            })
            .catch(err => {
                reject(err);
            });
    });
}
