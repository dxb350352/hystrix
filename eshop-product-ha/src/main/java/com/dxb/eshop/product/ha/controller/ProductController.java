package com.dxb.eshop.product.ha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 商品服务的接口
 *
 * @author Administrator
 */
@Controller
public class ProductController {

    @RequestMapping("/getProductInfo")
    @ResponseBody
    public String getProductInfo(Long productId) {
        return "{\"id\": " + productId + ", \"name\": \"iphone7手机\", \"cityId\": 1}";
    }

}
