/**
 * 直播管理管理初始化
 */
var Live = {
    id: "LiveTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    socket: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Live.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '房间号', field: 'roomNum', visible: true, align: 'center', valign: 'middle'},
        {title: '西施号', field: 'xishiNum', visible: true, align: 'center', valign: 'middle'},
        {title: '主播昵称', field: 'showerName', visible: true, align: 'center', valign: 'middle'},
        {title: '直播模式', field: 'liveMode', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
               var liveState = row.liveState
                if(liveState == 1){
                    if(value == 0) return "免费模式"
                    if(value == 1) return "密码模式"
                    if(value == 2) return "常规收费"
                    if(value == 3) return "计时收费"
                }else {
                    return "直播结束"
                }

            }
        },
        {title: '直播开始时间', field: 'liveStartTime', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                var liveState = row.liveState
                if(liveState == 1){
                   return value
                }else {
                    return ""
                }
            }
        },
        {title: '直播时长', field: 'showTime', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                var liveState = row.liveState
                if(liveState == 1){
                    return value
                }else {
                    return "0"
                }
            }
        },
        {title: '直播封面', field: 'cover', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                if(value == ""){
                    return "无封面"
                }else {
                    return '<img class="imageShow" alt="无封面" src="'+row.cover+'" width="50px" height="50px">'
                }
            }
        },
        {title: '直播标题', field: 'name', visible: true, align: 'center', valign: 'middle'},
        {title: '播流地址', field: 'pullUrl', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                var liveState = row.liveState
                if(liveState == 1){
                    return value
                }else {
                    return ""
                }
            }},
        {title: '当前观看人数', field: 'liveWatchNow', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                var liveState = row.liveState
                if(liveState == 1){
                    return value
                }else {
                    return "0"
                }
            }
        },
        {title: '历史观看人数', field: 'totalLiveWatch', visible: true, align: 'center', valign: 'middle'},
        {title: '本场收益(西施币)', field: 'amount', visible: true, align: 'center', valign: 'middle'},
    ];
};

/**
 * 检查是否选中
 */
Live.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Live.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加直播管理
 */
Live.openAddLive = function () {
    var index = layer.open({
        type: 2,
        title: '添加直播管理',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/live/live_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看直播管理详情
 */
Live.openLiveDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '直播管理详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/live/live_update/' + Live.seItem.id
        });
        this.layerIndex = index;
    }
};
Live.openLiveWarnDetail = function () {
    if (this.check()) {
        var liveState = Live.seItem.liveState
        if(liveState == 0){
            Feng.error("该用户未直播")
            return;
        }

        var index = layer.open({
            type: 2,
            title: '发送直播警告',
            area: ['590px', '420px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/live/live_warn/' + Live.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除直播管理
 */
Live.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/live/delete", function (data) {
            Feng.success("删除成功!");
            Live.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("liveId",Live.seItem.id);
        ajax.start();
      })
    }
};
/**
 * 禁播
 */
Live.stop = function () {
    if (this.check()) {
        var state = Live.seItem.state
        if(state == 1){
            Feng.error("该账户已禁播")
            return;
        }

        Feng.confirm("确定要禁播吗",function () {
            layer.prompt({title: '输入禁播原因', formType: 2}, function(text, index){
                layer.close(index);
                var ajax = new $ax(Feng.ctxPath + "/live/stop", function (data) {
                    Feng.success("禁播成功!");
                    Live.table.refresh();
                    sendStop(text)
                }, function (data) {
                    Feng.error("禁播失败!" + data.responseJSON.msg + "!");
                });
                ajax.set("liveId",Live.seItem.id);
                ajax.start();
            });
      })
    }
};
Live.start = function () {
    if (this.check()) {
        var state = Live.seItem.state
        if(state == 0){
            Feng.error("该账户已启用")
            return;
        }
        Feng.confirm("确定要启用吗",function () {
            var ajax = new $ax(Feng.ctxPath + "/live/start", function (data) {
                Feng.success("启用成功!");
                Live.table.refresh();
            }, function (data) {
                Feng.error("启用失败!" + data.responseJSON.msg + "!");
            });
            ajax.set("liveId",Live.seItem.id);
            ajax.start();
      })
    }
};



/**
 * 查询直播管理列表
 */
Live.search = function () {
    var queryData = {};
    queryData['roomNum'] = $("#roomNum").val();
    queryData['xishiNum'] = $("#xishiNum").val();
    queryData['showerName'] = $("#showerName").val();
    queryData['liveMode'] = $("#liveMode").val();
    queryData['name'] = $("#name").val();
    Live.table.refresh({query: queryData});
};

function sendStop(reason) {
    Live.socket.emit('socket_live_stop:', {liveId: Live.seItem.id, streamName: Live.seItem.streamName, reason: reason});
}
$(function () {
    var defaultColunms = Live.initColumn();
    var table = new BSTable(Live.id, "/live/list", defaultColunms);
    Live.table = table.init();


    var socket;
    connect();
    function connect() {
        var loginUserNum = '0';
        var opts = {
            query: 'userId=' + loginUserNum
        };
        socket = io.connect('socket.xishi.nhys.cdnhxx.com', opts);
        socket.on('connect', function () {
            console.log("socket连接成功");
        });

        socket.on('disconnect', function() {
            console.log("socket断开连接");
        });
    }
    Live.socket = socket
});
