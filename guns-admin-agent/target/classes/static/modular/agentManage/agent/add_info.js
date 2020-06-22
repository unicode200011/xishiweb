/**
 * 初始化代理商详情对话框
 */
var AddInfoDlg = {
    agentInfoData : {}
};

/**
 * 清除数据
 */
AddInfoDlg.clearData = function() {
    this.agentInfoData = {};
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AddInfoDlg.set = function(key, val) {
    this.agentInfoData[key] = (typeof val == "undefined") ? $("#" + key).val() : val;
    return this;
}

/**
 * 设置对话框中的数据
 *
 * @param key 数据的名称
 * @param val 数据的具体值
 */
AddInfoDlg.get = function(key) {
    return $("#" + key).val();
}

