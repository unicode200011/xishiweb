/**
 * 初始化举报列表详情对话框
 */
var ComplainInfoDlg = {
    complainInfoData : {}
};

/**
 * 清除数据
 */
ComplainInfoDlg.clearData = function() {
    this.complainInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ComplainInfoDlg.set = function(key, val) {
    this.complainInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ComplainInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ComplainInfoDlg.close = function() {
    parent.layer.close(window.parent.Complain.layerIndex);
}

/**
 * 收集数据
 */
ComplainInfoDlg.collectData = function() {
    this
    .set('id')
    .set('userId')
    .set('linkUid')
    .set('reason')
    .set('userName')
    .set('linkUname')
    .set('createTime');
}

/**
 * 提交添加
 */
ComplainInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/complain/add", function(data){
            Feng.success("添加成功!");
            window.parent.Complain.table.refresh();
            ComplainInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(ComplainInfoDlg.complainInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
ComplainInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/complain/update", function(data){
            Feng.success("修改成功!");
            window.parent.Complain.table.refresh();
            ComplainInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(ComplainInfoDlg.complainInfoData);
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
                        userId: {
                            validators: {
                                notEmpty: {
                                    message: "请填入举报用户id"
                                }
                            }
                        },
                        linkUid: {
                            validators: {
                                notEmpty: {
                                    message: "请填入被举报用户id"
                                }
                            }
                        },
                        reason: {
                            validators: {
                                notEmpty: {
                                    message: "请填入举报理由"
                                }
                            }
                        },
                        userName: {
                            validators: {
                                notEmpty: {
                                    message: "请填入举报者用户名"
                                }
                            }
                        },
                        linkUname: {
                            validators: {
                                notEmpty: {
                                    message: "请填入被举报者用户名"
                                }
                            }
                        },
                         createTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入举报时间"
                                }
                            }
                        }

            }
        });
    });
});
