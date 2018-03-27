package com.wolves.wolf.framework.monitor;


import java.util.concurrent.atomic.AtomicInteger;


class InterfaceMonitor {
    private String action;
    private String actVersion;
    private long startAt = 0L;
    private long endAt = 0L;
    private AtomicInteger jamCounter = new AtomicInteger(0);
    private volatile long totalCost = 0L;
    private volatile long totalHit = 0L;


    InterfaceMonitor(String action, String actVersion) {
        this.action = action;
        this.actVersion = actVersion;
        this.startAt = System.currentTimeMillis();
    }


    void beforeExe() {
        this.jamCounter.incrementAndGet();
    }


    void executed(long cost) {
        this.jamCounter.decrementAndGet();
        this.totalHit += 1L;
        this.totalCost += cost;

    }

    /**
     * 获取每分钟平均点击数
     *
     * @return 每分钟平均点击数
     */
    public long getAvaiHitsPerMin() {
        return (long) (60.0D / ((this.endAt - this.startAt) / 1000.0D) * this.totalHit);
    }

    /**
     * 获取每分钟平均响应时间
     *
     * @return 每分钟平均响应时间
     */

    public long getAvaiCostsPerMin() {
        if (this.totalHit != 0L) {
            return this.totalCost / this.totalHit;
        }
        return 0L;

    }

    /**
     * 获取接口拥堵数
     *
     * @return 接口拥堵数
     */

    public long getJams() {
        return this.jamCounter.get();
    }


    public String getAction() {
        return this.action;
    }


    public String getActVersion() {

        return this.actVersion;

    }

    /**
     * 计数重置, 重置对象不包括接口拥堵数
     *
     * @return 重置前的接口状态对象
     */
    InterfaceMonitor reset() {
        InterfaceMonitor returnValue = copyBackup();
        this.totalCost = 0L;
        this.totalHit = 0L;
        this.startAt = System.currentTimeMillis();
        return returnValue;

    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;
        InterfaceMonitor that = (InterfaceMonitor) o;
        return (this.actVersion.equals(that.actVersion)) && (this.action.equals(that.action));

    }


    public int hashCode() {
        int result = this.action.hashCode();
        result = 31 * result + this.actVersion.hashCode();
        return result;

    }


    private InterfaceMonitor copyBackup() {
        InterfaceMonitor returnValue = new InterfaceMonitor(this.action, this.actVersion);
        returnValue.startAt = this.startAt;
        returnValue.jamCounter = new AtomicInteger(this.jamCounter.get());
        returnValue.totalCost = this.totalCost;
        returnValue.totalHit = this.totalHit;
        returnValue.endAt = System.currentTimeMillis();
        return returnValue;
    }

}
