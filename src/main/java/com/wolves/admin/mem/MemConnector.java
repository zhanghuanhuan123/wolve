package com.wolves.admin.mem;


import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.wolves.admin.pb.Node;
import com.wolves.admin.pb.NodeGroup;
import com.wolves.admin.pb.NodeStatus;
import com.wolves.outpost.pb.Server;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;


public class MemConnector {
    private static Hashtable<String, ByteString> nodeStatusTable = new Hashtable();
    private static Hashtable<String, ByteString> ngCfgTable = new Hashtable();
    private static Hashtable<String, ByteString> serverInfoTable = new Hashtable();

    /**
     * 存储某一个节点
     *
     * @param node 需要存储节点
     * @throws Exception 异常
     */
    public static void storeNodeStatus(Node node)
            throws Exception {
        String ngVersion = genLv(node.getNgLabel(), node.getNgVersion());
        String key = ngVersion + "@" + node.getIp() + ":" + node.getPort();
        nodeStatusTable.put(key, node.toByteString());
    }

    /**
     * 移除某一个节点
     *
     * @param node 需要存储节点
     * @throws Exception 异常
     */
    public static void removeNodeStatus(Node node)
            throws Exception {
        String ngVersion = genLv(node.getNgLabel(), node.getNgVersion());
        String key = ngVersion + "@" + node.getIp() + ":" + node.getPort();
        nodeStatusTable.remove(key);

    }

    /**
     * 获取所有单元
     *
     * @return 单元列表
     * @throws Exception 异常
     */
    public static List<NodeGroup> getNodeGroups()
            throws Exception {
        // 从节点组（单元）开始进行轮询式的清理
        List<NodeGroup.Builder> ngBuilders = new ArrayList();
        List nodeStatusValues = new ArrayList();
        nodeStatusValues.addAll(nodeStatusTable.values());
        for (Iterator localIterator1 = nodeStatusValues.iterator(); localIterator1.hasNext(); ) {
            ByteString aByteString = (ByteString) localIterator1.next();
            Node node = Node.parseFrom(aByteString);
            boolean ngFounded = false;
            for (NodeGroup.Builder aNgBuilder : ngBuilders) {
                if ((StringUtils.equals(aNgBuilder.getNgLabel(), node.getNgLabel())) &&
                        (StringUtils.equals(aNgBuilder.getNgVersion(), node.getNgVersion()))) {
                    ngFounded = true;
                    aNgBuilder.addNodes(node);
                    break;
                }
            }
            if (!ngFounded) {
                NodeGroup.Builder aNodeGroup = NodeGroup.newBuilder();
                aNodeGroup.setNgLabel(node.getNgLabel()).setNgVersion(node.getNgVersion());
                aNodeGroup.addNodes(node);
                ngBuilders.add(aNodeGroup);
            }
        }
        ArrayList returnValues = new ArrayList();
        for (NodeGroup.Builder aNgBuilder : ngBuilders) {
            ((List) returnValues).add(aNgBuilder.build());
        }
        return returnValues;

    }

    /**
     * 获取单元配置
     *
     * @param label   单元标签
     * @param version 单元版本
     * @return 单元配置
     * @throws Exception 异常
     */

    public static byte[] getNgConfig(String label, String version) throws Exception {
        String key = genLv(label, version);
        ByteString byteString = (ByteString) ngCfgTable.get(key);
        if (byteString == null) {
            return new byte[0];
        }
        return byteString.toByteArray();

    }


    /**
     * 存储单元配置
     *
     * @param label   单元标签
     * @param version 单元版本
     * @param content 配置数据
     * @throws Exception 异常
     */
    public static void storeNgConfig(String label, String version, byte[] content)
            throws Exception {
        String key = genLv(label, version);
        ByteString byteString = ByteString.copyFrom(content);
        ngCfgTable.put(key, byteString);

    }

    /**
     * 存储节点状态历史
     *
     * @param node 待存储状态的节点
     * @throws Exception 异常
     */

    public static void storeNodeHis(Node node)
            throws Exception {
    }

    /**
     * 获取特定节点的状态历史
     *
     * @param label   单元标签
     * @param version 版本
     * @param ip      节点ip
     * @param port    节点端口
     * @param start   查询的起始时间
     * @param end     查询的结束时间
     * @return 节点状态列表
     * @throws Exception 异常
     */
    public static List<NodeStatus> getNodeHis(String label, String version, String ip, int port, long start, long end)
            throws Exception {
        return new ArrayList();
    }

    /**
     * 保存主机信息
     *
     * @param fromIp
     * @param server
     * @throws Exception
     */
    public static void storeServer(String fromIp, Server server)
            throws Exception {
        serverInfoTable.put(fromIp, server.toByteString());
    }

    /**
     * 获取主机信息
     *
     * @param ip 内网ip
     * @return 主机信息
     * @throws Exception 异常
     */

    public static Server getServer(String ip) throws Exception {
        ByteString byteString = (ByteString) serverInfoTable.get(ip);
        if (byteString == null) {
            return null;
        }
        return Server.parseFrom(byteString);
    }


    public static void removeServerInfo(String ip) throws Exception {
        serverInfoTable.remove(ip);
    }


    public static Hashtable<String, Server> getServerInfoTable()
            throws InvalidProtocolBufferException {
        Hashtable returnValue = new Hashtable();
        for (String ip : serverInfoTable.keySet()) {
            returnValue.put(ip, Server.parseFrom((ByteString) serverInfoTable.get(ip)));
        }
        return returnValue;
    }


    private static String genLv(String label, String version) {
        return label + "[" + version + "]";
    }

}
