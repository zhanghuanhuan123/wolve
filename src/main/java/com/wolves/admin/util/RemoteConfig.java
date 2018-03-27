package com.wolves.admin.util;


import com.wolves.admin.pb.BaseNodeConfig;


public class RemoteConfig {
    public static final int DEFAULT_SERVER_SIZE = 10;
    public static final int DEFAULT_SERVER_SIZE_MAX = 50;
    public static final int DEFAULT_SERVER_TIMEOUT = 5000;
    public static final int DEFAULT_MESSAGE_SIZE_MAX = 20480;
    public static final int DEFAULT_PING_INTERVAL = 10000;
    public static final int DEFAULT_SERVER_PORT = 7001;

    /**
     * 生成基本单元配置信息
     *
     * @param servicePort   服务端口，填0由系统自动分配
     * @param serverSize    服务器初始化时创建的服务器监听线程数
     * @param serverSizeMax 服务器服务器监听线程数最大值
     * @param serverTimeout 服务器响应超时时间
     * @param clientTimeout 客户端等待响应超时时间
     * @return 基本单元配置信息
     */
    public static BaseNodeConfig.Builder genBaseNodeConfig(int servicePort, int serverSize, int serverSizeMax,
                                                           int serverTimeout, int clientTimeout) {
        BaseNodeConfig.Builder bncBuilder = BaseNodeConfig.newBuilder();
        bncBuilder.setServicePort(servicePort)
                .setServerSize(serverSize)
                .setServerSizeMax(serverSizeMax)
                .setServerTimeout(serverTimeout)
                .setClientTimeout(clientTimeout);
        return bncBuilder;

    }

    /**
     * 自动修正远程配置数据，修正项为ServerSize、ServerSizeMax、ServerTimeout、ClientTimeout
     * 在DAF第一期，PingAdminInterval为不可配置项，该项值为10*1000ms
     *
     * @param nodeConfig 远程配置数据
     * @return 修正后的远程配置Builder
     */
    public static void autoFixNodeConfig(BaseNodeConfig.Builder nodeConfig) {
        if (nodeConfig.getServerSize() <= 0) {
            nodeConfig.setServerSize(DEFAULT_SERVER_SIZE);
        }
        if (nodeConfig.getServerSizeMax() <= 0) {
            nodeConfig.setServerSizeMax(DEFAULT_SERVER_SIZE_MAX);
        }
        if (nodeConfig.getServerTimeout() <= 0) {
            nodeConfig.setServerTimeout(DEFAULT_SERVER_TIMEOUT);
        }
        if (nodeConfig.getClientTimeout() <= 0) {
            nodeConfig.setClientTimeout(5500);
        }
        if (nodeConfig.getMessageSizeMax() <= 0) {
            nodeConfig.setMessageSizeMax(DEFAULT_MESSAGE_SIZE_MAX);
        }
        nodeConfig.setPingAdminInterval(DEFAULT_PING_INTERVAL);

    }

}
