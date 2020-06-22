/**
 * 初始化充值列表详情对话框
 */
var ChargeListInfoDlg = {
    chargeListInfoData : {}
};

/**
 * 清除数据
 */
ChargeListInfoDlg.clearData = function() {
    this.chargeListInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ChargeListInfoDlg.set = function(key, val) {
    this.chargeListInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ChargeListInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ChargeListInfoDlg.close = function() {
    parent.layer.close(window.parent.ChargeList.layerIndex);
}

/**
 * 收集数据
 */
ChargeListInfoDlg.collectData = function() {
    this
    .set('id')
    .set('xishiNum')
    .set('rmb')
    .set('createTime')
    .set('rate')
    .set('remark');
}

/**
 * 提交添加
 */
ChargeListInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/chargeList/add", function(data){
            Feng.success("添加成功!");
            window.parent.ChargeList.table.refresh();
            ChargeListInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(ChargeListInfoDlg.chargeListInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
ChargeListInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/chargeList/update", function(data){
            Feng.success("修改成功!");
            window.parent.ChargeList.table.refresh();
            ChargeListInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(ChargeListInfoDlg.chargeListInfoData);
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
                        xishiNum: {
                            validators: {
                                notEmpty: {
                                    message: "请填入西施币数量"
                                },
                                regexp: {
                                    regexp: /^[0-9]{0,5}$/,
                                    message: '请输入正确的西施币数量'
                                }
                            }
                        },
                        rmb: {
                            validators: {
                                notEmpty: {
                                    message: "请填入所需人民币"
                                },
                                regexp: {
                                    regexp: /^[0-9]{0,5}$/,
                                    message: '请输入正确的人民币数量'
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
                        rate: {
                            validators: {
                                notEmpty: {
                                    message: "请填入汇率"
                                },
                                regexp: {
                                    regexp: /^[0-9]{0,2}\.?\d{0,1}$/,
                                    message: '请输入正确的汇率'
                                }
                            }
                        },
                         remark: {
                            validators: {
                                notEmpty: {
                                    message: "请填入备注"
                                }
                            }
                        }

            }
        });
    });
});
