package com.stylefeng.guns.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.persistence.model.Live;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-12-05
 */
public interface LiveMapper extends BaseMapper<Live> {

    List<Live> selectLivePage(Page<Live> livePage,@Param("ew") EntityWrapper<Live> entityWrapper);

    List<Map> selectHotList();

}
