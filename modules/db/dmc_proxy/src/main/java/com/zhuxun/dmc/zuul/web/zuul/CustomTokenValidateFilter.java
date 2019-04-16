package com.zhuxun.dmc.zuul.web.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.zhuxun.dmc.zuul.config.SecretConstant;
import com.zhuxun.dmc.zuul.config.validation.ValidationErrorType;
import com.zhuxun.dmc.zuul.domain.exception.TokenException;
import com.zhuxun.dmc.zuul.domain.token.VToken;
import com.zhuxun.dmc.zuul.service.ApiAccessTokenService;
import com.zhuxun.dmc.zuul.utils.ZuulUtils;
import com.zhuxun.dmc.zuul.web.zuul.validation.ValidateFailException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 校验token是否被禁用,是否有效
 */
@Component
@Configuration
@Slf4j
public class CustomTokenValidateFilter extends ZuulFilter {


    @Autowired
    private ApiAccessTokenService accessTokenService;

    /**
     * 过滤类型
     *
     * @return
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    /**
     * 是否过滤
     *
     * @return 返回FALSE则不执行过滤操作
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 实际的控制操作
     *
     * @return
     */
    @Override
    public Object run() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        // 获取头部中携带的token
        String token = request.getHeader(SecretConstant.TURELORE_TOKEN);
        if (token == null) {
            ZuulUtils.packageErrorMessage(context, ValidationErrorType.ERROR_SERVICE_TOKEN_NOT_EXIST);
            log.info("请求 {} 没有发现token ", request.getRequestURL());
            throw new ValidateFailException("Token不能为空,请检查!");
        }
        try {
            // 解析token
            VToken vToken = VToken.of(token);
            log.info("token 解析结果 = {}", vToken);
            // 添加请求开始时间和token信息
            context.set("token", vToken);
        } catch (ExpiredJwtException e) {
            log.error("Token过期", e);
            ZuulUtils.packageErrorMessage(context, ValidationErrorType.ERROR_SERVICE_TOKEN_EXPIRED);
            throw e;
        } catch (MalformedJwtException e) {
            ZuulUtils.packageErrorMessage(context, ValidationErrorType.ERROR_SERVICE_TOKEN_PARSE);
            throw e;
        } catch (Exception e) {
            log.error("发生了未知异常", e);
            ZuulUtils.packageErrorMessage(context, ValidationErrorType.ERROR_SYSTEM_UNKNOWN_REASON);
            throw e;
        }
        // 判断token是否被弃用
        if (!accessTokenService.tokenExist(token)) {
            ZuulUtils.packageErrorMessage(RequestContext.getCurrentContext(), ValidationErrorType.ERROR_SERVER_TOKEN_ABANDON);
            log.info("token不存在已被企业用户弃用,请联系管理员,重新生成token");
            throw new TokenException("Token不存在或被弃用");
        }
        log.info("校验token有效,正在转交前置过滤器");
        return null;
    }


}
