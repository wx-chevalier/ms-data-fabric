package com.zhuxun.dmc.zuul.web.rest;

import com.google.common.base.Strings;
import com.netflix.zuul.exception.ZuulException;
import com.zhuxun.dmc.zuul.config.validation.RequestAttributeConstant;
import com.zhuxun.dmc.zuul.config.validation.ValidationErrorType;
import com.zhuxun.dmc.zuul.domain.zuul.VErrorBean;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作用：用于在Zuul拦截器中使用代理的时候重定向地址
 *
 * <p>时间：18-7-11 上午9:38
 *
 * <p>位置：com.zhuxun.dmc.zuul.web.rest
 *
 * @author Yan - tao
 */
@RestController
public class ErrorHandlerController implements ErrorController {

    /**
     * 出异常后进入该方法，交由下面的方法处理
     */
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = "/error", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<VErrorBean> error(HttpServletRequest request, HttpServletResponse response) {
        String proxyErrorMessage = (String) request.getAttribute(RequestAttributeConstant.ProxyErrorMessage);
        Integer errorStatusCode = null;
        if (!Strings.isNullOrEmpty(proxyErrorMessage)) {
            errorStatusCode = Integer.valueOf((Integer) request.getAttribute(RequestAttributeConstant.ProxyErrorCode));
        } else {
            Object exception = request.getAttribute(RequestAttributeConstant.ErrorException);
            if (exception instanceof ZuulException) {
                ZuulException zuulException = (ZuulException) exception;
                errorStatusCode = zuulException.nStatusCode;
                proxyErrorMessage = zuulException.getLocalizedMessage();
            } else {
                errorStatusCode = ValidationErrorType.ERROR_PROXY_TARGET.code();
                proxyErrorMessage = ValidationErrorType.ERROR_PROXY_TARGET.message();
            }
        }
        VErrorBean errorBean = new VErrorBean();
        errorBean.setMessage(proxyErrorMessage).setCode(errorStatusCode);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Max-Age", "3628800");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        return new ResponseEntity<VErrorBean>(errorBean, HttpStatus.NOT_ACCEPTABLE);
    }

}
