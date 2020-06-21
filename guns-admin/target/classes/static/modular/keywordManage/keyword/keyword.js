/**
 * 敏感词管理初始化
 */
var Keyword = {
    id: "KeywordTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Keyword.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '敏感词', field: 'word', visible: true, align: 'center', valign: 'middle'},
            {title: '添加时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
Keyword.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Keyword.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加敏感词
 */
Keyword.openAddKeyword = function () {
    var index = layer.open({
        type: 2,
        title: '添加敏感词',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/keyword/keyword_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看敏感词详情
 */
Keyword.openKeywordDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '敏感词详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/keyword/keyword_update/' + Keyword.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除敏感词
 */
Keyword.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/keyword/delete", function (data) {
            Feng.success("删除成功!");
            Keyword.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });

        ajax.set("keywordId",Keyword.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询敏感词列表
 */
Keyword.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Keyword.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Keyword.initColumn();
    var table = new BSTable(Keyword.id, "/keyword/list", defaultColunms);
    Keyword.table = table.init();
});
