/**
 * 初始化用户等级详情对话框
 */
var UserGradeInfoDlg = {
    userGradeInfoData : {}
};

/**
 * 清除数据
 */
UserGradeInfoDlg.clearData = function() {
    this.userGradeInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserGradeInfoDlg.set = function(key, val) {
    this.userGradeInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserGradeInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
UserGradeInfoDlg.close = function() {
    parent.layer.close(window.parent.UserGrade.layerIndex);
}

/**
 * 收集数据
 */
UserGradeInfoDlg.collectData = function() {
    this
    .set('id')
    .set('level')
    .set('image')
    .set('amount')
    .set('createTime')
    .set('updateTime')
    .set('levelName')
    .set('rgb');
}

/**
 * 提交添加
 */
UserGradeInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();
        var image = $("#image").val()
        var rgb = $("#rgb").val()
        var color = $("#color").val()
        if(image == ""){
            Feng.error("请上传标识")
            return
        }
        if(rgb == ""){
            Feng.error("请选择颜色")
            return
        }
        if(color == ""){
            Feng.error("请选择颜色")
            return
        }
        UserGradeInfoDlg.set("color","#"+color)
        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/userGrade/add", function(data){
            Feng.success("添加成功!");
            window.parent.UserGrade.table.refresh();
            UserGradeInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(UserGradeInfoDlg.userGradeInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
UserGradeInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();
        var color = $("#color").val()
        UserGradeInfoDlg.set("color","#"+color)
        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/userGrade/update", function(data){
            Feng.success("修改成功!");
            window.parent.UserGrade.table.refresh();
            UserGradeInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(UserGradeInfoDlg.userGradeInfoData);
        ajax.start();
    }
}
/**
 *  //regexp: {
 *    //regexp: /^\d*$/,
 *   // message: '你输入的fieldName有误'
 *  //}
 */

function update(picker) {
    var rgb = Math.round(picker.rgb[0])+','+ Math.round(picker.rgb[1])+','+ Math.round(picker.rgb[2])
    debugger
    $("#rgb").val(rgb)
}
$(function() {

    $('#imageContainer').easyUpload({
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
            fileType: "grade_image"
        },//文件filename以外的配置参数，格式：{key1:value1,key2:value2}
        timeout: 300000,//请求超时时间
        successFunc: function (res) {
            $("#image").val(res.successSingle[0].savePath)
        },//上传成功回调函数
        errorFunc: function (res) {
            console.log('失败回调', res);
        },//上传失败回调函数
        deleteFunc: function (res) {
            $("#image").val("")
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
                        level: {
                            validators: {
                                notEmpty: {
                                    message: "请填入会员等级"
                                },
                                regexp:{
                                    regexp:/^[0-9]{1,3}$/,
                                    message: "填入会员等级有误"
                                }
                            }
                        },
                        image: {
                            validators: {
                                notEmpty: {
                                    message: "请填入等级标识"
                                }
                            }
                        },
                        amount: {
                            validators: {
                                notEmpty: {
                                    message: "请填入达标接收西施币"
                                },
                                regexp:{
                                    regexp:/^[0-9]{1,7}$/,
                                    message: "填入达标接收西施币有误"
                                }
                            }
                        },
                        color: {
                            validators: {
                                notEmpty: {
                                    message: "请填入等级颜色"
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
                        updateTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入"
                                }
                            }
                        },
                        levelName: {
                            validators: {
                                notEmpty: {
                                    message: "请填入等级名字"
                                }
                            }
                        },
                         rgb: {
                            validators: {
                                notEmpty: {
                                    message: "请填入颜色rgb"
                                }
                            }
                        }

            }
        });
    });
});
