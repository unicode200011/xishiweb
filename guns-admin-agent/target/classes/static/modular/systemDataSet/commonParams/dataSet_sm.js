/**
 * 关键字屏蔽管理初始化
 */
var DataSet = {
    id: "DataSetTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    DataSetData: {},
    editor1: null,
    editor2: null,
    editor3: null,
    editor4: null,
};
DataSet.set = function (key, val) {
    this.DataSetData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}
/**
 * 初始化表格的列
 */
DataSet.initColumn = function () {
    return [
        {field: 'selectItem', radio: true},
        {
            title: 'ID', field: 'id', visible: true, align: 'center', valign: 'middle',
        },
        {title: '描述', field: 'description', visible: true, align: 'center', valign: 'middle'},
        {title: '更新时间', field: 'updateTime', visible: true, align: 'center', valign: 'middle'},
    ];
};

/**
 * 检查是否选中
 */
DataSet.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        DataSet.seItem = selected[0];
        return true;
    }
};

/**
 * 跳转数据设置页
 */
DataSet.openAddDataSet = function () {
    if (this.check()) {
        window.location = Feng.ctxPath + '/systemDataSet/dataSet_Edit/' + this.seItem.id
    }
};


/**
 * 取消
 */
DataSet.close = function () {
    window.location = Feng.ctxPath + '/systemDataSet/dataSet'
};

function change() {
    $("#change").val("1")
}

/**
 * 编辑
 */
DataSet.editSubmit = function () {
    $("#dataSetForm").bootstrapValidator('validate');//提交验证
    if ($("#dataSetForm").data('bootstrapValidator').isValid()) {//获取验证结果，如果成功，执行下面代码
        debugger
        var text1 = DataSet.editor1.txt.html()//指引手册
        var text2 = DataSet.editor2.txt.html()//卡片传说
        var text3 = DataSet.editor3.txt.html()//天书残页
        var text4 = DataSet.editor4.txt.html()//百科图鉴
        if (text1 == "") {
            Feng.error("请填写指引手册内容!")
            return
        }
        if (text2 == "") {
            Feng.error("请填写卡片传说内容!")
            return
        }
        if (text3 == "") {
            Feng.error("请填写天书残页内容!")
            return
        }
        if (text4 == "") {
            Feng.error("请填写百科图鉴内容!")
            return
        }
        var ajax = new $ax(Feng.ctxPath + "/systemDataSet/editDataSetSm", function (data) {
            Feng.success("编辑成功!");
            window.location = Feng.ctxPath + '/systemDataSet/dataSet'
        }, function (data) {
            Feng.error("编辑失败!");
        });
        ajax.set("text1",text1);
        ajax.set("text2",text2);
        ajax.set("text3",text3);
        ajax.set("text4",text4);
        ajax.start();
    }
};

/**
 * 删除关键字屏蔽
 */
DataSet.delete = function () {
    if (this.check()) {
        var ajax = new $ax(Feng.ctxPath + "/systemDataSet/delete", function (data) {
            Feng.success("删除成功!");
            KeywordForbid.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("DataSetId", this.seItem.id);
        ajax.start();
    }
};

/**
 * 查询关键字屏蔽列表
 */
DataSet.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    queryData['startTime'] = $("#startTime").val();
    queryData['endTime'] = $("#endTime").val();
    DataSet.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = DataSet.initColumn();
    var table = new BSTable(DataSet.id, "/systemDataSet/DataSetlist", defaultColunms);
    DataSet.table = table.init();
});
$(function () {
    var menu = [
        'head',  // 标题
        'bold',  // 粗体
        'fontSize',  // 字号
        'fontName',  // 字体
        'foreColor',  // 文字颜色
        'backColor',  // 背景颜色
        'list',  // 列表
        'justify',  // 对齐方式
        'emoticon',  // 表情
        'image',  // 插入图片
        'table',  // 表格
        'video',  // 插入视频
        'undo',  // 撤销
        'redo'  // 重复
    ]

    var E = window.wangEditor;
    var editor1 = new E('#editor1');
    var editor2 = new E('#editor2');
    var editor3 = new E('#editor3');
    var editor4 = new E('#editor4');
    editor1.customConfig.menus = menu
    editor1.customConfig.uploadImgShowBase64 = true   // 使用 base64 保存图片
    editor1.create();
    DataSet.editor1 = editor1;

    editor2.customConfig.menus = menu
    editor2.customConfig.uploadImgShowBase64 = true   // 使用 base64 保存图片
    editor2.create();
    DataSet.editor2 = editor2;

    editor3.customConfig.menus = menu
    editor3.customConfig.uploadImgShowBase64 = true   // 使用 base64 保存图片
    editor3.create();
    DataSet.editor3 = editor3;

    editor4.customConfig.menus = menu
    editor4.customConfig.uploadImgShowBase64 = true   // 使用 base64 保存图片
    editor4.create();
    DataSet.editor4 = editor4;


})