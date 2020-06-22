$(function () {
    var htmlCode = "<option value=''>请选择</option>";
    debugger
    $("#province").change(function () {
        var provinceId = $("#province").val();
        var area = $("#hiddenAreaId").val()
        $("#"+area).html(htmlCode);
        $("#city").html(htmlCode);
        $.post(Feng.ctxPath + "/areas/getCities",{"provinceId":provinceId},function (data) {
            $.each(data, function (i, n) {
                $("#city").append("<option value='" + n.cityId + "'>" + n.city + "</option>");
            });
        })

    })

    $("#city").change(function () {
        var cityId = $("#city").val();
        var area = $("#hiddenAreaId").val()
        $("#"+area).html(htmlCode);
        $.post(Feng.ctxPath + "/areas/getCounties",{"cityId":cityId},function (data) {
            $.each(data, function (i, n) {
                $("#"+area) .append("<option value='" + n.countyId + "'>" + n.county + "</option>");
            });
        })

    })
})