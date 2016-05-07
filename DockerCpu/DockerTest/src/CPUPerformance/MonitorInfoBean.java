package CPUPerformance;

public class MonitorInfoBean {
    /** 操作系统. */
    private String osName;
    /** cpu使用率. */
    private double cpuRatio;

    

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }
    
    public double getCpuRatio() {
        return cpuRatio;
    }

    public void setCpuRatio(double cpuRatio) {
        this.cpuRatio = cpuRatio;
    }
}