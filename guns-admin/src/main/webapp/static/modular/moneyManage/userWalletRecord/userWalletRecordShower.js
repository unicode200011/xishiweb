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
        {title: '主播西施号', field: 'xishiNum', visible: true, align: 'center', valign: 'middle'},
        {title: '主播昵称', field: 'userName', visible: true, align: 'center', valign: 'middle'},
        {title: '收益时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},

        {title: '收益礼物', field: 'custName', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                if(row.type == 2){
                    return row.custName;
                }

                return row.remark;
            }
        },
        {title: '数量', field: 'custNum', visible: true, align: 'center', valign: 'middle'},
        {title: '收益总价值(西施币)', field: 'amount', visible: true, align: 'center', valign: 'middle'},
        {title: '打赏人', field: 'linkUName', visible: true, align: 'center', valign: 'middle'},
        {title: '打赏人西施号', field: 'xishiNum2', visible: true, align: 'center', valign: 'middle'},
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
    var table = new BSTable(UserWalletRecord.id, "/userWalletRecord/showerList?userId="+userId, defaultColunms);
    UserWalletRecord.table = table.init();
});
