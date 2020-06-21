/**
 * 初始化直播管理详情对话框
 */
var LiveInfoDlg = {
    liveInfoData : {}
};

/**
 * 清除数据
 */
LiveInfoDlg.clearData = function() {
    this.liveInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
LiveInfoDlg.set = function(key, val) {
    this.liveInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
LiveInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
LiveInfoDlg.close = function() {
    parent.layer.close(window.parent.Live.layerIndex);
}

/**
 * 收集数据
 */
LiveInfoDlg.collectData = function() {
    this
    .set('id')
    .set('userId')
    .set('roomNum')
    .set('streamName')
    .set('pushUrl')
    .set('pullUrl')
    .set('totalLiveTime')
    .set('totalLiveWatch')
    .set('liveStartTime')
    .set('newLiveRecord')
    .set('state')
    .set('liveState')
    .set('updateTime')
    .set('createTime')
    .set('sortNum');
}

/**
 * 提交添加
 */
LiveInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/live/add", function(data){
            Feng.success("添加成功!");
            window.parent.Live.table.refresh();
            LiveInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(LiveInfoDlg.liveInfoData);
        ajax.start();
    }
}

/**
 * 提交警告
 */
LiveInfoDlg.warnSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        var id = $("#id").val()
        var streamName = $("#streamName").val()
        var title = $("#title").val()
        var reason = $("#reason").val()
        //提交信息
        window.parent.Live.socket.emit('socket_live_warn:', {liveId:id,title:title, streamName: streamName, reason: reason});
        LiveInfoDlg.close();
    }
}

/**
 * 提交修改
 */
LiveInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/live/update", function(data){
            Feng.success("修改成功!");
            window.parent.Live.table.refresh();
            LiveInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(LiveInfoDlg.liveInfoData);
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
                title: {
                            validators: {
                                notEmpty: {
                                    message: "请填入标题"
                                }
                            }
                        },
                reason: {
                            validators: {
                                notEmpty: {
                                    message: "请填入内容"
                                }
                            }
                        },
                        streamName: {
                            validators: {
                                notEmpty: {
                                    message: "请填入直播流名"
                                }
                            }
                        },
                        pushUrl: {
                            validators: {
                                notEmpty: {
                                    message: "请填入推流地址"
                                }
                            }
                        },
                        pullUrl: {
                            validators: {
                                notEmpty: {
                                    message: "请填入播流地址"
                                }
                            }
                        },
                        totalLiveTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入直播总时长 分钟数"
                                }
                            }
                        },
                        totalLiveWatch: {
                            validators: {
                                notEmpty: {
                                    message: "请填入直播历史总观看人数"
                                }
                            }
                        },
                        liveStartTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入最新直播开始时间"
                                }
                            }
                        },
                        newLiveRecord: {
                            validators: {
                                notEmpty: {
                                    message: "请填入最新直播记录id"
                                }
                            }
                        },
                        state: {
                            validators: {
                                notEmpty: {
                                    message: "请填入直播房间状态  0：正常  1：禁播 "
                                }
                            }
                        },
                        liveState: {
                            validators: {
                                notEmpty: {
                                    message: "请填入直播状态 0：未直播 1：正在直播"
                                }
                            }
                        },
                        updateTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入更新时间"
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
                         sortNum: {
                            validators: {
                                notEmpty: {
                                    message: "请填入排序值"
                                }
                            }
                        }

            }
        });
    });
});
