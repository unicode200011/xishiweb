/**
 * 用户反馈类型管理初始化
 */
var SuggestionType = {
    id: "SuggestionTypeTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
SuggestionType.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '状态 0 正常 1 删除', field: 'state', visible: true, align: 'center', valign: 'middle'},
            {title: '创建时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '修改时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
SuggestionType.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        SuggestionType.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加用户反馈类型
 */
SuggestionType.openAddSuggestionType = function () {
    var index = layer.open({
        type: 2,
        title: '添加用户反馈类型',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/suggestionType/suggestionType_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看用户反馈类型详情
 */
SuggestionType.openSuggestionTypeDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户反馈类型详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/suggestionType/suggestionType_update/' + SuggestionType.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除用户反馈类型
 */
SuggestionType.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/suggestionType/delete", function (data) {
            Feng.success("删除成功!");
            SuggestionType.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("suggestionTypeId",SuggestionType.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询用户反馈类型列表
 */
SuggestionType.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    SuggestionType.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = SuggestionType.initColumn();
    var table = new BSTable(SuggestionType.id, "/suggestionType/list", defaultColunms);
    SuggestionType.table = table.init();
});
