<div class="form-group">
    <label class="col-sm-3 control-label">${name}</label>
    <div class="col-sm-9">
        <div class="row">
            <div class="col-sm-4">
                <select  class="form-control" id="province" name="province">
                    <option value="">请选择</option>
                    @for(province in provinces){
                    <option value="${province.provinceId}"
                            @if(isNotEmpty(provinceId)){
                            @if(provinceId == province.provinceId){
                            selected
                            @}
                            @}
                    >${province.province}</option>
                    @}
                </select>
            </div>
            <div class="col-sm-4">
                <select  class="form-control" id="city" name="city">
                    <option value="">请选择</option>
                    @if(isNotEmpty(citys)){
                    @for(city in citys){
                    <option value="${city.cityId}"
                            @if(isNotEmpty(cityId)){
                            @if(city.cityId == cityId){
                            selected
                            @}
                            @}
                    >${city.city}</option>
                    @}
                    @}
                </select>
            </div>
            <div class="col-sm-4">
                <input type="hidden" id="hiddenAreaId" value="${areaId}">
                <select  class="form-control" id="${areaId}" name="${areaId}">
                    <option value="">请选择</option>
                    @if(isNotEmpty(countys)){
                    @for(county in countys){
                    <option value="${county.countyId}"
                            @if(isNotEmpty(countyId)){
                            @if(county.countyId == countyId){
                            selected
                            @}
                            @}
                    >${county.county}</option>
                    @}
                    @}
                </select>
            </div>
        </div>

    </div>
</div>
<script src="${ctxPath}/static/modular/common/excel/areas.js"></script>