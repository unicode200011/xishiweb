/**
 * 初始化代理商钱包详情对话框
 */
var AgentWalletInfoDlg = {
    agentWalletInfoData : {}
};

/**
 * 清除数据
 */
AgentWalletInfoDlg.clearData = function() {
    this.agentWalletInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AgentWalletInfoDlg.set = function(key, val) {
    this.agentWalletInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AgentWalletInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
AgentWalletInfoDlg.close = function() {
    parent.layer.close(window.parent.AgentWallet.layerIndex);
}

/**
 * 收集数据
 */
AgentWalletInfoDlg.collectData = function() {
    this
    .set('id')
    .set('gbAmount')
    .set('totalGiftAmount')
    .set('agentId')
    .set('createTime')
    .set('updateTime');
}

/**
 * 提交添加
 */
AgentWalletInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/agentWallet/add", function(data){
            Feng.success("添加成功!");
            window.parent.AgentWallet.table.refresh();
            AgentWalletInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(AgentWalletInfoDlg.agentWalletInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
AgentWalletInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/agentWallet/update", function(data){
            Feng.success("修改成功!");
            window.parent.AgentWallet.table.refresh();
            AgentWalletInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(AgentWalletInfoDlg.agentWalletInfoData);
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
                        gbAmount: {
                            validators: {
                                notEmpty: {
                                    message: "请填入账户余额"
                                }
                            }
                        },
                        totalGiftAmount: {
                            validators: {
                                notEmpty: {
                                    message: "请填入收益总额"
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
                        createTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入创建时间"
                                }
                            }
                        },
                         updateTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入更新时间"
                                }
                            }
                        }

            }
        });
    });
});
