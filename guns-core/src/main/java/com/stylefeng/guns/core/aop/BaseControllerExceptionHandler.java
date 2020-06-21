package com.stylefeng.guns.core.aop;

import com.stylefeng.guns.core.base.tips.ErrorTip;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.exception.GunsExceptionEnum;
import com.stylefeng.guns.core.exception.RequestLimitException;
import com.stylefeng.guns.core.exception.ValidateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

/**
 * 全局的的异常拦截器（拦截所有的控制器）（带有@RequestMapping注解的方法上都会拦截）
 */
public class BaseControllerExceptionHandler {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * 拦截业务异常
     *
     * @author fengshuonan
     */
    @ExceptionHandler(GunsException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorTip bizException(GunsException e) {
        log.error("业务异常:", e);
        return new ErrorTip(e.getCode(), e.getMessage());
    }

    /**
     * 请求限制
     *
     * @author LEIYU
     */
    @ExceptionHandler(RequestLimitException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ErrorTip limitException(RequestLimitException e) {
        return new ErrorTip(e.getCode(), e.getMessage());
    }

    /**
     * 拦截未知的运行时异常
     *
     * @author LEIYU
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorTip unknownException(RuntimeException e) {
        if (e instanceof ConstraintViolationException) {
            return new ErrorTip(500, "用户名为空");
        }
        if (e instanceof ValidateException) {
            return new ErrorTip(500, "参数不合法");
        }

        log.error("运行时异常:", e);
        return new ErrorTip(GunsExceptionEnum.SERVER_ERROR.getCode(), GunsExceptionEnum.SERVER_ERROR.getMsg());
    }

}
