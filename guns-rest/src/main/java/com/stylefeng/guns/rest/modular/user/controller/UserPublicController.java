package com.stylefeng.guns.rest.modular.user.controller;

import com.stylefeng.guns.core.util.MapUtil;
import com.stylefeng.guns.rest.core.util.BaseJson;
import com.stylefeng.guns.rest.core.util.LongUrlToShortUrl;
import com.stylefeng.guns.rest.modular.common.service.ICommonService;
import com.stylefeng.guns.rest.modular.user.service.IUserService;
import com.stylefent.guns.entity.query.CommonQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



/**
 * @author lx
 */
@Controller
@RequestMapping("/user")
public class UserPublicController {

    private static final String U_INDEX_URI_TEST = "http://api.douniu.nhys.cdnhxx.com/shareUser/?s=";
    private static final String U_INDEX_URI_PROD = "https://api.douniuv.com/shareUser/?s=";

    @Value("${rest.env-product}")
    private Boolean env;

    @Autowired
    private IUserService userService;


    /**
     * 查看用户主页
     */
    @PostMapping("/otherUserIndexInfo")
    public BaseJson otherUserIndexInfo(CommonQuery query) {
        return userService.otherUserIndexInfo(query);
    }


    @Autowired
    private ICommonService commonService;

    @RequestMapping("/shareIndex")
    public String shareIndex(Model model) {
        String androidUrl = commonService.findCommonDataFromCache(28L);
        String iosUrl = commonService.findCommonDataFromCache(29L);
        model.addAttribute("android", androidUrl);
        model.addAttribute("ios", iosUrl);
        return "/user_index";
    }



    Object sync = new Object();
    @RequestMapping("/getInviteShortUrl")
    @ResponseBody
    public BaseJson getInviteShortUrl(String rlbNum) {
        String inviteUrl = commonService.findCommonDataFromCache(34L);
        String url = inviteUrl+"/invite?u="+rlbNum;
        String conversion = "";
        synchronized (sync){
            try {
                conversion =  LongUrlToShortUrl.conversion(url);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                conversion = url;
            }
        }
        if(conversion.equals("")){
            conversion = url;
        }
        return BaseJson.ofSuccess(MapUtil.build().put("shortUrl",conversion).over());
    }


}
