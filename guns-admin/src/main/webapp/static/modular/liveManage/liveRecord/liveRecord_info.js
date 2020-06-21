/**
 * 初始化直播记录详情对话框
 */
var LiveRecordInfoDlg = {
    liveRecordInfoData : {}
};

/**
 * 清除数据
 */
LiveRecordInfoDlg.clearData = function() {
    this.liveRecordInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
LiveRecordInfoDlg.set = function(key, val) {
    this.liveRecordInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
LiveRecordInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
LiveRecordInfoDlg.close = function() {
    parent.layer.close(window.parent.LiveRecord.layerIndex);
}

/**
 * 收集数据
 */
LiveRecordInfoDlg.collectData = function() {
    this
    .set('id')
    .set('liveId')
    .set('cover')
    .set('name')
    .set('liveWatchNow')
    .set('liveWatchTotal')
    .set('showerName')
    .set('showTime')
    .set('amount')
    .set('state')
    .set('liveMode')
    .set('livePrice')
    .set('createTime')
    .set('endTime')
    .set('livePwd')
    .set('userId');
}

/**
 * 提交添加
 */
LiveRecordInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/liveRecord/add", function(data){
            Feng.success("添加成功!");
            window.parent.LiveRecord.table.refresh();
            LiveRecordInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(LiveRecordInfoDlg.liveRecordInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
LiveRecordInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/liveRecord/update", function(data){
            Feng.success("修改成功!");
            window.parent.LiveRecord.table.refresh();
            LiveRecordInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(LiveRecordInfoDlg.liveRecordInfoData);
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
                        liveId: {
                            validators: {
                                notEmpty: {
                                    message: "请填入直播id"
                                }
                            }
                        },
                        cover: {
                            validators: {
                                notEmpty: {
                                    message: "请填入直播封面"
                                }
                            }
                        },
                        name: {
                            validators: {
                                notEmpty: {
                                    message: "请填入直播标题"
                                }
                            }
                        },
                        liveWatchNow: {
                            validators: {
                                notEmpty: {
                                    message: "请填入当前观看人数"
                                }
                            }
                        },
                        liveWatchTotal: {
                            validators: {
                                notEmpty: {
                                    message: "请填入本场总管看人数"
                                }
                            }
                        },
                        showerName: {
                            validators: {
                                notEmpty: {
                                    message: "请填入主播名字"
                                }
                            }
                        },
                        showTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入已开播时长 分钟"
                                }
                            }
                        },
                        amount: {
                            validators: {
                                notEmpty: {
                                    message: "请填入本场直播当前总贡献量"
                                }
                            }
                        },
                        state: {
                            validators: {
                                notEmpty: {
                                    message: "请填入直播状态 0：正在直播 1：直播结束"
                                }
                            }
                        },
                        liveMode: {
                            validators: {
                                notEmpty: {
                                    message: "请填入直播模式 0：免费 1：密码模式 2：常规收费  3：计时收费"
                                }
                            }
                        },
                        livePrice: {
                            validators: {
                                notEmpty: {
                                    message: "请填入直播收费价格 收费模式有效  "
                                }
                            }
                        },
                        createTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入开始时间"
                                }
                            }
                        },
                        endTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入结束时间"
                                }
                            }
                        },
                        livePwd: {
                            validators: {
                                notEmpty: {
                                    message: "请填入密码  密码模式有效"
                                }
                            }
                        },
                         userId: {
                            validators: {
                                notEmpty: {
                                    message: "请填入用户id "
                                }
                            }
                        }

            }
        });
    });
});
