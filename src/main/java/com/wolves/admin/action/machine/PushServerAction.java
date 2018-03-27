package com.wolves.admin.action.machine;


import com.wolves.admin.mem.MemConnector;
import com.wolves.admin.pb.PushServerRequest;
import com.wolves.admin.pb.PushServerResponse;
import com.wolves.outpost.pb.Server;
import com.wolves.wolf.framework.cmd.WolAction;


public class PushServerAction extends WolAction {

    public void exec() throws Exception {
        PushServerRequest pushServerRequest = (PushServerRequest) getRequest(PushServerRequest.class);
        PushServerResponse.Builder responseBuilder = PushServerResponse.newBuilder();
        // 首先获取主机信息数据
        // CasConnector.storeMachineInfo(machineInfo);
        MemConnector.storeServer(getFromIp(),
                Server.newBuilder(pushServerRequest
                        .getServer()).setUpdateTime(System.currentTimeMillis()).build());
        setResponse("NA", null, responseBuilder.build());
    }
}

