package com.stylefeng.guns.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.persistence.model.County;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * 区县 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-04-03
 */
@Component
public interface CountyMapper extends BaseMapper<County> {

    List<County> selectCountyList(Page<County> page, @Param("ew") EntityWrapper<County> entityWrapper);
}
