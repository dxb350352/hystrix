package com.dxb.eshop.cache.ha.hystrix.command;

import com.alibaba.fastjson.JSONObject;
import com.dxb.eshop.cache.ha.entity.ProductInfo;
import com.dxb.eshop.cache.ha.http.HttpClientUtils;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class GetProductInfosCommand extends HystrixObservableCommand<ProductInfo> {

    private String[] productIds;

    public GetProductInfosCommand(String[] productIds) {
        super(HystrixCommandGroupKey.Factory.asKey("GetProductInfoGroup"));
        this.productIds = productIds;
    }

    @Override
    protected Observable<ProductInfo> construct() {
        return Observable.create(new Observable.OnSubscribe<ProductInfo>() {

            @Override
            public void call(Subscriber<? super ProductInfo> observer) {
                try {
                    for (String productId : productIds) {
                        String url = "http://127.0.0.1:8083/getProductInfo?productId=" + productId;
                        String response = HttpClientUtils.sendGetRequest(url);
                        ProductInfo productInfo = JSONObject.parseObject(response, ProductInfo.class);
                        observer.onNext(productInfo);
                    }
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
            }

        }).subscribeOn(Schedulers.io());
    }
}