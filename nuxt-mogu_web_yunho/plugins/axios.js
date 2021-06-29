export default function({$axios,redirect,store,route,$router}){//这里也可以从 app:{$cookies}获取，但是由于是从磁盘获取，效率不如store从内存获取高
    //基本配置信息
    $axios.defaults.timeout=20000;
    // $axios.defaults.baseURL='';
    //请求拦截
    $axios.onRequest(config=>{
        console.log("请求时拦截******");
        config.headers.cs="cc";
        config.headers.Authorization=store.state.token?store.state.token:'';
        console.log("store.state.user.token*****"+store.state.token);
        console.log("config.headers.token*****"+config.headers.token);
        // if(!config.headers.token){
        //     console.log("请求时 跳转******");
        //     redirect('/login?path='+route.fullPath);
        // }
        // Promise.reject();
        return config;
    });
    //响应时拦截
    $axios.onResponse(res=>{
        console.log("响应时拦截****** res.data.");
        console.log(res.data);
        // if(res.data.code!=0 && route.fullPath !='/login'){
        //     console.log("响应时 跳转******");
        //     redirect('/login?path='+route.fullPath);
        // }

        const resp = res.data
        console.log(resp.code);
        if (resp.code === 'success' || resp.code === 'error') {
        return resp
        } else if (resp.code === 401 || resp.code === 400) {
        console.log('返回错误内容', res)
        router.push('404')
        return resp
        } else if (resp.code === 500) {
        router.push('500')
        return Promise.reject('error')
        } else if (resp.code === 502) {
        router.push('502')
        return Promise.reject('error')
        } else {
        return Promise.reject('error')
        }
        return resp;
    });

    $axios.onRequestError(err => {
        console.log("请求异常捕获 ");
        console.log(err)
    })
 
    $axios.onResponseError(err => {
        console.log("响应异常捕获 ");
        redirect('500')
        Promise.reject(err)
        console.log(err)
    })


    //异常处理
    $axios.onError(error=>{
        // 出现网络超时
        
        console.log("网络异常捕获 ");
        console.log(error)
        // redirect('500')
        // Promise.reject(error)
        return error;
    });

}