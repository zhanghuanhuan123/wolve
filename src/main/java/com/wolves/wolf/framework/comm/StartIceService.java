package com.wolves.wolf.framework.comm;


public class StartIceService {
    private static int port;

    /**
     * 启动ice服务，直到服务启动成功，方法才返回；否则一直hold住。
     *
     * @param server_size      服务器线程池初始大小
     * @param server_size_max  服务器线程池最大值
     * @param override_timeout 响应超时时间,单位为ms
     * @param port             ice监听端口
     * @throws InterruptedException 线程中断异常
     */
    public static void startIceService(int server_size, int server_size_max, int message_size_max, int override_timeout, int port, String protocol)
            throws Exception {
        StartIceService.port = port;
        NodeAdapter nodeAdapter = new NodeAdapter(server_size, server_size_max, message_size_max, override_timeout, port, protocol);
        new Thread(nodeAdapter).start();
        for (Exception e = null; e == null; e = nodeAdapter.getE()) {
            Thread.sleep(1000L);
            if (nodeAdapter.isOnline()) {
                break;
            }
        }
        if (nodeAdapter.getE() != null)
            throw nodeAdapter.getE();

    }

    /**
     * 获取端口
     *
     * @return
     */
    public static int getPort() {
        return port;
    }

}

