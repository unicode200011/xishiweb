/**
 * 初始化用户反馈详情对话框
 */
var SuggestionInfoDlg = {
    suggestionInfoData : {}
};

/**
 * 清除数据
 */
SuggestionInfoDlg.clearData = function() {
    this.suggestionInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SuggestionInfoDlg.set = function(key, val) {
    this.suggestionInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SuggestionInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
SuggestionInfoDlg.close = function() {
    parent.layer.close(window.parent.Suggestion.layerIndex);
}

/**
 * 收集数据
 */
SuggestionInfoDlg.collectData = function() {
    this
    .set('id')
    .set('userId')
    .set('typeId')
    .set('adminId')
    .set('content')
    .set('state')
    .set('firstRepContent')
    .set('secondRepContent')
    .set('createTime')
    .set('updateTime');
}

/**
 * 提交添加
 */
SuggestionInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/suggestion/add", function(data){
            Feng.success("添加成功!");
            window.parent.Suggestion.table.refresh();
            SuggestionInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(SuggestionInfoDlg.suggestionInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
SuggestionInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/suggestion/update", function(data){
            Feng.success("修改成功!");
            window.parent.Suggestion.table.refresh();
            SuggestionInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(SuggestionInfoDlg.suggestionInfoData);
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
                                    message: "请填入反馈用户ID"
                                }
                            }
                        },
                        typeId: {
                            validators: {
                                notEmpty: {
                                    message: "请填入类型ID"
                                }
                            }
                        },
                        adminId: {
                            validators: {
                                notEmpty: {
                                    message: "请填入管理员ID"
                                }
                            }
                        },
                        content: {
                            validators: {
                                notEmpty: {
                                    message: "请填入反馈内容"
                                }
                            }
                        },
                        state: {
                            validators: {
                                notEmpty: {
                                    message: "请填入状态 0 待处理 1 已处理 2 删除"
                                }
                            }
                        },
                        firstRepContent: {
                            validators: {
                                notEmpty: {
                                    message: "请填入回复内容1"
                                }
                            }
                        },
                        secondRepContent: {
                            validators: {
                                notEmpty: {
                                    message: "请填入回复内容2"
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
                                    message: "请填入修改时间"
                                }
                            }
                        }

            }
        });
    });
});
