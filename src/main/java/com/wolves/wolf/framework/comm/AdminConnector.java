package com.wolves.wolf.framework.comm;


import com.google.protobuf.Any;
import com.google.protobuf.Message;
import com.wolves.framework.pb.WolRequest;
import com.wolves.framework.pb.WolResponse;
import com.wolves.wolf.StartupConstant;
import com.wolves.wolf.framework.config.LocalConfig;


public class AdminConnector {
    private static final int OVERRIDE_TIMEOUT = 2000;
    private static final int MESSAGE_SIZE_MAX = 20480;
    private static NodeConnector nodeConnector;


    public static synchronized void initial(boolean isLocalInvoke)
            throws Exception {
        if (nodeConnector == null) {
            nodeConnector = new NodeConnector(OVERRIDE_TIMEOUT, MESSAGE_SIZE_MAX,
                    LocalConfig.getAdminCommConfig("host"),
                    Integer.parseInt(LocalConfig.getAdminCommConfig("port")),
                    isLocalInvoke, NodeConnector.Protocol.TCP);
        }

    }


    public static <T extends Message> Response<T> send(String act, String actVersion, Message requestBody, Class<T> clazz) throws Exception {

        WolRequest.Builder wolRequestBuilder = WolRequest.newBuilder();
        wolRequestBuilder.setNgLabel(StartupConstant.ADMIN_NG_LABEL)
                .setNgVersion(StartupConstant.ADMIN_NG_VERSION)
                .setAction(act)
                .setActVersion(actVersion);
        if (requestBody != null) {
            wolRequestBuilder.setBody(Any.pack(requestBody));
        }


        if (nodeConnector == null) {
            initial(false);
        }


        WolResponse dafResponse = nodeConnector.send(wolRequestBuilder.build());
        Response response = new Response();
        response.setCode(dafResponse.getCode());
        response.setDesc(dafResponse.getDesc());
        if (dafResponse.hasBody()) {
            response.setBody(dafResponse.getBody().unpack(clazz));
        }
        return response;
    }

}

