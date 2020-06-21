/**
 * 直播等级管理初始化
 */
var LiveGrade = {
    id: "LiveGradeTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
LiveGrade.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '主播等级', field: 'level', visible: true, align: 'center', valign: 'middle'},
        {title: '等级标识', field: 'image', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                if(value == ""){
                    return "无标识"
                }else {
                    return '<img class="imageShow" alt="无标识" src="'+row.image+'" width="50px" height="50px">'
                }
            }
        },
        {title: '达标接收西施币', field: 'amount', visible: true, align: 'center', valign: 'middle'},
    ];
};

/**
 * 检查是否选中
 */
LiveGrade.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        LiveGrade.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加直播等级
 */
LiveGrade.openAddLiveGrade = function () {
    var index = layer.open({
        type: 2,
        title: '添加直播等级',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/liveGrade/liveGrade_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看直播等级详情
 */
LiveGrade.openLiveGradeDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '直播等级详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/liveGrade/liveGrade_update/' + LiveGrade.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除直播等级
 */
LiveGrade.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/liveGrade/delete", function (data) {
            Feng.success("删除成功!");
            LiveGrade.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("liveGradeId",LiveGrade.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询直播等级列表
 */
LiveGrade.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    LiveGrade.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = LiveGrade.initColumn();
    var table = new BSTable(LiveGrade.id, "/liveGrade/list", defaultColunms);
    LiveGrade.table = table.init();
});
