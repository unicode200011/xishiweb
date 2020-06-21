/**
 * 广告列表管理初始化
 */
var Ad = {
    id: "AdTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    socket: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Ad.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '广告分类', field: 'location', visible: true, align: 'center', valign: 'middle'},
            {title: '简要说明', field: 'intro', visible: true, align: 'center', valign: 'middle'},
            {title: '当前状态', field: 'state', visible: true, align: 'center', valign: 'middle',
                formatter:function (value, row) {
                    if(value == 0) return "启用";
                    if(value == 1) return "禁用";
                }
            },
        {title: '最后编辑时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'},

        {title: '广告图片', field: 'cover', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                if(value == ""){
                    return "无封面"
                }else {
                    return '<img class="imageShow" alt="无封面" src="'+row.cover+'" width="50px" height="50px">'
                }
            }
        },
        {title: '链接地址', field: 'url', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                // return '<a title="'+value+'"><p style="width: 130px;overflow: hidden;">'+value+'</p></a>'
                return value;
            }
        },
        {title: '广告说明', field: 'title', visible: true, align: 'center', valign: 'middle'},

    ];
};

/**
 * 检查是否选中
 */
Ad.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Ad.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加广告列表
 */
Ad.openAddAd = function () {
    var index = layer.open({
        type: 2,
        title: '添加广告列表',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/ad/ad_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看广告列表详情
 */
Ad.openAdDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '广告列表详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/ad/ad_update/' + Ad.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除广告列表
 */
Ad.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/ad/delete", function (data) {
            Feng.success("删除成功!");
            Ad.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("adId",Ad.seItem.id);
        ajax.start();
      })
    }
};



/**
 * 启用
 */
Ad.start = function () {
    if (this.check()) {
        var state = Ad.seItem.state
        if(state == 0){
            Feng.error("该广告启用中")
            return;
        }

        Feng.confirm("确定要启用吗",function () {
            var ajax = new $ax(Feng.ctxPath + "/ad/changeState", function (data) {
                Feng.success("启用成功!");
                Ad.table.refresh();
            }, function (data) {
                Feng.error("启用失败!" + data.responseJSON.msg + "!");
            });
            ajax.set("adId",Ad.seItem.id);
            ajax.set("state",0);
            ajax.start();
        })
    }
};

/**
 * 禁用
 */
Ad.stop = function () {
    if (this.check()) {
        var state = Ad.seItem.state
        if(state == 1){
            Feng.error("该广告禁用中")
            return;
        }

        Feng.confirm("确定要禁用吗",function () {
            var ajax = new $ax(Feng.ctxPath + "/ad/changeState", function (data) {
                Feng.success("禁用成功!");
                Ad.table.refresh();
            }, function (data) {
                Feng.error("禁用失败!" + data.responseJSON.msg + "!");
            });
            ajax.set("adId",Ad.seItem.id);
            ajax.set("state",1);
            ajax.start();
        })
    }
};


/**
 * 查询广告列表列表
 */
Ad.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    Ad.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Ad.initColumn();
    var table = new BSTable(Ad.id, "/ad/list", defaultColunms);
    Ad.table = table.init();


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
    Ad.socket = socket
});
