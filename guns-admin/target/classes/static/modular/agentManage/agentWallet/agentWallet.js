/**
 * 代理商钱包管理初始化
 */
var AgentWallet = {
    id: "AgentWalletTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
AgentWallet.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '账户余额', field: 'gbAmount', visible: true, align: 'center', valign: 'middle'},
            {title: '收益总额', field: 'totalGiftAmount', visible: true, align: 'center', valign: 'middle'},
            {title: '代理商id', field: 'agentId', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
AgentWallet.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        AgentWallet.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加代理商钱包
 */
AgentWallet.openAddAgentWallet = function () {
    var index = layer.open({
        type: 2,
        title: '添加代理商钱包',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/agentWallet/agentWallet_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看代理商钱包详情
 */
AgentWallet.openAgentWalletDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '代理商钱包详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/agentWallet/agentWallet_update/' + AgentWallet.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除代理商钱包
 */
AgentWallet.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/agentWallet/delete", function (data) {
            Feng.success("删除成功!");
            AgentWallet.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("agentWalletId",AgentWallet.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询代理商钱包列表
 */
AgentWallet.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    AgentWallet.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = AgentWallet.initColumn();
    var table = new BSTable(AgentWallet.id, "/agentWallet/list", defaultColunms);
    AgentWallet.table = table.init();
});
