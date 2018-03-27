package com.wolves.wolf.framework.cmd;


import com.google.protobuf.Any;
import com.opensymphony.xwork.ActionProxy;
import com.opensymphony.xwork.ActionProxyFactory;
import com.opensymphony.xwork.config.ConfigurationManager;
import com.opensymphony.xwork.config.providers.XmlConfigurationProvider;
import com.wolves.framework.pb.TimeStack;
import com.wolves.framework.pb.WolRequest;
import com.wolves.framework.pb.WolResponse;
import com.wolves.wolf.StartupConstant;
import com.wolves.wolf.framework.comm.StartIceService;
import com.wolves.wolf.framework.config.LocalConfig;
import com.wolves.wolf.framework.lang.WolfException;
import com.wolves.wolf.framework.monitor.ExecutorMonitor;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;


public class Executor {
    public static final ThreadLocal<ArrayList<TimeStack>> THREADLOCAL = new ThreadLocal();
    private static Logger logger = LogManager.getLogger(Executor.class);
    private static ActionProxyFactory actionProxyFactory = ActionProxyFactory.getFactory();


    public static void setXworkConfigurationFile(String fileName) {
        ConfigurationManager.addConfigurationProvider(new XmlConfigurationProvider(fileName));
    }

    public static WolResponse exec(WolRequest dafRequest)
            throws Exception {
        // 首先检测调用的NG和版本与本服务是否匹配
        if ((!StringUtils.equals(dafRequest.getNgLabel(), LocalConfig.getNG_LABEL())) ||
                (!StringUtils.equals(dafRequest
                        .getNgVersion(), LocalConfig.getNG_VERSION()))) {
            WolResponse.Builder dafResBuilder = WolResponse.newBuilder();
            dafResBuilder.setCode("00012").setDesc("ng and version is not match");
            return dafResBuilder.build();
        }
        ActionProxy actionProxy = actionProxyFactory.createActionProxy(dafRequest.getAction(), dafRequest
                .getActVersion(), null);
        WolAction WolAction = (WolAction) actionProxy.getAction();
        WolAction.setRequest(dafRequest.getFromNg(), dafRequest.getNgVersion(), dafRequest
                .getFromIp(), dafRequest.hasBody() ? dafRequest.getBody() : null);
        // 接口计数
        ExecutorMonitor.beforeExe(dafRequest.getAction(), dafRequest.getActVersion());
        long start = System.currentTimeMillis();
        WolResponse.Builder dafResBuilder;
        try {
            actionProxy.execute();
        } catch (WolfException e) {
            WolAction.setResponse(e.getCode(), e.getDesc(), WolAction.getResponse());
            logger.error(e.getCode() + ":" + e.getDesc(), e);
        } catch (Exception e) {
            WolAction.setResponse("00010", "Uncaught action execution Exception", WolAction.getResponse());
            logger.error(e.getMessage(), e);
        } finally {
            long cost = System.currentTimeMillis() - start;
            logger.info("process [" + dafRequest.getAction() + "] from >" + dafRequest.getFromNg() + "<@" + dafRequest
                    .getFromIp() + " " + WolAction.getCode() + " cost " + cost + " ms...");
            // 接口计数
            ExecutorMonitor.executed(dafRequest.getAction(), dafRequest.getActVersion(), cost);
            dafResBuilder = WolResponse.newBuilder();
            if (WolAction.getResponse() != null) dafResBuilder.setBody(Any.pack(WolAction.getResponse()));
            if (WolAction.getCode() != null) dafResBuilder.setCode(WolAction.getCode());
            if (WolAction.getDesc() != null) dafResBuilder.setDesc(WolAction.getDesc());
            TimeStack.Builder tsBuilder1 = TimeStack.newBuilder();
            tsBuilder1.setIp(StartupConstant.LOCAL_IP).setPort(StartIceService.getPort())
                    .setAction(dafRequest.getAction())
                    .setActVersion(dafRequest.getActVersion())
                    .setNgLabel(StartupConstant.NG_LABEL)
                    .setNgVersion(StartupConstant.NG_VERSION)
                    .setTime(cost);
            if (THREADLOCAL.get() != null) {
                tsBuilder1.addAllChildren((Iterable) THREADLOCAL.get());
                ((ArrayList) THREADLOCAL.get()).clear();
            }
        }
        return dafResBuilder.build();
    }

}
