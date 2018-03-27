package com.wolves.wolf.framework.comm;


public class Response<E> {
    private String code;
    private String desc;
    private E body;


    public String getCode() {
        return this.code;

    }

    public void setCode(String code) {
        this.code = code;

    }

    public String getDesc() {
        return this.desc;

    }

    public void setDesc(String desc) {
        this.desc = desc;

    }


    public E getBody() {
        return this.body;

    }

    public void setBody(E body) {
        this.body = body;

    }

}
