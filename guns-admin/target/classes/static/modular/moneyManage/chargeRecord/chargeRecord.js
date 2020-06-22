/**
 * 充值记录列表管理初始化
 */
var ChargeRecord = {
    id: "ChargeRecordTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ChargeRecord.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '西施号', field: 'xishiNum', visible: true, align: 'center', valign: 'middle'},
        {title: '充值交易号', field: 'orderNum', visible: true, align: 'center', valign: 'middle'},
        {title: '充值时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        {title: '充值金额（元）', field: 'rmbAmount', visible: true, align: 'center', valign: 'middle'},
        {title: '获取西施币', field: 'xishiAmount', visible: true, align: 'center', valign: 'middle'},
        {title: '充值类型', field: 'source', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                if(value == 0)return "微信"
                if(value == 1)return "支付宝"
            }
        },
    ];
};

/**
 * 检查是否选中
 */
ChargeRecord.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        ChargeRecord.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加充值记录列表
 */
ChargeRecord.openAddChargeRecord = function () {
    var index = layer.open({
        type: 2,
        title: '添加充值记录列表',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/chargeRecord/chargeRecord_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看充值记录列表详情
 */
ChargeRecord.openChargeRecordDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '充值记录列表详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/chargeRecord/chargeRecord_update/' + ChargeRecord.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除充值记录列表
 */
ChargeRecord.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/chargeRecord/delete", function (data) {
            Feng.success("删除成功!");
            ChargeRecord.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("chargeRecordId",ChargeRecord.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询充值记录列表列表
 */
ChargeRecord.search = function () {
    var queryData = {};
    queryData['source'] = $("#source").val();
    queryData['xishiNum'] = $("#xishiNum").val();
    queryData['name'] = $("#name").val();
    queryData['condition'] = $("#condition").val();
    var userId = $("#userId").val()
    queryData['userId'] = $("#userId").val();

    ChargeRecord.table.refresh({query: queryData});
};

$(function () {


    var defaultColunms = ChargeRecord.initColumn();
    var table = new BSTable(ChargeRecord.id, "/chargeRecord/list", defaultColunms);
    ChargeRecord.table = table.init();
    ChargeRecord.search()
});
