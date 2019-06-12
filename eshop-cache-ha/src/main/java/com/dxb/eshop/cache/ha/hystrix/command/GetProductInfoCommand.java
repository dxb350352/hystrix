package com.dxb.eshop.cache.ha.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.dxb.eshop.cache.ha.entity.ProductInfo;
import com.dxb.eshop.cache.ha.http.HttpClientUtils;
import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;

public class GetProductInfoCommand extends HystrixCommand<ProductInfo> {
    private static final HystrixCommandKey GETTER_KEY = HystrixCommandKey.Factory.asKey("GetterCommand");

    private Long productId;

    public GetProductInfoCommand(Long productId) {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GetProductInfoCommandGroup"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        //超时
                        .withExecutionTimeoutEnabled(true)
                        .withExecutionTimeoutInMilliseconds(2500)
                        //滑动窗口中，最少要有多少个请求时，才触发开启短路
                        .withCircuitBreakerRequestVolumeThreshold(20)
                        //当异常请求达到这个百分比时，就触发打开短路器
                        .withCircuitBreakerErrorThresholdPercentage(50)
                        //再重新导holf-open状态
                        .withCircuitBreakerSleepWindowInMilliseconds(5000))
                .andCommandKey(GETTER_KEY));
        this.productId = productId;
    }

    @Override
    protected ProductInfo run() throws Exception {
//        int i = 1 / 0;
        String url = "http://127.0.0.1:8083/getProductInfo?productId=" + productId;
        String response = HttpClientUtils.sendGetRequest(url);

        return JSONObject.parseObject(response, ProductInfo.class);
    }

    @Override
    protected ProductInfo getFallback() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("error");
        return productInfo;
    }

    /**
     * request cache
     *
     * @return
     */
    @Override
    protected String getCacheKey() {
        return gainCacheKey(productId);
    }

    private static String gainCacheKey(long id) {
        return "productId" + id;
    }

    /**
     * 删除缓存
     *
     * @param id
     */
    public static void flushCache(long id) {
        HystrixRequestCache.getInstance(GETTER_KEY,
                HystrixConcurrencyStrategyDefault.getInstance()).clear(gainCacheKey(id));
    }
}