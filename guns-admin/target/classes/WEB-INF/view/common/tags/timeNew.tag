<div class="form-group">
    <label class="col-sm-3 control-label">${name}</label>
    <div class="col-sm-9">
        <input class="form-control"
               @if(isNotEmpty(value)){
               value="${value}"
               @}
               readonly id="${id}" name="${id}" type="text">
    </div>

    <script src="/static/js/laydate/laydate.js"></script>
    <script>
        laydate.render({
            elem: ${id}
        });
    </script>
</div>