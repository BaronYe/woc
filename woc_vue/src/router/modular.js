const items = [
    {
        icon: 'el-icon-apple',
        index: 'dashboard',
        title: '系统首页'
    },{
        icon: 'el-icon-grape',
        index: 'user',
        title: '用户列表'
    },{
        icon: 'el-icon-orange',
        index: '2',
        title: '系统设置',
        subs:[
            {
                index:"applets",
                title:"小程序配置"
            }
        ]
    }
];

export default items;
