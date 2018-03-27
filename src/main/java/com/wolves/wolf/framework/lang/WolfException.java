package com.wolves.wolf.framework.lang;

public class WolfException extends RuntimeException {
    private String code;
    private String desc;


    public WolfException(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }


    public WolfException(Throwable cause, String code, String desc) {
        super(cause);
        this.code = code;
        this.desc = desc;
    }


    public String getCode() {
        return this.code;
    }


    public String getDesc() {
        return this.desc;
    }

}

