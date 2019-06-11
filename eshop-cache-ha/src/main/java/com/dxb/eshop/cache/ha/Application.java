package com.dxb.eshop.cache.ha;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * 不是spring cloud需要自己添加这个
     *
     * @return
     */
    @Bean
    public HystrixCommandAspect hystrixCommandAspect() {
        return new HystrixCommandAspect();
    }
}
