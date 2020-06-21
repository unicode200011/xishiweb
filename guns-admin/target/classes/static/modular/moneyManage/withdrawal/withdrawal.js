/**
 * 提现记录列表管理初始化
 */
var Withdrawal = {
    id: "WithdrawalTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Withdrawal.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '提现申请编号', field: 'orderNum', visible: true, align: 'center', valign: 'middle'},
        {title: '提现类型', field: 'type', visible: true, align: 'center', valign: 'middle',
            formatter(value,row){
                if(value == 0) return "个人"
                if(value == 1) return "代理商"
            }
        },
        {title: '代理商ID', field: 'agentNum', visible: true, align: 'center', valign: 'middle'},
        {title: '主播ID', field: 'xishiNum', visible: true, align: 'center', valign: 'middle'},
        {title: '用户昵称 / 代理商名称', field: 'userName', visible: true, align: 'center', valign: 'middle'},
        {title: '提现金额（西施币）', field: 'money', visible: true, align: 'center', valign: 'middle'},
        {title: '真实姓名', field: 'realName', visible: true, align: 'center', valign: 'middle'},
        {title: '申请时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        {title: '账户名称', field: 'accountName', visible: true, align: 'center', valign: 'middle'},
        {title: '二维码', field: 'qrcode', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                if(value == ""){
                    return "无二维码"
                }else {
                    return '<img class="imageShow" alt="无二维码" src="'+row.qrcode+'" width="50px" height="50px">'
                }
            }
        },
        {title: '账户号码', field: 'accountNum', visible: true, align: 'center', valign: 'middle'},
        {title: '审核状态', field: 'auditState', visible: true, align: 'center', valign: 'middle',
            formatter(value,row){
                if(value == 0) return "待审核"
                if(value == 1 || value == 4) return "通过"
                if(value == 3) return "驳回"
            }
        },
        {title: '打款状态', field: 'state', visible: true, align: 'center', valign: 'middle',
            formatter(value,row){
                if(value == 4) return "已打款"
                if(value == 1) return "待打款"
            }
        },
    ];
};

/**
 * 检查是否选中
 */
Withdrawal.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Withdrawal.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加提现记录列表
 */
Withdrawal.openAddWithdrawal = function () {
    var index = layer.open({
        type: 2,
        title: '添加提现记录列表',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/withdrawal/withdrawal_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看提现记录列表详情
 */
Withdrawal.openWithdrawalDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '提现记录列表详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/withdrawal/withdrawal_update/' + Withdrawal.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 打开查看提现记录列表详情
 */
Withdrawal.changeState = function (state) {
    if (this.check()) {
        var stateVal = Withdrawal.seItem.state
        var tip = "";
        if(state == 1){
            if(stateVal != 0){
                Feng.error("该记录已审核")
                return
            }
            tip = "确定要通过审核吗";
        }
        if(state == 3){
            if(stateVal != 0){
                Feng.error("该记录已审核")
                return
            }
            tip = "确定要驳回吗";
        }
        if(state == 4){
            if(stateVal != 1){
                Feng.error("该记录未审核通过")
                return
            }
            tip = "确定已打款了吗";
        }
        Feng.confirm(tip,function () {
            var ajax = new $ax(Feng.ctxPath + "/withdrawal/changeState", function (data) {
                Feng.success("修改完成!");
                Withdrawal.table.refresh();
            }, function (data) {
                Feng.error(data.responseJSON.msg + "!");
            });
            ajax.set("withdrawalId",Withdrawal.seItem.id);
            ajax.set("state",state);
            ajax.start();
        })
    }
};

/**
 * 删除提现记录列表
 */
Withdrawal.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/withdrawal/delete", function (data) {
            Feng.success("删除成功!");
            Withdrawal.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("withdrawalId",Withdrawal.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询提现记录列表列表
 */
Withdrawal.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['state'] = $("#state").val();
    queryData['stateTwo'] = $("#stateTwo").val();
    queryData['type'] = $("#type").val();
    queryData['source'] = $("#source").val();
    Withdrawal.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Withdrawal.initColumn();
    var table = new BSTable(Withdrawal.id, "/withdrawal/list", defaultColunms);
    Withdrawal.table = table.init();
});
