/**
 * 电影管理初始化
 */
var Movie = {
    id: "MovieTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Movie.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '上传渠道', field: 'source', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                if(value == 0) return "平台";
                if(value == 1) return "代理商";
            }
        },
        {title: '电影分类', field: 'cateName', visible: true, align: 'center', valign: 'middle'},
        {title: '名称', field: 'name', visible: true, align: 'center', valign: 'middle'},
        {title: '封面图片', field: 'cover', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                if(value == ""){
                    return "无封面"
                }else {
                    return '<img class="imageShow" alt="无封面" src="'+row.cover+'" width="50px" height="50px">'
                }
            }
        },
        {title: '上架时间', field: 'createTime', visible: true, align: 'center', valign: 'middle'},
        {title: '当前观看人数', field: 'watchNum', visible: true, align: 'center', valign: 'middle'},
        {title: '播放量', field: 'playNum', visible: true, align: 'center', valign: 'middle'},
        {title: '价格(西施币)', field: 'price', visible: true, align: 'center', valign: 'middle'},
        {title: '电影链接', field: 'url', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                return '<input type="button" class="btn btn-xs btn-info" onclick="showView(\''+row.url+'\')" value="点击播放">'
            }
        },
        {title: '状态', field: 'state', visible: true, align: 'center', valign: 'middle',
            formatter:function (value, row) {
                if(value == 0) return "正常";
                if(value == 1) return "已下架";
            }
        },
        {title: '上传人', field: 'uploadUser', visible: true, align: 'center', valign: 'middle'},
    ];
};

function showView(url){
    layer.open({
        type: 1,
        title: false,
        area: ['630px', '360px'],
        shade: 0.8,
        closeBtn: 0,
        shadeClose: true,
        content: '<video controls src="'+url+'">'
    });
}
/**
 * 检查是否选中
 */
Movie.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        Movie.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加电影
 */
Movie.openAddMovie = function () {
    var index = layer.open({
        type: 2,
        title: '添加电影',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/movie/movie_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看电影详情
 */
Movie.openMovieDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '电影详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/movie/movie_update/' + Movie.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除电影
 */
Movie.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/movie/delete", function (data) {
            Feng.success("删除成功!");
            Movie.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("movieId",Movie.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 启用
 */
Movie.start = function () {
    if (this.check()) {
        var state = Movie.seItem.state
        if(state == 0){
            Feng.error("该电影启用中")
            return;
        }

        Feng.confirm("确定要启用吗",function () {
            var ajax = new $ax(Feng.ctxPath + "/movie/changeState", function (data) {
                Feng.success("启用成功!");
                Movie.table.refresh();
            }, function (data) {
                Feng.error("启用失败!" + data.responseJSON.msg + "!");
            });
            ajax.set("movieId",Movie.seItem.id);
            ajax.set("state",0);
            ajax.start();
        })
    }
};

/**
 * 禁用
 */
Movie.stop = function () {
    if (this.check()) {
        var state = Movie.seItem.state
        if(state == 1){
            Feng.error("该电影已下架")
            return;
        }

        Feng.confirm("确定要下架吗",function () {
            var ajax = new $ax(Feng.ctxPath + "/movie/changeState", function (data) {
                Feng.success("下架成功!");
                Movie.table.refresh();
            }, function (data) {
                Feng.error("下架失败!" + data.responseJSON.msg + "!");
            });
            ajax.set("movieId",Movie.seItem.id);
            ajax.set("state",1);
            ajax.start();
        })
    }
};


/**
 * 查询电影列表
 */
Movie.search = function () {
    var queryData = {};
    queryData['source'] = $("#source").val();
    queryData['cate'] = $("#cate").val();
    queryData['state'] = $("#state").val();
    queryData['name'] = $("#name").val();
    queryData['uploMovieer'] = $("#uploMovieer").val();
    Movie.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = Movie.initColumn();
    var table = new BSTable(Movie.id, "/movie/list", defaultColunms);
    Movie.table = table.init();
});
