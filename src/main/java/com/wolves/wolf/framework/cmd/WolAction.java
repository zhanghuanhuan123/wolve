package com.wolves.wolf.framework.cmd;


import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.opensymphony.xwork.Action;


public abstract class WolAction implements Action {
    private String fromNg;
    private String fromNgVersion;
    private String fromIp;
    private Any request;
    private String code;
    private String desc;
    private Message response;


    protected <T extends Message> T getRequest(Class<T> clazz)
            throws InvalidProtocolBufferException {
        if (this.request != null) {
            return this.request.unpack(clazz);
        }
        return null;
    }

    public void setRequest(String fromNg, String fromNgVersion, String fromIp, Any request) {
        this.fromNg = fromNg;
        this.fromNgVersion = fromNgVersion;
        this.fromIp = fromIp;
        this.request = request;
    }

    public String getFromNg() {
        return this.fromNg;
    }

    public String getFromNgVersion() {
        return this.fromNgVersion;
    }

    public String getFromIp() {
        return this.fromIp;
    }

    public Message getResponse() {
        return this.response;
    }

    public void setResponse(String code, String desc, Message response) throws InvalidProtocolBufferException {
        this.code = code;
        this.desc = desc;
        this.response = response;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    public String execute() throws Exception {
        exec();
        return "none";
    }

    public abstract void exec() throws Exception;

}

