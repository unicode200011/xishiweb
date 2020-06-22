
<div class="form-group">
    <label class="col-sm-3 control-label">
        @if(isNotEmpty(requirePro)){
        <span class="require_span">*</span>
        @}
        ${name}</label>
    <div class="col-sm-9">
        <textarea
                @if(isNotEmpty(readonly)){
                    readonly
                @}
                class="form-control"  rows="${rows!5}" maxlength="${maxlength!200}" cols="100%"  id="${id}" name="${id}">${value!}</textarea>
    </div>
</div>
@if(isNotEmpty(underline) && underline == 'true'){
    <div class="hr-line-dashed"></div>
@}


