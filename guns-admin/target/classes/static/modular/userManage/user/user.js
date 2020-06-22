/**
 * 用户列表管理初始化
 */
var User = {
    id: "UserTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    socket:null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
User.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '西施号', field: 'xishiNum', visible: true, align: 'center', valign: 'middle'},
        {title: '用户头像', field: 'avatar', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                if(value == ""){
                    return "无头像"
                }else {
                    return '<img class="imageShow" alt="无头像" src="'+row.avatar+'" width="50px" height="50px">'
                }
            }
        },
        {title: '用户昵称', field: 'name', visible: true, align: 'center', valign: 'middle'},
        {title: '绑定手机号', field: 'phone', visible: true, align: 'center', valign: 'middle'},
        {title: '会员等级', field: 'userLevel', visible: true, align: 'center', valign: 'middle'},
        {title: '累计打赏（西施币）', field: 'giveAmount', visible: true, align: 'center', valign: 'middle'},
        {title: '当前余额（西施币）', field: 'gbMoeny', visible: true, align: 'center', valign: 'middle'},
        {title: '最近充值（元）', field: 'nearCharge', visible: true, align: 'center', valign: 'middle'},
        {title: '累计充值（元）', field: 'totalCharge', visible: true, align: 'center', valign: 'middle'},
        {title: '累计获取（西施币）', field: 'giftAmount', visible: true, align: 'center', valign: 'middle'},
        {title: '在线状态', field: 'onlineState', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                if(row.state == 1){
                    return "已禁用"
                }else {
                    if(value == 0)return "离线"
                    if(value == 1)return "在线"
                    if(value == 2)return "忙碌"
                }
            }
        },
    ];
};

/**
 * 检查是否选中
 */
User.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        User.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加用户列表
 */
User.openAddUser = function () {
    var index = layer.open({
        type: 2,
        title: '添加用户列表',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/user/user_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看用户列表详情
 */
User.openUserDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户列表详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/user/user_update/' + User.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除用户列表
 */
User.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/user/delete", function (data) {
            Feng.success("删除成功!");
            User.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("userId",User.seItem.id);
        ajax.start();
      })
    }
};


/**
 * 启用
 */
User.start = function () {
    if (this.check()) {
        var state = User.seItem.state
        if(state == 0){
            Feng.error("该用户启用中")
            return;
        }

        Feng.confirm("确定要启用吗",function () {
            var ajax = new $ax(Feng.ctxPath + "/user/changeState", function (data) {
                Feng.success("启用成功!");
                User.table.refresh();
            }, function (data) {
                Feng.error("启用失败!" + data.responseJSON.msg + "!");
            });
            ajax.set("userId",User.seItem.id);
            ajax.set("state",0);
            ajax.start();
        })
    }
};

/**
 * 禁用
 */
User.stop = function () {
    if (this.check()) {
        var state = User.seItem.state
        if(state == 1){
            Feng.error("该用户已禁用")
            return;
        }

        Feng.confirm("确定要禁用吗",function () {
            var ajax = new $ax(Feng.ctxPath + "/user/changeState", function (data) {
                Feng.success("禁用成功!");
                User.table.refresh();
                User.socket.emit('stop_user',{loginUserId: User.seItem.id, content: "已禁用"});
            }, function (data) {
                Feng.error("禁用失败!" + data.responseJSON.msg + "!");
            });
            ajax.set("userId",User.seItem.id);
            ajax.set("state",1);
            ajax.start();
        })
    }
};





User.export = function () {
    Feng.confirm("确定要导出吗", function () {
        var state = $("#state").val();
        var xishiNum = $("#xishiNum").val();
        var name = $("#name").val();
        var phone = $("#phone").val();
        window.location.href = Feng.ctxPath + "/user/export?state="+state+"&xishiNum="+xishiNum+"&name="+name+"&phone="+phone
    });
};

/**
 * 查询用户列表列表
 */
User.search = function () {
    var queryData = {};
    queryData['state'] = $("#state").val();
    queryData['xishiNum'] = $("#xishiNum").val();
    queryData['name'] = $("#name").val();
    queryData['phone'] = $("#phone").val();
    User.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = User.initColumn();
    var table = new BSTable(User.id, "/user/list", defaultColunms);
    User.table = table.init();



    var socket;
    connect();
    function connect() {
        var loginUserNum = '0';
        var opts = {
            query: 'userId=' + loginUserNum
        };
        socket = io.connect('socket.xishi.nhys.cdnhxx.com', opts);
        socket.on('connect', function () {
            console.log("socket连接成功");
        });

        socket.on('disconnect', function() {
            console.log("socket断开连接");
        });
    }
    User.socket = socket
});
