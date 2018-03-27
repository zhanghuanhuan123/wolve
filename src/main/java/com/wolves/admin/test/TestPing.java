package com.wolves.admin.test;


/**
 * Created by IntelliJ IDEA.
 * User: ZhangHuan
 * Date: 2015-9-2
 * Time: 11:25:58
 */
public class TestPing {

    public static void main(String[] args) throws Exception {
       /* while (true) {
            AdminCom.PingRequest.Builder prBd = AdminCom.PingRequest.newBuilder();
            prBd.setBaseRequest(GenerateBaseRequest.generate("ADMIN", "0.0.1", "ping", "0.0.1"));

            AdminCommon.Node.Builder nodeBuilder = AdminCommon.Node.newBuilder();

            AdminCommon.NodeStatus.Builder statusBuilder = AdminCommon.NodeStatus.newBuilder();
            statusBuilder.setNodeStatus(AdminCommon.NodeStatus.NodeStatusEnum.ON);
            statusBuilder.setCollectTime(System.currentTimeMillis()).setNodeStatus(AdminCommon.NodeStatus.NodeStatusEnum.ON);

            for (int i = 0; i < 5; i++) {
                AdminCommon.InterfaceStatus.Builder ifBuilder = AdminCommon.InterfaceStatus.newBuilder();
                ifBuilder.setAction("method" + i).setActVersion("0.0.1");
                ifBuilder.setAvaiCostsPerMin(RandomUtils.nextInt(100));
                ifBuilder.setAvaiHitsPerMin(RandomUtils.nextInt(100));
                ifBuilder.setJams(RandomUtils.nextInt(10));
                statusBuilder.addInterfaceStatus(ifBuilder);
            }

            nodeBuilder.setNodeStatus(statusBuilder);
            int random = RandomUtils.nextInt(7);
            nodeBuilder.setNgLabel("NEWS").setNgVersion("0.0.7").setIp("192.168.0.4").setPort(7009 + random);
            prBd.setNode(nodeBuilder);
            byte[] requestBody = prBd.build().toByteArray();
            byte[] resBytes = CommManager.sendForTestOnly("192.168.3.15", 80, requestBody);
            AdminCom.PingResponse response = AdminCom.PingResponse.parseFrom(resBytes);
            System.out.println(response.toString());
        }*/
    }

}
