package com.stylefeng.guns.admin.core.util;

import com.stylefeng.guns.admin.modular.areas.service.ICitiesService;
import com.stylefeng.guns.admin.modular.areas.service.ICountyService;
import com.stylefeng.guns.admin.modular.areas.service.IProvincesService;
import com.stylefeng.guns.core.constant.RedisConstants;
import com.stylefeng.guns.core.util.SpringContextHolder;
import com.stylefeng.guns.core.util.redis.RedisService;
import com.stylefeng.guns.persistence.model.Cities;
import com.stylefeng.guns.persistence.model.County;
import com.stylefeng.guns.persistence.model.Provinces;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
public class AreasUtil {

   private static IProvincesService provincesService = SpringContextHolder.getBean(IProvincesService.class);
   private static ICitiesService citiesService = SpringContextHolder.getBean(ICitiesService.class);
   private static ICountyService countyService = SpringContextHolder.getBean(ICountyService.class);
   private static RedisService redisService = SpringContextHolder.getBean(RedisService.class);

   public static void getAllList(Model model){
       checkPCListCache();
       Object o = redisService.get(RedisConstants.KW_ALL_CITIES);
       Object o1 = redisService.get(RedisConstants.KW_ALL_PROVICE);
       Object o2 = redisService.get(RedisConstants.KW_ALL_COUNTIES);
       model.addAttribute("cities",o);
       model.addAttribute("provinces",o1);
       model.addAttribute("counties",o2);
   }

    public static void getListByPidCid(Model model,String provinceId){
        checkPCListCache();
        List<Cities> citiesList = (List<Cities>)redisService.get(RedisConstants.KW_ALL_CITIES);
        List<Provinces> provincesList = (List<Provinces>)redisService.get(RedisConstants.KW_ALL_PROVICE);
        List<Cities> citiesCollect = citiesList.stream().filter(x -> x.getProvinceId().equals(provinceId)).collect(Collectors.toList());
        model.addAttribute("cities",citiesCollect);
        model.addAttribute("provinces",provincesList);
    }

    public static Object getCityListByPid(String provinceId){
        checkPCListCache();
        List<Cities> citiesList = (List<Cities>)redisService.get(RedisConstants.KW_ALL_CITIES);
        List<Cities> citiesCollect = citiesList.stream().filter(x -> x.getProvinceId().compareTo(Integer.valueOf(provinceId)) == 0 ).collect(Collectors.toList());
        return citiesCollect;
    }

    public static Object getCountiesListByCid(String cityId){
        checkPCListCache();
        List<County> counties = (List<County>)redisService.get(RedisConstants.KW_ALL_COUNTIES);
        List<County> citiesCollect = counties.stream().filter(x -> x.getCityId().compareTo(Integer.valueOf(cityId)) == 0 ).collect(Collectors.toList());
        return citiesCollect;
    }


    public static void checkPCListCache(){
        Object o = redisService.get(RedisConstants.KW_ALL_CITIES);
        Object o1 = redisService.get(RedisConstants.KW_ALL_PROVICE);
        Object o2 = redisService.get(RedisConstants.KW_ALL_COUNTIES);
        if(Objects.isNull(o)){
            List<Cities> cities = citiesService.selectList(null);
            redisService.set(RedisConstants.KW_ALL_CITIES,cities);
        }
        if(Objects.isNull(o1)){
            List<Provinces> provinces = provincesService.selectList(null);
            redisService.set(RedisConstants.KW_ALL_PROVICE,provinces);
        }
        if(Objects.isNull(o2)){
            List<County> counties = countyService.selectList(null);
            redisService.set(RedisConstants.KW_ALL_COUNTIES,counties);
        }
    }

}
