package com.dxb.eshop.cache.ha.controller;

import com.alibaba.fastjson.JSONObject;
import com.dxb.eshop.cache.ha.entity.ProductInfo;
import com.dxb.eshop.cache.ha.http.HttpClientUtils;
import com.dxb.eshop.cache.ha.hystrix.command.GetCityNameCommand;
import com.dxb.eshop.cache.ha.hystrix.command.GetProductInfoCommand;
import com.dxb.eshop.cache.ha.hystrix.command.GetProductInfosCommand;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixObservableCommand;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import rx.Observable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * 缓存服务的接口
 *
 * @author Administrator
 */
@Controller
public class CacheController {

    @RequestMapping("/getProductInfo")
    @ResponseBody
    public String getProductInfo(Long productId) {
        // 拿到一个商品id
        // 调用商品服务的接口，获取商品id对应的商品的最新数据
        // 用HttpClient去调用商品服务的http接口
        //1、原始
        String url = "http://127.0.0.1:8083/getProductInfo?productId=" + productId;
        String response = HttpClientUtils.sendGetRequest(url);
        System.out.println(response);
        //2、线程池
        HystrixCommand<ProductInfo> command = new GetProductInfoCommand(productId);
        ProductInfo productInfo = command.execute();
        System.out.println(productInfo);
        //3、信号量
        GetCityNameCommand getCityNameCommand = new GetCityNameCommand(productInfo.getCityId());
        productInfo.setCityName(getCityNameCommand.execute());
        return productInfo.toString();
    }

    @RequestMapping("/getProductInfos")
    @ResponseBody
    public String getProductInfos(String productIds) {
        HystrixObservableCommand<ProductInfo> getProductInfosCommand = new GetProductInfosCommand(productIds.split(","));
        // 还没有执行
        Observable<ProductInfo> observable = getProductInfosCommand.toObservable();
        // 等到调用subscribe然后才会执行
        List<ProductInfo> result = new ArrayList<>();
//        observable.subscribe(new Observer<ProductInfo>() {
//            @Override
//            public void onCompleted() {
//                System.out.println("获取完了所有的商品数据");
//            }
//            @Override
//            public void onError(Throwable e) {
//                e.printStackTrace();
//            }
//            @Override
//            public void onNext(ProductInfo productInfo) {
//                result.add(productInfo);
//                System.out.println(result.size());
//            }
//        });
        Iterator<ProductInfo> iter = observable.toBlocking().getIterator();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return JSONObject.toJSONString(result);
    }

}
