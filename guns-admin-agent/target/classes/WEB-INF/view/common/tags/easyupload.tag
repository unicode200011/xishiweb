<div class="form-group">
    <input type="hidden" id="${id}" value="${value!""}">
    <label class="col-sm-3 control-label">
        @if(isNotEmpty(requirePro)){
        <span class="require_span">*</span>
        @}
        ${name}</label>
    <div class="col-sm-9">
        @if(isNotEmpty(value)){
            @if(isNotEmpty(valueType) && valueType == 'video'){
                <div style="width: 100%;height:100%">
                    <video controls="controls" src="${value}" width="${width!'30%'}" height="${height!'30%'}"></video>
                </div>
            @}
            @if(isNotEmpty(valueType) && valueType == 'image'){
                <img src="${value}" width="125px" height="125px">
            @}
            @if(isNotEmpty(valueType) && valueType == 'audio'){
                <audio controls="controls" src="${value}">${value}</audio>
            @}
        @}
        <div
                @if(isNotEmpty(readonly)){
                    style="display: none"
                @}
                id="${id}Container" ></div>
        @if(isNotEmpty(remark)){
            <span style="font-size: 13px;color: grey" >${remark}</span>
        @}
    </div>
</div>


@if(isNotEmpty(underline) && underline == 'true'){
    <div class="hr-line-dashed"></div>
@}


