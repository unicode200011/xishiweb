/**
 * 直播记录管理初始化
 */
var LiveRecord = {
    id: "LiveRecordTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
LiveRecord.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '主播西施号', field: 'xishiNum', visible: true, align: 'center', valign: 'middle'},
        {title: '开播时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        {title: '直播时长（分钟）', field: 'showTime', visible: true, align: 'center', valign: 'middle'},
        {title: '直播标题', field: 'name', visible: true, align: 'center', valign: 'middle'},
        {title: '历史观看人数', field: 'liveWatchTotal', visible: true, align: 'center', valign: 'middle'},
        {title: '打赏礼物总量', field: 'liveGiftNum', visible: true, align: 'center', valign: 'middle'},
        {title: '直播收益（西施币）', field: 'amount', visible: true, align: 'center', valign: 'middle'},
    ];
};

/**
 * 检查是否选中
 */
LiveRecord.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        LiveRecord.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加直播记录
 */
LiveRecord.openAddLiveRecord = function () {
    var index = layer.open({
        type: 2,
        title: '添加直播记录',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/liveRecord/liveRecord_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看直播记录详情
 */
LiveRecord.openLiveRecordDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '直播记录详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/liveRecord/liveRecord_update/' + LiveRecord.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除直播记录
 */
LiveRecord.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/liveRecord/delete", function (data) {
            Feng.success("删除成功!");
            LiveRecord.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("liveRecordId",LiveRecord.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询直播记录列表
 */
LiveRecord.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    LiveRecord.table.refresh({query: queryData});
};

$(function () {
    var userId = $("#userId").val()
    var defaultColunms = LiveRecord.initColumn();
    var table = new BSTable(LiveRecord.id, "/liveRecord/list?userId="+userId, defaultColunms);
    LiveRecord.table = table.init();
});
