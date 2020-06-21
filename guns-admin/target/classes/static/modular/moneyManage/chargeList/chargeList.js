/**
 * 充值列表管理初始化
 */
var ChargeList = {
    id: "ChargeListTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
ChargeList.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '充值金额(元)', field: 'rmb', visible: true, align: 'center', valign: 'middle'},
        {title: '兑换西施币', field: 'xishiNum', visible: true, align: 'center', valign: 'middle'},
        {title: '汇率', field: 'rate', visible: true, align: 'center', valign: 'middle'},
        {title: '备注说明', field: 'remark', visible: true, align: 'center', valign: 'middle'},
        {title: '最后修改时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
ChargeList.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        ChargeList.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加充值列表
 */
ChargeList.openAddChargeList = function () {
    var index = layer.open({
        type: 2,
        title: '添加充值列表',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/chargeList/chargeList_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看充值列表详情
 */
ChargeList.openChargeListDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '充值列表详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/chargeList/chargeList_update/' + ChargeList.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除充值列表
 */
ChargeList.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/chargeList/delete", function (data) {
            Feng.success("删除成功!");
            ChargeList.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("chargeListId",ChargeList.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 修改汇率
 */
ChargeList.updateRate = function () {
     Feng.confirm("确定要修改汇率吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/chargeList/updateRate", function (data) {
            Feng.success("修改汇率成功!");
            ChargeList.table.refresh();
        }, function (data) {
            Feng.error("修改汇率失败!" + data.responseJSON.msg + "!");
        });
         var rate = $("#rate").val()
         var regexp =  /^[0-9]+([.]{1}[0-9]{1,2})?$/
         if(!regexp.test(rate)){
             Feng.error("您输入的数字有误")
             return;
         }
        ajax.set("rate",rate);
        ajax.start();
      })
};

/**
 * 查询充值列表列表
 */
ChargeList.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    ChargeList.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = ChargeList.initColumn();
    var table = new BSTable(ChargeList.id, "/chargeList/list", defaultColunms);
    ChargeList.table = table.init();
});
