/**
 * 关键字屏蔽管理初始化
 */
var DataSet = {
    id: "DataSetTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1,
    DataSetData: {},
    editor: null,
    ckEditor: null
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
            title: '编号', field: 'id', visible: true, align: 'center', valign: 'middle',
        },
        {title: '描述', field: 'description', visible: true, align: 'center', valign: 'middle'},
        {title: '位置', field: 'extra', visible: true, align: 'center', valign: 'middle'},
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
        var id = $("#id").val()
        var contentType = $("#contentType").val()
        if (contentType == 1 ) {
            var value = $("#value").val()
            DataSet.set("value", value)
        }else if(contentType == 2){
            var value = $("#value").val()
            var regexp =  /^[1-9][0-9]?$/
           if(!regexp.test(value)){
                Feng.error("您输入的数字有误,请输入1-99的整数")
               return;
           }
            DataSet.set("value", value)
        }else if(contentType == 3){
            var value = $("#value").val()
            var regexp =  /^[0-9]+([.]{1}[0-9]{1,2})?$/
            if(!regexp.test(value)){
                Feng.error("您输入的数字有误")
                return;
            }
            DataSet.set("value", value)
        }else {
            var text = DataSet.editor.txt.html()
            if (text == "") {
                Feng.error("请填写内容!")
                return
            }
            DataSet.set("value", text)
        }

        var name = $("#name").val()
        DataSet.set("description", name)
        DataSet.set("id", id)

        var ajax = new $ax(Feng.ctxPath + "/systemDataSet/editDataSet", function (data) {
            Feng.success("编辑成功!");
            window.location = Feng.ctxPath + '/systemDataSet/dataSet'
        }, function (data) {
            Feng.error("编辑失败!");
        });
        ajax.set(DataSet.DataSetData);
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


    $("#dataSetForm").bootstrapValidator({
        live: 'disabled',//验证时机，enabled是内容有变化就验证（默认），disabled和submitted是提交再验证
        excluded: [':disabled', ':hidden', ':not(:visible)'],//排除无需验证的控件，比如被禁用的或者被隐藏的
        submitButtons: '#btn-test',//指定提交按钮，如果验证失败则变成disabled，但我没试成功，反而加了这句话非submit按钮也会提交到action指定页面
        message: '通用的验证失败消息',//好像从来没出现过
        feedbackIcons: {//根据验证结果显示的各种图标
            valid: 'glyphicon glyphicon-ok',
            validating: 'glyphicon glyphicon-refresh'
        },
        fields: {
            name: {
                validators: {
                    notEmpty: {//检测非空,radio也可用
                        message: '*请填写标题'
                    }, stringLength: {
                        min: 1,
                        max: 10,
                        message: '标题长度必须在1到10位之间'
                    }
                }
            },
            value: {
                validators: {
                    notEmpty: {//检测非空,radio也可用
                        message: '*请填写内容'
                    }
                }
            }
        }
    });

});
$(function () {

    var E = window.wangEditor;
    var editor = new E('#editor');
    editor.customConfig.menus = [
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
    editor.customConfig.uploadImgShowBase64 = true   // 使用 base64 保存图片
    editor.create();
    DataSet.editor = editor;


})