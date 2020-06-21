/**
 * 家族申请记录管理初始化
 */
var AgentInvite = {
    id: "AgentInviteTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
AgentInvite.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '申请用户名', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '西施号', field: 'xishiNum', visible: true, align: 'center', valign: 'middle'},
            {title: '申请时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
            {title: '是否同意', field: 'joinState', visible: true, align: 'center', valign: 'middle',
                formatter:function (value, row) {
                    if(value == 0)return "未处理"
                    if(value == 1)return "已同意"
                    if(value == 2)return "已拒绝"
                }
            },
    ];
};

/**
 * 检查是否选中
 */
AgentInvite.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        AgentInvite.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加家族申请记录
 */
AgentInvite.openAddAgentInvite = function () {
    var index = layer.open({
        type: 2,
        title: '添加家族申请记录',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/agentInvite/agentInvite_add'
    });
    this.layerIndex = index;
};



/**
 * 打开查看家族申请记录详情
 */
AgentInvite.openAgentInviteDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '家族申请记录详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/agentInvite/agentInvite_update/' + AgentInvite.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除家族申请记录
 */
AgentInvite.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/agentInvite/delete", function (data) {
            Feng.success("删除成功!");
            AgentInvite.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("agentInviteId",AgentInvite.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询家族申请记录列表
 */
AgentInvite.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    AgentInvite.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = AgentInvite.initColumn();
    var table = new BSTable(AgentInvite.id, "/agentInvite/list", defaultColunms);
    AgentInvite.table = table.init();
});
