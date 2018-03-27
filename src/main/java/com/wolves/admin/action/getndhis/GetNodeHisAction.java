package com.wolves.admin.action.getndhis;


import com.wolves.admin.mem.MemConnector;
import com.wolves.admin.pb.GetNodeHisRequest;
import com.wolves.admin.pb.GetNodeHisResponse;
import com.wolves.wolf.framework.cmd.WolAction;

import java.util.List;


public class GetNodeHisAction extends WolAction {

    public void exec() throws Exception {
        GetNodeHisRequest getNodeHisRequest = (GetNodeHisRequest) getRequest(GetNodeHisRequest.class);
        GetNodeHisResponse.Builder responseBuilder = GetNodeHisResponse.newBuilder();
        List nodeStatuses = MemConnector.getNodeHis(getNodeHisRequest
                .getNgLabel(), getNodeHisRequest.getNgVersion(), getNodeHisRequest
                .getIp(), getNodeHisRequest.getPort(), getNodeHisRequest
                .getStart(), getNodeHisRequest.getEnd());
        responseBuilder.addAllNodeStatuz(nodeStatuses);
        setResponse("NA", null, responseBuilder.build());
    }

}

