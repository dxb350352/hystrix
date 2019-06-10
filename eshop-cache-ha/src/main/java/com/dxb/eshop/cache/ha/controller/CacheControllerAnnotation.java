package com.dxb.eshop.cache.ha.controller;

import com.dxb.eshop.cache.ha.service.CacheService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 缓存服务的接口
 *
 * @author Administrator
 */
@Controller
public class CacheControllerAnnotation {
    @Autowired
    private CacheService cacheService;

    @RequestMapping("/getProductInfo1")
    @ResponseBody
    public String getProductInfo1(Long productId) {
        return cacheService.getProductInfo1(productId);
    }

    @RequestMapping("/getProductInfo2")
    @ResponseBody
    public String getProductInfo2(Long productId) {
        return cacheService.getProductInfo2(productId);
    }


}
