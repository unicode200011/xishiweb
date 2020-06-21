package com.stylefeng.guns.rest.common.annotatoin;

import com.stylefeng.guns.rest.constants.Limit;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.lang.annotation.*;

/**
 * @author: lx
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@Order(Ordered.HIGHEST_PRECEDENCE)
public @interface RequestLimit {

    /**
     * 限制类型
     */
    Limit limit();

    /**
     * 限制请求次数/时间;
     */
    int count();

    /**
     * 次数限制间隔时间
     */
    long interval() default 10;

    /**
     * 是否使用token参数
     *
     * @return
     */
    boolean tokenAttr() default false;

    /**
     * token参数名
     *
     * @return
     */
    String tokenAttrName() default "";

    /**
     * 操作錯誤提示信息
     *
     * @return
     */
    String msg() default "操作太快,请稍后再试";

}
