package com.dxb.eshop.cache.ha.entity;

import com.alibaba.fastjson.JSONObject;

public class ProductInfo {
    private String id;
    private String name;
    private Long cityId;
    private String cityName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
