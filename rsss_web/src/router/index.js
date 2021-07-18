import Vue from 'vue';
import Router from 'vue-router';

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/h5Comment',
            component: () => import(/* webpackChunkName: "login" */ '../components/page/H5Comment.vue'),
            meta: { title: '投诉建议' }
        },
        {
            path: '/',
            redirect: '/login'
        },
        {
            path: '/admin',
            redirect: '/admin/dashboard'
        },
        {
            path: '/admin',
            component: () => import(/* webpackChunkName: "home" */ '../components/common/Home.vue'),
            meta: { title: '自述文件' },
            children: [
                {
                    path: '/admin/dashboard',
                    component: () => import(/* webpackChunkName: "dashboard" */ '../components/page/Dashboard.vue'),
                    meta: { title: '系统首页' }
                },
                {
                    path: '/admin/orderTable',
                    component: () => import(/* webpackChunkName: "table" */ '../components/page/OrderTable.vue'),
                    meta: { title: '订单管理' }
                },
                {
                    path: '/admin/goodsTable',
                    component: () => import(/* webpackChunkName: "table" */ '../components/page/GoodsTable.vue'),
                    meta: { title: '商品管理' }
                },    {
                    path: '/admin/userTable',
                    component: () => import(/* webpackChunkName: "table" */ '../components/page/UserTable.vue'),
                    meta: { title: '用户管理' }
                },
                {
                    path: '/admin/Comment',
                    component: () => import(/* webpackChunkName: "tabs" */ '../components/page/Comment.vue'),
                    meta: { title: '留言管理' }
                },
                {
                    path: '/admin/Market',
                    component: () => import(/* webpackChunkName: "tabs" */ '../components/page/Market.vue'),
                    meta: { title: '营销管理' }
                },
                {
                    path: '/admin/AliPaySetting',
                    component: () => import(/* webpackChunkName: "form" */ '../components/page/AliPaySetting.vue'),
                    meta: { title: '支付宝设置' }
                },{
                    path: '/admin/CarouselSetting',
                    component: () => import(/* webpackChunkName: "form" */ '../components/page/CarouselSetting.vue'),
                    meta: { title: '轮播广告设置' }
                },
                {
                    // 图片上传组件
                    path: '/admin/CarouselUpload',
                    component: () => import(/* webpackChunkName: "upload" */ '../components/page/CarouselUpload.vue'),
                    meta: { title: '添加轮播广告' }
                },
                {
                    path: '/404',
                    component: () => import(/* webpackChunkName: "404" */ '../components/page/404.vue'),
                    meta: { title: '404' }
                },
                {
                    path: '/admin/403',
                    component: () => import(/* webpackChunkName: "403" */ '../components/page/403.vue'),
                    meta: { title: '403' }
                }
            ]
        },
        {
            path: '/login',
            component: () => import(/* webpackChunkName: "login" */ '../components/page/Login.vue'),
            meta: { title: '登录' }
        },
        {
            path: '*',
            redirect: '/404'
        },
        {
            path: '/front',
            redirect: '/front/index'
        },
        {
            path: '/front',
            component: () => import(/* webpackChunkName: "home" */ '../components/frontCommon/Home.vue'),
            meta: { title: 'index' },
            children: [
                {
                    path: '/front/index',
                    component: () => import(/* webpackChunkName: "403" */ '../components/frontPage/Index.vue'),
                    meta: { title: '首页' }
                },
                {
                    path: '/front/step1',
                    component: () => import(/* webpackChunkName: "403" */ '../components/frontPage/Step1.vue'),
                    meta: { title: '餐盘识别' }
                },
                {
                    path: '/front/step2',
                    component: () => import(/* webpackChunkName: "403" */ '../components/frontPage/Step2.vue'),
                    meta: { title: '订单支付' }
                },
                {
                    path: '/front/step3',
                    component: () => import(/* webpackChunkName: "403" */ '../components/frontPage/Step3.vue'),
                    meta: { title: '交易完成' }
                }
            ]
        }

    ]

});
