<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="/relianba/css/download.css">
    <title>推荐下载</title>
</head>
<body>
<div id="content">
    <div class="content">
        <div class="logo">
            <p><img src="/relianba/images/logo.png" alt="" width="100%" height="100%"></p>
            <span>高颜质美女一对一视频，1分钟找到附近美女<br />10分钟告别单身，同城约会更方便.</span>
        </div>
        <div class="box_img"><img src="/relianba/images/pic_phone.png" alt="" width="100%" height="100%"></div>
        <div class="btn"><img src="/relianba/images/btn_ljxz.png" alt="" width="100%" height="100%"></div>
    </div>
</div>
<div id="weixin">
    <img src="/relianba/images/img_jt.png" alt="">
    <p class="p1"><span>1</span> 请点击上面右上角"···"</p>
    <p class="p2"><span>2</span>选择在浏览器中打开</p>
</div>
</body>
<script src="/relianba/js/jquery-1.7.2.min.js"></script>
<script>
    window.onresize = document.documentElement.style.fontSize = document.documentElement.clientWidth / 7.5 + 'px';
    var browser = {
        versions: function () {
            var u = navigator.userAgent, app = navigator.appVersion;
            return {//移动终端浏览器版本信息
                trident: u.indexOf('Trident') > -1, //IE内核
                presto: u.indexOf('Presto') > -1, //opera内核
                webKit: u.indexOf('AppleWebKit') > -1, //苹果、谷歌内核
                gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, //火狐内核
                mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), //是否为移动终端
                ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), //ios终端
                android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, //android终端或者uc浏览器
                iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, //是否为iPhone或者QQHD浏览器
                iPad: u.indexOf('iPad') > -1, //是否iPad
                webApp: u.indexOf('Safari') == -1 //是否web应该程序，没有头部与底部
            };
        }(),
        language: (navigator.browserLanguage || navigator.language).toLowerCase()
    };
    //判断是否是微信浏览器的函数
    function isWeiXin(){
        //window.navigator.userAgent属性包含了浏览器类型、版本、操作系统类型、浏览器引擎类型等信息，这个属性可以用来判断浏览器类型
        var ua = window.navigator.userAgent.toLowerCase();
        //通过正则表达式匹配ua中是否含有MicroMessenger字符串
        if(ua.match(/MicroMessenger/i) == 'micromessenger'){
            return true;
        }else{
            return false;
        }
    }
    if(isWeiXin()){
        $("#content").hide();
        $("#weixin").show();
    }else{
        $("#content").show();
        $("#weixin").hide();
    }
    var oHeight = $("#content").height();
    var oWin =  $(window).height();
    if(oHeight < oWin){
        $("#content").css({
            "position": "fixed",
            "top": 0,
            "left": 0,
            "right": 0,
            "bottom": 0,
        });
    }
    function isWeiXin() {
        var ua = window.navigator.userAgent.toLowerCase();
        if (ua.match(/MicroMessenger/i) == 'micromessenger') {
            return true;
        } else {
            return false;
        }
    }
    // 下载按钮
    var andAppUrl = '${android}';
    var IOSAppUrl = '${ios}';
    // 下载按钮
    $("#content .content .btn").click(function () {
        if (browser.versions.ios || browser.versions.iPhone || browser.versions.iPad) {
            window.location.href = IOSAppUrl;
        } else if (browser.versions.android) {
            window.location.href = andAppUrl;
        }
    });
    // 下载按钮
</script>
</html>