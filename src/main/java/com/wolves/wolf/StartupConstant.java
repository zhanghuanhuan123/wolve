package com.wolves.wolf;


import com.wolves.admin.pb.NodeStatusEnum;

//业务单元启动常量, 这些常量应该由业务研发人员编写
public class StartupConstant {
    // 业务单元标签
    public static String NG_LABEL;
    // 单元版本号
    public static String NG_VERSION;
    // xwork配置文件
    public static String XWORK_FILE_NAME;
    // startup启动类
    public static String STARTUP_STEP_CLASS;
    // admin单元标签
    public static String ADMIN_NG_LABEL;
    // admin单元版本
    public static String ADMIN_NG_VERSION;
    // 本地Ip
    public static String LOCAL_IP;
    // 协议类型
    public static String PROTOCOL;
    // 本机状态
    public static NodeStatusEnum NODE_STATUS = NodeStatusEnum.OFF;
    // 进程号
    public static String PROCESS_ID;
    //启动ice服务
    public static boolean STARTUP_ICE_SERVICE;

}

