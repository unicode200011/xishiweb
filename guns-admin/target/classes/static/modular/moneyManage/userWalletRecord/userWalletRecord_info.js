/**
 * 初始化消费记录列表详情对话框
 */
var UserWalletRecordInfoDlg = {
    userWalletRecordInfoData : {}
};

/**
 * 清除数据
 */
UserWalletRecordInfoDlg.clearData = function() {
    this.userWalletRecordInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserWalletRecordInfoDlg.set = function(key, val) {
    this.userWalletRecordInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserWalletRecordInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
UserWalletRecordInfoDlg.close = function() {
    parent.layer.close(window.parent.UserWalletRecord.layerIndex);
}

/**
 * 收集数据
 */
UserWalletRecordInfoDlg.collectData = function() {
    this
    .set('id')
    .set('userId')
    .set('amount')
    .set('useType')
    .set('type')
    .set('remark')
    .set('createTime')
    .set('linkUid')
    .set('walletAmount')
    .set('custId')
    .set('custNum')
    .set('custName');
}

/**
 * 提交添加
 */
UserWalletRecordInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/userWalletRecord/add", function(data){
            Feng.success("添加成功!");
            window.parent.UserWalletRecord.table.refresh();
            UserWalletRecordInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(UserWalletRecordInfoDlg.userWalletRecordInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
UserWalletRecordInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/userWalletRecord/update", function(data){
            Feng.success("修改成功!");
            window.parent.UserWalletRecord.table.refresh();
            UserWalletRecordInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(UserWalletRecordInfoDlg.userWalletRecordInfoData);
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
                        amount: {
                            validators: {
                                notEmpty: {
                                    message: "请填入消费总金额"
                                }
                            }
                        },
                        useType: {
                            validators: {
                                notEmpty: {
                                    message: "请填入收支方式:0-收入 1-支出"
                                }
                            }
                        },
                        type: {
                            validators: {
                                notEmpty: {
                                    message: "请填入消费类型:  
0.充值+  
1.送礼物-  2.获得礼物+ 3：观看常规收费直播 4观看计时收费直播 5:看电影 6：坐骑 7：靓号"
                                }
                            }
                        },
                        remark: {
                            validators: {
                                notEmpty: {
                                    message: "请填入备注"
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
                        linkUid: {
                            validators: {
                                notEmpty: {
                                    message: "请填入收支对象 用户id"
                                }
                            }
                        },
                        walletAmount: {
                            validators: {
                                notEmpty: {
                                    message: "请填入插入记录时用户钱包余额"
                                }
                            }
                        },
                        custId: {
                            validators: {
                                notEmpty: {
                                    message: "请填入送礼物时 保存的礼物id  直播时的 直播间id"
                                }
                            }
                        },
                        custNum: {
                            validators: {
                                notEmpty: {
                                    message: "请填入消费数量 礼物个数 计时模式 分钟数"
                                }
                            }
                        },
                         custName: {
                            validators: {
                                notEmpty: {
                                    message: "请填入消费的对象名字 如礼物名  直播间名字"
                                }
                            }
                        }

            }
        });
    });
});
