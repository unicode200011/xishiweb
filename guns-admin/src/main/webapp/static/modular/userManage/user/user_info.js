/**
 * 初始化用户列表详情对话框
 */
var UserInfoDlg = {
    userInfoData : {}
};

/**
 * 清除数据
 */
UserInfoDlg.clearData = function() {
    this.userInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserInfoDlg.set = function(key, val) {
    this.userInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserInfoDlg.get = function(key) {
    return $("#" + key).val();
}

/**
 * 关闭此对话框
 */
UserInfoDlg.close = function() {
    parent.layer.close(window.parent.User.layerIndex);
}

/**
 * 收集数据
 */
UserInfoDlg.collectData = function() {
    this
    .set('id')
    .set('phone')
    .set('password')
    .set('salt')
    .set('name')
    .set('avatar')
    .set('city')
    .set('province')
    .set('gender')
    .set('age')
    .set('intro')
    .set('payPwd')
    .set('state')
    .set('qq')
    .set('wx')
    .set('birthday')
    .set('source')
    .set('machine')
    .set('loginTime')
    .set('createTime')
    .set('updateTime')
    .set('version')
    .set('xishiNum')
    .set('onlineState')
    .set('weightSort')
    .set('recommended')
    .set('belongAgent')
    .set('applyStatus')
    .set('registeredState')
    .set('applyStatusTime')
    .set('agentId')
    .set('shower')
    .set('agentRate')
    .set('userLevel')
    .set('adminRate')
    .set('liveLevel');
}

/**
 * 提交添加
 */
UserInfoDlg.addSubmit = function() {
var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        this.clearData();
        this.collectData();

        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/user/add", function(data){
            Feng.success("添加成功!");
            window.parent.User.table.refresh();
            UserInfoDlg.close();
        },function(data){
            Feng.error("添加失败!" + data.responseJSON.msg + "!");
        });
        ajax.set(UserInfoDlg.userInfoData);
        ajax.start();
    }
}

/**
 * 提交修改
 */
UserInfoDlg.editSubmit = function() {
    // var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    // //手动触发验证
    // bootstrapValidator.validate();
    // if (bootstrapValidator.isValid()) {
    //
    // }

    this.clearData();
    this.collectData();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/user/update", function(data){
        Feng.success("修改成功!");
        window.parent.User.table.refresh();
        UserInfoDlg.close();
    },function(data){
        Feng.error("修改失败!" + data.responseJSON.msg + "!");
    });

    ajax.set(UserInfoDlg.userInfoData);
    ajax.start();
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
                                    message: "请填入id"
                                }
                            }
                        },
                        phone: {
                            validators: {
                                notEmpty: {
                                    message: "请填入用户手机号/登录名"
                                }
                            }
                        },
                        password: {
                            validators: {
                                notEmpty: {
                                    message: "请填入密码"
                                }
                            }
                        },
                        salt: {
                            validators: {
                                notEmpty: {
                                    message: "请填入salt"
                                }
                            }
                        },
                        name: {
                            validators: {
                                notEmpty: {
                                    message: "请填入昵称"
                                }
                            }
                        },
                        agentRate: {
                            validators: {
                                notEmpty: {
                                    message: "请填入"
                                }
                            }
                        },
                        adminRate: {
                            validators: {
                                notEmpty: {
                                    message: "请填入"
                                }
                            }
                        },
                        avatar: {
                            validators: {
                                notEmpty: {
                                    message: "请填入头像"
                                }
                            }
                        },
                        city: {
                            validators: {
                                notEmpty: {
                                    message: "请填入城市"
                                }
                            }
                        },
                        province: {
                            validators: {
                                notEmpty: {
                                    message: "请填入省份"
                                }
                            }
                        },
                        gender: {
                            validators: {
                                notEmpty: {
                                    message: "请填入性别: 1:男2:女  3:保密"
                                }
                            }
                        },
                        age: {
                            validators: {
                                notEmpty: {
                                    message: "请填入年龄"
                                }
                            }
                        },
                        intro: {
                            validators: {
                                notEmpty: {
                                    message: "请填入个人简介"
                                }
                            }
                        },
                        payPwd: {
                            validators: {
                                notEmpty: {
                                    message: "请填入支付密码"
                                }
                            }
                        },
                        state: {
                            validators: {
                                notEmpty: {
                                    message: "请填入用户总状态 0:正常 1:冻结  2:删除"
                                }
                            }
                        },
                        qq: {
                            validators: {
                                notEmpty: {
                                    message: "请填入QQ"
                                }
                            }
                        },
                        wx: {
                            validators: {
                                notEmpty: {
                                    message: "请填入VX"
                                }
                            }
                        },
                        birthday: {
                            validators: {
                                notEmpty: {
                                    message: "请填入生日"
                                }
                            }
                        },
                        source: {
                            validators: {
                                notEmpty: {
                                    message: "请填入用户来源: 0 注册  1 系统添加"
                                }
                            }
                        },
                        machine: {
                            validators: {
                                notEmpty: {
                                    message: "请填入设备号"
                                }
                            }
                        },
                        loginTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入最后登录时间"
                                }
                            }
                        },
                        createTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入注册时间"
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
                        version: {
                            validators: {
                                notEmpty: {
                                    message: "请填入版本号"
                                }
                            }
                        },
                        xishiNum: {
                            validators: {
                                notEmpty: {
                                    message: "请填入西施id"
                                }
                            }
                        },
                        onlineState: {
                            validators: {
                                notEmpty: {
                                    message: "请填入用户在线状态0:离线,1:在线 2:忙碌"
                                }
                            }
                        },
                        weightSort: {
                            validators: {
                                notEmpty: {
                                    message: "请填入权重顺序"
                                }
                            }
                        },
                        recommended: {
                            validators: {
                                notEmpty: {
                                    message: "请填入是否推荐1是0否"
                                }
                            }
                        },
                        belongAgent: {
                            validators: {
                                notEmpty: {
                                    message: "请填入所属家族"
                                }
                            }
                        },
                        applyStatus: {
                            validators: {
                                notEmpty: {
                                    message: "请填入主播认证申请状态:1:已申请2:未申请3:拒绝4:通过"
                                }
                            }
                        },
                        registeredState: {
                            validators: {
                                notEmpty: {
                                    message: "请填入注册方式 0：手机 1：微信 2：qq"
                                }
                            }
                        },
                        applyStatusTime: {
                            validators: {
                                notEmpty: {
                                    message: "请填入主播认证通过时间"
                                }
                            }
                        },
                        shower: {
                            validators: {
                                notEmpty: {
                                    message: "请填入是否主播 0 ： 否 1：是"
                                }
                            }
                        },
                        userLevel: {
                            validators: {
                                notEmpty: {
                                    message: "请填入用户等级"
                                }
                            }
                        },
                         liveLevel: {
                            validators: {
                                notEmpty: {
                                    message: "请填入直播等级"
                                }
                            }
                        }

            }
        });
    });
});
