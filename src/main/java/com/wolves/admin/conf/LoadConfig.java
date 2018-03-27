package com.wolves.admin.conf;


import com.wolves.wolf.base.conf.ConfigManager;
import com.wolves.wolf.framework.config.LocalConfig;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class LoadConfig {
    private static Logger logger = LogManager.getLogger(LoadConfig.class);

    /**
     * 获取配置文件当中的cassandra节点列表
     *
     * @return cassandra节点列表
     * @throws ConfigurationException 配置异常
     */
    public static CassandraNodeVO getCassandraNodes()
            throws ConfigurationException {
        ConfigManager configManager = ConfigManager.getSingleton(LocalConfig.CONFIG_FILE_NAME);
        CassandraNodeVO cassandraNodeVo = new CassandraNodeVO();
        String port = configManager.selectValue("cassandra[@port]");
        cassandraNodeVo.setPort(Integer.parseInt(port));
        String host = configManager.selectValue("cassandra.server[@host]");
        cassandraNodeVo.setHost(host);
        return cassandraNodeVo;

    }

    /**
     * 获取ice通讯相关配置
     *
     * @param arg 配置参数
     * @return 配置数据
     * @throws ConfigurationException 配置异常
     */
    public static String getCommConfig(String arg)
            throws ConfigurationException {
        ConfigManager configManager = ConfigManager.getSingleton(LocalConfig.CONFIG_FILE_NAME);

        return configManager.selectValue("communication." + arg);

    }

    /**
     * 获取端口范围最小值
     *
     * @return 端口范围最小值
     * @throws ConfigurationException 配置错误
     */
    public static String getPortRangeMin()
            throws ConfigurationException {
        ConfigManager configManager = ConfigManager.getSingleton(LocalConfig.CONFIG_FILE_NAME);
        return configManager.selectValue("port_range.min");

    }

    /**
     * 获取端口范围最大值
     *
     * @return 端口范围最大值
     * @throws ConfigurationException 配置错误
     */

    public static String getPortRangeMax()
            throws ConfigurationException {

        ConfigManager configManager = ConfigManager.getSingleton(LocalConfig.CONFIG_FILE_NAME);
        return configManager.selectValue("port_range.max");

    }


    /**
     * 获取"节点不作ping多久可判定节点死亡"，单位秒
     *
     * @return 节点不作ping多久可判定节点死亡
     * @throws ConfigurationException 配置异常s
     */
    public static String getNodeDeadDuration()
            throws ConfigurationException {

        ConfigManager configManager = ConfigManager.getSingleton(LocalConfig.CONFIG_FILE_NAME);
        return configManager.selectValue("node_dead_duration");

    }

    /**
     * 获取"节点不作ping多久可判定节点死亡"，单位秒
     *
     * @return 节点不作ping多久可判定节点死亡
     * @throws ConfigurationException 配置异常s
     */
    public static String getNodeCleanupDuration()
            throws ConfigurationException {

        ConfigManager configManager = ConfigManager.getSingleton(LocalConfig.CONFIG_FILE_NAME);
        return configManager.selectValue("node_cleanup_duration");

    }

    /**
     * 获取"节点不作ping多久可进行数据清理"，单位秒
     *
     * @return 节点不作ping多久可进行数据清理
     * @throws ConfigurationException 配置异常
     */
    public static String getServerCleanupDuration()
            throws ConfigurationException {

        ConfigManager configManager = ConfigManager.getSingleton(LocalConfig.CONFIG_FILE_NAME);
        return configManager.selectValue("server_cleanup_duration");

    }

}
