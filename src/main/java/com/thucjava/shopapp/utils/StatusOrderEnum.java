package com.thucjava.shopapp.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public enum StatusOrderEnum {
    PROCESSING("PROCESSING"),
    SHIPPING("SHIPPING"),
    ARRIVED("ARRIVED");
    private final String value;
    StatusOrderEnum(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
    public static LinkedHashMap<String,String> getMap() {
        LinkedHashMap<String,String> map = new LinkedHashMap<>();
        for(StatusOrderEnum statusOrderEnum : StatusOrderEnum.values()) {
            map.put(statusOrderEnum.name(), statusOrderEnum.value);
        }
        return map;
    }
}
