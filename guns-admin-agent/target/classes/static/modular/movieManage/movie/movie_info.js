/**
 * 初始化电影详情对话框
 */
var MovieInfoDlg = {
    movieInfoData : {}
};

/**
 * 清除数据
 */
MovieInfoDlg.clearData = function() {
    this.movieInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MovieInfoDlg.set = function(key, val) {
    this.movieInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
MovieInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
MovieInfoDlg.close = function() {
    parent.layer.close(window.parent.Movie.layerIndex);
}

/**
 * 收集数据
 */
MovieInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('source')
    .set('cover')
    .set('createTime')
    .set('playNum')
    .set('price')
    .set('url')
    .set('cateId')
    .set('state')
    .set('uploadUser')
    .set('remark');
}

/**
 * 提交添加
 */
MovieInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();
        var cover = $("#cover").val()
        var url = $("#url").val()
        if(url == ""){
            Feng.error("请先上传电影")
            return
        }
        if(cover == ""){
            Feng.error("请先上传电影封面")
            return
        }
        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/movie/add", function(data){
            Feng.success("添加成功!");
            window.parent.Movie.table.refresh();
            MovieInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(MovieInfoDlg.movieInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
MovieInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/movie/update", function(data){
            Feng.success("修改成功!");
            window.parent.Movie.table.refresh();
            MovieInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(MovieInfoDlg.movieInfoData);
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
            fileType: "movie_cover"
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
$('#urlContainer').lx_easyUpload({
        allowFileTypes: '*.mp4;',//允许上传文件类型，格式';*.doc;*.pdf'
        allowFileSize: 5000000,//允许上传文件大小(KB)
        selectText: '选择文件',//选择文件按钮文案
        multi: false,//是否允许多文件上传
        multiNum: 1,//多文件上传时允许的文件数
        showNote: true,//是否展示文件上传说明
        note: '上传电影',//文件上传说明
        showPreview: false,//是否显示文件预览
        url: Feng.ctxPath + '/upload/part_upload',//上传文件地址
        fileName: 'file',//文件filename配置参数
        formParam: {
            fileType: "movie"
        },//文件filename以外的配置参数，格式：{key1:value1,key2:value2}
        timeout: 3000000,//请求超时时间
        successFunc: function (res) {
            $("#url").val(res.successSingle[0].savePath)
        },//上传成功回调函数
        errorFunc: function (res) {
            console.log('失败回调', res);
        },//上传失败回调函数
        deleteFunc: function (res) {
            $("#url").val("")
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
                        name: {
                            validators: {
                                notEmpty: {
                                    message: "请填入名称"
                                }
                            }
                        },
                        source: {
                            validators: {
                                notEmpty: {
                                    message: "请填入上传渠道 0: 平台 1:代理商"
                                }
                            }
                        },
                        cover: {
                            validators: {
                                notEmpty: {
                                    message: "请填入封面图片"
                                }
                            }
                        },
                        createTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入上架时间"
                                }
                            }
                        },
                        playNum: {
                            validators: {
                                notEmpty: {
                                    message: "请填入播放量"
                                }
                            }
                        },
                        price: {
                            validators: {
                                notEmpty: {
                                    message: "请填入价格"
                                },
                                regexp:{
                                    // regexp:/^[0-9]+([.]{1}[0-9]{1,2})?$/,
                                    regexp:/^[0-9]{0,5}$/,
                                    message: "请填入价格有误"
                                }
                            }
                        },
                        url: {
                            validators: {
                                notEmpty: {
                                    message: "请填入电影链接"
                                }
                            }
                        },
                        cateId: {
                            validators: {
                                notEmpty: {
                                    message: "请选择电影分类"
                                }
                            }
                        },
                        state: {
                            validators: {
                                notEmpty: {
                                    message: "请填入状态 0：上架  1：下架 2：删除"
                                }
                            }
                        },
                        uploadUser: {
                            validators: {
                                notEmpty: {
                                    message: "请填入上传人"
                                }
                            }
                        },
                         remark: {
                            validators: {
                                notEmpty: {
                                    message: "请填入电影简介"
                                }
                            }
                        }

            }
        });
    });
});
