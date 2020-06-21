/**
 * 提现设置管理初始化
 */
var WithdrawalSet = {
    id: "WithdrawalSetTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
WithdrawalSet.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '提现西施币', field: 'gbMoney', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'},
            {title: '最后更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
WithdrawalSet.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        WithdrawalSet.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加提现设置
 */
WithdrawalSet.openAddWithdrawalSet = function () {
    var index = layer.open({
        type: 2,
        title: '添加提现设置',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/withdrawalSet/withdrawalSet_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看提现设置详情
 */
WithdrawalSet.openWithdrawalSetDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '提现设置详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/withdrawalSet/withdrawalSet_update/' + WithdrawalSet.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除提现设置
 */
WithdrawalSet.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/withdrawalSet/delete", function (data) {
            Feng.success("删除成功!");
            WithdrawalSet.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("withdrawalSetId",WithdrawalSet.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询提现设置列表
 */
WithdrawalSet.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    WithdrawalSet.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = WithdrawalSet.initColumn();
    var table = new BSTable(WithdrawalSet.id, "/withdrawalSet/list", defaultColunms);
    WithdrawalSet.table = table.init();
});
