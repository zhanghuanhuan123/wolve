package com.wolves.admin.test;


import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ZhangHuan
 * Date: 2015-9-7
 * Time: 14:41:01
 */
public class TestGetNgsStaz {

    public static void main(String[] args) throws Exception {
      /*  String ip;
        int port;
        if (args.length != 2) {
            ip = "110.76.38.169";
            port = 8000;
        } else {
            ip = args[0];
            port = Integer.parseInt(args[1]);
        }

        GetNgStatuzRequest.Builder gnsrBuilder = GetNgStatuzRequest.newBuilder();
        gnsrBuilder.setBaseRequest(GenerateBaseRequest.generate("ADMIN",
                "0.0.1", "getngstz", "0.0.1"));
        byte[] requestBody = gnsrBuilder.build().toByteArray();
        byte[] resBytes = new LoadHttpResource()
                .postBinResource("http://121.41.53.178:8080/kxe-core/action.do", requestBody);
//                 .postBinResource("https://api.kaxiaoer.cn:8443/kxe-core/action.do", requestBody);
//        byte[] resBytes = CommManager.sendForTestOnly(ip, port, requestBody);
        AdminCom.GetNgStatuzResponse response = AdminCom.GetNgStatuzResponse.parseFrom(resBytes);
        System.out.println("ErrCode : " + response.getBaseResponse().getErrCode());
        long total = 0;
        for (AdminCommon.NodeGroup nodeGroup : response.getNodeGroupsList()) {
            System.out.println(StringUtils.repeat("=", 40));
            System.out.println("NgLabel : " + nodeGroup.getNgLabel());
            System.out.println("NgVersion : " + nodeGroup.getNgVersion());
            System.out.println("NodesCount : " + nodeGroup.getNodesCount());
            for (AdminCommon.Node node : nodeGroup.getNodesList()) {
                System.out.println(StringUtils.repeat("-", 20));
                System.out.println("Ip : " + node.getIp());
                System.out.println("Port : " + node.getPort());
                System.out.println("LoadFactor : " + node.getLoadFactor());
                System.out.println("CollectTime : " + new Date(node.getNodeStatus().getCollectTime()));
                for (AdminCommon.InterfaceStatus interfaceStatus : node.getNodeStatus().getInterfaceStatusList()) {
                    System.out.println("    Action = " + interfaceStatus.getAction());
                    System.out.println("    AvaiCostsPerMin = " + interfaceStatus.getAvaiCostsPerMin());
                    System.out.println("    AvaiHitsPerMin = " + interfaceStatus.getAvaiHitsPerMin());
                    total += interfaceStatus.getAvaiHitsPerMin();
                    System.out.println("    Jams = " + interfaceStatus.getJams());
                    System.out.println("    " + StringUtils.repeat("-", 16));
                }
                System.out.println("NodeStatus : " + node.getNodeStatus().getNodeStatus());
            }
        }
        System.out.println(total + " " + (total / 60));*/
    }

}
