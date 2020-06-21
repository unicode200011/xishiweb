/**
 * 初始化直播管理详情对话框
 */
var LiveRecordInfoDlg = {
    liveRecordInfoData : {}
};
/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
LiveRecordInfoDlg.set = function(key, val) {
    this.liveRecordInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
LiveRecordInfoDlg.get = function(key) {
    return $("#" + key).val();
}
/**
 * 发送消息
 */
LiveRecordInfoDlg.sendMsg = function() {
    $("#giftform").bootstrapValidator('validate');//提交验证
    if ($("#giftform").data('bootstrapValidator').isValid()) {//获取验证结果，如果成功，执行下面代码
        var userText = $("#userText").val()+"";
        var type = $("#type").val()
        var msg = $("#msg").val()
        var tital = $("#tital").val()

        if(type == 0 && userText == ""){
            Feng.error("请填写接受对象!")
            return
        }
        LiveRecordInfoDlg.set("users",userText)
        LiveRecordInfoDlg.set("type",1)
        LiveRecordInfoDlg.set("msg",msg)
        LiveRecordInfoDlg.set("tital",tital)
        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/message/doSendMsg",
            function (data) {
                Feng.success("发送成功!");
                parent.layer.close(window.parent.Message.layerIndex);
                window.parent.Message.table.refresh();
            },
            function (data) {
                Feng.error("未找到自定义对象，发送失败!");
            })
        ajax.set(LiveRecordInfoDlg.liveRecordInfoData)
        ajax.start()
    }
}

$(function() {
    $("#giftform").bootstrapValidator({
        live: 'disabled',//验证时机，enabled是内容有变化就验证（默认），disabled和submitted是提交再验证
        excluded: [':disabled', ':hidden', ':not(:visible)'],//排除无需验证的控件，比如被禁用的或者被隐藏的
        submitButtons: '#btn-test',//指定提交按钮，如果验证失败则变成disabled，但我没试成功，反而加了这句话非submit按钮也会提交到action指定页面
        message: '通用的验证失败消息',//好像从来没出现过
        feedbackIcons: {//根据验证结果显示的各种图标
            valid: 'glyphicon glyphicon-ok',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            msg: {
                validators: {
                    notEmpty: {//检测非空,radio也可用
                        message: '*请填写消息内容'
                    },stringLength: {
                        min: 1,
                        max: 255,
                        message: '消息内容字数必须在1到255之间'
                    }

                }
            },
            type: {
                validators: {
                    notEmpty: {//检测非空,radio也可用
                        message: '*请选择对象'
                    }
                }
            },
            tital:{
                validators: {
                    notEmpty: {//检测非空,radio也可用
                        message: '*请填写标题'
                    }
                }
            }
        }
    });
});
