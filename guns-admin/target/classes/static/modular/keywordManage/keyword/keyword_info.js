/**
 * 初始化敏感词详情对话框
 */
var KeywordInfoDlg = {
    keywordInfoData : {}
};

/**
 * 清除数据
 */
KeywordInfoDlg.clearData = function() {
    this.keywordInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
KeywordInfoDlg.set = function(key, val) {
    this.keywordInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
KeywordInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
KeywordInfoDlg.close = function() {
    parent.layer.close(window.parent.Keyword.layerIndex);
}

/**
 * 收集数据
 */
KeywordInfoDlg.collectData = function() {
    this
    .set('id')
    .set('word')
    .set('createTime');
}

/**
 * 提交添加
 */
KeywordInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/keyword/add", function(data){
            Feng.success("添加成功!");
            window.parent.Keyword.table.refresh();
            KeywordInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(KeywordInfoDlg.keywordInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
KeywordInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/keyword/update", function(data){
            Feng.success("修改成功!");
            window.parent.Keyword.table.refresh();
            KeywordInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(KeywordInfoDlg.keywordInfoData);
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
                        word: {
                            validators: {
                                notEmpty: {
                                    message: "请填入敏感词"
                                }
                            }
                        },
                         createTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入添加时间"
                                }
                            }
                        }

            }
        });
    });
});
