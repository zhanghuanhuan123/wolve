package com.wolves.admin.action.whereis;


import com.wolves.admin.mem.MemConnector;
import com.wolves.admin.pb.*;
import com.wolves.admin.util.RemoteConfig;
import com.wolves.wolf.framework.cmd.WolAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;


public class WhereIsAction extends WolAction {
    private static Logger logger = LogManager.getLogger(WhereIsAction.class);

    public void exec() throws Exception {
        WhereIsRequest whereIsRequest = (WhereIsRequest) getRequest(WhereIsRequest.class);
        WhereIsResponse.Builder responseBuilder = WhereIsResponse.newBuilder();
        // 首先从Cassandra当中提取该单元的配置信息
        // byte[] configData = CasConnector.getNgConfig(whereIsRequest.getNgLabel(), whereIsRequest.getNgVersion());
        byte[] configData = MemConnector.getNgConfig(whereIsRequest.getNgLabel(), whereIsRequest.getNgVersion());
        BaseNodeConfig.Builder nodeConfig = BaseNodeConfig.newBuilder(BaseNodeConfig.parseFrom(configData));
        // 然后从Cassandra当中提取所有的节点信息
        // List<AdminCommon.NodeGroup> nodeGroups = CasConnector.getNodeGroups();
        List<NodeGroup> nodeGroups = MemConnector.getNodeGroups();
        for (NodeGroup aNodeGroup : nodeGroups) {
            // 根据单元标签和版本提取指定单元
            if ((aNodeGroup.getNgLabel().equals(whereIsRequest.getNgLabel())) &&
                    (aNodeGroup.getNgVersion().equals(whereIsRequest.getNgVersion()))) {
                for (Node aNode : aNodeGroup.getNodesList()) {
                    if ((aNode.getNodeStatus().getNodeStatus() == NodeStatusEnum.ON) ||
                            (aNode.getNodeStatus().getNodeStatus() == NodeStatusEnum.READY)) {
                        RemoteConfig.autoFixNodeConfig(nodeConfig);
                        int totalJam = 0;
                        int aHPM = 0;
                        for (InterfaceStatus aIs : aNode.getNodeStatus().getInterfaceStatusList()) {
                            totalJam += aIs.getJams();
                            aHPM += aIs.getAvaiHitsPerMin();
                        }
                        // 为了避免loadfactor过于敏感, 为loadfactor / 10 再统一加 5
                        Node.Builder nodeBuilder = Node.newBuilder(aNode);
                        int loadFactor = totalJam / 10 + 5;
                        // 然后根据主机的设置信息，为loadfactor添加权重
                        // int plus = CasConnector.getMachineInfo(aNode.getIp()).getLoadfactorPlus();
                        int plus = 0;//MemConnector.getServer(aNode.getIp()).getLoadfactorPlus();
                        nodeBuilder.setLoadFactor(loadFactor + plus);
                        // 把节点配置塞进NodeBuilder
                        nodeBuilder.setNodeConfig(nodeConfig);
                        responseBuilder.addNodes(nodeBuilder);
                    }
                }
            }
        }
        setResponse("NA", null, responseBuilder.build());
    }

}

