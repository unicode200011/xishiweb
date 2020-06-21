/**
 * 角色管理的单例
 */
var Role = {
    id: "roleTable",	//表格id
    seItem: null,		//选中的条目
    table: null,
    layerIndex: -1
};

/**
 * 初始化表格的列
 */
Role.initColumn = function () {
    var columns = [
        {field: 'selectItem', radio: true},
        {title: 'ID', field: 'id', visible: true, align: 'center', valign: 'middle'},
        {title: '角色名称', field: 'name', align: 'center', valign: 'middle', sortable: true},
        {title: '角色描述', field: 'describetion', align: 'center', valign: 'middle', sortable: true}
        ]
    return columns;
};


/**
 * 检查是否选中
 */
Role.check = function () {
    var selected = $('#' + this.id).bootstrapTable('getSelections');
    if (selected.length == 0) {
        Feng.info("请先选中表格中的某一记录！");
        return false;
    } else {
        Role.seItem = selected[0];
        return true;
    }
};

/**
 * 点击添加
 */
Role.openAddRole = function () {
    var index = layer.open({
        type: 2,
        title: '添加角色',
        area: ['800px', '450px'], //宽高
        fix: false, //不固定
        maxmin: true,
        content: Feng.ctxPath + '/role/role_add'
    });
    this.layerIndex = index;
};

/**
 * 点击修改按钮时
 */
Role.openChangeRole = function () {
    if (this.check()) {
        if(Role.seItem.name == "超级管理员"){
            Feng.error("超级管理员不能被修改!")
            return
        }
        if(Role.seItem.id == 2 ){
            Feng.error("单位角色不能被修改!")
            return
        }
        var index = layer.open({
            type: 2,
            title: '修改角色',
            area: ['800px', '450px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/role/role_edit/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 删除角色
 */
Role.delRole = function () {
    if (this.check()) {

        if(Role.seItem.name == "超级管理员" ){
            Feng.error("超级管理员不能被删除!")
            return
        }
        if(Role.seItem.id == 2 ){
            Feng.error("单位角色不能被删除!")
            return
        }

        layer.confirm('确定要删除该角色吗', {
            btn: ['确定','取消'] //按钮
        }, function(){
            $.post(Feng.ctxPath + "/role/remove",{"roleId":Role.seItem.id},function (data) {
                if(data.code == "200"){
                    Feng.success("删除成功!");
                    Role.table.refresh();
                    layer.closeAll()
                }else if(data.code == "300"){
                    Feng.error(data.msg)
                    layer.closeAll()
                }else {
                    Feng.error("删除失败")
                    layer.closeAll()
                }

            })
        }, function(){
        });
    }
};

/**
 * 权限配置
 */
Role.assign = function () {
    if (this.check()) {

        var index = layer.open({
            type: 2,
            title: '权限配置',
            area: ['800px', '450px'], //宽高
            fix: false, //不固定
            maxmin: true,
            content: Feng.ctxPath + '/role/role_assign/' + this.seItem.id
        });
        this.layerIndex = index;
    }
};

/**
 * 搜索角色
 */
Role.search = function () {
    var queryData = {};
    queryData['roleName'] = $("#roleName").val();
    Role.table.refresh({query: queryData});
}

$(function () {
    var defaultColunms = Role.initColumn();
    var table = new BSTable(Role.id, "/role/list", defaultColunms);
    table.init();
    Role.table = table;
});
