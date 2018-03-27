package com.wolves.admin.action.cfg;


import com.wolves.admin.conf.LoadConfig;
import com.wolves.admin.mem.MemConnector;
import com.wolves.admin.pb.*;
import com.wolves.admin.util.GenerateAvailablePort;
import com.wolves.admin.util.RemoteConfig;
import com.wolves.wolf.framework.cmd.WolAction;

import java.util.HashSet;
import java.util.List;

public class GetCfgAction extends WolAction {

    public void exec() throws Exception {
        GetConfigRequest getConfigRequest = (GetConfigRequest) getRequest(GetConfigRequest.class);
        GetConfigResponse.Builder responseBuilder = GetConfigResponse.newBuilder();
        byte[] content = MemConnector.getNgConfig(getConfigRequest.getNgLabel(), getConfigRequest.getNgVersion());
        byte[] defaultContent = MemConnector.getNgConfig("", "");
        HashSet usedPorts = new HashSet();
        List<NodeGroup> nodeGroups = MemConnector.getNodeGroups();
        for (NodeGroup aNg : nodeGroups) {
            for (Node node : aNg.getNodesList()) {
                if (node.getIp().equals(getConfigRequest.getIp())) {
                    usedPorts.add(node.getPort() + "");
                }
            }
        }

        BaseNodeConfig.Builder nodeConfig = BaseNodeConfig.newBuilder(BaseNodeConfig.parseFrom(content));
        BaseNodeConfig.Builder defaultNodeConfig = BaseNodeConfig.newBuilder(
                BaseNodeConfig.parseFrom(defaultContent));
        if (nodeConfig.getServerSize() == 0) {
            nodeConfig.setServerSize(defaultNodeConfig.getServerSize());
        }
        if (nodeConfig.getServerSizeMax() == 0) {
            nodeConfig.setServerSizeMax(defaultNodeConfig.getServerSizeMax());
        }
        if (nodeConfig.getServerTimeout() == 0) {
            nodeConfig.setServerTimeout(defaultNodeConfig.getServerTimeout());
        }
        if (nodeConfig.getClientTimeout() == 0) {
            nodeConfig.setClientTimeout(defaultNodeConfig.getClientTimeout());
        }
        if (nodeConfig.getPingAdminInterval() == 0) {
            nodeConfig.setPingAdminInterval(defaultNodeConfig.getPingAdminInterval());
        }
        if (nodeConfig.getWhiteIpsCount() == 0) {
            nodeConfig.addAllWhiteIps(defaultNodeConfig.getWhiteIpsList());
        }
        if (nodeConfig.getMessageSizeMax() == 0) {
            nodeConfig.setMessageSizeMax(defaultNodeConfig.getServerSizeMax());
        }
        RemoteConfig.autoFixNodeConfig(nodeConfig);
        if (nodeConfig.getServicePort() == 0) {
            int port = GenerateAvailablePort.generate(LoadConfig.getPortRangeMin(), LoadConfig.getPortRangeMax(), usedPorts);
            nodeConfig.setServicePort(port);
        }
        responseBuilder.setNodeConfig(nodeConfig);
        setResponse("NA", null, responseBuilder.build());

    }

}
