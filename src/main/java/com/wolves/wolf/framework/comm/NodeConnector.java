package com.wolves.wolf.framework.comm;


import Ice.*;
import com.wolves.framework.pb.WolRequest;
import com.wolves.framework.pb.WolResponse;
import com.wolves.wolf.StartupConstant;
import com.wolves.wolf.framework.cmd.Executor;
import com.wolves.wolf.framework.comm.generated.DIceCommStationPrx;
import com.wolves.wolf.framework.comm.generated.DIceCommStationPrxHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.Exception;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ZhangHuan
 * Date: 2015-8-17
 * Time: 16:42:28
 * 连接到其他节点的ice客户端
 */
class NodeConnector {
    private static final Logger logger = LogManager.getLogger(NodeConnector.class);
    private DIceCommStationPrx proxy;
    private String ip;
    private int port;
    private int override_timeout;
    private int message_size_max;
    private Communicator ic;
    private int hitFactor;
    private int loadFactor;
    private boolean isLocalInvoke = false;
    private Protocol protocol = Protocol.TCP;


    NodeConnector(int override_timeout, int message_size_max, String ip, int port, boolean isLocalInvoke, String protocol) {

        if (protocol.equalsIgnoreCase("UDP"))
            initial(override_timeout, message_size_max, ip, port, isLocalInvoke, Protocol.UDP);

        else
            initial(override_timeout, message_size_max, ip, port, isLocalInvoke, Protocol.TCP);

    }


    NodeConnector(int override_timeout, int message_size_max, String ip, int port, boolean isLocalInvoke, Protocol protocol) {

        initial(override_timeout, message_size_max, ip, port, isLocalInvoke, protocol);

    }


    private void initial(int override_timeout, int message_size_max, String ip, int port, boolean isLocalInvoke, Protocol protocol) {
        logger.info("NodeConnector initializing with para: override_timeout=" + override_timeout + ", ip=" + ip + ", port=" + port + ", isLocalInvoke=" + isLocalInvoke + ", protocol=" + protocol);
        this.isLocalInvoke = isLocalInvoke;
        this.protocol = protocol;
        if (!isLocalInvoke) {
            this.override_timeout = override_timeout;
            this.message_size_max = message_size_max;
            this.ip = ip;
            this.port = port;
            StringSeqHolder argsH = new StringSeqHolder(new String[]{"Ice.Override.Timeout", "Ice.MessageSizeMax"});
            Properties properties = Util.createProperties(argsH);
            properties.setProperty("Ice.Override.Timeout", String.valueOf(this.override_timeout));
            properties.setProperty("Ice.MessageSizeMax", String.valueOf(this.message_size_max));
            InitializationData id = new InitializationData();
            id.properties = properties;
            this.ic = Util.initialize(id);
            if (protocol == Protocol.TCP) {
                ObjectPrx base = this.ic.stringToProxy("DIceCommStation:default -p " + this.port + " -h " + this.ip);
                this.proxy = DIceCommStationPrxHelper.checkedCast(base);
            } else {
                ObjectPrx base = this.ic.stringToProxy("DIceCommStation -d:udp -p " + this.port + " -h " + this.ip);
                this.proxy = DIceCommStationPrxHelper.uncheckedCast(base);
            }
        }

    }


    public WolResponse send(WolRequest request) throws Exception {
        // 添加调用方NG信息 START
        request = WolRequest.newBuilder(request)
                .setFromNg(StartupConstant.NG_LABEL)
                .setFromNgVersion(StartupConstant.NG_VERSION)
                .build();
        // 添加调用方NG信息 END
        byte[] dIceRequestPackage = request.toByteArray();
        WolResponse dafResponse;
        if (!this.isLocalInvoke) {
            byte[] dIceResponsePackage;
            if (this.protocol == Protocol.TCP) {
                dIceResponsePackage = this.proxy._do(dIceRequestPackage);
            } else {
                this.proxy.touch(dIceRequestPackage);
                return WolResponse.parseFrom(new byte[0]);
            }
            dafResponse = WolResponse.parseFrom(dIceResponsePackage);
            if (dafResponse.getResponseTimeStack() != null) {
                if (Executor.THREADLOCAL.get() == null) {
                    Executor.THREADLOCAL.set(new ArrayList());
                }
                ArrayList timeStacks = (ArrayList) Executor.THREADLOCAL.get();
                if (timeStacks.size() < 30)
                    timeStacks.add(dafResponse.getResponseTimeStack());
            }
        } else {
            dafResponse = Executor.exec(request);
        }
        return dafResponse;

    }


    public byte[] send(byte[] dIceRequestPackage) throws Exception {
        byte[] returnValue;
        if (!this.isLocalInvoke) {
            if (this.protocol == Protocol.TCP) {
                returnValue = this.proxy._do(dIceRequestPackage);
            } else {
                this.proxy.touch(dIceRequestPackage);
                return new byte[0];
            }
            WolResponse dafResponse = WolResponse.parseFrom(returnValue);
            if (!StringUtils.equals(dafResponse.getCode(), "NA")) {
                throw new RuntimeException();
            }
            if (dafResponse.getResponseTimeStack() != null) {
                if (Executor.THREADLOCAL.get() == null) {
                    Executor.THREADLOCAL.set(new ArrayList());
                }
                ArrayList timeStacks = (ArrayList) Executor.THREADLOCAL.get();
                if (timeStacks.size() < 30)
                    timeStacks.add(dafResponse.getResponseTimeStack());
            }
        } else {
            returnValue = Executor.exec(WolRequest.parseFrom(dIceRequestPackage)).toByteArray();
        }
        return returnValue;
    }


    public int getHitFactor() {
        return this.hitFactor;
    }


    public void setHitFactor(int hitFactor) {
        this.hitFactor = hitFactor;
    }


    public int getLoadFactor() {
        return this.loadFactor;
    }


    public void setLoadFactor(int loadFactor) {
        this.loadFactor = loadFactor;

    }


    public void destroy() {
        this.ic.destroy();
    }

    static enum Protocol {
        TCP, UDP;
    }

}
