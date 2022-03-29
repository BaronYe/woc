const getComponent = (name, component) => () => import(`@views/${name}/${component}.vue`);

export default [
    {
        path: '/',
        redirect: '/dashboard'
    },
    {
        path: '/',
        component: () => import('../components/common/Home.vue'),
        meta: { title: '自述文件' },
        children: [
            {
                path: '/dashboard',
                component: () => import('../views/Dashboard.vue'),
                meta: { title: '系统首页' }
            },
            {
                path: '/userinfo',
                component: getComponent("userinfo","userinfo"),
                meta: { title: '个人信息' }
            },{
                path: '/applets',
                component: getComponent("applets","applets"),
                meta: { title: '小程序设置'}
            },{
                path: '/user',
                component: getComponent("user","user"),
                meta: { title: '用户列表' }
            },{
                path: '/coupon',
                component: getComponent("coupon","coupon"),
                meta: { title: '优惠券' }
            },{
                path: '/activity',
                component: getComponent("activity","activity"),
                meta: { title: '活动管理' }
            },{
                path: '/goods',
                component: getComponent("goods","goods"),
                meta: { title: '商品管理' }
            },{
                path: '/classifys',
                component: getComponent("classifys","classifys"),
                meta: { title: '商品分类' }
            },{
                path: '/shop',
                component: getComponent("shop","shop"),
                meta: { title: '店铺管理' }
            },{
                path: '/blindOrder',
                component: getComponent("order","blindOrder"),
                meta: { title: '盲盒订单' }
            },{
                path: '/integralOrder',
                component: getComponent("order","integralOrder"),
                meta: { title: '积分订单' }
            },{
                path: '/shopOrder',
                component: getComponent("order","shopOrder"),
                meta: { title: '商城订单' }
            },{
                path: '/region',
                component: getComponent("region","region"),
                meta: { title: '地区管理' }
            },{
                path: '/box',
                component: getComponent("box","box"),
                meta: { title: '盲盒管理' }
            },{
                path: '/boxClassify',
                component: getComponent("box","classify"),
                meta: { title: '盲盒分类' }
            },{
                path: '/merchant',
                component: getComponent("merchant","merchant"),
                meta: { title: '商户管理' }
            },{
                path: '/banner',
                component: getComponent("banner","banner"),
                meta: { title: '轮播图管理' }
            },{
                path: '/configs',
                component: getComponent("configs","configs"),
                meta: { title: '基础配置' }
            },{
                path: '/cashout',
                component: getComponent("cashout","cashout"),
                meta: { title: '会员提现' }
            },{
                path: '/feedbacks',
                component: getComponent("feedbacks","feedbacks"),
                meta: { title: '会员反馈' }
            },{
                path: '/integral',
                component: getComponent("integral","integral"),
                meta: { title: '积分商城' }
            },{
                path: '/notices',
                component: getComponent("notices","notices"),
                meta: { title: '系统通知' }
            },{
                path: '/topics',
                component: getComponent("topics","topics"),
                meta: { title: '话题管理' }
            },{
                path: '/dynamic',
                component: getComponent("dynamic","dynamic"),
                meta: { title: '动态管理' }
            },
        ]
    },
    {
        path: '/login',
        component: getComponent("login","login"),
        meta: { title: '登录' }
    },
    {
        path: '/404',
        component: () => import('../views/404.vue'),
        meta: { title: '错误页面' }
    },
    {
        path: '*',
        redirect: '/404',
        name: 'notFound',
        hidden: true
    }
];
