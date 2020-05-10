<!doctype html>
<html>
<head>
    <META http-equiv="content-type" content="text/html; charset=utf-8">
    <title>${webConfig.name}</title>
    <meta name="keywords" content="${webConfig.keyword}"/>
    <meta name="description" content="${webConfig.summary}"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href="${staticBasePath}/static/css/index.css" rel="stylesheet">
    <link href="${staticBasePath}/static/css/ckeditor.css" rel="stylesheet">
    <link href="https://apps.bdimg.com/libs/highlight.js/9.1.0/styles/monokai-sublime.min.css" rel="stylesheet">
    <![endif]-->
</head>
<body>
<header class="header-navigation" id="header">
    <nav>
        <div class="logo"><a href="${vueWebBasePath}">${webConfig.name}</a></div>
        <h2 id="mnavh"><span class="navicon"></span></h2>
        <ul id="starlist">
            <li><a href="${vueWebBasePath}">网站首页</a></li>
            <li><a href="${vueWebBasePath}about">关于我</a></li>
            <li><a href="${vueWebBasePath}sort">归档</a>
            <li><a href="${vueWebBasePath}classify">归档</a>
            <li><a href="${vueWebBasePath}time">时间轴</a></li>
            <li><a href="${vueWebBasePath}messageBoard">留言板</a></li>
        </ul>
        <div class="searchbox">
            <div id="search_bar" class="search_bar">
                <form id="searchform" action="[!--news.url--]e/search/index.php" method="post" name="searchform">
                    <input class="input" placeholder="想搜点什么呢.." type="text" name="keyboard" id="keyboard">
                    <input type="hidden" name="show" value="title">
                    <input type="hidden" name="tempid" value="1">
                    <input type="hidden" name="tbname" value="news">
                    <input type="hidden" name="Submit" value="搜索">
                    <p class="search_ico"><span></span></p>
                </form>
            </div>
        </div>
    </nav>
</header>

<article>
    <h1 class="t_nav"><a href="/" class="n1">网站首页</a><a href="/" class="n2">${blog.blogSort.sortName}</a></h1>
    <div class="infosbox">
        <div class="newsview">
            <h3 class="news_title">${blog.title}</h3>
            <div class="bloginfo">
                <ul>
                    <li class="author"><a href="/">${blog.author}</a></li>
                    <#if blog.blogSort??>
                        <li class="lmname"><a href="/">${blog.blogSort.sortName}</a></li>
                    </#if>
                    <li class="timer">${(blog.createTime)?string("yyyy-MM-dd hh:mm:ss")}</li>
                    <li class="view">${blog.clickCount}</li>
                    <li class="like">${blog.collectCount}</li>
                </ul>
            </div>
            <div class="tags">
                <#if blog.blogTag??>
                    <#list blog.tagList as tag>
                        <a href="/" target="_blank">${tag.content}</a> &nbsp;
                    </#list>
                </#if>
            </div>
            <div class="news_about"><strong>简介</strong>${blog.summary}</div>
            <div class="news_con ck-content">
                ${blog.content}
            </div>
        </div>
        <div class="share">
            <p class="diggit"><a
                        href="JavaScript:makeRequest('/e/public/digg/?classid=3&amp;id=19&amp;dotop=1&amp;doajax=1&amp;ajaxarea=diggnum','EchoReturnedText','GET','');">
                    很赞哦！ </a>(<b id="diggnum">
                    <script type="text/javascript" src="/e/public/ViewClick/?classid=2&id=20&down=5"></script>
                    13</b>)
            </p>
            <p class="dasbox"><a href="javascript:void(0)" onClick="dashangToggle()" class="dashang" title="打赏，支持一下">打赏本站</a>
            </p>
            <div class="hide_box"></div>
            <div class="shang_box"><a class="shang_close" href="javascript:void(0)" onclick="dashangToggle()"
                                      title="关闭">关闭</a>
                <div class="shang_tit">
                    <p>感谢您的支持，我会继续努力的!</p>
                </div>
                <div class="shang_payimg"><img src="${staticBasePath}/images/alipayimg.jpg" alt="扫码支持" title="扫一扫">
                </div>
                <div class="pay_explain">扫码打赏，你说多少就多少</div>
                <div class="shang_payselect">
                    <div class="pay_item checked" data-id="alipay"><span class="radiobox"></span> <span
                                class="pay_logo"><img src="images/alipay.jpg" alt="支付宝"></span></div>
                    <div class="pay_item" data-id="weipay"><span class="radiobox"></span> <span class="pay_logo"><img
                                    src="images/wechat.jpg" alt="微信"></span></div>
                </div>
                <script type="text/javascript">
                    $(function () {
                        $(".pay_item").click(function () {
                            $(this).addClass('checked').siblings('.pay_item').removeClass('checked');
                            var dataid = $(this).attr('data-id');
                            $(".shang_payimg img").attr("src", "images/" + dataid + "img.jpg");
                            $("#shang_pay_txt").text(dataid == "alipay" ? "支付宝" : "微信");
                        });
                    });

                    function dashangToggle() {
                        $(".hide_box").fadeToggle();
                        $(".shang_box").fadeToggle();
                    }
                </script>
            </div>
        </div>
        <div class="nextinfo">
            <p>上一篇：<a href="/news/life/2018-03-13/804.html">作为一个设计师,如果遭到质疑你是否能恪守自己的原则?</a></p>
            <p>下一篇：<a href="/news/life/">返回列表</a></p>
        </div>
        <div class="otherlink">
            <h2>相关文章</h2>
            <ul>
                <#if sameBlog??>
                    <#list sameBlog as blog>
                        <li><a href="${webBasePath}/freemarker/info/${blog.uid}" title="${blog.title}">${blog.title}</a>
                        </li>
                    </#list>
                </#if>
            </ul>
        </div>
        <div class="news_pl">
            <h2>文章评论</h2>
            <ul>
                <div class="gbko"></div>
            </ul>
        </div>
    </div>
    <div class="sidebar">
        <div class="zhuanti">
            <h2 class="hometitle">特别推荐</h2>
            <ul>
                <#if thirdBlogList??>
                    <#list thirdBlogList as blog>
                        <li>
                            <#if blog.photoList[0]??>
                                <i><img src="${blog.photoList[0]}"></i>
                            </#if>
                            <p>${blog.title} <span><a href="${webBasePath}/freemarker/info/${blog.uid}">阅读</a></span>
                            </p>
                        </li>
                    </#list>
                </#if>
            </ul>
        </div>
        <div class="tuijian">
            <h2 class="hometitle">推荐文章</h2>
            <ul class="tjpic">
                <#if fourthBlogList[0]??>
                    <li>
                        <i><img src="${fourthBlogList[0].photoList[0]}"></i>
                        <p>
                            <a href="${webBasePath}/freemarker/info/${fourthBlogList[0].uid}">${fourthBlogList[0].title}</a>
                        </p>
                    </li>
                </#if>
            </ul>
            <ul class="sidenews">
                <#if fourthBlogList??>
                    <#list fourthBlogList as blog>
                        <#if blog_index != 0 >
                            <li><i><img src="${blog.photoList[0]}"></i>
                                <p><a href="${webBasePath}/freemarker/info/${blog.uid}">${blog.title}</a></p>
                                <span>${(blog.createTime)?string("yyyy-MM-dd hh:mm:ss")}</span></li>
                        </#if>
                    </#list>
                </#if>
            </ul>
        </div>
        <div class="tuijian">
            <h2 class="hometitle">点击排行</h2>
            <ul class="tjpic">
                <#if hotBlogList[0]??>
                    <li>
                        <i><img src="${hotBlogList[0].photoList[0]}"></i>
                        <p><a href="${webBasePath}/freemarker/info/${hotBlogList[0].uid}">${hotBlogList[0].title}</a>
                        </p>
                    </li>
                </#if>
            </ul>
            <ul class="sidenews">
                <#if hotBlogList??>
                    <#list hotBlogList as blog>
                        <#if blog_index != 0 >
                            <li><i><img src="${blog.photoList[0]}"></i>
                                <p><a href="${webBasePath}/freemarker/info/${blog.uid}">${blog.title}</a></p>
                                <span>${(blog.createTime)?string("yyyy-MM-dd hh:mm:ss")}</span></li>
                        </#if>
                    </#list>
                </#if>
            </ul>
        </div>
        <div class="cloud">
            <h2 class="hometitle">标签云</h2>
            <ul>
                <a href="/">陌上花开</a> <a href="/">校园生活</a> <a href="/">html5</a> <a href="/">SumSung</a> <a
                        href="/">青春</a> <a href="/">温暖</a> <a href="/">阳光</a> <a href="/">三星</a><a href="/">索尼</a> <a
                        href="/">华维荣耀</a> <a href="/">三星</a> <a href="/">索尼</a>
            </ul>
        </div>
        <div class="guanzhu" id="gd">
            <h2 class="hometitle">关注我们 么么哒！</h2>
            <ul>
                <li class="sina"><a href="/" target="_blank"><span>新浪微博</span>杨青博客</a></li>
                <li class="tencent"><a href="/" target="_blank"><span>腾讯微博</span>杨青博客</a></li>
                <li class="qq"><a href="/" target="_blank"><span>QQ号</span>476847113</a></li>
                <li class="email"><a href="/" target="_blank"><span>邮箱帐号</span>dancesmiling@qq.com</a></li>
                <li class="wxgzh"><a href="/" target="_blank"><span>微信号</span>yangqq_1987</a></li>
                <li class="wx"><img src="${staticBasePath}/static/images/wx.jpg"></li>
            </ul>
        </div>
    </div>
</article>
<footer>
    <p>Design by <a href="http://www.yangqq.com" target="_blank">杨青个人博客</a> <a href="/">蜀ICP备11002373号-1</a></p>
</footer>
<a href="#" class="cd-top">Top</a>
</body>
<script src="http://apps.bdimg.com/libs/highlight.js/9.1.0/highlight.min.js"></script>
<script>hljs.initHighlightingOnLoad();</script>
<script src="${staticBasePath}/static/js/jquery.min.js"></script>
<script src="${staticBasePath}/static/js/nav.js"></script>
<script src="${staticBasePath}/static/js/scrollReveal.js"></script>
<!--[if lt IE 9]>
<script src="${staticBasePath}/static/js/modernizr.js"></script>
</html>
