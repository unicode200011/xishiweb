package com.stylefeng.guns.rest.modular.auth.validator;


import com.stylefeng.guns.rest.core.util.BaseJson;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author fengshuonan
 * @date 2017-08-23 11:48
 */
public interface IReqValidator {

    /**
     * 通过请求参数验证
     *
     * @author fengshuonan
     * @Date 2017/8/23 11:49
     */
    BaseJson validate(Map body, HttpServletResponse response);
}
