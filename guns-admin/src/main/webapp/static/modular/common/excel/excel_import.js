/**
 * 初始化详情对话框
 */
var ExcelImport = {
    excelImportData: {}
};

/**
 * 关闭此对话框
 */
ExcelImport.close = function () {
    switch (closeType) {
        case "1":
            parent.layer.close(window.parent.Contract.layerIndex);
            window.parent.Contract.table.refresh();
            break;
        case "2":
            parent.layer.close(window.parent.Customer.layerIndex);
            window.parent.Customer.table.refresh();
            break;
        case "3":
            parent.layer.close(window.parent.Elevator.layerIndex);
            window.parent.Elevator.table.refresh();
            break;
         case "4":
            parent.layer.close(window.parent.BearProjectItems.layerIndex);
            window.parent.BearProjectItems.table.refresh();
            break;
        case "5":
            parent.layer.close(window.parent.MaintainPlan.layerIndex);
            window.parent.MaintainPlan.table.refresh();
            break;
        case "6":
            parent.layer.close(window.parent.MaintainItems.layerIndex);
            window.parent.MaintainItems.table.refresh();
            break;
    }
};


