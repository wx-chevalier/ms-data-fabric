package com.zhuxun.dmc.zuul.web.zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.zhuxun.dmc.zuul.domain.token.VToken;
import com.zhuxun.dmc.zuul.repository.model.DcApiStatistics;
import com.zhuxun.dmc.zuul.service.ApiStatisticsService;
import com.zhuxun.dmc.zuul.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 透明代理的后置过滤、作用于统计API接口的请求事件，如参数，请求耗时，验证等
 *
 * @author tao
 */
@Component
@Slf4j
public class CustomZuulPostFilter extends ZuulFilter {

    @Autowired
    private ApiStatisticsService apiStatisticsService;

    @Override
    public String filterType() {
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        log.info("进入POST拦截器,开始统计代理结果参数....");
        RequestContext context = RequestContext.getCurrentContext();
        try {
            // 从context中获取在PRE拦截器中放置的参数信息
            long startTime = context.get("startTime") == null ? 0 : (long) context.get("startTime");
            VToken vtoken = (VToken) context.get("token");
            DcApiStatistics apiStatistics = new DcApiStatistics();
            apiStatistics.setId(UUIDUtils.getUUID());
            apiStatistics.setApiPath(context.getRequest().getRequestURL().toString());
            apiStatistics.setProxyPath(context.getRouteHost().toString() + context.get("requestURI"));
            apiStatistics.setAccessTime(new Date());
            apiStatistics.setApiId(vtoken == null ? "NO TOKEN" : vtoken.getApiId());
            apiStatistics.setUserId(vtoken == null ? "NO TOKEN" : vtoken.getUserId());
            apiStatistics.setProxyMethod(context.getRequest().getMethod());
            apiStatistics.setStatusCode(context.getResponseStatusCode());
            apiStatistics.setConsumeTime(
                    startTime == 0 ? 0 : (int) (System.currentTimeMillis() - startTime));
            apiStatisticsService.addApiStatisticsRecord(apiStatistics);
        } catch (Exception ex) {
            log.error("记录接口代理访问出错,出错信息:", ex);
            // TODO 消息通知出现问题
        }
        HttpServletResponse response = context.getResponse();

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Max-Age", "3628800");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "*");
        return null;
    }
}
