export default ({app,redirect,params,query,store })=>{
    // console.log("插件已加载");
    // app==vue实例
    //redirect 跳转函数
    // app.router.beforeEach((to,from,next)=>{
    //     //全局前置的守卫，插件
    //     //next(true)/next(false) 允许/不允许 不能使用next('/login'),需要使用redirect
    // console.log("全局前置的守卫，插件",to);
    // if(to.name == "login" || to.name=='reg'){
    //     next(true);
    // }else{
    //     // next(false);
    //     redirect({name:'login'})
    // }
    // });
    app.router.afterEach((to,from)=>{
        // console.log("全局后置的守卫，插件",to);

    });
}