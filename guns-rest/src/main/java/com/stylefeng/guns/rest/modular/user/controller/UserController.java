package com.stylefeng.guns.rest.modular.user.controller;


import com.stylefeng.guns.core.intercepter.ApiIdempotent;
import com.stylefeng.guns.core.support.StrKit;
import com.stylefeng.guns.persistence.model.*;
import com.stylefeng.guns.rest.core.util.BaseJson;
import com.stylefeng.guns.rest.modular.auth.service.IAccountService;
import com.stylefeng.guns.rest.modular.common.service.ICommonService;
import com.stylefeng.guns.rest.modular.user.service.IUserService;
import com.stylefent.guns.entity.query.CommonPageQuery;
import com.stylefent.guns.entity.query.CommonQuery;
import com.stylefent.guns.entity.vo.AccountVo;
import com.stylefent.guns.entity.vo.BingdingPhoneVo;
import com.stylefent.guns.entity.vo.SessionUserVo;
import com.stylefent.guns.entity.vo.UpdateUserInfoVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;


/**
 * @description:用户
 * @author: lx
 */
@Slf4j
@RestController
@RequestMapping("/api/user")
@Api(value = "用户信息管理")
public class UserController {


    @Autowired
    private IUserService userService;

    @Autowired
    private IAccountService accountService;


    /**
     * @description: 绑定手机号
     * @author: lx
     */
    @PostMapping("/binding")
    @ApiIdempotent
    @ApiOperation(value = "绑定手机号", notes = "绑定手机号")
    public BaseJson bind(@RequestAttribute("issuer") SessionUserVo userVo, HttpServletResponse response, @ApiParam @RequestBody BingdingPhoneVo bingdingPhoneVo) {

        bingdingPhoneVo.setUserId(userVo.getUserId());
        return accountService.bind(bingdingPhoneVo,response);
    }


    /**
     * 修改手机号
     *
     * @param userVo
     * @param accountVo
     * @return
     */
    @PostMapping("/modifyPhone")
    @ApiIdempotent
    public BaseJson modifyPhone(@RequestAttribute("issuer") SessionUserVo userVo, AccountVo accountVo) {
        accountVo.setUserId(userVo.getUserId());
        return accountService.modifyPhone(accountVo);
    }

    /**
     * 修改密码
     *
     * @param userVo
     * @param accountVo
     * @return
     */
    @PostMapping("/modifyPwd")
    @ApiIdempotent
    public BaseJson modifyPwd(@RequestAttribute("issuer") SessionUserVo userVo, AccountVo accountVo) {
        accountVo.setUserId(userVo.getUserId());
        return accountService.modifyPwd(accountVo);
    }

    /**
     * 修改支付密码
     *
     * @param userVo
     * @param accountVo
     * @return
     */
    @PostMapping("/payPwd")
    public BaseJson payPwd(@RequestAttribute("issuer") SessionUserVo userVo, AccountVo accountVo) {
        accountVo.setUserId(userVo.getUserId());
        return userService.changePwd(accountVo);
    }

    /**
     * @description: 验证支付密码
     * @author: lx
     */
    @PostMapping("/v1/validatePayPwd")
    public BaseJson validatePwd(AccountVo accountVo, @RequestAttribute("issuer") SessionUserVo issuer) {
        accountVo.setUserId(issuer.getUserId());
        return userService.validatePwd(accountVo);
    }



    /**
     * @description: 用户首页信息
     * @author: lx
     */
    @PostMapping("/userIndexInfo")
    public BaseJson userIndexInfo(@RequestAttribute("issuer") SessionUserVo issuer) {
        return userService.userIndexInfo(issuer.getUserId());
    }

    /**
     * @description: 用户自己个人资料
     * @author: lx
     */
    @PostMapping("/userInfo")
    public BaseJson userInfo(@RequestAttribute("issuer") SessionUserVo issuer) {
        return userService.userInfo(issuer.getUserId());
    }

    /**
     * @description: 查看其他用户个人资料
     * @author: lx
     */
    @PostMapping("/checkOtherUserInfo")
    public BaseJson checkOtherUserInfo(@RequestAttribute("issuer") SessionUserVo issuer,User user) {
        return userService.checkUserInfo(user.getId(),issuer.getUserId());
    }


    /**
     * @description: 用户更新个人资料
     * @author: lx
     */
    @PostMapping("/updateUserInfo")
    @ApiIdempotent
    public BaseJson updateUserInfo(@RequestAttribute("issuer") SessionUserVo issuer, UpdateUserInfoVo updateUserInfoVo) {
        updateUserInfoVo.setUserId(issuer.getUserId());
        return userService.updateUserInfo(updateUserInfoVo);
    }

    /**
     * 举报用户
     * @param issuer
     * @param query
     * @return
     */
    @PostMapping("/reasons")
    @ApiIdempotent
    public BaseJson reasons(@RequestAttribute("issuer") SessionUserVo issuer,CommonQuery query) {
        query.setUserId(issuer.getUserId());
        String content = query.getKeyword();
        if (StrKit.isEmpty(content)) {
            return BaseJson.ofFail("举报内容不能为空");
        }
        userService.userReport(query);
//        boolean result = messageSender.userReport(query);
//        return result ? BaseJson.ofSuccess("举报成功") : BaseJson.ofFail("举报失败,请稍后再试");
        return  BaseJson.ofSuccess("举报成功");
    }


    /**
     *加关注
     * @param issuer
     * @param query
     * @return
     */
    @PostMapping("/addAttention")
    public BaseJson attention(@RequestAttribute("issuer") SessionUserVo issuer,CommonQuery query){
        query.setUserId(issuer.getUserId());
        query.setType(0);
        return userService.addAttention(query);
    }

    /**
     *取消关注
     * @param issuer
     * @param query
     * @return
     */
    @PostMapping("/cancelAttention")
    public BaseJson cancelAttention(@RequestAttribute("issuer") SessionUserVo issuer,CommonQuery query){
        query.setUserId(issuer.getUserId());
        query.setType(0);
        return userService.cancelAttention(query);
    }

    /**
     *钱包首页
     * @param issuer
     * @return
     */
    @PostMapping("/walletIndex")
    public BaseJson walletIndex(@RequestAttribute("issuer") SessionUserVo issuer){
       return userService.selectWalletIndex(issuer.getUserId());
    }

    /**
     * 礼物列表
     * @param issuer
     * @return
     */
    @PostMapping("/getGiftList")
    public BaseJson getGiftList(@RequestAttribute("issuer") SessionUserVo issuer){
        return userService.getGiftList();
    }



}