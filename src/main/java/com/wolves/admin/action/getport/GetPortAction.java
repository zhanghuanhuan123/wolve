package com.wolves.admin.action.getport;


import com.wolves.admin.conf.LoadConfig;
import com.wolves.admin.mem.MemConnector;
import com.wolves.admin.pb.GetPortRequest;
import com.wolves.admin.pb.GetPortResponse;
import com.wolves.admin.pb.Node;
import com.wolves.admin.pb.NodeGroup;
import com.wolves.admin.util.GenerateAvailablePort;
import com.wolves.wolf.framework.cmd.WolAction;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


public class GetPortAction extends WolAction {

    public void exec() throws Exception {
        GetPortRequest getPortRequest = (GetPortRequest) getRequest(GetPortRequest.class);
        GetPortResponse.Builder responseBuilder = GetPortResponse.newBuilder();
        HashSet excludesPorts = new HashSet();
        // 将上次启动失败的ice端口添加到exclude ports里面
        for (Integer failedPort : getPortRequest.getFailedPortsList()) {
            excludesPorts.add(failedPort + "");
        }
        // List<AdminCommon.NodeGroup> nodeGroups = CasConnector.getNodeGroups();//抛弃nosql
        List<NodeGroup> nodeGroups = MemConnector.getNodeGroups();
        for (NodeGroup aNg : nodeGroups) {
            for (Node node : aNg.getNodesList()) {
                if (node.getIp().equals(getPortRequest.getLocalip())) {
                    excludesPorts.add(node.getPort() + "");
                }
            }
        }
        int port = GenerateAvailablePort.generate(LoadConfig.getPortRangeMin(), LoadConfig.getPortRangeMax(), excludesPorts);
        responseBuilder.setServicePort(port);
        setResponse("NA", null, responseBuilder.build());

    }

}
