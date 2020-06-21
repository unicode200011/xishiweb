package com.stylefeng.guns.admin.modular.movieManage.service;

import com.stylefeng.guns.persistence.model.Movie;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 * 电影 服务类
 * </p>
 *
 * @author stylefeng
 * @since 2019-11-12
 */
public interface IMovieService extends IService<Movie> {

    int selectWatchNum(Long id);

    int selectPlayNum(Long id);
}
