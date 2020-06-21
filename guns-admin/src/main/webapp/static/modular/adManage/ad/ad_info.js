/**
 * 初始化广告列表详情对话框
 */
var AdInfoDlg = {
    adInfoData : {}
};

/**
 * 清除数据
 */
AdInfoDlg.clearData = function() {
    this.adInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AdInfoDlg.set = function(key, val) {
    this.adInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AdInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
AdInfoDlg.close = function() {
    parent.layer.close(window.parent.Ad.layerIndex);
}

/**
 * 收集数据
 */
AdInfoDlg.collectData = function() {
    this
    .set('id')
    .set('title')
    .set('location')
    .set('cover')
    .set('intro')
    .set('remark')
    .set('url')
    .set('state')
    .set('createTime')
    .set('updateTime');
}

/**
 * 提交添加
 */
AdInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/ad/add", function(data){
            Feng.success("添加成功!");
            window.parent.Ad.table.refresh();
            AdInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(AdInfoDlg.adInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
AdInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/ad/update", function(data){
            Feng.success("修改成功!");
            window.parent.Ad.table.refresh();
            AdInfoDlg.close();
            window.parent.Ad.socket.emit('socket_ad_update', {content: AdInfoDlg.get("id")});
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(AdInfoDlg.adInfoData);
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
    $('#coverContainer').easyUpload({
        allowFileTypes: '*.jpg;*.png',//允许上传文件类型，格式';*.doc;*.pdf'
        allowFileSize: 100000,//允许上传文件大小(KB)
        selectText: '选择文件',//选择文件按钮文案
        multi: false,//是否允许多文件上传
        multiNum: 1,//多文件上传时允许的文件数
        showNote: true,//是否展示文件上传说明
        note: '上传图片',//文件上传说明
        showPreview: true,//是否显示文件预览
        url: Feng.ctxPath + '/upload/ue_upload',//上传文件地址
        fileName: 'file',//文件filename配置参数
        formParam: {
            fileType: "ad"
        },//文件filename以外的配置参数，格式：{key1:value1,key2:value2}
        timeout: 300000,//请求超时时间
        successFunc: function (res) {
            $("#cover").val(res.successSingle[0].savePath)
        },//上传成功回调函数
        errorFunc: function (res) {
            console.log('失败回调', res);
        },//上传失败回调函数
        deleteFunc: function (res) {
            $("#cover").val("")
        }//删除文件回调函数
    });


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
                                    message: "请填入广告说明"
                                }
                            }
                        },
                        location: {
                            validators: {
                                notEmpty: {
                                    message: "请选择广告位置"
                                }
                            }
                        },
                        cover: {
                            validators: {
                                notEmpty: {
                                    message: "请填入广告封面"
                                }
                            }
                        },
                        intro: {
                            validators: {
                                notEmpty: {
                                    message: "请填入简要说明"
                                }
                            }
                        },
                        remark: {
                            validators: {
                                notEmpty: {
                                    message: "请填入备注"
                                }
                            }
                        },
                        url: {
                            validators: {
                                notEmpty: {
                                    message: "请填入广告连接地址"
                                }
                            }
                        },
                        state: {
                            validators: {
                                notEmpty: {
                                    message: "请填入状态：0：正常 1：禁用"
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
                        }

            }
        });
    });
});
