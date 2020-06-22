/**
 * 初始化账户分类详情对话框
 */
var BankAccountInfoDlg = {
    bankAccountInfoData : {}
};

/**
 * 清除数据
 */
BankAccountInfoDlg.clearData = function() {
    this.bankAccountInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BankAccountInfoDlg.set = function(key, val) {
    this.bankAccountInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
BankAccountInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
BankAccountInfoDlg.close = function() {
    parent.layer.close(window.parent.BankAccount.layerIndex);
}

/**
 * 收集数据
 */
BankAccountInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('acount')
    .set('code')
    .set('createTime');
}

/**
 * 提交添加
 */
BankAccountInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/bankAccount/add", function(data){
            Feng.success("添加成功!");
            window.parent.BankAccount.table.refresh();
            BankAccountInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(BankAccountInfoDlg.bankAccountInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
BankAccountInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/bankAccount/update", function(data){
            Feng.success("修改成功!");
            window.parent.BankAccount.table.refresh();
            BankAccountInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(BankAccountInfoDlg.bankAccountInfoData);
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
                                    message: "请填入编号"
                                }
                            }
                        },
                        name: {
                            validators: {
                                notEmpty: {
                                    message: "请填入账户类型"
                                }
                            }
                        },
                        acount: {
                            validators: {
                                notEmpty: {
                                    message: "请填入账户号码"
                                }
                            }
                        },
                        code: {
                            validators: {
                                notEmpty: {
                                    message: "请填入账户二维码"
                                }
                            }
                        },
                         createTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入创建事件"
                                }
                            }
                        }

            }
        });
    });
});
