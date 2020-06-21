/**
 * 初始化观看记录详情对话框
 */
var WatchRecordInfoDlg = {
    watchRecordInfoData : {}
};

/**
 * 清除数据
 */
WatchRecordInfoDlg.clearData = function() {
    this.watchRecordInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
WatchRecordInfoDlg.set = function(key, val) {
    this.watchRecordInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
WatchRecordInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
WatchRecordInfoDlg.close = function() {
    parent.layer.close(window.parent.WatchRecord.layerIndex);
}

/**
 * 收集数据
 */
WatchRecordInfoDlg.collectData = function() {
    this
    .set('id')
    .set('userId')
    .set('liveRecordId')
    .set('createTime')
    .set('endTime')
    .set('state')
    .set('time');
}

/**
 * 提交添加
 */
WatchRecordInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/watchRecord/add", function(data){
            Feng.success("添加成功!");
            window.parent.WatchRecord.table.refresh();
            WatchRecordInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(WatchRecordInfoDlg.watchRecordInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
WatchRecordInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/watchRecord/update", function(data){
            Feng.success("修改成功!");
            window.parent.WatchRecord.table.refresh();
            WatchRecordInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(WatchRecordInfoDlg.watchRecordInfoData);
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
                        liveRecordId: {
                            validators: {
                                notEmpty: {
                                    message: "请填入"
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
                        endTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入退出时间"
                                }
                            }
                        },
                        state: {
                            validators: {
                                notEmpty: {
                                    message: "请填入是否退出直播间 0：否 1：是"
                                }
                            }
                        },
                         time: {
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
