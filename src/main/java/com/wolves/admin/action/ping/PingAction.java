package com.wolves.admin.action.ping;


import com.wolves.admin.mem.MemConnector;
import com.wolves.admin.pb.Node;
import com.wolves.admin.pb.PingRequest;
import com.wolves.admin.pb.PingResponse;
import com.wolves.wolf.framework.cmd.WolAction;


public class PingAction extends WolAction {

    public void exec() throws Exception {
        PingRequest pingRequest = (PingRequest) getRequest(PingRequest.class);
        Node nodeProto = pingRequest.getNode();
        Node.Builder nodeBuilder = Node.newBuilder(nodeProto);
        nodeBuilder.setRecordTime(System.currentTimeMillis());
        Node node = nodeBuilder.build();
        // 直接将节点信息封装保存到cassandra
        // CasConnector.storeNodeStatus(node);
        MemConnector.storeNodeStatus(node);
        // 另外需要将节点状态保存到节点历史当中
        // CasConnector.storeNodeHis(node);
        MemConnector.storeNodeHis(node);
        setResponse("NA", null, PingResponse.newBuilder().build());
    }

}
