/**
 * 电影分类管理初始化
 */
var MovieCate = {
    id: "MovieCateTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
MovieCate.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
            {title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle'},
            {title: '电影分类', field: 'name', visible: true, align: 'center', valign: 'middle'},
            {title: '影片数量', field: 'movieNum', visible: true, align: 'center', valign: 'middle'},
            {title: '当前观看人数', field: 'watchNum', visible: true, align: 'center', valign: 'middle'},
            {title: '播放总量', field: 'playNum', visible: true, align: 'center', valign: 'middle'}
    ];
};

/**
 * 检查是否选中
 */
MovieCate.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        MovieCate.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加电影分类
 */
MovieCate.openAddMovieCate = function () {
    var index = layer.open({
        type: 2,
        title: '添加电影分类',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/movieCate/movieCate_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看电影分类详情
 */
MovieCate.openMovieCateDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '电影分类详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/movieCate/movieCate_update/' + MovieCate.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除电影分类
 */
MovieCate.delete = function () {
    if (this.check()) {
        var movieNum = MovieCate.seItem.movieNum
        if(movieNum > 0){
            Feng.error("该分类下有电影，不能被删除")
            return
        }
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/movieCate/delete", function (data) {
            Feng.success("删除成功!");
            MovieCate.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("movieCateId",MovieCate.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询电影分类列表
 */
MovieCate.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    MovieCate.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = MovieCate.initColumn();
    var table = new BSTable(MovieCate.id, "/movieCate/list", defaultColunms);
    MovieCate.table = table.init();
});
