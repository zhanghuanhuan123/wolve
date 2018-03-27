package com.wolves.wolf.framework.monitor;


import com.wolves.admin.pb.*;
import com.wolves.wolf.StartupConstant;
import com.wolves.wolf.framework.comm.AdminConnector;
import com.wolves.wolf.framework.config.RemoteConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: ZhangHuan
 * Date: 2015-9-6
 * Time: 15:52:58
 */
public class PingTask extends TimerTask {
    private static final Logger logger = LogManager.getLogger(PingTask.class);


    public static synchronized void schedule() {
        Timer timer = new Timer();
        timer.schedule(new PingTask(), 0L, RemoteConfig.PING_INTERVAL);

    }


    public void run() {

        try {
            PingRequest.Builder builder = PingRequest.newBuilder();
            Node.Builder nodeBuilder = Node.newBuilder();
            nodeBuilder.setIp(StartupConstant.LOCAL_IP).setNgLabel(StartupConstant.NG_LABEL).setNgVersion(StartupConstant.NG_VERSION);
            if (StartupConstant.PROTOCOL.equals("UDP"))
                nodeBuilder.setProtocol(Protocol.UDP);
            else {
                nodeBuilder.setProtocol(Protocol.TCP);

            }
            if (StartupConstant.STARTUP_ICE_SERVICE)
                nodeBuilder.setPort(RemoteConfig.SERVER_PORT);
            else {
                nodeBuilder.setPort(-1);
            }
            NodeStatus.Builder nsBuilder = NodeStatus.newBuilder();

            nsBuilder.setNodeStatus(StartupConstant.NODE_STATUS).setCollectTime(System.currentTimeMillis());
            // 从ExecutorMonitor获取全量的接口状态数据封装
            List<InterfaceMonitor> interfaceMonitors = ExecutorMonitor.fetchAndResetAll();
            for (InterfaceMonitor aInterfaceMonitor : interfaceMonitors) {
                InterfaceStatus.Builder isBuilder = InterfaceStatus.newBuilder();
                isBuilder.setAction(aInterfaceMonitor.getAction()).setActVersion(aInterfaceMonitor.getActVersion())
                        .setJams((int) aInterfaceMonitor.getJams())
                        .setAvaiCostsPerMin((int) aInterfaceMonitor.getAvaiCostsPerMin())
                        .setAvaiHitsPerMin((int) aInterfaceMonitor
                                .getAvaiHitsPerMin());
                nsBuilder.addInterfaceStatus(isBuilder);
            }

            // 填写进程号
            nodeBuilder.setPid(StartupConstant.PROCESS_ID);
            nodeBuilder.setNodeStatus(nsBuilder);
            builder.setNode(nodeBuilder);
            // 发送请求
            AdminConnector.send("ping", "0.0.1", builder.build(), PingResponse.class);
            logger.info("node status syn success !!!");

        } catch (Exception e) {
            logger.error("ops , something wrong with node status syn !!!", e);
        }

    }

}

