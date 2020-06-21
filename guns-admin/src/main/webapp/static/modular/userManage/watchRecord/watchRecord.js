/**
 * 观看记录管理初始化
 */
var WatchRecord = {
    id: "WatchRecordTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
WatchRecord.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '用户西施号', field: 'userXishiNum', visible: true, align: 'center', valign: 'middle'},
            {title: '主播西施号', field: 'showerXishiNum', visible: true, align: 'center', valign: 'middle'},
            {title: '主播昵称', field: 'showerName', visible: true, align: 'center', valign: 'middle'},
            {title: '进入房间时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '分钟数', field: 'time', visible: true, align: 'center', valign: 'middle'},
            {title: '打赏金额', field: 'amount', visible: true, align: 'center', valign: 'middle'},
            {title: '打赏礼物总量', field: 'giftNum', visible: true, align: 'center', valign: 'middle'},
    ];
};

/**
 * 检查是否选中
 */
WatchRecord.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        WatchRecord.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加观看记录
 */
WatchRecord.openAddWatchRecord = function () {
    var index = layer.open({
        type: 2,
        title: '添加观看记录',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/watchRecord/watchRecord_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看观看记录详情
 */
WatchRecord.openWatchRecordDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '观看记录详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/watchRecord/watchRecord_update/' + WatchRecord.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除观看记录
 */
WatchRecord.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/watchRecord/delete", function (data) {
            Feng.success("删除成功!");
            WatchRecord.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("watchRecordId",WatchRecord.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询观看记录列表
 */
WatchRecord.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    WatchRecord.table.refresh({query: queryData});
};

$(function () {
    var userId = $("#userId").val()
    var defaultColunms = WatchRecord.initColumn();
    var table = new BSTable(WatchRecord.id, "/watchRecord/list?userId="+userId, defaultColunms);
    WatchRecord.table = table.init();
});
