/**
 * 初始化主播申请详情对话框
 */
var UserAuthInfoInfoDlg = {
    userAuthInfoInfoData : {}
};

/**
 * 清除数据
 */
UserAuthInfoInfoDlg.clearData = function() {
    this.userAuthInfoInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserAuthInfoInfoDlg.set = function(key, val) {
    this.userAuthInfoInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserAuthInfoInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
UserAuthInfoInfoDlg.close = function() {
    parent.layer.close(window.parent.UserAuthInfo.layerIndex);
}

/**
 * 收集数据
 */
UserAuthInfoInfoDlg.collectData = function() {
    this
    .set('id')
    .set('userId')
    .set('name')
    .set('idCard')
    .set('state')
    .set('bizNo')
    .set('reason')
    .set('createTime')
    .set('updateTime')
    .set('hasPay')
    .set('authTime')
    .set('cardFront')
    .set('cardBack')
    .set('cardHand');
}

/**
 * 提交添加
 */
UserAuthInfoInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/userAuthInfo/add", function(data){
            Feng.success("添加成功!");
            window.parent.UserAuthInfo.table.refresh();
            UserAuthInfoInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(UserAuthInfoInfoDlg.userAuthInfoInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
UserAuthInfoInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/userAuthInfo/update", function(data){
            Feng.success("修改成功!");
            window.parent.UserAuthInfo.table.refresh();
            UserAuthInfoInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(UserAuthInfoInfoDlg.userAuthInfoInfoData);
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
                        name: {
                            validators: {
                                notEmpty: {
                                    message: "请填入姓名"
                                }
                            }
                        },
                        idCard: {
                            validators: {
                                notEmpty: {
                                    message: "请填入身份证"
                                }
                            }
                        },
                        state: {
                            validators: {
                                notEmpty: {
                                    message: "请填入0 待认证 1 认证中 2 已认证 3 认证失败"
                                }
                            }
                        },
                        bizNo: {
                            validators: {
                                notEmpty: {
                                    message: "请填入实名操作业务编号"
                                }
                            }
                        },
                        reason: {
                            validators: {
                                notEmpty: {
                                    message: "请填入实名未通过原因"
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
                        },
                        hasPay: {
                            validators: {
                                notEmpty: {
                                    message: "请填入是否已支付,0--未支付，1--已支付"
                                }
                            }
                        },
                        authTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入实名认证次数"
                                }
                            }
                        },
                        cardFront: {
                            validators: {
                                notEmpty: {
                                    message: "请填入身份证正面"
                                }
                            }
                        },
                        cardBack: {
                            validators: {
                                notEmpty: {
                                    message: "请填入身份证反面"
                                }
                            }
                        },
                         cardHand: {
                            validators: {
                                notEmpty: {
                                    message: "请填入手持身份证"
                                }
                            }
                        }

            }
        });
    });
});
