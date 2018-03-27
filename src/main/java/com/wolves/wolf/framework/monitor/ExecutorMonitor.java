package com.wolves.wolf.framework.monitor;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


public class ExecutorMonitor {
    private static final Hashtable<String, InterfaceMonitor> MONITORS = new Hashtable();


    public static void beforeExe(String act, String actVersion) {

        String key = act + "[" + actVersion + "]";
        InterfaceMonitor interfaceMonitor = (InterfaceMonitor) MONITORS.get(key);
        // 这里也是当遇到线程问题时, 需要反注释的代码
        if (interfaceMonitor == null) {
            synchronized (MONITORS) {
                if (MONITORS.get(key) == null) {
                    interfaceMonitor = new InterfaceMonitor(act, actVersion);
                    MONITORS.put(key, interfaceMonitor);
                }
            }

        }
        assert (interfaceMonitor != null);
        interfaceMonitor.beforeExe();

    }


    public static void executed(String act, String actVersion, long cost) {

        String key = act + "[" + actVersion + "]";
        InterfaceMonitor interfaceMonitor = (InterfaceMonitor) MONITORS.get(key);
        interfaceMonitor.executed(cost);

    }

    /**
     * 获取所有接口的状态数据封装并重置
     * fetchAll方法中没有对MONITORS加锁, 因为文档中描述hashtable是线程安全的, 但这种安全性是否是指的我们遇到的这种安全性还未进行测试,
     * 所以到这里的代码遇到了线程冲突, 就需要为MONITORS加锁,这个应该很简单, 反注释代码就可以了.
     *
     * @return 所有接口的状态数据封装
     */
    static List<InterfaceMonitor> fetchAndResetAll() {

        ArrayList interfaceMonitors = new ArrayList();
        synchronized (MONITORS) {
            for (InterfaceMonitor aInterfaceMonitor : MONITORS.values()) {
                interfaceMonitors.add(aInterfaceMonitor.reset());
            }
        }
        return interfaceMonitors;
    }

}
