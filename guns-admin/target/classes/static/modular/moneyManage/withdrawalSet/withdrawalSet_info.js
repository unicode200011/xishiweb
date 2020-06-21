/**
 * 初始化提现设置详情对话框
 */
var WithdrawalSetInfoDlg = {
    withdrawalSetInfoData : {}
};

/**
 * 清除数据
 */
WithdrawalSetInfoDlg.clearData = function() {
    this.withdrawalSetInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
WithdrawalSetInfoDlg.set = function(key, val) {
    this.withdrawalSetInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
WithdrawalSetInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
WithdrawalSetInfoDlg.close = function() {
    parent.layer.close(window.parent.WithdrawalSet.layerIndex);
}

/**
 * 收集数据
 */
WithdrawalSetInfoDlg.collectData = function() {
    this
    .set('id')
    .set('gbMoney')
    .set('remark')
    .set('updateTime');
}

/**
 * 提交添加
 */
WithdrawalSetInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/withdrawalSet/add", function(data){
            Feng.success("添加成功!");
            window.parent.WithdrawalSet.table.refresh();
            WithdrawalSetInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(WithdrawalSetInfoDlg.withdrawalSetInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
WithdrawalSetInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/withdrawalSet/update", function(data){
            Feng.success("修改成功!");
            window.parent.WithdrawalSet.table.refresh();
            WithdrawalSetInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(WithdrawalSetInfoDlg.withdrawalSetInfoData);
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
                        gbMoney: {
                            validators: {
                                notEmpty: {
                                    message: "请填入提现西施币"
                                },
                                regexp: {
                                    regexp: /^(0|\+?[1-9][0-9]{0,5})$/,
                                    message: "提现西施币范围0-999999"
                                }
                            }
                        },
                        remark: {
                            validators: {
                                notEmpty: {
                                    message: "请填入"
                                }
                            }
                        },
                         updateTime: {
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
