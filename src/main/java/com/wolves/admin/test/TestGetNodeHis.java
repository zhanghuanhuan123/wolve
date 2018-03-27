package com.wolves.admin.test;


/**
 * Created by IntelliJ IDEA.
 * User: ZhangHuan
 * Date: 2015-9-2
 * Time: 17:32:59
 */
public class TestGetNodeHis {

    public static void main(String[] args) throws Exception {
       /* AdminCom.GetNodeHisRequest.Builder gnhBd = AdminCom.GetNodeHisRequest.newBuilder();
        gnhBd.setBaseRequest(GenerateBaseRequest.generate(StartupConstant.ADMIN_NG_LABEL, StartupConstant.ADMIN_NG_VERSION, "getndhis", "0.0.1"));
        gnhBd.setStart(0).setEnd(System.currentTimeMillis())
                .setNgLabel("DaYuNG").setNgVersion("0.0.1").setIp("10.241.85.239").setPort(7000);
        byte[] requestBody = gnhBd.build().toByteArray();
        byte[] resBytes = CommManager.sendForTestOnly("110.76.38.169", 8000, requestBody);
        AdminCom.GetNodeHisResponse response = AdminCom.GetNodeHisResponse.parseFrom(resBytes);
        System.out.println(response.toString());*/
    }

}
