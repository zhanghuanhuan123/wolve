package com.wolves.admin.test;

import com.wolves.admin.pb.GetServersRequest;
import com.wolves.admin.pb.GetServersResponse;
import com.wolves.framework.pb.WolResponse;
import com.wolves.wolf.framework.comm.CommManager;

/**
 * Created by IntelliJ IDEA.
 * User: ZhangHuan
 * Date: 2015-10-13
 * Time: 14:03:28
 * s
 */
public class TestGetMachine {

    public static void main(String[] args) throws Exception {

        GetServersRequest.Builder gcrBd = GetServersRequest.newBuilder();
        gcrBd.addIps("192.168.3.15");
        byte[] requestBody = gcrBd.build().toByteArray();
        byte[] resBytes = CommManager.sendForTestOnly("110.76.38.169", 8000, requestBody);
        WolResponse dafResponse = WolResponse.parseFrom(resBytes);
        System.out.println("ErrCode : " +dafResponse.getCode() );
        GetServersResponse response=dafResponse.getBody().unpack(GetServersResponse.class);
        System.out.println(response.toString());
    }

}
