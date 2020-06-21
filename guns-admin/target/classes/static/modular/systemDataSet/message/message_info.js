/**
 * 初始化系统消息详情对话框
 */
var MessageInfoDlg = {
    messageInfoData : {}
};

/**
 * 清除数据
 */
MessageInfoDlg.clearData = function() {
    this.messageInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MessageInfoDlg.set = function(key, val) {
    this.messageInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MessageInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MessageInfoDlg.close = function() {
    parent.layer.close(window.parent.Message.layerIndex);
}

/**
 * 收集数据
 */
MessageInfoDlg.collectData = function() {
    this
    .set('id')
    .set('title')
    .set('content')
    .set('msgType')
    .set('recType')
    .set('userId')
    .set('oneLinkId')
    .set('twoLinkId')
    .set('createTime');
}

/**
 * 提交添加
 */
MessageInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/message/add", function(data){
            Feng.success("添加成功!");
            window.parent.Message.table.refresh();
            MessageInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(MessageInfoDlg.messageInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
MessageInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/message/update", function(data){
            Feng.success("修改成功!");
            window.parent.Message.table.refresh();
            MessageInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(MessageInfoDlg.messageInfoData);
        ajax.start();
    }
}
/**
 *  //regexp: {
 *    //regexp: /^\d*$/,
 *   // message: '你输入的fieldName有误'
 *  //}
 */
$(function() {
    $(document).ready(function() {
        $('#validatorForm').bootstrapValidator({
            message: 'This value is not valid',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                        id: {
                            validators: {
                                notEmpty: {
                                    message: "请填入"
                                }
                            }
                        },
                        title: {
                            validators: {
                                notEmpty: {
                                    message: "请填入标题"
                                }
                            }
                        },
                        content: {
                            validators: {
                                notEmpty: {
                                    message: "请填入内容"
                                }
                            }
                        },
                        msgType: {
                            validators: {
                                notEmpty: {
                                    message: "请填入类型: 0 打赏消息 1 礼物消息 2 粉丝消息 3 直播消息 4 系统消息"
                                }
                            }
                        },
                        recType: {
                            validators: {
                                notEmpty: {
                                    message: "请填入接收类型(系统消息有效) 0 全部用户 1 所有用户 2 所有主播 3 自定义用户"
                                }
                            }
                        },
                        userId: {
                            validators: {
                                notEmpty: {
                                    message: "请填入用户ID(系统消息无效)"
                                }
                            }
                        },
                        oneLinkId: {
                            validators: {
                                notEmpty: {
                                    message: "请填入与之关联的用户/直播等"
                                }
                            }
                        },
                        twoLinkId: {
                            validators: {
                                notEmpty: {
                                    message: "请填入与之关联的直播/直播ID"
                                }
                            }
                        },
                         createTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入"
                                }
                            }
                        }

            }
        });
    });
});
