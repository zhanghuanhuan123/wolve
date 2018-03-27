package com.wolves.admin.action.cfg;


import com.wolves.admin.mem.MemConnector;
import com.wolves.admin.pb.SetConfigRequest;
import com.wolves.admin.pb.SetConfigResponse;
import com.wolves.wolf.framework.cmd.WolAction;


public class SetCfgAction extends WolAction {

    public void exec() throws Exception {
        SetConfigRequest setConfigRequest = (SetConfigRequest) getRequest(SetConfigRequest.class);
        SetConfigResponse.Builder responseBuilder = SetConfigResponse.newBuilder();
        byte[] configData = setConfigRequest.getNodeConfig().toByteArray();
        MemConnector.storeNgConfig(setConfigRequest.getNgLabel(), setConfigRequest.getNgVersion(), configData);
        setResponse("NA", null, SetConfigResponse.newBuilder().build());
    }

}
