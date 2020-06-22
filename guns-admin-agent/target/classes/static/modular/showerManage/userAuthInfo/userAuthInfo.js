/**
 * 主播申请管理初始化
 */
var UserAuthInfo = {
    id: "UserAuthInfoTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
UserAuthInfo.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '西施号', field: 'xishiNum', visible: true, align: 'center', valign: 'middle'},
        {title: '用户昵称', field: 'userName', visible: true, align: 'center', valign: 'middle'},
        {title: '所属家族', field: 'belongAgentName', visible: true, align: 'center', valign: 'middle'},
        {title: '手机号', field: 'phone', visible: true, align: 'center', valign: 'middle'},
        {title: '申请开通时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        {title: '处理时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'},
        {title: '认证状态', field: 'state', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                if(value == 0) return "待认证";
                if(value == 2) return "认证成功";
                if(value == 3) return "认证失败";
            }
        },
    ];
};

/**
 * 检查是否选中
 */
UserAuthInfo.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        UserAuthInfo.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加主播申请
 */
UserAuthInfo.openAddUserAuthInfo = function () {
    var index = layer.open({
        type: 2,
        title: '添加主播申请',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/userAuthInfo/userAuthInfo_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看主播申请详情
 */
UserAuthInfo.openUserAuthInfoDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '主播申请详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/userAuthInfo/userAuthInfo_update/' + UserAuthInfo.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除主播申请
 */
UserAuthInfo.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/userAuthInfo/delete", function (data) {
            Feng.success("删除成功!");
            UserAuthInfo.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("userAuthInfoId",UserAuthInfo.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询主播申请列表
 */
UserAuthInfo.search = function () {
    var queryData = {};
    queryData['state'] = $("#state").val();
    queryData['xishiNum'] = $("#xishiNum").val();
    queryData['name'] = $("#name").val();
    queryData['phone'] = $("#phone").val();
    UserAuthInfo.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = UserAuthInfo.initColumn();
    var table = new BSTable(UserAuthInfo.id, "/userAuthInfo/list", defaultColunms);
    UserAuthInfo.table = table.init();
});
