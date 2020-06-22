/**
 * 初始化代理商处理记录详情对话框
 */
var AgentDealRecordInfoDlg = {
    agentDealRecordInfoData : {}
};

/**
 * 清除数据
 */
AgentDealRecordInfoDlg.clearData = function() {
    this.agentDealRecordInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AgentDealRecordInfoDlg.set = function(key, val) {
    this.agentDealRecordInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AgentDealRecordInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
AgentDealRecordInfoDlg.close = function() {
    parent.layer.close(window.parent.AgentDealRecord.layerIndex);
}

/**
 * 收集数据
 */
AgentDealRecordInfoDlg.collectData = function() {
    this
    .set('id')
    .set('agentId')
    .set('adminId')
    .set('remark')
    .set('createTime')
    .set('updateTime');
}

/**
 * 提交添加
 */
AgentDealRecordInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/agentDealRecord/add", function(data){
            Feng.success("添加成功!");
            window.parent.AgentDealRecord.table.refresh();
            AgentDealRecordInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(AgentDealRecordInfoDlg.agentDealRecordInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
AgentDealRecordInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/agentDealRecord/update", function(data){
            Feng.success("修改成功!");
            window.parent.AgentDealRecord.table.refresh();
            AgentDealRecordInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(AgentDealRecordInfoDlg.agentDealRecordInfoData);
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
                        adminId: {
                            validators: {
                                notEmpty: {
                                    message: "请填入处理人"
                                }
                            }
                        },
                        remark: {
                            validators: {
                                notEmpty: {
                                    message: "请填入处理备注"
                                }
                            }
                        },
                        createTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入申请时间"
                                }
                            }
                        },
                         updateTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入处理时间"
                                }
                            }
                        }

            }
        });
    });
});
