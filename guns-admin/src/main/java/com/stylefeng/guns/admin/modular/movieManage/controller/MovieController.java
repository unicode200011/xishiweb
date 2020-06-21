package com.stylefeng.guns.admin.modular.movieManage.controller;

import com.stylefeng.guns.admin.common.annotion.BussinessLog;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.admin.core.shiro.ShiroKit;
import com.stylefeng.guns.admin.modular.movieManage.service.IMovieCateService;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.persistence.model.Ad;
import com.stylefeng.guns.persistence.model.MovieCate;
import org.springframework.web.bind.annotation.RequestMapping;
import com.stylefeng.guns.core.base.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.annotation.Autowired;
import com.stylefeng.guns.admin.core.log.LogObjectHolder;
import org.springframework.web.bind.annotation.RequestParam;
import com.stylefeng.guns.persistence.model.Movie;
import com.stylefeng.guns.admin.modular.movieManage.service.IMovieService;

import java.util.Date;
import java.util.List;

/**
 * 电影控制器
 *
 * @author stylefeng
 * @Date 2019-11-12 15:01:07
 */
@Controller
@RequestMapping("/movie")
public class MovieController extends BaseController {

    private String PREFIX = "/movieManage/movie/";

    @Autowired
    private IMovieService movieService;
    @Autowired
    private IMovieCateService movieCateService;
    /**
     * 跳转到电影首页
     */
    @RequestMapping("")
    public String index(Model model) {
        List<MovieCate> cates = movieCateService.selectList(new EntityWrapper<MovieCate>().eq("state", 0));
        model.addAttribute("cates",cates);
        return PREFIX + "movie.html";
    }

    /**
     * 跳转到添加电影
     */
    @RequestMapping("/movie_add")
    public String movieAdd(Model model) {
        List<MovieCate> cates = movieCateService.selectList(new EntityWrapper<MovieCate>().eq("state", 0));
        model.addAttribute("cates",cates);
        return PREFIX + "movie_add.html";
    }

    /**
     * 跳转到修改电影
     */
    @RequestMapping("/movie_update/{movieId}")
    public String movieUpdate(@PathVariable Integer movieId, Model model) {
        Movie movie = movieService.selectById(movieId);
        model.addAttribute("item",movie);
        List<MovieCate> cates = movieCateService.selectList(new EntityWrapper<MovieCate>().eq("state", 0));
        model.addAttribute("cates",cates);
        return PREFIX + "movie_edit.html";
    }

    /**
     * 获取电影列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                       @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                       String source,String cate,String state,String name,String uploader) {
        EntityWrapper<Movie> entityWrapper = new EntityWrapper();
        if(StrKit.isNotEmpty(source)) entityWrapper.eq("source",source);
        if(StrKit.isNotEmpty(cate)) entityWrapper.eq("cate_id",cate);
        if(StrKit.isNotEmpty(state)) entityWrapper.eq("state",state);
        if(StrKit.isNotEmpty(name)) entityWrapper.like("name",name);
        if(StrKit.isNotEmpty(uploader)) entityWrapper.like("upload_user",uploader);
        entityWrapper.eq("state",0);
        entityWrapper.orderBy("id",false);
        Page<Movie> page = movieService.selectPage(new Page<Movie>(pageNumber, pageSize), entityWrapper);
        List<Movie> records = page.getRecords();
        for (Movie record : records) {
            MovieCate movieCate = movieCateService.selectById(record.getCateId());
            if(movieCate != null){
                record.setCateName(movieCate.getName());
            }
        }
        return page;
    }

    /**
     * 新增电影
     */
    @RequestMapping(value = "/add")
    @ResponseBody
    @BussinessLog(value = "新增电影")
    public Object add(Movie movie) {
        movie.setUploadUser(ShiroKit.getUser().getName());
        movieService.insert(movie);
        return super.SUCCESS_TIP;
    }

    @RequestMapping(value = "/changeState")
    @BussinessLog(value = "改变电影状态")
    @ResponseBody
    public Object changeState(@RequestParam Long movieId,Integer state) {
        Movie movie = movieService.selectById(movieId);
        movie.setState(state);
        movieService.updateById(movie);
        return SUCCESS_TIP;
    }
    /**
     * 删除电影
     */
    @RequestMapping(value = "/delete")
    @BussinessLog(value = "删除电影")
    @ResponseBody
    public Object delete(@RequestParam Long movieId) {
        Movie movie = movieService.selectById(movieId);
        movie.setState(2);
        movieService.updateById(movie);
        return SUCCESS_TIP;
    }

    /**
     * 修改电影
     */
    @RequestMapping(value = "/update")
    @ResponseBody
    @BussinessLog(value = "修改电影")
    public Object update(Movie movie) {
        movieService.updateById(movie);
        return super.SUCCESS_TIP;
    }

    /**
     * 电影详情
     */
    @RequestMapping(value = "/detail/{movieId}")
    @ResponseBody
    public Object detail(@PathVariable("movieId") Integer movieId) {
        return movieService.selectById(movieId);
    }
}
