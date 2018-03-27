package com.wolves.admin.test;


import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: ZhangHuan
 * Date: 2015-9-2
 * Time: 10:44:19
 */
public class TestGetPort {

    public static void main(String[] args) throws Exception {
    /*    AdminCom.GetPortRequest.Builder gprBd = AdminCom.GetPortRequest.newBuilder();
        gprBd.setBaseRequest(GenerateBaseRequest.generate("ADMIN", "0.0.1", "getport", "0.0.1"));
        gprBd.addFailedPorts(8001).setLocalip("192.168.174.12");
        byte[] requestBody = gprBd.build().toByteArray();
        FileUtils.writeByteArrayToFile(new File("/root/tempFileByMe"), requestBody);
        byte[] resBytes = CommManager.sendForTestOnly("192.168.1.70", 8999, requestBody);
        AdminCom.GetPortResponse response = AdminCom.GetPortResponse.parseFrom(resBytes);
        System.out.println(response.toString());*/
    }

}
