/**
 * 初始化电影分类详情对话框
 */
var MovieCateInfoDlg = {
    movieCateInfoData : {}
};

/**
 * 清除数据
 */
MovieCateInfoDlg.clearData = function() {
    this.movieCateInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MovieCateInfoDlg.set = function(key, val) {
    this.movieCateInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MovieCateInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MovieCateInfoDlg.close = function() {
    parent.layer.close(window.parent.MovieCate.layerIndex);
}

/**
 * 收集数据
 */
MovieCateInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('num')
    .set('state')
    .set('createTime')
    .set('updateTime')
    .set('playNum');
}

/**
 * 提交添加
 */
MovieCateInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/movieCate/add", function(data){
            Feng.success("添加成功!");
            window.parent.MovieCate.table.refresh();
            MovieCateInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(MovieCateInfoDlg.movieCateInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
MovieCateInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/movieCate/update", function(data){
            Feng.success("修改成功!");
            window.parent.MovieCate.table.refresh();
            MovieCateInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(MovieCateInfoDlg.movieCateInfoData);
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
                                    message: "请填入分类名"
                                }
                            }
                        },
                        num: {
                            validators: {
                                notEmpty: {
                                    message: "请填入排序值"
                                }
                            }
                        },
                        state: {
                            validators: {
                                notEmpty: {
                                    message: "请填入状态 0：正常 1：禁用 2：删除"
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
                                    message: "请填入更新时间"
                                }
                            }
                        },
                         playNum: {
                            validators: {
                                notEmpty: {
                                    message: "请填入播放总量"
                                }
                            }
                        }

            }
        });
    });
});
