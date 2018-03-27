package com.wolves.wolf.framework.startup;


import com.wolves.admin.pb.GetConfigRequest;
import com.wolves.admin.pb.GetConfigResponse;
import com.wolves.admin.pb.GetPortRequest;
import com.wolves.admin.pb.GetPortResponse;
import com.wolves.wolf.StartupConstant;
import com.wolves.wolf.framework.cmd.Executor;
import com.wolves.wolf.framework.comm.AdminConnector;
import com.wolves.wolf.framework.comm.Response;
import com.wolves.wolf.framework.comm.StartIceService;
import com.wolves.wolf.framework.config.RemoteConfig;
import com.wolves.wolf.framework.monitor.PingTask;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;


public abstract class StartupStepClass {
    private static final Logger logger = LogManager.getLogger(StartupStepClass.class);


    public abstract void beforeInitialXwork()
            throws Exception;


    /**
     * 初始化xwork
     */
    public final void initialXwork() {

        Executor.setXworkConfigurationFile(StartupConstant.XWORK_FILE_NAME);

    }


    public abstract void afterInitialXwork()
            throws Exception;


    public abstract void beforeInitialAc()
            throws Exception;


    /**
     * 初始化Ice服务
     *
     * @throws Exception 异常
     */
    public final void initialAc()
            throws Exception {

        AdminConnector.initial((StartupConstant.ADMIN_NG_LABEL.equals(StartupConstant.NG_LABEL)) &&
                (StartupConstant.ADMIN_NG_VERSION
                        .equals(StartupConstant.NG_VERSION)));

    }


    public abstract void afterInitialAc()
            throws Exception;


    public abstract void beforeLoadRemoteConfig()
            throws Exception;


    /**
     * 获取远程配置
     *
     * @throws Exception 异常
     */
    public final void loadRemoteConfig()
            throws Exception {

        if ((!StartupConstant.ADMIN_NG_LABEL.equals(StartupConstant.NG_LABEL)) ||
                (!StartupConstant.ADMIN_NG_VERSION
                        .equals(StartupConstant.NG_VERSION))) {

            GetConfigRequest.Builder gcrBd = GetConfigRequest.newBuilder();

            gcrBd.setNgLabel(StartupConstant.NG_LABEL).setIp(StartupConstant.LOCAL_IP)
                    .setNgVersion(StartupConstant.NG_VERSION);

            try {

                Response response = AdminConnector.send("getcfg", "0.0.1", gcrBd
                        .build(), GetConfigResponse.class);

                if (!response.getCode().equals("NA")) {

                    throw new RuntimeException(response.getCode());

                }

                RemoteConfig.PING_INTERVAL = ((GetConfigResponse) response.getBody()).getNodeConfig().getPingAdminInterval();

                RemoteConfig.SERVER_PORT = ((GetConfigResponse) response.getBody()).getNodeConfig().getServicePort();

                RemoteConfig.SERVER_SIZE = ((GetConfigResponse) response.getBody()).getNodeConfig().getServerSize();

                RemoteConfig.SERVER_SIZE_MAX = ((GetConfigResponse) response.getBody()).getNodeConfig().getServerSizeMax();

                RemoteConfig.MESSAGE_SIZE_MAX = ((GetConfigResponse) response.getBody()).getNodeConfig().getMessageSizeMax();

                RemoteConfig.SERVER_OVERRIDE_TIMEOUT = ((GetConfigResponse) response.getBody()).getNodeConfig().getServerTimeout();

                // 如果返回的应答当中包含有第三方类库所需要的配置文件，那么把这些配置文件解包到props文件夹
                if (((GetConfigResponse) response.getBody()).getNodeConfig().getConfFiles3RdList().size() != 0)

                    for (com.wolves.admin.pb.File aFile : ((GetConfigResponse) response.getBody()).getNodeConfig().getConfFiles3RdList())

                        FileUtils.writeByteArrayToFile(new java.io.File("props" + java.io.File.separator + aFile.getFileName()), aFile
                                .getFileContent().toByteArray());

            } catch (Exception e) {

                logger.error("从远程服务器获取配置失败", e);

            }

        }

    }


    public abstract void afterLoadRemoteConfig()
            throws Exception;


    public abstract void beforeInitialIceServer()
            throws Exception;

    /**
     * 初始化Ice服务
     *
     * @throws Exception 异常
     */
    public final void initialIceServer()
            throws Exception {

        if ((StringUtils.isBlank(StartupConstant.LOCAL_IP)) || (!StartupConstant.STARTUP_ICE_SERVICE)) {
            return;
        }
        ArrayList failedPorts = new ArrayList();
        while (true)
            try {
                logger.info("try to start ice service. Type=" + StartupConstant.PROTOCOL +
                        " Port=" + RemoteConfig.SERVER_PORT + " SERVER_SIZE=" + RemoteConfig.SERVER_SIZE +
                        " SERVER_SIZE_MAX=" + RemoteConfig.SERVER_SIZE_MAX + " MESSAGE_SIZE_MAX="
                        + RemoteConfig.MESSAGE_SIZE_MAX + " SERVER_OVERRIDE_TIMEOUT=" + RemoteConfig.SERVER_OVERRIDE_TIMEOUT);

                StartIceService.startIceService(RemoteConfig.SERVER_SIZE, RemoteConfig.SERVER_SIZE_MAX,
                        RemoteConfig.MESSAGE_SIZE_MAX, RemoteConfig.SERVER_OVERRIDE_TIMEOUT,
                        RemoteConfig.SERVER_PORT, StartupConstant.PROTOCOL);
                logger.info("Ice service started. Type=" + StartupConstant.PROTOCOL +
                        " Port=" + RemoteConfig.SERVER_PORT + " SERVER_SIZE=" + RemoteConfig.SERVER_SIZE +
                        " SERVER_SIZE_MAX=" + RemoteConfig.SERVER_SIZE_MAX + " MESSAGE_SIZE_MAX="
                        + RemoteConfig.MESSAGE_SIZE_MAX + " SERVER_OVERRIDE_TIMEOUT=" + RemoteConfig.SERVER_OVERRIDE_TIMEOUT);

                failedPorts.clear();
                failedPorts = null;
                break;

            } catch (Exception e) {
                Throwable throwable = e.getCause();
                while (throwable != null) {
                    if (!throwable.getMessage().contains("Address already in use")) {
                        throwable = throwable.getCause();
                        continue;
                    } else {
                        // 首先记录已经被占用的端口
                        failedPorts.add(Integer.valueOf(RemoteConfig.SERVER_PORT));
                        GetPortRequest.Builder gprBuilder = GetPortRequest.newBuilder();
                        gprBuilder.setLocalip(StartupConstant.LOCAL_IP)
                                .addAllFailedPorts(failedPorts);
                        // 从Admin获取新的端口
                        Response response = AdminConnector.send("getport", "0.0.1", gprBuilder
                                .build(), GetPortResponse.class);
                        if (!response.getCode().equals("NA")) {
                            throw new RuntimeException(response.getCode());
                        }
                        RemoteConfig.SERVER_PORT = ((GetPortResponse) response.getBody()).getServicePort();
                        break;
                    }
                }
                if (throwable == null)
                    throw e;
            }

    }

    public abstract void afterInitialIceServer()
            throws Exception;

    public abstract void beforeStartPingTask()
            throws Exception;

    /**
     * 启动ping定时任务
     */

    public final void startPingTask() {
        PingTask.schedule();

    }

    public abstract void afterStartPingTask()
            throws Exception;

}
