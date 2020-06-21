/**
 * 银行账户管理初始化
 */
var BankUser = {
    id: "BankUserTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
BankUser.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '账户分类id', field: 'bankAccountId', visible: true, align: 'center', valign: 'middle'},
            {title: '用户id', field: 'userId', visible: true, align: 'center', valign: 'middle'},
            {title: '卡号信息', field: 'bankCard', visible: true, align: 'center', valign: 'middle'},
            {title: '二维码', field: 'qrcode', visible: true, align: 'center', valign: 'middle'},
            {title: '', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '类型0：用户账号 1：代理商账号', field: 'type', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
BankUser.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        BankUser.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加银行账户
 */
BankUser.openAddBankUser = function () {
    var index = layer.open({
        type: 2,
        title: '添加银行账户',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/bankUser/bankUser_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看银行账户详情
 */
BankUser.openBankUserDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '银行账户详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bankUser/bankUser_update/' + BankUser.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除银行账户
 */
BankUser.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/bankUser/delete", function (data) {
            Feng.success("删除成功!");
            BankUser.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("bankUserId",BankUser.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询银行账户列表
 */
BankUser.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    BankUser.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = BankUser.initColumn();
    var table = new BSTable(BankUser.id, "/bankUser/list", defaultColunms);
    BankUser.table = table.init();
});
