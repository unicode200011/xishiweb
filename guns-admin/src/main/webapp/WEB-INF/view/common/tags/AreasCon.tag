<div class="input-group">
    <div class="input-group-btn">
        <button data-toggle="dropdown" class="btn btn-white dropdown-toggle"
                type="button">${name}
        </button>
    </div>
    <div class="row">
        <div class="col-sm-4">
            <select  class="form-control" id="province" name="province">
                <option value="">请选择</option>
                @for(province in provinces){
                <option value="${province.provinceId}">${province.province}</option>
                @}
            </select>
        </div>
        <div class="col-sm-4">
            <select  class="form-control" id="city" name="city">
                <option value="">请选择</option>
            </select>
        </div>
        <div class="col-sm-4">
            <input type="hidden" id="hiddenAreaId" value="${areaId}">
            <select  class="form-control" id="${areaId}" name="${areaId}">
                <option value="">请选择</option>
            </select>
        </div>
    </div>
</div>
<script src="${ctxPath}/static/modular/common/excel/areas.js"></script>