package com.wolves.wolf.framework.comm;


import com.google.protobuf.Any;
import com.google.protobuf.Message;
import com.wolves.framework.pb.WolRequest;
import com.wolves.framework.pb.WolResponse;
import com.wolves.wolf.framework.config.RemoteConfig;

import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: ZhangHuan
 * Date: 2015-8-18
 * Time: 9:51:59
 * 通讯管理需要返回指定版本单元下的节点, 这些信息来源于注册节点, 暂存在节点内存中, 在很短的时间内有效(即快速过期)
 */
public class CommManager {
    // 在初始化NodeGroupConnector时，所需的锁
    private static final String LOCK = "CommManager.LOCK";
    // proxyCache当中的key是标签和版本的组合, value是proxy的名称
    private static Hashtable<String, NodeGroupConnector> connectorCache = new Hashtable();


    /**
     * 发送请求,获取应答
     *
     * @param requestBody 请求体
     * @return 应答题
     * @throws Exception 异常
     */
    public static <T extends Message> Response<T> send(String ng, String ngVersion, String action, String actVersion, Message requestBody, Class<T> clazz)
            throws Exception {
        WolRequest.Builder dafRequestBuilder = WolRequest.newBuilder();
        dafRequestBuilder.setNgLabel(ng).setNgVersion(ngVersion).setAction(action).setActVersion(actVersion);
        if (requestBody != null) {
            dafRequestBuilder.setBody(Any.pack(requestBody));
        }
        WolResponse dafResponse = send(dafRequestBuilder.build());
        Response response = new Response();
        response.setCode(dafResponse.getCode());
        response.setDesc(dafResponse.getDesc());
        if (dafResponse.hasBody()) {
            response.setBody(dafResponse.getBody().unpack(clazz));
        }
        return response;

    }

    /**
     * 发送请求,获取应答
     *
     * @param wolRequest 请求体
     * @return 应答题
     * @throws Exception 异常
     */
    public static WolResponse send(WolRequest wolRequest) throws Exception {
        String key = wolRequest.getNgLabel() + "[" + wolRequest.getNgVersion() + "]";
        NodeGroupConnector nodeGroupConnector = (NodeGroupConnector) connectorCache.get(key);
        if (nodeGroupConnector == null) {
            synchronized (LOCK) {
                nodeGroupConnector = (NodeGroupConnector) connectorCache.get(key);
                if (nodeGroupConnector == null) {
                    nodeGroupConnector = new NodeGroupConnector();
                    nodeGroupConnector.initial(wolRequest.getNgLabel(), wolRequest.getNgVersion());
                    connectorCache.put(key, nodeGroupConnector);
                    nodeGroupConnector.schedule();
                }
            }
        }
        return nodeGroupConnector.send(wolRequest);
    }


    public static byte[] sendForTestOnly(String ip, int port, byte[] request) throws Exception {

        return sendForTestOnly(ip, port, request, "TCP");

    }


    public static byte[] sendForTestOnly(String ip, int port, byte[] request, String protocol) throws Exception {
        NodeConnector ncForTestOnly = new NodeConnector(10000, RemoteConfig.MESSAGE_SIZE_MAX, ip, port, false, protocol);
        byte[] returnValue = ncForTestOnly.send(request);
        ncForTestOnly.destroy();
        return returnValue;
    }

    public static void  remove(String key) throws Exception {
       connectorCache.remove(key);
    }

}

