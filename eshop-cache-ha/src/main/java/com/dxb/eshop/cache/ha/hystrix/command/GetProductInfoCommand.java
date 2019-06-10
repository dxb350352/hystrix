package com.dxb.eshop.cache.ha.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.dxb.eshop.cache.ha.entity.ProductInfo;
import com.dxb.eshop.cache.ha.http.HttpClientUtils;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

import java.util.concurrent.TimeUnit;

public class GetProductInfoCommand extends HystrixCommand<ProductInfo> {

    private Long productId;

    public GetProductInfoCommand(Long productId) {
        super(HystrixCommand.Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GetProductInfoCommandGroup"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(2500)))
        ;
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
}