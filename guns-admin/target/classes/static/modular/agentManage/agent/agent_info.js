/**
 * 初始化代理商详情对话框
 */
var AgentInfoDlg = {
    agentInfoData : {}
};

/**
 * 清除数据
 */
AgentInfoDlg.clearData = function() {
    this.agentInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AgentInfoDlg.set = function(key, val) {
    this.agentInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AgentInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
AgentInfoDlg.close = function() {
    parent.layer.close(window.parent.Agent.layerIndex);
}

/**
 * 收集数据
 */
AgentInfoDlg.collectData = function() {
    this
    .set('id')
    .set('agentNum')
    .set('agentPhone')
    .set('auditState')
    .set('auditTime')
    .set('reason')
    .set('state')
    .set('permission')
    .set('userId')
    .set('agentUserName')
    .set('agentName')
    .set('card')
    .set('agentLogo')
    .set('agentIntro')
    .set('cardFront')
    .set('cardBack')
    .set('createTime')
    .set('realName')
    .set('adminRate')
    .set('showerNum');
}

/**
 * 提交添加
 */
AgentInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();
        var agentLogo = $("#agentLogo").val()
        var cardFront = $("#cardFront").val()
        var cardBack = $("#cardBack").val()
        if(agentLogo == ""){
            Feng.error("请先上传logo")
            return
        }
        if(cardFront == ""){
            Feng.error("身份证正面照")
            return
        }
        if(cardBack == ""){
            Feng.error("身份证反面照")
            return
        }
        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/agent/add", function(data){
            Feng.success("添加成功!");
            window.parent.Agent.table.refresh();
            AgentInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(AgentInfoDlg.agentInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
AgentInfoDlg.editSubmit = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/agent/update", function(data){
            Feng.success("修改成功!");
            window.parent.Agent.table.refresh();
            AgentInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(AgentInfoDlg.agentInfoData);
        ajax.start();
    }
}

/**
 * 提交设置抽成比
 */
AgentInfoDlg.updateAdminRate = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/agent/updateAdminRate", function(data){
            Feng.success("设置成功!");
            window.parent.Agent.table.refresh();
            AgentInfoDlg.close();
        },function(data){
            Feng.error("设置失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(AgentInfoDlg.agentInfoData);
        ajax.start();
    }
}



/**
 * 驳回
 */
AgentInfoDlg.refuse = function () {
    Feng.confirm("确定要驳回吗",function () {

        //prompt层
        layer.prompt({title: '请输入驳回原因', formType: 2}, function(text, index){
            layer.close(index);
            if(text == ""){
                Feng.error("请输入驳回原因")
                return
            }
            var ajax = new $ax(Feng.ctxPath + "/agent/refuse", function (data) {
                Feng.success("驳回成功!");
                window.parent.Agent.table.refresh();
                AgentInfoDlg.close();
            }, function (data) {
                Feng.error("驳回失败!" + data.responseJSON.msg + "!");
            });
            ajax.set("agentId",$("#id").val());
            ajax.set("reason",text);
            ajax.start();
        });
    })
};

/**
 * 通过
 */
AgentInfoDlg.pass = function () {
    Feng.confirm("确定要通过吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/agent/pass", function (data) {
            Feng.success("通过成功!");
            window.parent.Agent.table.refresh();
            AgentInfoDlg.close();
        }, function (data) {
            Feng.error("通过失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("agentId",$("#id").val());
        ajax.start();
    })
};

/**
 * 重置密码
 */
AgentInfoDlg.resetPwd = function () {
    Feng.confirm("确定要重置密码吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/agent/resetPwd", function (data) {
            Feng.success("重置成功!");
            window.parent.Agent.table.refresh();
            AgentInfoDlg.close();
        }, function (data) {
            Feng.error("重置失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("agentId",$("#id").val());
        ajax.start();
    })
};



/**
 *  //regexp: {
 *    //regexp: /^\d*$/,
 *   // message: '你输入的fieldName有误'
 *  //}
 */
$(function() {

    $('#agentLogoContainer').easyUpload({
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
            $("#agentLogo").val(res.successSingle[0].savePath)
        },//上传成功回调函数
        errorFunc: function (res) {
            console.log('失败回调', res);
        },//上传失败回调函数
        deleteFunc: function (res) {
            $("#agentLogo").val("")
        }//删除文件回调函数
    });


    $('#cardBackContainer').xx_easyUpload({
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
            $("#cardBack").val(res.successSingle[0].savePath)
        },//上传成功回调函数
        errorFunc: function (res) {
            console.log('失败回调', res);
        },//上传失败回调函数
        deleteFunc: function (res) {
            $("#cardBack").val("")
        }//删除文件回调函数
    });


    $('#cardFrontContainer').lx_easyUpload({
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
            $("#cardFront").val(res.successSingle[0].savePath)
        },//上传成功回调函数
        errorFunc: function (res) {
            console.log('失败回调', res);
        },//上传失败回调函数
        deleteFunc: function (res) {
            $("#cardFront").val("")
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
                        agentNum: {
                            validators: {
                                notEmpty: {
                                    message: "请填入代理商编号"
                                }
                            }
                        },
                        agentPhone: {
                            validators: {
                                notEmpty: {
                                    message: "请填入绑定手机号"
                                }
                            }
                        },

                        adminRate: {
                            validators: {
                                notEmpty: {
                                    message: "请填入抽成比"
                                }
                            }
                        },
                        auditState: {
                            validators: {
                                notEmpty: {
                                    message: "请填入审核状态 0：待审核 1：通过 2：驳回"
                                }
                            }
                        },
                        auditTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入审核时间"
                                }
                            }
                        },
                        reason: {
                            validators: {
                                notEmpty: {
                                    message: "请填入拒绝理由"
                                }
                            }
                        },
                        state: {
                            validators: {
                                notEmpty: {
                                    message: "请填入状态  0：正常 1：已禁用"
                                }
                            }
                        },
                        permission: {
                            validators: {
                                notEmpty: {
                                    message: "请填入上传电影权限 0：有  1：无"
                                }
                            }
                        },
                        userId: {
                            validators: {
                                notEmpty: {
                                    message: "请填入申请人id"
                                }
                            }
                        },
                        agentUserName: {
                            validators: {
                                notEmpty: {
                                    message: "请填入代理人姓名"
                                }
                            }
                        },
                        agentName: {
                            validators: {
                                notEmpty: {
                                    message: "请填入家族名称"
                                }
                            }
                        },
                        card: {
                            validators: {
                                notEmpty: {
                                    message: "请填入身份证信息"
                                }
                            }
                        },
                        agentLogo: {
                            validators: {
                                notEmpty: {
                                    message: "请填入家族Logo"
                                }
                            }
                        },
                        agentIntro: {
                            validators: {
                                notEmpty: {
                                    message: "请填入家族简介"
                                }
                            }
                        },
                        cardFront: {
                            validators: {
                                notEmpty: {
                                    message: "请填入身份证正面照"
                                }
                            }
                        },
                        cardBack: {
                            validators: {
                                notEmpty: {
                                    message: "请填入身份证反面照"
                                }
                            }
                        },
                        createTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入申请时间"
                                }
                            }
                        },
                        realName: {
                            validators: {
                                notEmpty: {
                                    message: "请填入真实姓名"
                                }
                            }
                        },
                         showerNum: {
                            validators: {
                                notEmpty: {
                                    message: "请填入主播个数"
                                }
                            }
                        }

            }
        });
    });
});
