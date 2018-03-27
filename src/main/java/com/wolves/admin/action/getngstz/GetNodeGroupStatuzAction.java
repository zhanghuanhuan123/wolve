package com.wolves.admin.action.getngstz;


import com.wolves.admin.mem.MemConnector;
import com.wolves.admin.pb.GetNgStatuzResponse;
import com.wolves.wolf.framework.cmd.WolAction;

import java.util.List;


public class GetNodeGroupStatuzAction extends WolAction {

    public void exec() throws Exception {
        GetNgStatuzResponse.Builder gnsrBuilder = GetNgStatuzResponse.newBuilder();
        List ngs = MemConnector.getNodeGroups();
        gnsrBuilder.addAllNodeGroups(ngs);
        setResponse("NA", null, gnsrBuilder.build());

    }

}
