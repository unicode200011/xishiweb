/**
 * 初始化用户反馈类型详情对话框
 */
var SuggestionTypeInfoDlg = {
    suggestionTypeInfoData : {}
};

/**
 * 清除数据
 */
SuggestionTypeInfoDlg.clearData = function() {
    this.suggestionTypeInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SuggestionTypeInfoDlg.set = function(key, val) {
    this.suggestionTypeInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
SuggestionTypeInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
SuggestionTypeInfoDlg.close = function() {
    parent.layer.close(window.parent.SuggestionType.layerIndex);
}

/**
 * 收集数据
 */
SuggestionTypeInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('state')
    .set('createTime')
    .set('updateTime');
}

/**
 * 提交添加
 */
SuggestionTypeInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/suggestionType/add", function(data){
            Feng.success("添加成功!");
            window.parent.SuggestionType.table.refresh();
            SuggestionTypeInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(SuggestionTypeInfoDlg.suggestionTypeInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
SuggestionTypeInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/suggestionType/update", function(data){
            Feng.success("修改成功!");
            window.parent.SuggestionType.table.refresh();
            SuggestionTypeInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(SuggestionTypeInfoDlg.suggestionTypeInfoData);
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
                        name: {
                            validators: {
                                notEmpty: {
                                    message: "请填入名称"
                                }
                            }
                        },
                        state: {
                            validators: {
                                notEmpty: {
                                    message: "请填入状态 0 正常 1 删除"
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
