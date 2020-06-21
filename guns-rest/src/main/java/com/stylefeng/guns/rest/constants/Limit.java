package com.stylefeng.guns.rest.constants;

/**
 * 请求限制类型
 *
 * Time开头: 表示指定时间内只能请求一次接口,需要配置(count)属性,不需要配置(interval)属性;
 * COUNT: 表示指定间隔时间(interval)内可以请求指定的(count)次数,需要配置count,interval属性值;
 * MACHINE: 表示限定以设备号或者用户ID为限制条件,指定时间内只能请求一次接口,需要配置(count)属性,不需要配置(interval)属性;
 *
 * @author: lx
 */
public enum Limit {

    TIME_OF_SECOND,
    TIME_OF_MINUTES,
    TIME_OF_HOUR,
    TIME_OF_DAY,
    COUNT,
    MACHINE;

    Limit() {
    }

}
