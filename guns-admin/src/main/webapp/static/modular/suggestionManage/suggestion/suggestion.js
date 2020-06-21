/**
 * 用户反馈管理初始化
 */
var Suggestion = {
    id: "SuggestionTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Suggestion.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '反馈用户ID', field: 'userId', visible: true, align: 'center', valign: 'middle'},
            {title: '反馈用户昵称', field: 'userName', visible: true, align: 'center', valign: 'middle'},
        {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        {title: '反馈内容', field: 'content', visible: true, align: 'center', valign: 'middle'},
    ];
};

/**
 * 检查是否选中
 */
Suggestion.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Suggestion.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加用户反馈
 */
Suggestion.openAddSuggestion = function () {
    var index = layer.open({
        type: 2,
        title: '添加用户反馈',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/suggestion/suggestion_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看用户反馈详情
 */
Suggestion.openSuggestionDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户反馈详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/suggestion/suggestion_update/' + Suggestion.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除用户反馈
 */
Suggestion.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/suggestion/delete", function (data) {
            Feng.success("删除成功!");
            Suggestion.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("suggestionId",Suggestion.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询用户反馈列表
 */
Suggestion.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Suggestion.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Suggestion.initColumn();
    var table = new BSTable(Suggestion.id, "/suggestion/list", defaultColunms);
    Suggestion.table = table.init();
});
