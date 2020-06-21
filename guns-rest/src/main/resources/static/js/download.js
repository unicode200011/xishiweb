var a_app = "douniu://jp.app/openwith";
var i_app = "douniu://";

$(function () {

    var btn_download = $("#btn_download");
    var ua = window.navigator.userAgent.toLowerCase();
    console.log(ua)
    var isweixin = navigator.userAgent.toLowerCase().match(/micromessenger/i) != null;
    var isqq = navigator.userAgent.toLowerCase().indexOf("qq/") > -1;
    var isweibo = ua.match(/weibo/i) == "weibo";
    if (isweixin || isweibo) {
        $('.downApp').click(function () {
            $('.isweixin-top').removeClass('hide');
        });
        if (/android/i.test(navigator.userAgent)) { //如果是Android微信浏览器
            $('.isweixin-top').find('.wxword').text('点击右上角，选择“在浏览器中打开”');
        } else if (/ipad|iphone|mac/i.test(navigator.userAgent)) { //如果是ios微信浏览器
            $('.isweixin-top').find('.wxword').text('点击右上角，选择“在safari中打开”');
        }
    } else {
        if (/android/i.test(navigator.userAgent)) {
            if (!isweixin && !isqq) {
                btn_download.attr("href", a_app);
                btn_download.click(function () {
                    openandIndex()
                })
            } else if (isqq) {
                // btn_android.removeClass('hide');
                $('.isweixin-top').find('.wxword').text('点击右上角，选择“用浏览器打开”');
                btn_download.click(function () {
                    $('.isweixin-top').removeClass('hide');
                    $('.isweixin-top').css('background-color', '#12b7f5');
                })
            }
        } else if (/ipad|iphone|mac/i.test(navigator.userAgent)) {
            if (!isweixin && !isqq) {
                // btn_ios.removeClass('hide');
                btn_download.click(function () {
                    window.location.href = i_app;
                    openiosIndex()
                })
            } else if (isqq) {
                // btn_ios.removeClass('hide');
                btn_download.attr('href', i_app);
                btn_download.click(function () {
                    openiosIndex()
                });
            }
        }
    }

})

function openandIndex() {
    var t = setTimeout(function () {
        // window.location.href = "http://a.app.qq.com/o/simple.jsp?pkgname=yilanTech.EduYunClient"    //APP下载地址
        window.location.href = "https://fir.im/laj3"    //APP下载地址
    }, 3000);
    setTimeout(function () {
        clearTimeout(t)
    }, 3500)
}

function openiosIndex() {
    var t = setTimeout(function () {
        // window.location.href = "https://itunes.apple.com/cn/app/shi-ji-shou-hu/id1067211489?l=zh&mt=8"    //您的APP下载地址，这里只是举个例子
        window.location.href = "https://www.pgyer.com/5DE6"    //您的APP下载地址，这里只是举个例子
    }, 1500);
    setTimeout(function () {
        clearTimeout(t)
    }, 1500)
}