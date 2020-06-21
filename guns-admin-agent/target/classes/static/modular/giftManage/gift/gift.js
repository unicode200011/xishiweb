/**
 * 礼物管理初始化
 */
var Gift = {
    id: "GiftTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Gift.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '礼物名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
        {title: '礼物样式', field: 'image', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                if(value == ""){
                    return "无样式"
                }else {
                    return '<img class="imageShow" alt="无样式" src="'+row.image+'" width="50px" height="50px">'
                }
            }
        },
        {title: '礼物单价（西施币）', field: 'money', visible: true, align: 'center', valign: 'middle'},
        {title: '状态', field: 'state', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                if(value == 0)return "显示"
                if(value == 1)return "隐藏"
            }
        },
        {title: '使用数量', field: 'useCount', visible: true, align: 'center', valign: 'middle'},
        {title: '添加时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
    ];
};

/**
 * 检查是否选中
 */
Gift.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Gift.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加礼物
 */
Gift.openAddGift = function () {
    var index = layer.open({
        type: 2,
        title: '添加礼物',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/gift/gift_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看礼物详情
 */
Gift.openGiftDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '礼物详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/gift/gift_update/' + Gift.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除礼物
 */
Gift.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/gift/delete", function (data) {
            Feng.success("删除成功!");
            Gift.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("giftId",Gift.seItem.id);
        ajax.start();
      })
    }
};


/**
 * 启用
 */
Gift.start = function () {
    if (this.check()) {
        var state = Gift.seItem.state
        if(state == 0){
            Feng.error("该礼物显示中")
            return;
        }
        Feng.confirm("确定要显示吗",function () {
            var ajax = new $ax(Feng.ctxPath + "/gift/changeState", function (data) {
                Feng.success("显示成功!");
                Gift.table.refresh();
            }, function (data) {
                Feng.error("显示失败!" + data.responseJSON.msg + "!");
            });
            ajax.set("giftId",Gift.seItem.id);
            ajax.set("state",0);
            ajax.start();
        })
    }
};

/**
 * 禁用
 */
Gift.stop = function () {
    if (this.check()) {
        var state = Gift.seItem.state
        if(state == 1){
            Feng.error("该礼物已隐藏")
            return;
        }

        Feng.confirm("确定要隐藏吗",function () {
            var ajax = new $ax(Feng.ctxPath + "/gift/changeState", function (data) {
                Feng.success("隐藏成功!");
                Gift.table.refresh();
            }, function (data) {
                Feng.error("隐藏失败!" + data.responseJSON.msg + "!");
            });
            ajax.set("giftId",Gift.seItem.id);
            ajax.set("state",1);
            ajax.start();
        })
    }
};



/**
 * 查询礼物列表
 */
Gift.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['state'] = $("#state").val();
    Gift.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Gift.initColumn();
    var table = new BSTable(Gift.id, "/gift/list", defaultColunms);
    Gift.table = table.init();
});
