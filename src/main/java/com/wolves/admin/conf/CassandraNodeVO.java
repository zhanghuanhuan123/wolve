package com.wolves.admin.conf;


public class CassandraNodeVO {
    private String host;
    private int port;

    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
