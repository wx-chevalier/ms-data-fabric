package com.zhuxun.dmc.zuul.utils;

import com.netflix.zuul.context.RequestContext;
import com.zhuxun.dmc.zuul.config.validation.RequestAttributeConstant;
import com.zhuxun.dmc.zuul.config.validation.ValidationErrorType;

import javax.servlet.http.HttpServletRequest;

/**
 * Zuul工具类
 */
public class ZuulUtils {

    /**
     * 封装错误信息
     *
     * @param context
     * @param errorType
     */
    public static void packageErrorMessage(RequestContext context, ValidationErrorType errorType) {
        packageErrorMessage(context, errorType.message(), errorType.code());
    }

    /**
     * 封装错误信息
     * @param context
     * @param proxyErrorMessage
     * @param proxyErrorCode
     */
    public static void packageErrorMessage(RequestContext context, String proxyErrorMessage, int proxyErrorCode) {
        HttpServletRequest request = context.getRequest();
        request.setAttribute(RequestAttributeConstant.ProxyErrorMessage, proxyErrorMessage);
        request.setAttribute(RequestAttributeConstant.ProxyErrorCode, proxyErrorCode);
        context.setSendZuulResponse(false);
    }
}
