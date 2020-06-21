package com.stylefent.guns.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author: lx
 */
@Data
@ApiModel(value = "BingdingPhoneVo",description="用户对象user")
public class BingdingPhoneVo {
        @ApiModelProperty(value="手机号",name="username")
        private String phone;

        private Long userId;
}
