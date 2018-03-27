package com.wolves.admin.startup;


import com.wolves.admin.conf.LoadConfig;
import com.wolves.admin.mem.MemCleaner;
import com.wolves.wolf.framework.config.RemoteConfig;
import com.wolves.wolf.framework.startup.StartupStepClass;


public class StartupStepI extends StartupStepClass {

    public void beforeInitialXwork() throws Exception {
        MemCleaner.schedule();
    }

    public void afterInitialXwork()
            throws Exception {
    }

    public void beforeInitialAc() throws Exception {
    }

    public void afterInitialAc() throws Exception {
    }

    public void beforeLoadRemoteConfig() throws Exception {
    }

    public void afterLoadRemoteConfig() throws Exception {
        // 不管远程配置数据加载是否成功, 为ice设置启动参数
        RemoteConfig.SERVER_SIZE = Integer.parseInt(LoadConfig.getCommConfig("server_size"));
        RemoteConfig.SERVER_SIZE_MAX = Integer.parseInt(LoadConfig.getCommConfig("server_size_max"));
        RemoteConfig.SERVER_OVERRIDE_TIMEOUT = Integer.parseInt(LoadConfig.getCommConfig("override_timeout"));
        RemoteConfig.SERVER_PORT = Integer.parseInt(LoadConfig.getCommConfig("listen_port"));
    }

    public void beforeInitialIceServer()
            throws Exception {
    }

    public void afterInitialIceServer()
            throws Exception {
    }

    public void beforeStartPingTask()
            throws Exception {
    }

    public void afterStartPingTask()
            throws Exception {
    }

}
