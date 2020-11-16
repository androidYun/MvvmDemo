package com.xhs.mvvm;

public class ResponseOK<T> {

    private T t;

    public  ResponseOK error() {
        return new ResponseOK();
    }
}
