package com.wolves.admin.mem;


import com.wolves.admin.conf.LoadConfig;
import com.wolves.admin.pb.Node;
import com.wolves.admin.pb.NodeGroup;
import com.wolves.admin.pb.NodeStatus;
import com.wolves.admin.pb.NodeStatusEnum;
import com.wolves.outpost.pb.Server;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;


public class MemCleaner extends TimerTask {
    private static final Logger logger = LogManager.getLogger(MemCleaner.class);


    public static void schedule() {
        Timer timer = new Timer();
        timer.schedule(new MemCleaner(), 0L, 5000L);
    }


    public void run() {
        try {
            for (Iterator localIterator1 = MemConnector.getNodeGroups().iterator(); localIterator1.hasNext(); ) {
                // 从节点组（单元）开始进行轮询式的清理
                NodeGroup aNg = (NodeGroup) localIterator1.next();
                // 再从节点开始进行轮询式的清理
                for (Node aNode : aNg.getNodesList()) {
                    logger.info(aNode.getNgLabel() + "[" + aNode.getNgVersion() + "]@" + aNode
                            .getIp() + ":" + aNode.getPort() + " i am alive.");
                    if (System.currentTimeMillis() - aNode.getRecordTime() >
                            Integer.parseInt(LoadConfig.getNodeCleanupDuration()) * 1000) {
                        MemConnector.removeNodeStatus(aNode);
                        logger.info(aNode.getNgLabel() + "[" + aNode.getNgVersion() + "]@" + aNode
                                .getIp() + ":" + aNode.getPort() + " seems dead for a long time, and has been cleaned up.");

                    } else if ((System.currentTimeMillis() - aNode.getRecordTime() >
                            Integer.parseInt(LoadConfig.getNodeDeadDuration()) * 1000) &&
                            (!aNode.getNodeStatus().equals(NodeStatusEnum.OFF))) {
                        Node.Builder nodeBuilder = Node.newBuilder(aNode);
                        NodeStatus.Builder statBuilder = NodeStatus.newBuilder(nodeBuilder.getNodeStatus());
                        statBuilder.setNodeStatus(NodeStatusEnum.OFF);
                        nodeBuilder.setNodeStatus(statBuilder);
                        MemConnector.storeNodeStatus(nodeBuilder.build());
                        logger.info(aNode.getNgLabel() + "[" + aNode.getNgVersion() + "]@" + aNode
                                .getIp() + ":" + aNode.getPort() + " seems dead, and has been marked up.");
                    }

                }

            }

            Hashtable<String, Server> serverTable = MemConnector.getServerInfoTable();
            for (String ip : serverTable.keySet()) {
                Server server = serverTable.get(ip);
                if (System.currentTimeMillis() - server.getUpdateTime() >
                        Integer.parseInt(LoadConfig.getServerCleanupDuration()) * 1000) {
                    MemConnector.removeServerInfo(ip);
                    logger.info("server " + ip + " seems dead for a long time, and has been cleaned up.");
                }
            }

        } catch (Exception e) {
            logger.error("Ops ! MemCleaner encounter error ...", e);

        }

    }

}

