package com.stylefeng.guns.admin.modular.movieManage.controller;

import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.modular.movieManage.service.IMovieService;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.persistence.model.Movie;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.persistence.model.MovieCate;
import com.stylefeng.guns.admin.modular.movieManage.service.IMovieCateService;

import java.util.List;

/**
 * 电影分类控制器
 *
 * @author stylefeng
 * @Date 2019-11-12 15:01:53
 */
@Controller
@RequestMapping("/movieCate")
public class MovieCateController extends BaseController {

    private String PREFIX = "/movieManage/movieCate/";

    @Autowired
    private IMovieCateService movieCateService;
    @Autowired
    private IMovieService movieService;
    /**
     * 跳转到电影分类首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "movieCate.html";
    }

    /**
     * 跳转到添加电影分类
     */
    @RequestMapping("/movieCate_add")
    public String movieCateAdd() {
        return PREFIX + "movieCate_add.html";
    }

    /**
     * 跳转到修改电影分类
     */
    @RequestMapping("/movieCate_update/{movieCateId}")
    public String movieCateUpdate(@PathVariable Integer movieCateId, Model model) {
        MovieCate movieCate = movieCateService.selectById(movieCateId);
        model.addAttribute("item",movieCate);
        LogObjectHolder.me().set(movieCate);
        return PREFIX + "movieCate_edit.html";
    }

    /**
     * 获取电影分类列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String condition) {
        EntityWrapper<MovieCate> entityWrapper = new EntityWrapper();
        entityWrapper.orderBy("id",false);
        entityWrapper.eq("state", 0);
        Page<MovieCate> page = movieCateService.selectPage(new Page<MovieCate>(pageNumber, pageSize), entityWrapper);
        List<MovieCate> records = page.getRecords();
        for (MovieCate record : records) {
            int movieNum = movieService.selectCount(new EntityWrapper<Movie>().eq("cate_id", record.getId()).eq("state", 0));
            int watchNum = movieService.selectWatchNum(record.getId());
            int playNum = movieService.selectPlayNum(record.getId());
            record.setWatchNum(watchNum);
            record.setPlayNum(playNum);
            record.setMovieNum(movieNum);
        }
        return page;
    }

    /**
     * 新增电影分类
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增电影分类")
    public Object add(MovieCate movieCate) {
        movieCateService.insert(movieCate);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除电影分类
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除电影分类")
    @ResponseBody
    public Object delete(@RequestParam Long movieCateId) {
        int cate_id = movieService.selectCount(new EntityWrapper<Movie>().eq("cate_id", movieCateId));
        if(cate_id > 0){
            throw new GunsException(502,"该分类下有电影，不能被删除");
        }
        movieCateService.deleteById(movieCateId);
        return SUCCESS_TIP;
    }

    /**
     * 修改电影分类
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改电影分类")
    public Object update(MovieCate movieCate) {
        movieCateService.updateById(movieCate);
        return super.SUCCESS_TIP;
    }

    /**
     * 电影分类详情
     */
    @RequestMapping(value = "/detail/{movieCateId}")
    @ResponseBody
    public Object detail(@PathVariable("movieCateId") Integer movieCateId) {
        return movieCateService.selectById(movieCateId);
    }
}
