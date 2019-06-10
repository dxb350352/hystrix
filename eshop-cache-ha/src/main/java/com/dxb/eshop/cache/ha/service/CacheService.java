package com.dxb.eshop.cache.ha.service;

import com.dxb.eshop.cache.ha.http.HttpClientUtils;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    @HystrixCommand(fallbackMethod = "getProductInfoFallback", groupKey = "GetProductInfoCommandGroup",
            //ignoreExceptions = {BadRequestException.class} 指定哪一类异常可以忽略,不走fallback
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000"),//方法执行超时时间
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),//当并发错误个数达到此阀值时(在时间窗口内)，触发隔断器
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "6000"),//滚动窗口时间长度
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),//滚动窗口的桶数
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),//隔断器被触发后，睡眠多长时间开始重试请求
            },
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "20"),
                    @HystrixProperty(name = "maxQueueSize", value = "100"),
            })
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

    public String getProductInfoFallback(Long productId) {
        return "error";
    }
}
