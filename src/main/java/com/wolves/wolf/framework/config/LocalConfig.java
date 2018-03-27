package com.wolves.wolf.framework.config;


import com.wolves.wolf.base.conf.ConfigManager;
import org.apache.commons.configuration2.ex.ConfigurationException;


public class LocalConfig {
    public static String CONFIG_FILE_NAME = "node_xxxx.xml";

    /**
     * 获取单元标签
     *
     * @return 单元标签
     * @throws ConfigurationException 配置异常
     */
    public static String getNG_LABEL()
            throws ConfigurationException {
        ConfigManager configManager = ConfigManager.getSingleton(CONFIG_FILE_NAME);
        return configManager.selectValue("NG_LABEL");
    }

    /**
     * 获取单元版本
     *
     * @return 单元版本
     * @throws ConfigurationException 配置异常
     */

    public static String getNG_VERSION()
            throws ConfigurationException {
        ConfigManager configManager = ConfigManager.getSingleton(CONFIG_FILE_NAME);
        return configManager.selectValue("NG_VERSION");

    }

    /**
     * 获取xwork配置文件
     *
     * @return xwork配置文件
     * @throws ConfigurationException 配置异常
     */
    public static String getXWORK_FILE_NAME()
            throws ConfigurationException {
        ConfigManager configManager = ConfigManager.getSingleton(CONFIG_FILE_NAME);
        return configManager.selectValue("XWORK_FILE_NAME");

    }

    /**
     * 获取启动类路径
     *
     * @return 启动类路径
     * @throws ConfigurationException 配置异常
     */
    public static String getSTARTUP_STEP_CLASS()
            throws ConfigurationException {
        ConfigManager configManager = ConfigManager.getSingleton(CONFIG_FILE_NAME);
        return configManager.selectValue("STARTUP_STEP_CLASS");

    }

    /**
     * 获取与admin节点的通讯相关配置
     *
     * @param arg 配置参数
     * @return 配置数据
     * @throws ConfigurationException 配置异常
     */
    public static String getAdminCommConfig(String arg)
            throws ConfigurationException {
        ConfigManager configManager = ConfigManager.getSingleton(CONFIG_FILE_NAME);
        return configManager.selectValue("admin_node." + arg);

    }

    /**
     * 获取本机ip
     *
     * @return 本机ip
     * @throws ConfigurationException 配置异常
     */
    public static String getLOCAL_IP()
            throws ConfigurationException {
        ConfigManager configManager = ConfigManager.getSingleton(CONFIG_FILE_NAME);
        return configManager.selectValue("LOCAL_IP");

    }

    /**
     * 获得协议类型
     *
     * @return 协议类型
     * @throws ConfigurationException 配置异常
     */
    public static String getPROTOCOL()
            throws ConfigurationException {
        ConfigManager configManager = ConfigManager.getSingleton(CONFIG_FILE_NAME);
        return configManager.selectValue("PROTOCOL");

    }


    public static boolean getSTARTUP_ICE_SERVICE()
            throws ConfigurationException {
        ConfigManager configManager = ConfigManager.getSingleton(CONFIG_FILE_NAME);
        return Boolean.parseBoolean(configManager.selectValue("STARTUP_ICE_SERVICE"));

    }

}

