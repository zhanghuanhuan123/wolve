package com.wolves.admin.test;


/**
 * Created by IntelliJ IDEA.
 * User: ZhangHuan
 * Date: 2015-9-2
 * Time: 9:19:42
 */
public class TestSetConfig {

    public static void main(String[] args) throws Exception {
      /*  AdminCom.SetConfigRequest.Builder scrBd = AdminCom.SetConfigRequest.newBuilder();
        scrBd.setBaseRequest(GenerateBaseRequest.generate("ADMIN", "0.0.1", "setcfg", "0.0.1"));
        scrBd.setNgLabel("CashmeCoreNG").setNgVersion("0.0.1");
//        scrBd.setNgLabel("ReverseNG").setNgVersion("0.0.1");
        AdminCommon.BaseNodeConfig.Builder bncBuilder = AdminCommon.BaseNodeConfig.newBuilder();
        *//*bncBuilder.setPingAdminInterval(10)
                .setServicePort(80)
                .setServerSize(1)
                .setServerSizeMax(100)
                .setServerTimeout(2000)
                .setClientTimeout(20000);*//*
        bncBuilder.setClientTimeout(20000);
        // 下面开始加载tigase服务所需要的文件
        *//*String[] files = null;
        files = new String[]{"/root/IdeaProjects/tigase-server/etc/init.properties",
                "/root/IdeaProjects/tigase-server/etc/tigase.conf"};
        if (files != null) {
            for (String aFile : files) {
                File file = new File(aFile);
                byte[] fileContent = FileUtils.readFileToByteArray(file);
                AdminCommon.File.Builder fileBuilder = AdminCommon.File.newBuilder();
                fileBuilder.setFileName(file.getName());
                fileBuilder.setFileContent(ByteString.copyFrom(fileContent));
                bncBuilder.addConfFiles3Rd(fileBuilder);
            }
        }*//*
        scrBd.setNodeConfig(bncBuilder);
        byte[] requestBody = scrBd.build().toByteArray();
        byte[] resBytes = CommManager.sendForTestOnly("110.76.38.169", 8000, requestBody);
        AdminCom.SetConfigResponse response = AdminCom.SetConfigResponse.parseFrom(resBytes);
        System.out.println(response.toString());
        System.out.println(response.getBaseResponse().getErrCode());*/
    }

}
