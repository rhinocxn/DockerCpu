package CPUPerformance;

public class MonitorInfoBean {
    /** ����ϵͳ. */
    private String osName;
    /** cpuʹ����. */
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