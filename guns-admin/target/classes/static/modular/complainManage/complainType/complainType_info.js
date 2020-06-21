/**
 * 初始化举报类型列表详情对话框
 */
var ComplainTypeInfoDlg = {
    complainTypeInfoData : {}
};

/**
 * 清除数据
 */
ComplainTypeInfoDlg.clearData = function() {
    this.complainTypeInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ComplainTypeInfoDlg.set = function(key, val) {
    this.complainTypeInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
ComplainTypeInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
ComplainTypeInfoDlg.close = function() {
    parent.layer.close(window.parent.ComplainType.layerIndex);
}

/**
 * 收集数据
 */
ComplainTypeInfoDlg.collectData = function() {
    this
    .set('id')
    .set('reason');
}

/**
 * 提交添加
 */
ComplainTypeInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/complainType/add", function(data){
            Feng.success("添加成功!");
            window.parent.ComplainType.table.refresh();
            ComplainTypeInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(ComplainTypeInfoDlg.complainTypeInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
ComplainTypeInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/complainType/update", function(data){
            Feng.success("修改成功!");
            window.parent.ComplainType.table.refresh();
            ComplainTypeInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(ComplainTypeInfoDlg.complainTypeInfoData);
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
                         reason: {
                            validators: {
                                notEmpty: {
                                    message: "请填入举报理由"
                                }
                            }
                        }

            }
        });
    });
});
