package com.dxb.eshop.cache.ha.service;

import com.dxb.eshop.cache.ha.http.HttpClientUtils;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    @HystrixCommand(fallbackMethod = "getProductInfoFallback")
    public String getProductInfo1(Long productId) {
        //同步的方式
        return getProductInfo(productId);
    }

    @HystrixCommand(fallbackMethod = "getProductInfoFallback")
    public String getProductInfo2(Long productId) {
        //异步的执行
        return new AsyncResult<String>() {
            @Override
            public String invoke() {
                return getProductInfo(productId);
            }
        }.invoke();
    }

    public String getProductInfo(Long productId) {
        int a = 1 / 0;
        String url = "http://127.0.0.1:8083/getProductInfo?productId=" + productId;
        return HttpClientUtils.sendGetRequest(url);
    }

    public String getProductInfoFallback() {
        return "error";
    }
}
