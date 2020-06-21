/**
 * 用户详情对话框（可用于添加和修改对话框）
 */
var UserInfoDlg = {
    userInfoData: {},
    feedbackIcons: {//根据验证结果显示的各种图标
        valid: 'glyphicon glyphicon-ok',
        validating: 'glyphicon glyphicon-refresh'
    },
    validateFields: {
        account: {
            validators: {
                notEmpty: {
                    message: '姓名不能为空'
                }
            }
        },
        name: {
            validators: {
                notEmpty: {
                    message: '管理员名称不能为空'
                }
            }
        },
        phone: {
            validators: {
                notEmpty: {
                    message: '手机号不能为空'
                },
                regexp: {
                    regexp: /^(1[3|4|5|6|7|8|9][0-9]{9})$|^(400\d{7})$|^(0\d{2}\d{9})$/,
                    message: '你输入的手机号码有误'
                }
            }
        },
        password: {
            validators: {
                notEmpty: {
                    message: '请输入密码'
                },
                regexp: {
                    regexp: /^(?![^a-zA-Z]+$)(?!\\D+$).{8,16}$/,
                    message: '你输入的新密码有误,密码由字母+数字，8-16位组成！'
                }
            }
        },
        Astatus:{
            validators: {
                notEmpty: {
                    message: '请选择状态'
                }
            }
        },
        roleid: {
            validators: {
                notEmpty: {
                    message: "请选择角色"
                }
            }
        }
    }
};

/**
 * 清除数据
 */
UserInfoDlg.clearData = function () {
    this.userInfoData = {};
};

UserInfoDlg.refresh = function (adminId) {
    window.location = Feng.ctxPath + "/logout";
}

UserInfoDlg.back = function (adminId) {
    window.location = Feng.ctxPath + "/mgr/admin_user_edit/" +adminId ;
}
/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserInfoDlg.set = function (key, val) {
    this.userInfoData[key] = (typeof value == "undefined") ? $("#" + key).val() : value;
    return this;
};

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
UserInfoDlg.get = function (key) {
    return $("#" + key).val();
};

/**
 * 关闭此对话框
 */
UserInfoDlg.close = function () {
    parent.layer.close(window.parent.MgrUser.layerIndex);
};

/**
 * 点击部门input框时
 *
 * @param e
 * @param treeId
 * @param treeNode
 * @returns
 */
UserInfoDlg.onClickDept = function (e, treeId, treeNode) {
    $("#citySel").attr("value", instance.getSelectedVal());
    $("#deptid").attr("value", treeNode.id);
};

/**
 * 显示部门选择的树
 *
 * @returns
 */
UserInfoDlg.showDeptSelectTree = function () {
    var cityObj = $("#citySel");
    var cityOffset = $("#citySel").offset();
    $("#menuContent").css({
        left: cityOffset.left + "px",
        top: cityOffset.top + cityObj.outerHeight() + "px"
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
};

/**
 * 显示用户详情部门选择的树
 *
 * @returns
 */
UserInfoDlg.showInfoDeptSelectTree = function () {
    var cityObj = $("#citySel");
    var cityPosition = $("#citySel").position();
    $("#menuContent").css({
        left: cityPosition.left + "px",
        top: cityPosition.top + cityObj.outerHeight() + "px"
    }).slideDown("fast");

    $("body").bind("mousedown", onBodyDown);
};

/**
 * 隐藏部门选择的树
 */
UserInfoDlg.hideDeptSelectTree = function () {
    $("#menuContent").fadeOut("fast");
    $("body").unbind("mousedown", onBodyDown);// mousedown当鼠标按下就可以触发，不用弹起
};

/**
 * 收集数据
 */
UserInfoDlg.collectData = function () {
    this.set('id').set('account').set('sex').set('password').set('avatar').set('status')
        .set('email').set('name').set('birthday').set('rePassword').set('deptid').set('phone');
};

/**
 * 验证两个密码是否一致
 */
UserInfoDlg.validatePwd = function () {
    var password = this.get("password");
    var rePassword = this.get("rePassword");
    if (password == rePassword) {
        return true;
    } else {
        return false;
    }
};

/**
 * 验证数据是否为空
 */
UserInfoDlg.validate = function () {
    $('#userInfoForm').data("bootstrapValidator").resetForm();
    $('#userInfoForm').bootstrapValidator('validate');
    return $("#userInfoForm").data('bootstrapValidator').isValid();
};

/**
 * 提交添加用户
 */
UserInfoDlg.addSubmit = function () {

    if (!this.validate()) {

        return;
    }
    var roleidSele = JSON.stringify($("#roleidSele").val())
    var infoData = {};
    infoData['roleid'] = roleidSele;
    infoData['name'] = $("#name").val();
    infoData['account'] = $("#account").val();
    infoData['phone'] = $("#phone").val();
    infoData['password'] = $("#password").val();
    infoData['status'] = $("#Astatus").val();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/mgr/add", function (data) {
        Feng.success("添加成功!");
        window.parent.MgrUser.table.refresh();
        UserInfoDlg.close();
    }, function (data) {
        Feng.error("添加失败!" + data.responseJSON.msg + "!");
    });
    ajax.set(infoData);
    ajax.start();



};

UserInfoDlg.checkPhone=function () {
    $.post(Feng.ctxPath + "/mgr/checkPhone",{"phone":phone},function () {
        if(data.code == 500){
            return false
        }
    })
    return true
}

UserInfoDlg.checkAccount=function (account,oldaccount) {
    if(account != oldaccount){
        $.post(Feng.ctxPath + "/mgr/checkAccount",{"account":account},function (data) {
            if(data.code == 500){
                return false
            }
        })
    }


    return true
}


/**
 * 个人资料修改
 */

UserInfoDlg.editUserSubmit = function () {
//询问框

    layer.confirm('确定要修改个人资料吗', {
        btn: ['确定','取消'] //按钮
    }, function(){
        UserInfoDlg.clearData();
        UserInfoDlg.collectData();

        if (!UserInfoDlg.validate()) {
            return;
        }
        var account = $("#account").val();
        var oldaccount = $("#oldaccount").val()
        var adminid = $("#id").val();

        if(!UserInfoDlg.checkAccount(account,oldaccount)){
            Feng.error("账号已存在!")
            return
        }
        //提交信息
        var ajax = new $ax(Feng.ctxPath + "/mgr/edit", function (data) {
            Feng.success("修改成功!");
            UserInfoDlg.refresh(adminid)
        }, function (data) {
            Feng.error("修改失败!");
        });
        ajax.set(UserInfoDlg.userInfoData);
        ajax.start();
    }, function(){
    });

};




/**
 * 提交修改
 */
UserInfoDlg.editSubmit = function () {


    if (!this.validate()) {
        return;
    }
    var roleidSele = JSON.stringify($("#roleidSele").val())
    var infoData = {};
    infoData['roleid'] = roleidSele;
    infoData['id'] = $("#id").val();
    infoData['name'] = $("#name").val();
    infoData['account'] = $("#account").val();
    infoData['phone'] = $("#phone").val();
    infoData['password'] = $("#password").val();
    infoData['status'] = $("#Astatus").val();

    //提交信息
    var ajax = new $ax(Feng.ctxPath + "/mgr/edit", function (data) {
        Feng.success("修改成功!");
        if (window.parent.MgrUser != undefined) {
            window.parent.MgrUser.table.refresh();
            UserInfoDlg.close();
        }
    }, function (data) {
        Feng.error("修改失败!" + data.responseJSON.msg + "!");
    });
    ajax.set(infoData);
    ajax.start();

};


/**
 * 修改密码
 */
UserInfoDlg.chPwd = function () {
    var bootstrapValidator = $('#validatorForm').data('bootstrapValidator');
    //手动触发验证
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
        var pwd = {};
        var ajax = new $ax(Feng.ctxPath + "/mgr/changePwd", function (data) {
            Feng.success("修改成功!");
            window.location.href=Feng.ctxPath + "/logout"
        }, function (data) {
            Feng.error("修改失败!" + data.responseJSON.msg + "!");
        });
        pwd["oldPwd"] = $("#oldPwd").val()
        pwd["newPwd"] = $("#newPwd").val()
        pwd["rePwd"] = $("#rePwd").val()
        ajax.set(pwd);
        ajax.start();
    }

};

function onBodyDown(event) {
    if (!(event.target.id == "menuBtn" || event.target.id == "menuContent" || $(
            event.target).parents("#menuContent").length > 0)) {
        UserInfoDlg.hideDeptSelectTree();
    }
}

$(function () {
    Feng.initValidator("userInfoForm", UserInfoDlg.validateFields);

    var ztree = new $ZTree("treeDemo", "/dept/tree");
    ztree.bindOnClick(UserInfoDlg.onClickDept);
    ztree.init();
    instance = ztree;

    //初始化性别选项
    $("#sex").val($("#sexValue").val());

    // 初始化头像上传
    var avatarUp = new $WebUpload("avatar");
    avatarUp.setUploadBarId("progressBar");
    avatarUp.init();


});
function show(){
    obj = document.getElementsByName("roles");
    check_val = [];
    var roleids = "";
    for(k in obj){
        if(obj[k].checked)
            check_val.push(obj[k].value);
    }
    for(role in check_val){
        roleids = roleids + check_val[role] +","
    }
    $("#roleid").val(roleids);
    return roleids;
}

$(function() {
    $(document).ready(function() {
        $('#validatorForm').bootstrapValidator({
            message: 'This value is not valid',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                oldPwd: {
                    validators: {
                        notEmpty: {
                            message: "请输入旧密码"
                        }
                    }
                },
                newPwd: {
                    validators: {
                        notEmpty: {
                            message: "请输入新密码"
                        },
                        regexp: {
                            regexp: /^(?![^a-zA-Z]+$)(?!\\D+$).{8,16}$/,
                            message: '你输入的新密码有误,密码由字母+数字，8-16位组成！'
                        }
                    }
                },
                rePwd: {
                    validators: {
                        notEmpty: {
                            message: "请输入新密码"
                        },
                        regexp: {
                            regexp: /^(?![^a-zA-Z]+$)(?!\\D+$).{8,16}$/,
                            message: '你输入的新密码有误,密码由字母+数字，8-16位组成！'
                        }
                    }
                }
            }
        });
    });
});

