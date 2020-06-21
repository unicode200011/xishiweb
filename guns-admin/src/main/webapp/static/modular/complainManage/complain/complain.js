/**
 * 举报列表管理初始化
 */
var Complain = {
    id: "ComplainTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Complain.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '举报人ID', field: 'userId', visible: true, align: 'center', valign: 'middle'},
        {title: '举报人昵称', field: 'userName', visible: true, align: 'center', valign: 'middle'},
        {title: '举报时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        {title: '被举报人ID', field: 'linkUid', visible: true, align: 'center', valign: 'middle'},
        {title: '被举报人昵称', field: 'linkUname', visible: true, align: 'center', valign: 'middle'},
        {title: '举报理由', field: 'reason', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Complain.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Complain.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加举报列表
 */
Complain.openAddComplain = function () {
    var index = layer.open({
        type: 2,
        title: '添加举报列表',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/complain/complain_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看举报列表详情
 */
Complain.openComplainDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '举报列表详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/complain/complain_update/' + Complain.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除举报列表
 */
Complain.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/complain/delete", function (data) {
            Feng.success("删除成功!");
            Complain.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("complainId",Complain.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询举报列表列表
 */
Complain.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Complain.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Complain.initColumn();
    var table = new BSTable(Complain.id, "/complain/list", defaultColunms);
    Complain.table = table.init();
});
