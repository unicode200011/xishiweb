/**
 * 初始化礼物详情对话框
 */
var GiftInfoDlg = {
    giftInfoData : {}
};

/**
 * 清除数据
 */
GiftInfoDlg.clearData = function() {
    this.giftInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
GiftInfoDlg.set = function(key, val) {
    this.giftInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
GiftInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
GiftInfoDlg.close = function() {
    parent.layer.close(window.parent.Gift.layerIndex);
}

/**
 * 收集数据
 */
GiftInfoDlg.collectData = function() {
    this
    .set('id')
    .set('name')
    .set('money')
    .set('createTime')
    .set('image')
    .set('type')
    .set('animation')
    .set('num')
    .set('state')
    .set('useCount');
}

/**
 * 提交添加
 */
GiftInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();
        var image = $("#image").val()
        if(image == ""){
            Feng.error("请上传样式")
            return
        }
        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/gift/add", function(data){
            Feng.success("添加成功!");
            window.parent.Gift.table.refresh();
            GiftInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(GiftInfoDlg.giftInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
GiftInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/gift/update", function(data){
            Feng.success("修改成功!");
            window.parent.Gift.table.refresh();
            GiftInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(GiftInfoDlg.giftInfoData);
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
                                    message: "请填入礼物名称"
                                }
                            }
                        },
                        money: {
                            validators: {
                                notEmpty: {
                                    message: "请填入礼物单价（西施币）"
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
                        image: {
                            validators: {
                                notEmpty: {
                                    message: "请填入礼物样式"
                                }
                            }
                        },
                        type: {
                            validators: {
                                notEmpty: {
                                    message: "请填入类型 0:小礼物 1:大礼物"
                                }
                            }
                        },
                        animation: {
                            validators: {
                                notEmpty: {
                                    message: "请填入礼物效果: 0.无; 1.666[上浮,抖动,渐隐]; 2.fans[上浮,抖动,螺旋飞出]; 3.打Call[上浮,放大,渐隐]; 4.飞机[从右到左弧线移动]; 5.挠痒痒[上浮,手指头移动,消失]; 6.跑车[从右到左直线移动]; 7.热气球[从屏幕下方上升至屏幕上方]; 8.星星[闪动进入,抖动,闪动退出]; 9.荧光棒[上浮,晃动,渐隐]; 10.游艇[从右到左直线移动]"
                                }
                            }
                        },
                        num: {
                            validators: {
                                notEmpty: {
                                    message: "请填入排序值 大靠前"
                                }
                            }
                        },
                        state: {
                            validators: {
                                notEmpty: {
                                    message: "请填入状态：0：显示  1：隐藏"
                                }
                            }
                        },
                         useCount: {
                            validators: {
                                notEmpty: {
                                    message: "请填入使用数量"
                                }
                            }
                        }

            }
        });
    });
});
