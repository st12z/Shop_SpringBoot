package com.thucjava.shopapp.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public class ResponseData<T> implements Serializable {
    private int status;
    private String messge;
    private T data;
//    Response data for the api success
    public ResponseData(int status, String messge, T data) {
        this.status = status;
        this.messge = messge;
        this.data = data;
    }

    public ResponseData(int status, String messge) {
        this.status = status;
        this.messge = messge;
    }
}
