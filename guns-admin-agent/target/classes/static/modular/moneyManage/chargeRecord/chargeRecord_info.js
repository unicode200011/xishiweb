/**
 * 初始化充值记录列表详情对话框
 */
var ChargeRecordInfoDlg = {
    chargeRecordInfoData : {}
};

/**
 * 清除数据
 */
ChargeRecordInfoDlg.clearData = function() {
    this.chargeRecordInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ChargeRecordInfoDlg.set = function(key, val) {
    this.chargeRecordInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ChargeRecordInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ChargeRecordInfoDlg.close = function() {
    parent.layer.close(window.parent.ChargeRecord.layerIndex);
}

/**
 * 收集数据
 */
ChargeRecordInfoDlg.collectData = function() {
    this
    .set('id')
    .set('userId')
    .set('rmbAmount')
    .set('xishiAmount')
    .set('walletAmount')
    .set('source')
    .set('createTime');
}

/**
 * 提交添加
 */
ChargeRecordInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/chargeRecord/add", function(data){
            Feng.success("添加成功!");
            window.parent.ChargeRecord.table.refresh();
            ChargeRecordInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(ChargeRecordInfoDlg.chargeRecordInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
ChargeRecordInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/chargeRecord/update", function(data){
            Feng.success("修改成功!");
            window.parent.ChargeRecord.table.refresh();
            ChargeRecordInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(ChargeRecordInfoDlg.chargeRecordInfoData);
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
                                    message: "请填入充值用户"
                                }
                            }
                        },
                        rmbAmount: {
                            validators: {
                                notEmpty: {
                                    message: "请填入充值人民币金额"
                                }
                            }
                        },
                        xishiAmount: {
                            validators: {
                                notEmpty: {
                                    message: "请填入西施币数量"
                                }
                            }
                        },
                        walletAmount: {
                            validators: {
                                notEmpty: {
                                    message: "请填入充值后钱包余额"
                                }
                            }
                        },
                        source: {
                            validators: {
                                notEmpty: {
                                    message: "请填入充值途径 0微信 1支付宝"
                                }
                            }
                        },
                         createTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入充值时间"
                                }
                            }
                        }

            }
        });
    });
});
