/**
 * Created by Administrator on 2018/9/7.
 */
$(".greenBtn").click(function(e){
    openApp(e);
});

function openApp(e){
    var u = window.navigator.userAgent;
    var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器
    var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/);
    if(isAndroid){
        android();
    }
    if(isiOS){
        ios();
    }
}
function android(){
    window.location.href = "打开APP的协议"; /***打开app的协议，有安卓同事提供***/
    window.setTimeout(function(){
        window.location.href = "下载APP的地址"; /***Android移动端下载地址***/
    },2000);
}

function ios(){
    var ifr = document.createElement("iframe");
    ifr.src = "打开APP的协议"; /***打开app的协议，有ios同事提供***/
    ifr.style.display = "none";
    document.body.appendChild(ifr);
    window.setTimeout(function(){
        document.body.removeChild(ifr);
        window.location.href = "下载APP的地址"; /***下载app的地址***/
    },2000)
}
