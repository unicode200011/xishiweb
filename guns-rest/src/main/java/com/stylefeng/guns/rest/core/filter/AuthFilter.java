package com.stylefeng.guns.rest.core.filter;

import com.stylefeng.guns.core.util.RenderUtil;
import com.stylefeng.guns.core.util.SpringContextHolder;
import com.stylefeng.guns.persistence.model.User;
import com.stylefeng.guns.rest.config.properties.JwtProperties;
import com.stylefeng.guns.rest.core.util.BaseJson;
import com.stylefeng.guns.rest.core.util.JwtTokenUtil;
import com.stylefeng.guns.rest.modular.user.service.IUserService;
import com.stylefent.guns.entity.vo.SessionUserVo;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * @description: 对客户端请求的jwt token验证过滤器
 * @author: lx
 */
@Slf4j
public class AuthFilter extends OncePerRequestFilter {

    private final Log logger = LogFactory.getLog(this.getClass());
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private IUserService userService;

    private String protectUrlPattern;
    private static final PathMatcher pathMatcher = new AntPathMatcher();

    public AuthFilter(String protectUrlPattern) {
        this.protectUrlPattern = protectUrlPattern;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String servletPath = request.getServletPath();
        if (servletPath.equals("/" + jwtProperties.getAuthPath())) {
            chain.doFilter(request, response);
            return;
        }

        try {
            if (pathMatcher.match(protectUrlPattern, servletPath)) {

                response.setCharacterEncoding("UTF-8");
                response.setContentType("application/json");

                final String requestHeader = request.getHeader(jwtProperties.getHeader());
                if (requestHeader == null || !requestHeader.startsWith("Bearer ")) {
                    logger.warn("Authorization header is null or not startsWith Bearer ");
                    RenderUtil.renderJson(response, new BaseJson<>("", "401", "用户登录过期"));
                    return;
                }

                String authToken = requestHeader.substring(7);
                Claims claims = this.jwtTokenUtil.getClaimFromToken(authToken);
                Date ExpiredDate = claims.getExpiration();
                //验证token是否过期,包含了验证jwt是否正确
                if (ExpiredDate.before(new Date())) {
                    logger.warn("Authorization token is expired");
                    RenderUtil.renderJson(response, new BaseJson<>("", "401", "用户登录过期"));
                    return;
                }
                SessionUserVo issuer = new SessionUserVo(Long.valueOf(claims.getId()), claims.getSubject());
                request.setAttribute("issuer", issuer);
                User user = userService.selectById(issuer.getUserId());
                if(user.getState() == 1){
                    logger.warn("用户【"+issuer.getUserId()+"】已被冻结");
                    RenderUtil.renderJson(response, new BaseJson<>("", "401", "账户已被冻结"));
                    return;
                }
                // 生成新的token放入header中
                String newToken = this.jwtTokenUtil.generateToken(claims.getSubject(), Long.valueOf(claims.getId()), (String) claims.get(this.jwtProperties.getMd5Key()));
                response.setHeader(this.jwtProperties.getTokenKey(), newToken);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            RenderUtil.renderJson(response, new BaseJson<>("", "401", "用户登录过期"));
            return;
        }
        chain.doFilter(request, response);
    }
}
