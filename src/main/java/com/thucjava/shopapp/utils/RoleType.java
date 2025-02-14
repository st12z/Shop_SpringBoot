package com.thucjava.shopapp.utils;

import lombok.*;


public enum RoleType {
    ADMIN("ADMIN"),
    USER("USER"),
    MANAGER("MANAGER"),
    STAFF("STAFF");
    private final String value;

    RoleType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
