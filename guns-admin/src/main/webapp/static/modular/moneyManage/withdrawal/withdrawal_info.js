/**
 * 初始化提现记录列表详情对话框
 */
var WithdrawalInfoDlg = {
    withdrawalInfoData : {}
};

/**
 * 清除数据
 */
WithdrawalInfoDlg.clearData = function() {
    this.withdrawalInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
WithdrawalInfoDlg.set = function(key, val) {
    this.withdrawalInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
WithdrawalInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
WithdrawalInfoDlg.close = function() {
    parent.layer.close(window.parent.Withdrawal.layerIndex);
}

/**
 * 收集数据
 */
WithdrawalInfoDlg.collectData = function() {
    this
    .set('id')
    .set('userId')
    .set('userName')
    .set('account')
    .set('money')
    .set('rate')
    .set('rateMoney')
    .set('realMoney')
    .set('state')
    .set('way')
    .set('adminId')
    .set('auditTime')
    .set('remark')
    .set('orderNum')
    .set('tradeNum')
    .set('createTime')
    .set('updateTime');
}

/**
 * 提交添加
 */
WithdrawalInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/withdrawal/add", function(data){
            Feng.success("添加成功!");
            window.parent.Withdrawal.table.refresh();
            WithdrawalInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(WithdrawalInfoDlg.withdrawalInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
WithdrawalInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/withdrawal/update", function(data){
            Feng.success("修改成功!");
            window.parent.Withdrawal.table.refresh();
            WithdrawalInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(WithdrawalInfoDlg.withdrawalInfoData);
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
                                    message: "请填入"
                                }
                            }
                        },
                        userName: {
                            validators: {
                                notEmpty: {
                                    message: "请填入真实姓名"
                                }
                            }
                        },
                        account: {
                            validators: {
                                notEmpty: {
                                    message: "请填入提现账户/支付宝这里是支付宝账户,银行卡这里是银行卡iD"
                                }
                            }
                        },
                        money: {
                            validators: {
                                notEmpty: {
                                    message: "请填入对应金额"
                                }
                            }
                        },
                        rate: {
                            validators: {
                                notEmpty: {
                                    message: "请填入手续费率"
                                }
                            }
                        },
                        rateMoney: {
                            validators: {
                                notEmpty: {
                                    message: "请填入手续费"
                                }
                            }
                        },
                        realMoney: {
                            validators: {
                                notEmpty: {
                                    message: "请填入实际到账金额"
                                }
                            }
                        },
                        state: {
                            validators: {
                                notEmpty: {
                                    message: "请填入状态 0: 待审核 1:通过 2:进行中 3:驳回"
                                }
                            }
                        },
                        way: {
                            validators: {
                                notEmpty: {
                                    message: "请填入提现方式 0:支付宝 1:银行卡"
                                }
                            }
                        },
                        adminId: {
                            validators: {
                                notEmpty: {
                                    message: "请填入审核人ID"
                                }
                            }
                        },
                        auditTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入审核时间"
                                }
                            }
                        },
                        remark: {
                            validators: {
                                notEmpty: {
                                    message: "请填入审核备注"
                                }
                            }
                        },
                        orderNum: {
                            validators: {
                                notEmpty: {
                                    message: "请填入提现单号"
                                }
                            }
                        },
                        tradeNum: {
                            validators: {
                                notEmpty: {
                                    message: "请填入支付宝交易单号"
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
