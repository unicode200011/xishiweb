/**
 * 代理商钱包记录管理初始化
 */
var AgentWalletRecord = {
    id: "AgentWalletRecordTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
AgentWalletRecord.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '代理商id', field: 'agentId', visible: true, align: 'center', valign: 'middle'},
            {title: '关联主播id', field: 'showerId', visible: true, align: 'center', valign: 'middle'},
            {title: '收益类型 2.获得礼物+ 3：观看常规收费直播 4观看计时收费直播 5:看电影 6：坐骑 7：靓号8：提现', field: 'type', visible: true, align: 'center', valign: 'middle'},
            {title: '收支类型：0 ：收入 1：支出', field: 'io', visible: true, align: 'center', valign: 'middle'},
            {title: '备注', field: 'remark', visible: true, align: 'center', valign: 'middle'},
            {title: '收支对象 用户id', field: 'linkUid', visible: true, align: 'center', valign: 'middle'},
            {title: '插入记录时代理商钱包余额', field: 'walletAmount', visible: true, align: 'center', valign: 'middle'},
            {title: '送礼物时 保存的礼物id  直播时的 直播间id', field: 'custId', visible: true, align: 'center', valign: 'middle'},
            {title: '消费数量 礼物个数 计时模式 分钟数', field: 'custNum', visible: true, align: 'center', valign: 'middle'},
            {title: '消费的对象名字 如礼物名  直播间名字', field: 'custName', visible: true, align: 'center', valign: 'middle'},
            {title: '直播记录id', field: 'liveRecordId', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
AgentWalletRecord.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        AgentWalletRecord.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加代理商钱包记录
 */
AgentWalletRecord.openAddAgentWalletRecord = function () {
    var index = layer.open({
        type: 2,
        title: '添加代理商钱包记录',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/agentWalletRecord/agentWalletRecord_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看代理商钱包记录详情
 */
AgentWalletRecord.openAgentWalletRecordDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '代理商钱包记录详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/agentWalletRecord/agentWalletRecord_update/' + AgentWalletRecord.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除代理商钱包记录
 */
AgentWalletRecord.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/agentWalletRecord/delete", function (data) {
            Feng.success("删除成功!");
            AgentWalletRecord.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("agentWalletRecordId",AgentWalletRecord.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询代理商钱包记录列表
 */
AgentWalletRecord.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    AgentWalletRecord.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = AgentWalletRecord.initColumn();
    var table = new BSTable(AgentWalletRecord.id, "/agentWalletRecord/list", defaultColunms);
    AgentWalletRecord.table = table.init();
});
