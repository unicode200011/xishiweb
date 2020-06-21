/**
 * 初始化家族申请记录详情对话框
 */
var AgentInviteInfoDlg = {
    agentInviteInfoData : {}
};

/**
 * 清除数据
 */
AgentInviteInfoDlg.clearData = function() {
    this.agentInviteInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AgentInviteInfoDlg.set = function(key, val) {
    this.agentInviteInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AgentInviteInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
AgentInviteInfoDlg.close = function() {
    parent.layer.close(window.parent.AgentInvite.layerIndex);
}

/**
 * 收集数据
 */
AgentInviteInfoDlg.collectData = function() {
    this
    .set('id')
    .set('userId')
    .set('createTime')
    .set('agentNum')
    .set('joinState')
    .set('type')
    .set('state');
}

/**
 * 提交添加
 */
AgentInviteInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/agentInvite/add", function(data){
            Feng.success("添加成功!");
            window.parent.AgentInvite.table.refresh();
            AgentInviteInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(AgentInviteInfoDlg.agentInviteInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
AgentInviteInfoDlg.editSubmit = function(state) {
    Feng.confirm("确定要提交审核吗？",function () {
        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/agentInvite/update", function(data){
            Feng.success("修改成功!");
            window.parent.AgentInvite.table.refresh();
            AgentInviteInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("id",$("#id").val());
        ajax.set("state",state);
        ajax.start();
    })
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
                                    message: "请填入被邀请用户id"
                                }
                            }
                        },
                        createTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入邀请时间"
                                }
                            }
                        },
                        agentNum: {
                            validators: {
                                notEmpty: {
                                    message: "请填入家族编号"
                                }
                            }
                        },
                        joinState: {
                            validators: {
                                notEmpty: {
                                    message: "请填入是否同意：0 未处理 1 已同意 2：已拒绝"
                                }
                            }
                        },
                        type: {
                            validators: {
                                notEmpty: {
                                    message: "请填入类型 0：用户申请  1：打理人邀请"
                                }
                            }
                        },
                         state: {
                            validators: {
                                notEmpty: {
                                    message: "请填入状态：0：正常 1：已过期"
                                }
                            }
                        }

            }
        });
    });
});
