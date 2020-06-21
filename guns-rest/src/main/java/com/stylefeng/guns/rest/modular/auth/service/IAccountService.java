package com.stylefeng.guns.rest.modular.auth.service;

import com.stylefeng.guns.persistence.model.User;
import com.stylefeng.guns.rest.core.util.BaseJson;
import com.stylefent.guns.entity.vo.AccountVo;
import com.stylefent.guns.entity.vo.BingdingPhoneVo;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * @author: lx
 */
public interface IAccountService {
    /**
     * 检查用户状态
     *
     * @param dbUser
     * @return
     */
    boolean checkUser(User dbUser);

    /**
     * 登录
     *
     * @param accountVo
     * @param response
     * @param loginType 登录类型
     * @return
     */
    BaseJson login(AccountVo accountVo, HttpServletResponse response, int loginType);


    /**
     * 退出登录
     */
    BaseJson logout(AccountVo accountVo);
    /**
     * 发送短信
     *
     * @param accountVo
     * @return
     */
    BaseJson send(AccountVo accountVo);

    /**
     * 绑定手机
     *
     * @param bingdingPhoneVo
     * @return
     */
    BaseJson bind(BingdingPhoneVo bingdingPhoneVo,HttpServletResponse response);

    /**
     * 验证验证码
     *
     * @param accountVo
     * @return
     */
    BaseJson smsValidate(AccountVo accountVo);

    /**
     * 重置密码
     *
     * @param accountVo
     * @return
     */
    BaseJson resetPwd(AccountVo accountVo);





    /**
     * 修改手机号
     *
     * @param accountVo
     * @return
     */
    BaseJson modifyPhone(AccountVo accountVo);

    /**
     * 修改密码
     *
     * @param accountVo
     * @return
     */
    BaseJson modifyPwd(AccountVo accountVo);


    /**
     * 注册
     *
     * @param accountVo
     * @return
     */
    BaseJson register(AccountVo accountVo);

    BaseJson inviteRegister(AccountVo accountVo);

}
