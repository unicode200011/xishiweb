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
var indexs = 1;

/**
 * 关闭此对话框
 */
AgentInfoDlg.close = function() {

    parent.layer.close(window.parent.Agent.layerIndex);
}

/**
 * 关闭此对话框
 */
AgentInfoDlg.closeLast = function() {
    parent.layer.close(window.parent.indexs);
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
    .set('bankAccountId')
    .set('bankCard')
    .set('showerNum');
}

/**
 * 添加信息
 */
AgentInfoDlg.openInfo = function() {

        var userId = $("#userId").val()
        var index = layer.open({
            type: 2,
            title: '添加信息',
            area: ['900px', '480px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/agent/openInfo/' + userId
        });

        indexs = index;

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
 * 提现
 */
AgentInfoDlg.applyWith = function () {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();

    var agentId = $("#agentId").val();
    var money = $("#money").val();
    var bankAccountId = $("#bankAccountId").val();

    if (money == '') {
        Feng.error("提现金额不能为空!");
        return;
    }
	
	 var m = money.split(".");
    console.log(m[1] == '')

    if(m.length > 2 || (m.length != 1 && m[1].length > 1 || m[1] == '')){
        Feng.error("提现金额有误!");
        return;
    }

    if (bankAccountId == '') {
        Feng.error("请选择账户!");
        return;
    }


    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();


        var ajax = new $ax(Feng.ctxPath + "/agent/withdrawal", function (data) {
            Feng.success("申请提现成功!");
            window.parent.Agent.table.refresh();
            AgentInfoDlg.close();
        }, function (data) {
            Feng.error("申请提现失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("agentId", agentId);
        ajax.set("money", money);
        ajax.set("bankAccountId", bankAccountId);

        ajax.start();
    }
};



/**
 * 提交添加信息
 */
AgentInfoDlg.addInfo = function() {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    var userId = $("#userId").val();
    var agentLogo = $("#agentLogo").val();
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/agent/addInfo", function(data){
            Feng.success("添加成功!");

            AgentInfoDlg.closeLast();
            //window.parent.Agent.table.refresh();

        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(AgentInfoDlg.agentInfoData);
        ajax.set("userId", userId);
        ajax.set("agentLogo", agentLogo);
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
            window.parent.location.reload()
            AgentInfoDlg.close();
        },function(data){
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(AgentInfoDlg.agentInfoData);
        ajax.start();
    }
}

AgentInfoDlg.delete = function(id){
    Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/agent/deleteBankUser/" + id, function (data) {
            Feng.success("删除成功!");
            Agent.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        // ajax.set("agentId",Agent.seItem.id);

        ajax.start();
    })
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
