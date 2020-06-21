/**
 * 举报类型列表管理初始化
 */
var ComplainType = {
    id: "ComplainTypeTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ComplainType.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '举报理由', field: 'reason', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
ComplainType.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        ComplainType.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加举报类型列表
 */
ComplainType.openAddComplainType = function () {
    var index = layer.open({
        type: 2,
        title: '添加举报类型列表',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/complainType/complainType_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看举报类型列表详情
 */
ComplainType.openComplainTypeDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '举报类型列表详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/complainType/complainType_update/' + ComplainType.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除举报类型列表
 */
ComplainType.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/complainType/delete", function (data) {
            Feng.success("删除成功!");
            ComplainType.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("complainTypeId",ComplainType.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询举报类型列表列表
 */
ComplainType.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    ComplainType.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = ComplainType.initColumn();
    var table = new BSTable(ComplainType.id, "/complainType/list", defaultColunms);
    ComplainType.table = table.init();
});
