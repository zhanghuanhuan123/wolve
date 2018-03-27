package com.wolves.admin.test;


import com.wolves.admin.pb.GetConfigRequest;
import com.wolves.admin.pb.GetConfigResponse;
import com.wolves.framework.pb.WolResponse;
import com.wolves.wolf.framework.comm.CommManager;

/**
 * Created by IntelliJ IDEA.
 * User: ZhangHuan
 * Date: 2017-8-30
 * Time: 10:29:44
 */
public class TestGetConfig {

    public static void main(String[] args) throws Exception {
        GetConfigRequest.Builder gcrBd = GetConfigRequest.newBuilder();
        gcrBd.setNgLabel("HttpProxyNG").setNgVersion("0.0.1").setIp("127.0.0.1");

        byte[] requestBody = gcrBd.build().toByteArray();

        byte[] resBytes = CommManager.sendForTestOnly("110.76.38.169", 8000, requestBody);
        WolResponse dafResponse = WolResponse.parseFrom(resBytes);
        System.out.println("ErrCode : " +dafResponse.getCode() );
        GetConfigResponse response=dafResponse.getBody().unpack(GetConfigResponse.class);
        System.out.println("ClientTimeout : " + response.getNodeConfig().getClientTimeout());
        System.out.println("PingAdminInterval : " + response.getNodeConfig().getPingAdminInterval());
        System.out.println("SerializedSize : " + response.getNodeConfig().getSerializedSize());
        System.out.println("ServerSize : " + response.getNodeConfig().getServerSize());
        System.out.println("ServerSizeMax : " + response.getNodeConfig().getServerSizeMax());
        System.out.println("ServerTimeout : " + response.getNodeConfig().getServerTimeout());
        System.out.println("ServicePort : " + response.getNodeConfig().getServicePort());
    }

}
