/**
 * 代理商管理初始化
 */
var Agent = {
    id: "AgentTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Agent.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '代理商ID', field: 'agentNum', visible: true, align: 'center', valign: 'middle'},
        {title: '代理商名称', field: 'agentName', visible: true, align: 'center', valign: 'middle'},
        {title: '绑定手机号', field: 'agentPhone', visible: true, align: 'center', valign: 'middle'},
        {title: '审核状态', field: 'auditState', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                if(value == 0)return "待审核"
                if(value == 1)return "通过"
                if(value == 2)return "驳回"
            }
        },
        {title: '审核时间', field: 'auditTime', visible: true, align: 'center', valign: 'middle'},
        {title: '主播个数', field: 'showerNum', visible: true, align: 'center', valign: 'middle'},
        {title: '累计收益（西施币）', field: 'totalGiftAmount', visible: true, align: 'center', valign: 'middle'},
        {title: '账户状态', field: 'state', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                if(value == 0)return "正常"
                if(value == 1)return "已禁用"
            }
        },
        {title: '设置抽成比', field: 'adminRate', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                return '<input type="button" class="btn btn-info btn-xs" onclick="Agent.openAdminRate(' + row.id + ')" value="设置抽成比">'
            }
        },
    ];
};

/**
 * 检查是否选中
 */
Agent.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Agent.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加代理商
 */
Agent.openAddAgent = function () {
    var index = layer.open({
        type: 2,
        title: '添加代理商',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/agent/agent_add'
    });
    this.layerIndex = index;
};

/**
 * 打开抽成比设置
 */
Agent.openAdminRate = function (id) {
    var index = layer.open({
        type: 2,
        title: '设置抽成比',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/agent/openRate/' + id
    });
    this.layerIndex = index;
};

/**
 * 打开查看代理商详情
 */
Agent.openAgentDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '代理商详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/agent/agent_update/' + Agent.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除代理商
 */
Agent.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/agent/delete", function (data) {
            Feng.success("删除成功!");
            Agent.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("agentId",Agent.seItem.id);
        ajax.start();
      })
    }
};


/**
 * 账户启用
 */
Agent.start = function () {
    if (this.check()) {
        var state = Agent.seItem.state
        if(state == 0){
            Feng.error("该账户启用中")
            return;
        }
        Feng.confirm("确定要启用账户吗",function () {
            var ajax = new $ax(Feng.ctxPath + "/agent/changeState", function (data) {
                Feng.success("账户启用成功!");
                Agent.table.refresh();
            }, function (data) {
                Feng.error("账户启用失败!" + data.responseJSON.msg + "!");
            });
            ajax.set("agentId",Agent.seItem.id);
            ajax.set("state",0);
            ajax.start();
        })
    }
};

/**
 * 禁用账户
 */
Agent.stop = function () {
    if (this.check()) {
        var state = Agent.seItem.state
        if(state == 1){
            Feng.error("该账户已禁用")
            return;
        }

        Feng.confirm("确定要禁用账户吗",function () {
            var ajax = new $ax(Feng.ctxPath + "/agent/changeState", function (data) {
                Feng.success("禁用账户成功!");
                Agent.table.refresh();
            }, function (data) {
                Feng.error("禁用账户失败!" + data.responseJSON.msg + "!");
            });
            ajax.set("agentId",Agent.seItem.id);
            ajax.set("state",1);
            ajax.start();
        })
    }
};




/**
 * 查询代理商列表
 */
Agent.search = function () {
    var queryData = {};
    queryData['auditState'] = $("#auditState").val();
    queryData['state'] = $("#state").val();
    queryData['permission'] = $("#permission").val();
    queryData['agentName'] = $("#agentName").val();
    queryData['agentPhone'] = $("#agentPhone").val();
    Agent.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Agent.initColumn();
    var table = new BSTable(Agent.id, "/agent/list", defaultColunms);
    Agent.table = table.init();
});
