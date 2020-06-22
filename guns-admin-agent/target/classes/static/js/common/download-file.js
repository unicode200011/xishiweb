function downloadFile(fileName) {

    window.open(Feng.ctxPath + "/upload/download?fileName=" + fileName);
}

/*
    * 下载PDF
    */
function downPDF(title,requestURL){
    console.log($("#pdf").html())
    debugger;
    window.open(Feng.ctxPath + "/bearProject/downPdf?requestURL=" + encodeURIComponent(requestURL)+"&title="+title)
};


function checkImg(url) {
    layer.open({
        type: 1,
        title: false,
        closeBtn: 0,
        skin: 'layui-layer-nobg', //没有背景色
        shadeClose: true,
        content: "<img src="+url+" width='100%' height='100%'>"
    });
};
