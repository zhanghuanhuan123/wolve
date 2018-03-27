package com.wolves.wolf.framework.comm;

import Ice.Current;
import Ice.TCPConnectionInfo;
import com.wolves.admin.pb.NodeStatusEnum;
import com.wolves.framework.pb.WolRequest;
import com.wolves.framework.pb.WolResponse;
import com.wolves.wolf.StartupConstant;
import com.wolves.wolf.framework.cmd.Executor;
import com.wolves.wolf.framework.comm.generated._DIceCommStationDisp;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


class DIceCommStationI extends _DIceCommStationDisp {
    private static Logger logger = LogManager.getLogger(DIceCommStationI.class);

    public byte[] _do(byte[] request, Current __current) {
        if (StartupConstant.NODE_STATUS != NodeStatusEnum.ON) {
            StartupConstant.NODE_STATUS = NodeStatusEnum.ON;
        }

        String remoteIp = null;
        if (__current != null) {
            TCPConnectionInfo info = (TCPConnectionInfo) __current.con.getInfo();
            remoteIp = info.remoteAddress;
        }
        WolResponse response;
        try {
            WolRequest.Builder requestBuilder = WolRequest.newBuilder(WolRequest.parseFrom(request));
            requestBuilder.setFromIp((String) StringUtils.defaultIfBlank(remoteIp, ""));
            response = Executor.exec(requestBuilder.build());
        } catch (Exception e) {

            WolResponse.Builder responseBuilder = WolResponse.newBuilder();
            responseBuilder.setCode("00011").setDesc("no such action ...");
            response = responseBuilder.build();
            logger.error(response.getCode() + ":" + response.getDesc(), e);
        }
        return response.toByteArray();
    }

    public void touch(byte[] request, Current __current) {
        if (StartupConstant.NODE_STATUS != NodeStatusEnum.ON) {
            StartupConstant.NODE_STATUS = NodeStatusEnum.ON;
        }

        String remoteIp = null;
        if (__current != null) {
            TCPConnectionInfo info = (TCPConnectionInfo) __current.con.getInfo();

            remoteIp = info.remoteAddress;
        }
        try {
            WolRequest.Builder requestBuilder = WolRequest.newBuilder(WolRequest.parseFrom(request));
            Executor.exec(requestBuilder.build());
        } catch (Exception e) {
            logger.error("00011:no such action ...", e);
        }
    }
}
