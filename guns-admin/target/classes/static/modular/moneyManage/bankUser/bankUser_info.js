/**
 * 初始化银行账户详情对话框
 */
var BankUserInfoDlg = {
    bankUserInfoData : {}
};

/**
 * 清除数据
 */
BankUserInfoDlg.clearData = function() {
    this.bankUserInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BankUserInfoDlg.set = function(key, val) {
    this.bankUserInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BankUserInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BankUserInfoDlg.close = function() {
    parent.layer.close(window.parent.BankUser.layerIndex);
}

/**
 * 收集数据
 */
BankUserInfoDlg.collectData = function() {
    this
    .set('id')
    .set('bankAccountId')
    .set('userId')
    .set('bankCard')
    .set('qrcode')
    .set('createTime')
    .set('type');
}

/**
 * 提交添加
 */
BankUserInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/bankUser/add", function(data){
            Feng.success("添加成功!");
            window.parent.BankUser.table.refresh();
            BankUserInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(BankUserInfoDlg.bankUserInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
BankUserInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/bankUser/update", function(data){
            Feng.success("修改成功!");
            window.parent.BankUser.table.refresh();
            BankUserInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(BankUserInfoDlg.bankUserInfoData);
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
                        bankAccountId: {
                            validators: {
                                notEmpty: {
                                    message: "请填入账户分类id"
                                }
                            }
                        },
                        userId: {
                            validators: {
                                notEmpty: {
                                    message: "请填入用户id"
                                }
                            }
                        },
                        bankCard: {
                            validators: {
                                notEmpty: {
                                    message: "请填入卡号信息"
                                }
                            }
                        },
                        qrcode: {
                            validators: {
                                notEmpty: {
                                    message: "请填入二维码"
                                }
                            }
                        },
                        createTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入"
                                }
                            }
                        },
                         type: {
                            validators: {
                                notEmpty: {
                                    message: "请填入类型0：用户账号 1：代理商账号"
                                }
                            }
                        }

            }
        });
    });
});
