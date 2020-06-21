<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport"
          content="width=device-width,height=device-height,initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>免费注册</title>
    <script src="https://cdn.bootcss.com/jquery/1.7.2/jquery.min.js"></script>
    <script src="https://cdn.bootcss.com/layer/2.3/layer.js"></script>
    <style type="text/css">
        body {
            background-color: #F7F7F7;
            margin: 0;
            padding: 0;
            height: 100%;
            width: 100%;
        }

        .header {
            background-color: #ffffff;
            margin: 15px;
            border-radius: 5px;
        }

        .logoDiv {

            border-bottom: 1px solid #EBEBEB;
            height: 77px;
        }

        .logoDiv img {
            float: left;
            width: 47px;
            padding: 15px;
        }

        .logoDiv label {
            font-size: 16px;
            color: #222222;
            line-height: 77px;
            display: block;
            float: left;
        }

        .codeDiv {
            padding: 20px;
        }

        .codeDiv p {
            margin: 0px;
            text-align: center;
        }

        .codeText {
            font-size: 20px;
            color: #222222;
            font-weight: bold;
        }

        .codeValue {
            font-size: 23px;
            color: #FC4286;
            font-weight: bold;
            margin-top: 15px !important;
        }

        .codeP {
            font-size: 14px;
            color: #222222;
        }

        .codeDiv img {
            margin: 20px 0px;
            width: 100%;
        }

        .content {
            background-color: #ffffff;
            padding: 0px 15px;
        }

        .content div {
            /*width: 100%;*/
            padding-top: 16px;
        }

        .content input {
            border: 0px;
            background-color: #f6f6f6;
            font-size: 18px;
            height: 50px;
            line-height: 50px;
            border-radius: 5px;
            color: #B3B3B3;
            width: 100%;
            padding-left: 26px;
            box-sizing: border-box;
        / / 其他浏览器 -webkit-box-sizing: border-box;
        / / 谷歌 -moz-box-sizing: border-box;
        / / 火
        }

        .content input::-webkit-input-placeholder {
            /*padding-left: 26px;*/
            color: #B3B3B3;
            font-size: 18px;
        }

        .content .code {
            width: 60% !important;
            margin-right: 20px;
        }

        .content .getCode {
            width: 30%;
            border: 0px;
            background-color: #f6f6f6;
            font-size: 15px;
            height: 50px;
            line-height: 50px;
            border-radius: 5px;
            color: #B3B3B3;
            float: right;
        }

        .finishDiv {
            background-color: #FC4286;
            font-size: 17px;
            color: #5A4712;
            font-weight: bold;
            text-align: center;
            line-height: 45px;
            padding: 0px !important;
            border-radius: 5px;
            margin-bottom: 29px;
        }

        .contentHttp {
            height: 17px;
            padding-bottom: 40px;
        }

        .contentHttp img {
            width: 17px !important;
            float: left;
            padding-right: 5px;
        }

        .contentHttp label {
            color: #FC4286;
            font-size: 13px;
            float: left;
        }
    </style>
</head>
<body>
<div id="xy" style="display: none;">
    <div style="width: 95%;padding: 3%;">${userXy}</div>
</div>
<div class="header">
    <div class="logoDiv">
        <img src="/img/rlbLogo.png">
        <label>西施</label>
    </div>
    <div class="codeDiv">
        <p class="codeText">邀请码</p>
        <p class="codeValue">${code}</p>
    <#--<img src="/share/image/banner.png">-->
        <p class="codeP">您的好友“${user.name}”邀请你一起加入西施</p>
    </div>
</div>
<div class="content">
    <input type="hidden" value="${domain}" id="domain">
    <div>
        <input type="tel" placeholder="请输入手机号" class="phone">
    </div>
    <div>
        <input type="tel" placeholder="请输入验证码" class="code">
        <input class="getCode" type="button" id="btn" value="获取验证码" onclick="getCode(this)"/>
    </div>
    <div>
        <input type="password" placeholder="请输入密码" class="password">
    </div>
    <div class="contentHttp">
        <img src="/img/icon_xz@3x.png">
        <label onclick="showXy()">我同意《西施APP用户协议》</label>
    </div>
    <div class="finishDiv" onclick="submitData()">完成</div>
</div>
<div id="getcode"></div>
<script src="/js/module/download.js"></script>
<script src="https://cdn.dingxiang-inc.com/ctu-group/captcha-ui/index.js"></script>
<script type="text/javascript">
    var phoneReg = /^1[3|4|5|6|9|8|7][0-9]\d{4,8}$/;
    var countdown = 59;
    var andAppUrl = '${android}';
    var IOSAppUrl = '${ios}';
    // var andAppUrl = 'https://www.xrkapp.com/n7ZR';
    // var IOSAppUrl = 'https://www.xrkapp.com/n7ZR';
    var xy = $("#xy").html();

    function showXy() {
        var w = (document.documentElement.clientWidth || document.body.clientWidth);
        var h = (document.documentElement.clientHeight || document.body.clientHeight);
        layer.open({
            type: 1,
            title: "",
            skin: 'layui-layer-rim', //加上边框
            area: [(w - 50) + "px", (h - 80) + "px"], //宽高
            content: xy
        });
    }

    function submitData() {
        var phone = $(".phone").val();
        if (phone === "") {
            layer.msg("请输入手机号");
            return;
        }
        if (!phoneReg.test(phone)) {
            layer.msg("请输入正确的手机号");
            return;
        }
        var code = $(".code").val();
        if (code === "") {
            layer.msg("请输入验证码");
            return;
        }
        var password = $(".password").val();
        if (password === "") {
            layer.msg("请输入密码");
            return;
        }
        var param = {
            "account": phone,
            "code": code,
            "pwd": password,
            "inviteCode": "${code}"
        };
        sendData(param);
    }

    function sendData(param) {
        $.ajax({
            url: $("#domain").val() + "/account/inviteRegister",
            type: "post",
            // contentType: "application/json",
            data: param,
            success: function (result) {
                if (result.code === "200") {
                    layer.msg("注册成功");
                    if (isAndroid_ios()) {
                        window.location.href = andAppUrl
                    } else {
                        window.location.href = IOSAppUrl
                    }
                } else {
                    var msg = result.msg
                    layer.msg(msg);

                }
            }, error: function () {

            }
        })
    }

    // var appId = '4adfa417f53837ebef9bcc9929a2acf4';
    // var Input = '';
    // var myCaptcha = _dx.Captcha(document.getElementById('getcode'), {
    //     appId: appId,   //appId,开通服务后可在控制台中“服务管理”模块获取
    //     style: 'popup',
    //     success: function (token) {
    //         // console.log(token);
    //         var phone = $(".phone").val();

    //
    //         myCaptcha.hide();
    //     }
    // })

    function getCode(val) {
        Input = val
        var phone = $(".phone").val();
        if (phone === "") {
            layer.msg("请输入手机号");
            return;
        }
        if (!phoneReg.test(phone)) {
            layer.msg("请输入正确的手机号");
            return;
        }
        var datas = {
            "account": phone,
            "type": "0",
        }
        $.ajax({
            url: "http://api.hksm.video/account/sms",
            type: "post",
            // contentType: "application/json",
            data: datas,
            success: function (result) {
                if (result.code !== "200") {
                    var msg = result.msg
                    layer.msg(msg);
                    if(msg === "该手机号已注册"){
                        setTimeout(function () {
                            if (isAndroid_ios()) {
                                window.location.href = andAppUrl
                            } else {
                                window.location.href = IOSAppUrl
                            }
                        },1000)
                    }
                } else {
                    settime(Input);
                    layer.msg(result.msg);
                }
            }
        });
    }

    function settime(val) {
        if (countdown >= 0) {
            val.setAttribute("disabled", true);
            val.value = "重新发送(" + countdown + ")";
            countdown--;
            setTimeout(function () {
                settime(val)
            }, 1000)
        } else {
            val.removeAttribute("disabled");
            val.value = "获取验证码";
            countdown = 59;
        }
    }

    //判断是否是安卓还是ios  
    function isAndroid_ios() {
        var u = navigator.userAgent, app = navigator.appVersion;
        var isAndroid = u.indexOf('Android') > -1 || u.indexOf('Linux') > -1; //android终端或者uc浏览器  
        var isiOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端  
        return isAndroid == true ? true : false;
    }

</script>
</body>
</html>