/**
 * 消费记录列表管理初始化
 */
var UserWalletRecord = {
    id: "UserWalletRecordTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
UserWalletRecord.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '用户西施号', field: 'xishiNum', visible: true, align: 'center', valign: 'middle'},
        {title: '用户昵称', field: 'userName', visible: true, align: 'center', valign: 'middle'},
        {title: '消费时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        {title: '类型', field: 'type', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                if(value == 1) return "打赏"
                if(value == 3 || value == 4) return "直播间"
                if(value == 5) return "电影"
                if(value == 6) return "坐骑"
                if(value == 7) return "靓号"
            }
        },
        {title: '具体事项', field: 'custName', visible: true, align: 'center', valign: 'middle'},
        {title: '数量', field: 'custNum', visible: true, align: 'center', valign: 'middle'},
        {title: '西施币', field: 'amount', visible: true, align: 'center', valign: 'middle'},
        {title: '收益对象', field: 'linkUName', visible: true, align: 'center', valign: 'middle'},
        {title: '所属家族', field: 'agent', visible: true, align: 'center', valign: 'middle'},
    ];
};

/**
 * 检查是否选中
 */
UserWalletRecord.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        UserWalletRecord.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加消费记录列表
 */
UserWalletRecord.openAddUserWalletRecord = function () {
    var index = layer.open({
        type: 2,
        title: '添加消费记录列表',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/userWalletRecord/userWalletRecord_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看消费记录列表详情
 */
UserWalletRecord.openUserWalletRecordDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '消费记录列表详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/userWalletRecord/userWalletRecord_update/' + UserWalletRecord.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 导出
 */
UserWalletRecord.import = function () {
    Feng.confirm("确定要导出吗",function () {
        Feng.success("导出成功!");
        var a = document.createElement("a");
        a.href =Feng.ctxPath + "/userWalletRecord/getExcel";
        a.click();
    })
};

/**
 * 删除消费记录列表
 */
UserWalletRecord.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/userWalletRecord/delete", function (data) {
            Feng.success("删除成功!");
            UserWalletRecord.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("userWalletRecordId",UserWalletRecord.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询消费记录列表列表
 */
UserWalletRecord.search = function () {
    var queryData = {};
    queryData['source'] = $("#source").val();
    queryData['xishiNum'] = $("#xishiNum").val();
    queryData['name'] = $("#name").val();
    queryData['condition'] = $("#condition").val();
    UserWalletRecord.table.refresh({query: queryData});
};

$(function () {
    var userId = $("#userId").val()
    var defaultColunms = UserWalletRecord.initColumn();
    var table = new BSTable(UserWalletRecord.id, "/userWalletRecord/list?userId="+userId, defaultColunms);
    UserWalletRecord.table = table.init();
});
