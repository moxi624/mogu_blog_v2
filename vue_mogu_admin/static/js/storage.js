/*!
 * 本地化存储(localStorage) 组件
 *
 * 版权所有(C) 2013 马超 (zjcn5205@yeah.net)
 *
 * 这一程序是自由软件，你可以遵照自由软件基金会出版的GNU通用公共许可证条款来修改和重新发布
 * 这一程序。或者用许可证的第二版，或者（根据你的选择）用任何更新的版本。
 * 发布这一程序的目的是希望它有用，但没有任何担保。甚至没有适合特定目的的隐含的担保。更详细
 * 的情况请参阅GNU通用公共许可证。
 * 你应该已经和程序一起收到一份GNU通用公共许可证的副本。如果还没有，写信给：
 * The Free Software Foundation, Inc., 675 Mass Ave, Cambridge, MA02139, USA
 */
/*
 *[功能描述]
 * 给不支持本地存储的浏览器创建一个 window.localStorage 对象来提供类似接口
 * 该对象支持以下方法或属性
	setItem : function(key, value)
	getItem : function(key)
	removeItem : function(key)
	clear : function()
	length : int
	key : function(i)
	isVirtualObject : true
 * 二次包装的接口 window.LS 提供以下方法和属性（如果有jQuery则同样会扩展该对象），推荐使用
	set : function(key, vlaue)
	get : function(key)
	remove : function(key)
	clear : function()
	each : function(callback) callback接受两个参数 key 和 value
	obj : function() 返回一个对象描述的localStorage副本
	length : int
 * 
 *[已知问题、使用限制]
 * 原生本地存储的key是区分大小写的，模拟对象不区分（因为userData不区分key的大小写）
 * 模拟对象的 clear 方法仅仅能清理通过本组件设定的数据
 * 模拟对象的实际的存储容量跟原生本地存储有差异
 * 模拟对象不支持任何localStorage事件件
 *
 *[更新日志]
 * 2012-06-20 马超 创建
 * 2012-06-27 马超 增加clear相关方法和属性
 * 2012-07-02 马超 修改节点存储方式
 * 2012-07-03 马超 增加二次包装以优化接口使用
 * 2012-07-04 马超 修改内部逻辑，取消原有的单独存储key的方案，修改查询不存在key的时候的默认值为undefined
 * 2012-07-05 马超 增加二次包装的obj方法
 * 2013-03-06 胡志明 基于瑞星的刘瑞明提供的方案，兼容360急速浏览器IE模式（IE6），降低浏览器自带localStorage优先级，优先使用userData。
 * 2013-03-11 胡志明 恢复iframe代理创建head标签用户存储数据，修正了userData无法垮目录读写问题
 * 2013-03-14 胡志明 对部分无法创建iframe代理的浏览器尝试使用自带localStorage，如果不自带则暂时不支持本地存储
 * 2013-03-18 马超 优化加载判断逻辑和代码，以最大限度保证组件可用
 * 2013-04-23 马超 增加更多的错误处理，进一步提高浏览器的兼容性
 * 2013-05-04 马超 增加对userData的key的无效字符的转义处理功能
 * 2013-06-06 马超 优先探测本地存储，解决IE9下userData使用问题（刷新页面后无效）
 */
(function(window){
	//准备模拟对象、空函数等
	var LS, noop = function(){}, document = window.document, notSupport = {set:noop,get:noop,remove:noop,clear:noop,each:noop,obj:noop,length:0};
	
	//优先探测userData是否支持，如果支持，则直接使用userData，而不使用localStorage
	//以防止IE浏览器关闭localStorage功能或提高安全级别(*_* 万恶的IE)
	//万恶的IE9(9.0.11）)，使用userData也会出现类似seesion一样的效果，刷新页面后设置的东西就没有了...
	//只好优先探测本地存储，不能用再尝试使用userData
	(function(){
		// 先探测本地存储 2013-06-06 马超
		// 尝试访问本地存储，如果访问被拒绝，则继续尝试用userData，注： "localStorage" in window 却不会返回权限错误
		// 防止IE10早期版本安全设置有问题导致的脚本访问权限错误
		if( "localStorage" in window ){
			try{
				LS = window.localStorage;
				return;
			}catch(e){
				//如果报错，说明浏览器已经关闭了本地存储或者提高了安全级别
				//则尝试使用userData
			}
		}
		
		//继续探测userData
		var o = document.getElementsByTagName("head")[0], hostKey = window.location.hostname || "localStorage", d = new Date(), doc, agent;
		
		//typeof o.addBehavior 在IE6下是object，在IE10下是function，因此这里直接用!判断
		//如果不支持userData则跳出使用原生localStorage，如果原生localStorage报错，则放弃本地存储
		if(!o.addBehavior){
			try{
				LS = window.localStorage;
			}catch(e){
				LS = null;
			}
			return;
		}
		
		try{ //尝试创建iframe代理，以解决跨目录存储的问题
			agent = new ActiveXObject('htmlfile');
			agent.open();
			agent.write('<s' + 'cript>document.w=window;</s' + 'cript><iframe src="/favicon.ico"></iframe>');
			agent.close();
			doc = agent.w.frames[0].document;
			//这里通过代理document创建head，可以使存储数据垮文件夹访问
			o = doc.createElement('head');
			doc.appendChild(o);
		}catch(e){
			//不处理跨路径问题，直接使用当前页面元素处理
			//不能跨路径存储，也能满足多数的本地存储需求
			//2013-03-15 马超
			o = document.getElementsByTagName("head")[0];
		}
		
		//初始化userData
		try{
			d.setDate(d.getDate() + 36500);
			o.addBehavior("#default#userData");
			o.expires = d.toUTCString();
			o.load(hostKey);
			o.save(hostKey);
		}catch(e){
			//防止部分外壳浏览器的bug出现导致后续js无法运行
			//如果有错，放弃本地存储
			//2013-04-23 马超 增加
			return;
		}
		//开始处理userData
		//以下代码感谢瑞星的刘瑞明友情支持，做了大量的兼容测试和修改
		//并处理了userData设置的key不能以数字开头的问题
		var root, attrs;
		try{
			root = o.XMLDocument.documentElement;
			attrs = root.attributes;
		}catch(e){
			//防止部分外壳浏览器的bug出现导致后续js无法运行
			//如果有错，放弃本地存储
			//2013-04-23 马超 增加
			return;
		}
		var prefix = "p__hack_", spfix = "m-_-c",
			reg1 = new RegExp("^"+prefix),
			reg2 = new RegExp(spfix,"g"),
			encode = function(key){ return encodeURIComponent(prefix + key).replace(/%/g, spfix); },
			decode = function(key){ return decodeURIComponent(key.replace(reg2, "%")).replace(reg1,""); };
		//创建模拟对象
		LS= {
			length: attrs.length,
			isVirtualObject: true,
			getItem: function(key){
				//IE9中 通过o.getAttribute(name);取不到值，所以才用了下面比较复杂的方法。
				return (attrs.getNamedItem( encode(key) ) || {nodeValue: null}).nodeValue||root.getAttribute(encode(key)); 
			},
			setItem: function(key, value){
				//IE9中无法通过 o.setAttribute(name, value); 设置#userData值，而用下面的方法却可以。
				try{
					root.setAttribute( encode(key), value);
					o.save(hostKey);
					this.length = attrs.length;
				}catch(e){//这里IE9经常报没权限错误,但是不影响数据存储
				}
			},
			removeItem: function(key){
				//IE9中无法通过 o.removeAttribute(name); 删除#userData值，而用下面的方法却可以。
				try{
					root.removeAttribute( encode(key) );
					o.save(hostKey);
					this.length = attrs.length;
				}catch(e){//这里IE9经常报没权限错误,但是不影响数据存储
				}
			},
			clear: function(){
				while(attrs.length){
					this.removeItem( attrs[0].nodeName );
				}
				this.length = 0;
			},
			key: function(i){
				return attrs[i] ? decode(attrs[i].nodeName) : undefined;
			}
		};
		//提供模拟的"localStorage"接口
		if( !("localStorage" in window) )
			window.localStorage = LS;
	})();
	
	//二次包装接口
	window.LS = !LS ? notSupport : {
		set : function(key, value){
			//fixed iPhone/iPad 'QUOTA_EXCEEDED_ERR' bug
			if( this.get(key) !== undefined )
				this.remove(key);
			LS.setItem(key, value);
			this.length = LS.length;
		},
		//查询不存在的key时，有的浏览器返回null，这里统一返回undefined
		get : function(key){
			var v = LS.getItem(key);
			return v === null ? undefined : v;
		},
		remove : function(key){ LS.removeItem(key);this.length = LS.length; },
		clear : function(){ LS.clear();this.length = 0; },
		//本地存储数据遍历，callback接受两个参数 key 和 value，如果返回false则终止遍历
		each : function(callback){
			var list = this.obj(), fn = callback || function(){}, key;
			for(key in list)
				if( fn.call(this, key, this.get(key)) === false )
					break;
		},
		//返回一个对象描述的localStorage副本
		obj : function(){
			var list={}, i=0, n, key;
			if( LS.isVirtualObject ){
				list = LS.key(-1);
			}else{
				n = LS.length;
				for(; i<n; i++){
					key = LS.key(i);
					list[key] = this.get(key);
				}
			}
			return list;
		},
		length : LS.length
	};
	//如果有jQuery，则同样扩展到jQuery
	if( window.jQuery ) window.jQuery.LS = window.LS;
})(window); 