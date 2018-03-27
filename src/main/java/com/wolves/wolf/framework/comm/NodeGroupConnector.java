package com.wolves.wolf.framework.comm;


import com.wolves.admin.pb.Node;
import com.wolves.admin.pb.Protocol;
import com.wolves.admin.pb.WhereIsRequest;
import com.wolves.admin.pb.WhereIsResponse;
import com.wolves.framework.pb.WolRequest;
import com.wolves.framework.pb.WolResponse;
import com.wolves.wolf.framework.config.RemoteConfig;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

;

/**
 * zhanghuan
 */
public class NodeGroupConnector extends TimerTask {
    private static Logger logger = LogManager.getLogger(NodeGroupConnector.class);
    private Hashtable<String, NodeConnector> loadfactorTable = new Hashtable();
    private String LABEL;
    private String VERSION;
    private long mostRecentlyRefresh = 0L;
    private int random_max = 0;
    private boolean isBgRunning = false;
    private long  freeTime = 0L;
    private long  maxFreeTime = 10;
    synchronized void schedule() {

        if (!this.isBgRunning) {
            Timer timer = new Timer();
            timer.schedule(this, 0L, RemoteConfig.PING_INTERVAL);
            this.isBgRunning = true;

        }

    }


    synchronized void initial(String label, String version) throws Exception {
        if (StringUtils.isEmpty(this.LABEL)) {
            this.LABEL = label;
            this.VERSION = version;
            refreshNodeConnectors();
        }

    }

    /**
     * 綫程安全
     *
     * @throws Exception
     */
    private synchronized void refreshNodeConnectors() throws Exception {
        if (System.currentTimeMillis() - this.mostRecentlyRefresh > RemoteConfig.PING_INTERVAL) {
            List<Node> nodesFromAdmin = getNodesFromAdmin(this.LABEL, this.VERSION);
            Hashtable<String, NodeConnector> loadfactorTable = new Hashtable();
            // 首先生成应该使用的Connector集合, 并顺带计算随机数范围
            int random_max = 0;
            for (Iterator localIterator = nodesFromAdmin.iterator(); localIterator.hasNext(); ) {
                Node aNode = (Node) localIterator.next();
                random_max += aNode.getLoadFactor();
                String key = aNode.getNgLabel() + ":" + aNode.getPort();
                NodeConnector localNode = (NodeConnector) this.loadfactorTable.get(key);
                if (localNode != null) {
                    loadfactorTable.put(key, localNode);
                    localNode.setLoadFactor(aNode.getLoadFactor());
                } else {
                    NodeConnector.Protocol protocol;
                    if (aNode.getProtocol() == Protocol.TCP)
                        protocol = NodeConnector.Protocol.TCP;
                    else {
                        protocol = NodeConnector.Protocol.UDP;
                    }
                    NodeConnector nodeConnector = new NodeConnector(aNode
                            .getNodeConfig().getClientTimeout(), aNode
                            .getNodeConfig().getMessageSizeMax(), aNode
                            .getIp(), aNode.getPort(), false, protocol);
                    nodeConnector.setLoadFactor(aNode.getLoadFactor());
                    loadfactorTable.put(key, nodeConnector);
                }
            }
            // 然后找到应该删除的Connector集合,并销毁释放内存
            Collection<NodeConnector> nodesFromLocal = this.loadfactorTable.values();

            nodesFromLocal.removeAll(loadfactorTable.values());
            for (NodeConnector shouldRemoveNode : nodesFromLocal) {
                shouldRemoveNode.destroy();
            }
            // 最后为NodeConnector设置hitFactor
            for (NodeConnector aNc : loadfactorTable.values()) {
                aNc.setHitFactor(random_max - aNc.getLoadFactor());
            }
            this.loadfactorTable = loadfactorTable;
            //logger.info("NodeGroupConnector@Size{" + this.LABEL + "}:::{" + this.VERSION + "}size"+this.loadfactorTable.values().size());
            //节点组长时间没有子节点存在，停止对应维护线程
            if( this.loadfactorTable.values().size()==0){
                this.freeTime++;
                //logger.info("NodeGroupConnector@Size{" + this.LABEL + "}:::{" + this.VERSION + "}size"+this.freeTime);
                //大于maxFreeTime
                if(this.freeTime>this.maxFreeTime) {
                    String key = this.LABEL + "[" + this.VERSION + "]";
                    //移除内存中节点组记录
                    CommManager.remove(key);
                    //停止维护线程
                    this.cancel();
                }
            }else{
                if(this.freeTime>0) {
                    this.freeTime=0;
                }
            }
            this.random_max = (random_max * (nodesFromAdmin.size() - 1));
            this.mostRecentlyRefresh = System.currentTimeMillis();
        }
    }

    public void run() {
        try {
            refreshNodeConnectors();
            logger.info(this.LABEL + "[" + this.VERSION + "] is refreshed.");
        } catch (Exception localException) {
            logger.error("", localException);
        }
    }

    /**
     * 这里要实现路由算法
     * 考虑到节点个数不会很多,就使用了一个比较笨的办法,这里有优化的空间
     *
     * @param request 请求体
     * @return 应答体
     * @throws Exception 异常
     */
    WolResponse send(WolRequest request) throws Exception {
        int random;
        if (random_max == 0) {
            random = 0;
        } else {
            random = RandomUtils.nextInt(0, random_max);
        }
        int startIndex = 0, endIndex;
        Collection<NodeConnector> nodes = loadfactorTable.values();
        for (NodeConnector aNc : nodes) {
            if (aNc.getHitFactor() != 0) {
                endIndex = startIndex + aNc.getHitFactor() - 1;
            } else {
                endIndex = startIndex;
            }
            if (random >= startIndex && random <= endIndex) {
                return aNc.send(request);
            }
            startIndex = endIndex + 1;
        }
        throw new RuntimeException("specified NodeGroup NOT found ...");
    }

    /**
     * 生成whereis请求数据体
     *
     * @param label   单元标签
     * @param version 单元版本
     * @return whereis应答数据体
     * @throws Exception 异常
     */
    private List<Node> getNodesFromAdmin(String label, String version) throws Exception {
        WhereIsRequest.Builder requestBuilder = WhereIsRequest.newBuilder();
        requestBuilder.setNgLabel(label).setNgVersion(version);
        Response response = AdminConnector.send("whereis", "0.0.1", requestBuilder
                .build(), WhereIsResponse.class);
        if (!response.getCode().equals("NA")) {
            throw new RuntimeException(response.getCode());
        }
        logger.info("getNodesFromAdmin...."+((WhereIsResponse) response.getBody()).getNodesList().toString());
        return ((WhereIsResponse) response.getBody()).getNodesList();
    }

}
