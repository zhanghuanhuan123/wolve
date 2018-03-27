package com.wolves.wolf;


import com.wolves.admin.pb.NodeStatusEnum;
import com.wolves.wolf.framework.config.LocalConfig;
import com.wolves.wolf.framework.startup.StartupStepClass;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.management.ManagementFactory;

/**
 * Created by IntelliJ IDEA.
 * User: ZhangHuan
 * Date: 2015-8-12
 * Time: 9:35:19
 */
public class Startup {
    private static final Logger logger = LogManager.getLogger(Startup.class);


    public static void main(String[] args) throws Exception {

        try {
            beforeStartup(args);
            logger.info(StringUtils.repeat("*", 60));
            logger.info(StringUtils.center("NOW STARTING " + StartupConstant.NG_LABEL + " " + StartupConstant.NG_VERSION, 60, " "));
            logger.info(StringUtils.repeat("*", 60));
            StartupStepClass startupStepClass = (StartupStepClass) Class.forName(StartupConstant.STARTUP_STEP_CLASS).newInstance();
            startupStepClass.beforeInitialXwork();
            // 初始化xwork
            startupStepClass.initialXwork();
            startupStepClass.afterInitialXwork();
            // 初始化AdminConnector，AdminConnector会自动读取配置文件并进行初始化
            startupStepClass.beforeInitialAc();
            startupStepClass.initialAc();
            startupStepClass.afterInitialAc();
            startupStepClass.beforeLoadRemoteConfig();
            // 连接admin节点，获取远程配置数据
            startupStepClass.loadRemoteConfig();
            // 变更节点状态为Standby
            StartupConstant.NODE_STATUS = NodeStatusEnum.STANDBY;
            startupStepClass.afterLoadRemoteConfig();
            startupStepClass.beforeInitialIceServer();
            // 根据配置信息，启动ice监听端口
            if ((StringUtils.isNotBlank(StartupConstant.LOCAL_IP)) && (StartupConstant.STARTUP_ICE_SERVICE)) {
                startupStepClass.initialIceServer();
            }
            startupStepClass.afterInitialIceServer();
            // 变更节点状态为Ready
            StartupConstant.NODE_STATUS = NodeStatusEnum.READY;
            startupStepClass.beforeStartPingTask();
            // 启动周期性ping汇报线程
            startupStepClass.startPingTask();
            startupStepClass.afterStartPingTask();
        } catch (Exception e) {
            shutdown(e);
        }

    }


    private static void beforeStartup(String[] args) throws Exception {

        if (args.length != 1) {
            printHelp();
        }
        LocalConfig.CONFIG_FILE_NAME = args[0];
        StartupConstant.NG_LABEL = LocalConfig.getNG_LABEL();
        StartupConstant.NG_VERSION = LocalConfig.getNG_VERSION();
        StartupConstant.STARTUP_STEP_CLASS = LocalConfig.getSTARTUP_STEP_CLASS();
        StartupConstant.XWORK_FILE_NAME = LocalConfig.getXWORK_FILE_NAME();
        StartupConstant.ADMIN_NG_LABEL = LocalConfig.getAdminCommConfig("label");
        StartupConstant.ADMIN_NG_VERSION = LocalConfig.getAdminCommConfig("version");
        StartupConstant.LOCAL_IP = LocalConfig.getLOCAL_IP();
        StartupConstant.PROTOCOL = LocalConfig.getPROTOCOL();
        StartupConstant.STARTUP_ICE_SERVICE = LocalConfig.getSTARTUP_ICE_SERVICE();
        String processName = ManagementFactory.getRuntimeMXBean().getName();
        if ((StringUtils.isNotBlank(processName)) && (processName.contains("@")))
            StartupConstant.PROCESS_ID = processName.substring(0, processName.indexOf("@"));

    }


    private static void shutdown(Exception e) {
        logger.info(StringUtils.center("System shutting down due to Exeception!", 60, " "), e);
        System.exit(0);
    }


    private static void printHelp() {
        System.out.println("usage : java com.wolves.wolf.Startup config-file");
        System.out.println("  in most case, a config-file named like <xxx_node.xml>");
        logger.info("usage : java com.wolves.wolf.Startup config-file");
        logger.info("  in most case, a config-file named like <xxx_node.xml>");

    }

}

