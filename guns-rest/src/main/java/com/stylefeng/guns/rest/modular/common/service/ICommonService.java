package com.stylefeng.guns.rest.modular.common.service;

import com.stylefeng.guns.persistence.model.CommonData;
import com.stylefeng.guns.rest.core.util.BaseJson;
import com.stylefent.guns.entity.query.CommonQuery;

/**
 * @author: lx
 */
public interface ICommonService {

    /**
     * 根据ID查询公共参数
     *
     * @param id
     * @return
     */
    CommonData findCommonDataById(Long id);

    /**
     * 从缓存获取设置
     *
     * @param id
     * @return
     */
    String findCommonDataFromCache(Long id);

    /**
     * 根据 key type 查询公共参数
     *
     * @param
     * @return
     */
    CommonData findCommonDataByKT(String key, String type);



    /**
     * 搜索
     *
     * @param query
     * @return
     */
    BaseJson search(CommonQuery query);


    /**
     * 帮助中心
     *
     * @param query
     * @return
     */
    BaseJson helpCenter(CommonQuery query);

    /**
     * 礼物列表
     *
     * @param query
     * @return
     */
    BaseJson gifts(CommonQuery query);

    /**
     * 财富榜
     * @param query
     * @return
     */
    BaseJson richList(CommonQuery query);

    BaseJson getWithRate();
}
