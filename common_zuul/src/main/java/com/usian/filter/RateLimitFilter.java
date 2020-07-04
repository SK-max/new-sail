package com.usian.filter;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.usian.JsonUtils;
import com.usian.Result;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;


/**
 * 限流器
 */
@Component
public class RateLimitFilter extends ZuulFilter {

    /**
     * 创建谷歌令牌桶 1是每秒生成令牌桶的数量
     * 数值越大代表处理量越多，数值越小代表处理量越小
     */
    private static final RateLimiter RATE_LIMIT = RateLimiter.create(1);

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 限流器的优先级 设置为最高
     * @return
     */
    @Override
    public int filterOrder() {
        return FilterConstants.SERVLET_DETECTION_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        //能否从令牌桶中获取到令牌
        if (!RATE_LIMIT.tryAcquire()) {
            RequestContext currentContext = RequestContext.getCurrentContext();
            currentContext.setSendZuulResponse(false);
            currentContext.setResponseBody(JsonUtils.objectToJson(Result.error("访问太过频繁，请稍后访问")));
            currentContext.getResponse().setContentType("application/json;charset=utf-8");
        }
        return null;
    }
}
