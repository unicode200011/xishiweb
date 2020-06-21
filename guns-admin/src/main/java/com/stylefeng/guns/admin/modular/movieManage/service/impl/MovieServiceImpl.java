package com.stylefeng.guns.admin.modular.movieManage.service.impl;

import com.stylefeng.guns.persistence.model.Movie;
import com.stylefeng.guns.persistence.dao.MovieMapper;
import com.stylefeng.guns.admin.modular.movieManage.service.IMovieService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 电影 服务实现类
 * </p>
 *
 * @author stylefeng
 * @since 2019-11-12
 */
@Service
public class MovieServiceImpl extends ServiceImpl<MovieMapper, Movie> implements IMovieService {

    @Override
    public int selectWatchNum(Long id) {
        return baseMapper.selectWatchNum(id);
    }

    @Override
    public int selectPlayNum(Long id) {
        return baseMapper.selectPlayNum(id);
    }
}
