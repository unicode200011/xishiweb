/**
 * 提现记录列表管理初始化
 */
var Withdrawal = {
    id: "WithdrawalTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Withdrawal.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '提现申请编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '提现金额（西施币）', field: 'money', visible: true, align: 'center', valign: 'middle'},
        {title: '账户名称', field: 'rate', visible: true, align: 'center', valign: 'middle'},
        {title: '账户类型', field: 'way', visible: true, align: 'center', valign: 'middle'},
        {title: '账户号码', field: 'rate', visible: true, align: 'center', valign: 'middle'},
        {title: '申请时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        {title: '审核时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        {title: '提现状态', field: 'rate', visible: true, align: 'center', valign: 'middle'},
        {title: '到账状态', field: 'rate', visible: true, align: 'center', valign: 'middle'},
    ];
};

/**
 * 检查是否选中
 */
Withdrawal.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Withdrawal.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加提现记录列表
 */
Withdrawal.openAddWithdrawal = function () {
    var index = layer.open({
        type: 2,
        title: '添加提现记录列表',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/withdrawal/withdrawal_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看提现记录列表详情
 */
Withdrawal.openWithdrawalDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '提现记录列表详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/withdrawal/withdrawal_update/' + Withdrawal.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除提现记录列表
 */
Withdrawal.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/withdrawal/delete", function (data) {
            Feng.success("删除成功!");
            Withdrawal.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("withdrawalId",Withdrawal.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询提现记录列表列表
 */
Withdrawal.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Withdrawal.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Withdrawal.initColumn();
    var table = new BSTable(Withdrawal.id, "/withdrawal/list", defaultColunms);
    Withdrawal.table = table.init();
});
