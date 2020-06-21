/**
 * 用户等级管理初始化
 */
var UserGrade = {
    id: "UserGradeTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
UserGrade.initColumn = function () {
    var levelone='http://nahanhb1.oss-cn-qingdao.aliyuncs.com/resources/grade_image/4cd3d8ee00fa4a4599b3b88e9c6cf076.png'
    var levelfive='http://nahanhb1.oss-cn-qingdao.aliyuncs.com/resources/grade_image/55572731c0f44260b0983072e4f54e20.png'
    var levelten='http://nahanhb1.oss-cn-qingdao.aliyuncs.com/resources/grade_image/511713c410c840e1955ff23282e714f2.png'
    return [
        {field: 'selectItem', radio: true},
            {title: '会员等级', field: 'level', visible: true, align: 'center', valign: 'middle'},
            {title: '等级标识', field: 'image', visible: true, align: 'center', valign: 'middle',
                formatter:function (value, row) {
                    var html = ''
                    if(row.level < 5){
                        for(var i =0;i<row.level;i++){
                            html +='<img class="imageShow" alt="无标识" src="'+levelone+'" width="15px" height="15px">'
                        }
                    }else if(row.level>5 && row.level<10){
                        html +='<img class="imageShow" alt="无标识" src="'+levelfive+'" width="15px" height="15px">'
                        for(var i =0;i<row.level-5;i++){
                            html +='<img class="imageShow" alt="无标识" src="'+levelone+'" width="15px" height="15px">'
                        }
                    }else if(row.level == 5){
                        html +='<img class="imageShow" alt="无标识" src="'+levelfive+'" width="15px" height="15px">'

                    }else if(row.level == 10){
                        html +='<img class="imageShow" alt="无标识" src="'+levelten+'" width="15px" height="15px">'
                    }
                    return html
                }
            },
            {title: '达标接收西施币', field: 'amount', visible: true, align: 'center', valign: 'middle'},
            {title: '等级颜色', field: 'color', visible: true, align: 'center', valign: 'middle'},
    ];
};

/**
 * 检查是否选中
 */
UserGrade.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if(selected.length == 0){
        Feng.info("请先选中表格中的某一记录！");
        return false;
    }else{
        UserGrade.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加用户等级
 */
UserGrade.openAddUserGrade = function () {
    var index = layer.open({
        type: 2,
        title: '添加用户等级',
        area: ['990px', '520px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/userGrade/userGrade_add'
    });
    this.layerIndex = index;
};

/**
 * 打开查看用户等级详情
 */
UserGrade.openUserGradeDetail = function () {
    if (this.check()) {
        var index = layer.open({
            type: 2,
            title: '用户等级详情',
            area: ['990px', '520px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/userGrade/userGrade_update/' + UserGrade.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除用户等级
 */
UserGrade.delete = function () {
    if (this.check()) {
     Feng.confirm("确定要删除吗",function () {
        var ajax = new $ax(Feng.ctxPath + "/userGrade/delete", function (data) {
            Feng.success("删除成功!");
            UserGrade.table.refresh();
        }, function (data) {
            Feng.error("删除失败!" + data.responseJSON.msg + "!");
        });
        ajax.set("userGradeId",UserGrade.seItem.id);
        ajax.start();
      })
    }
};

/**
 * 查询用户等级列表
 */
UserGrade.search = function () {
    var queryData = {};
    queryData['condition'] = $("#condition").val();
    UserGrade.table.refresh({query: queryData});
};

$(function () {
    var defaultColunms = UserGrade.initColumn();
    var table = new BSTable(UserGrade.id, "/userGrade/list", defaultColunms);
    UserGrade.table = table.init();
});
