package com.wolves.admin.action.machine;


import com.wolves.admin.mem.MemConnector;
import com.wolves.admin.pb.GetServersRequest;
import com.wolves.admin.pb.GetServersResponse;
import com.wolves.outpost.pb.Server;
import com.wolves.wolf.framework.cmd.WolAction;


public class GetServersAction extends WolAction {

    public void exec() throws Exception {
        GetServersRequest getServersRequest = (GetServersRequest) getRequest(GetServersRequest.class);
        GetServersResponse.Builder responseBuilder = GetServersResponse.newBuilder();
        if (getServersRequest.getIpsCount() == 0) {
            // AdminCommon.MachineInfo machineInfo = CasConnector.getMachineInfo(lip);
            for (Server server : MemConnector.getServerInfoTable().values())
                responseBuilder.addServers(server);
        } else {
            for (String ip : getServersRequest.getIpsList()) {
                Server server = MemConnector.getServer(ip);
                if (server != null) {
                    responseBuilder.addServers(server);
                }
            }
        }
        setResponse("NA", null, responseBuilder.build());
    }

}

