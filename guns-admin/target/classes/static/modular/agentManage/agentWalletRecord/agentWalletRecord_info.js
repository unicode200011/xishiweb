/**
 * 初始化代理商钱包记录详情对话框
 */
var AgentWalletRecordInfoDlg = {
    agentWalletRecordInfoData : {}
};

/**
 * 清除数据
 */
AgentWalletRecordInfoDlg.clearData = function() {
    this.agentWalletRecordInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AgentWalletRecordInfoDlg.set = function(key, val) {
    this.agentWalletRecordInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AgentWalletRecordInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
AgentWalletRecordInfoDlg.close = function() {
    parent.layer.close(window.parent.AgentWalletRecord.layerIndex);
}

/**
 * 收集数据
 */
AgentWalletRecordInfoDlg.collectData = function() {
    this
    .set('id')
    .set('agentId')
    .set('showerId')
    .set('type')
    .set('io')
    .set('remark')
    .set('linkUid')
    .set('walletAmount')
    .set('custId')
    .set('custNum')
    .set('custName')
    .set('liveRecordId')
    .set('createTime');
}

/**
 * 提交添加
 */
AgentWalletRecordInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/agentWalletRecord/add", function(data){
            Feng.success("添加成功!");
            window.parent.AgentWalletRecord.table.refresh();
            AgentWalletRecordInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(AgentWalletRecordInfoDlg.agentWalletRecordInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
AgentWalletRecordInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/agentWalletRecord/update", function(data){
            Feng.success("修改成功!");
            window.parent.AgentWalletRecord.table.refresh();
            AgentWalletRecordInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(AgentWalletRecordInfoDlg.agentWalletRecordInfoData);
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
                        agentId: {
                            validators: {
                                notEmpty: {
                                    message: "请填入代理商id"
                                }
                            }
                        },
                        showerId: {
                            validators: {
                                notEmpty: {
                                    message: "请填入关联主播id"
                                }
                            }
                        },
                        type: {
                            validators: {
                                notEmpty: {
                                    message: "请填入收益类型 2.获得礼物+ 3：观看常规收费直播 4观看计时收费直播 5:看电影 6：坐骑 7：靓号8：提现"
                                }
                            }
                        },
                        io: {
                            validators: {
                                notEmpty: {
                                    message: "请填入收支类型：0 ：收入 1：支出"
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
                                    message: "请填入插入记录时代理商钱包余额"
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
                        },
                        liveRecordId: {
                            validators: {
                                notEmpty: {
                                    message: "请填入直播记录id"
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
