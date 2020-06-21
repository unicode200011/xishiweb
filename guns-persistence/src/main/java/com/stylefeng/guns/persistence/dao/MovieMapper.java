package com.stylefeng.guns.persistence.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.persistence.model.Movie;

/**
 * <p>
 * 电影 Mapper 接口
 * </p>
 *
 * @author stylefeng
 * @since 2019-11-12
 */
public interface MovieMapper extends BaseMapper<Movie> {

    int selectWatchNum(Long id);

    int selectPlayNum(Long id);
}
