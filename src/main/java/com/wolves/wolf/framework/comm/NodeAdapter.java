package com.wolves.wolf.framework.comm;


import Ice.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.Exception;
/**
 * Created by IntelliJ IDEA.
 * User: ZhangHuan
 * Date: 2015-8-17
 * Time: 16:42:54
 * 服务器端用于启动ice服务的线程
 */

class NodeAdapter
        implements Runnable {
    private static final Logger logger = LogManager.getLogger(NodeAdapter.class);
    private static boolean isOnline = false;

    private static Exception e = null;
    private int server_size;
    private int server_size_max;
    private int message_size_max;
    private int override_timeout;
    private int port;
    private String protocol;

    NodeAdapter(int server_size, int server_size_max, int message_size_max, int override_timeout, int port, String protocol) {
        this.server_size = server_size;
        this.server_size_max = server_size_max;
        this.message_size_max = message_size_max;
        this.override_timeout = override_timeout;
        this.port = port;
        this.protocol = protocol;
    }


    public void run() {
        if (!isOnline) {
            Communicator ic = null;
            try {
                try {
                    StringSeqHolder argsH = new StringSeqHolder(new String[]{"Ice.ThreadPool.Server.Size", "Ice.ThreadPool.Server.SizeMax", "Ice.MessageSizeMax", "Ice.Override.Timeout", "Ice.Warn.Connections"});
                    Properties properties = Util.createProperties(argsH);
                    properties.setProperty("Ice.ThreadPool.Server.Size", String.valueOf(this.server_size));
                    properties.setProperty("Ice.ThreadPool.Server.SizeMax", String.valueOf(this.server_size_max));
                    properties.setProperty("Ice.MessageSizeMax", String.valueOf(this.message_size_max));
                    properties.setProperty("Ice.Override.Timeout", String.valueOf(this.override_timeout));
                    properties.setProperty("Ice.Warn.Connections", "1");
                    InitializationData id = new InitializationData();
                    id.properties = properties;
                    ic = Util.initialize(id);
                    ObjectAdapter IOAdapter;
                    if (this.port != 0) {
                        if (this.protocol.equalsIgnoreCase("UDP"))
                            IOAdapter = ic.createObjectAdapterWithEndpoints("DIceCommStationAdapter", "udp -p " + this.port);
                        else
                            IOAdapter = ic.createObjectAdapterWithEndpoints("DIceCommStationAdapter", "default -p " + this.port);
                    } else {
                        throw new RuntimeException("Ports predefined by admin are all in use");
                    }
                    Ice.Object IObject = new DIceCommStationI();
                    IOAdapter.add(IObject, Util.stringToIdentity("DIceCommStation"));
                    IOAdapter.activate();
                    isOnline = true;
                    ic.waitForShutdown();
                } finally {
                    if (ic != null)
                        ic.destroy();

                }
            } catch (Exception e) {
                e = e;
            }
        }
    }

    public boolean isOnline() {
        return isOnline;
    }

    public Exception getE() {
        Exception returnValue = e;
        e = null;
        return returnValue;

    }

}

