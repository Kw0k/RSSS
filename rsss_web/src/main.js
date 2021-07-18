import Vue from 'vue';
import App from './App.vue';
import router from './router';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css'; // 默认主题
import './assets/css/icon/iconfont.css'
import './components/common/directives';
import 'babel-polyfill';
import axios from 'axios'
import VueAxios from 'vue-axios'
import qs from 'qs';
import dataV from '@jiaminghi/data-view'

Vue.use(dataV)
Vue.prototype.qs = qs;
Vue.use(VueAxios, axios)
Vue.config.productionTip = false;
Vue.use(ElementUI, {
    size: 'small'
});
axios.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';
axios.defaults.withCredentials = true //解决跨域请求时sessionid不一致
axios.defaults.baseURL="http://localhost:8888" //后端地址
//使用钩子函数对路由进行权限跳转
router.beforeEach((to, from, next) => {
    document.title = `${to.meta.title} | 餐厅自助结算系统`;
    next();
});

new Vue({
    router,
    render: h => h(App)
}).$mount('#app');
