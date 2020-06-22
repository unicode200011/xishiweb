/**
 * 代理商处理记录管理初始化
 */
var AgentDealRecord = {
    id: "AgentDealRecordTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
AgentDealRecord.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '代理商id', field: 'agentId', visible: true, align: 'center', valign: 'middle'},
            {title: '处理人', field: 'adminId', visible: true, align: 'center', valign: 'middle'},
            {title: '处理备注', field: 'remark', visible: true, align: 'center', valign: 'middle'},
            {title: '申请时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '处理时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
AgentDealRecord.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        AgentDealRecord.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加代理商处理记录
 */
AgentDealRecord.openAddAgentDealRecord = function () {
    var index = layer.open({
        type: 2,
        title: '添加代理商处理记录',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/agentDealRecord/agentDealRecord_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看代理商处理记录详情
 */
AgentDealRecord.openAgentDealRecordDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '代理商处理记录详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/agentDealRecord/agentDealRecord_update/' + AgentDealRecord.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除代理商处理记录
 */
AgentDealRecord.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/agentDealRecord/delete", function (data) {
            Feng.success("删除成功!");
            AgentDealRecord.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("agentDealRecordId",AgentDealRecord.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询代理商处理记录列表
 */
AgentDealRecord.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    AgentDealRecord.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = AgentDealRecord.initColumn();
    var table = new BSTable(AgentDealRecord.id, "/agentDealRecord/list", defaultColunms);
    AgentDealRecord.table = table.init();
});
