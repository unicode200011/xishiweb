/**
 * 账户分类管理初始化
 */
var BankAccount = {
    id: "BankAccountTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
BankAccount.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '账户类型', field: 'name', visible: true, align: 'center', valign: 'middle',},
            {title: '账户号码', field: 'acount', visible: true, align: 'center', valign: 'middle',
                formatter:function (value,row) {
                    if(value == 0) return "关闭"
                    if(value == 1) return "开启"
                }
            },
            {title: '账户二维码', field: 'code', visible: true, align: 'center', valign: 'middle',
                formatter:function (value,row) {
                    if(value == 0) return "关闭"
                    if(value == 1) return "开启"
                }
            },
            {title: '创建事件', field: 'createTime', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
BankAccount.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        BankAccount.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加账户分类
 */
BankAccount.openAddBankAccount = function () {
    var index = layer.open({
        type: 2,
        title: '添加账户分类',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/bankAccount/bankAccount_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看账户分类详情
 */
BankAccount.openBankAccountDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '账户分类详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/bankAccount/bankAccount_update/' + BankAccount.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除账户分类
 */
BankAccount.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/bankAccount/delete", function (data) {
            Feng.success("删除成功!");
            BankAccount.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("bankAccountId",BankAccount.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询账户分类列表
 */
BankAccount.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    BankAccount.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = BankAccount.initColumn();
    var table = new BSTable(BankAccount.id, "/bankAccount/list", defaultColunms);
    BankAccount.table = table.init();
});
