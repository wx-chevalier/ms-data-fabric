package com.zhuxun.dmc.zuul.web.zuul;

import com.google.gson.JsonSyntaxException;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.zhuxun.dmc.zuul.config.validation.ValidationErrorType;
import com.zhuxun.dmc.zuul.domain.token.VToken;
import com.zhuxun.dmc.zuul.service.ValidationSchemaService;
import com.zhuxun.dmc.zuul.utils.ZuulUtils;
import com.zhuxun.dmc.zuul.web.zuul.validation.ValidateFailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author tao
 */
@Component
@Configuration
@Slf4j
public class CustomZuulPreFilter extends ZuulFilter {

    @Autowired
    RouteLocator routeLocator;

    @Autowired
    private ValidationSchemaService validationSchemaService;

    private UrlPathHelper urlPathHelper = new UrlPathHelper();

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
        return FilterConstants.PRE_DECORATION_FILTER_ORDER;
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
        context.set("startTime", System.currentTimeMillis());
        try {
            VToken vtoken = (VToken) context.get("token");
            validationSchemaService.validateRequestSchema(request, vtoken.getApiId());
        } catch (IOException e) {
            log.error("反序列化校验模型出错", e);
            ZuulUtils.packageErrorMessage(context, ValidationErrorType.ERROR_SERVICE_DESERIALIZE);
        } catch (ValidateFailException e) {
            log.error("请求参数校验失败", e);
            ZuulUtils.packageErrorMessage(context, ValidationErrorType.ERROR_SERVICE_VALIDAT_QUERY.message(e.getMessage()));
            throw e;
        } catch (JsonSyntaxException e) {
            log.error("反序列化校验模型出错", e);
            ZuulUtils.packageErrorMessage(context, ValidationErrorType.ERROR_SERVICE_DESERIALIZE);
            throw e;
        }catch (Exception e) {
            log.error("发生了未知异常", e);
            ZuulUtils.packageErrorMessage(context, ValidationErrorType.ERROR_SYSTEM_UNKNOWN_REASON);
            throw e;
        }
        return null;
    }
}
