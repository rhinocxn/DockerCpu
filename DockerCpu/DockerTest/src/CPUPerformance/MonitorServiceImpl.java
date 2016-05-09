package CPUPerformance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.StringTokenizer;


public class MonitorServiceImpl implements IMonitorService {
    private static final int CPUTIME = 3;
    private static final int PERCENT = 100;
    private static final int FAULTLENGTH = 10;
    private static String linuxVersion = "2.4 64bit";

    
    public MonitorInfoBean getMonitorInfoBean() throws Exception {
        // 操作系统
        String osName = System.getProperty("os.name");
        double cpuRatio = 0;
        if (osName.toLowerCase().startsWith("windows")) {
            cpuRatio = this.getCpuRatioForWindows();
        } else {
            cpuRatio = getCpuRateForLinux();
        }
        // 构造返回对象
        MonitorInfoBean infoBean = new MonitorInfoBean();
        infoBean.setCpuRatio(cpuRatio);
        return infoBean;
    }
    
    private static double getCpuRateForLinux() {  
        double cpuUsage = 0;  
        Process pro1,pro2;  
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader in1 = null;
        InputStream is2 = null;
        InputStreamReader isr2 = null;
        BufferedReader in2 = null;
        Runtime r = Runtime.getRuntime();  
        try {  
            String command = "cat /proc/stat";  
            //第一次采集CPU时间  
            long startTime = System.currentTimeMillis();  
            pro1 = r.exec(command);  
            
            is = pro1.getInputStream();
            isr = new InputStreamReader(is);
            in1 = new BufferedReader(isr);
            String line = null;  
            long idleCpuTime1 = 0, totalCpuTime1 = 0;   //分别为系统启动后空闲的CPU时间和总的CPU时间  
            while((line=in1.readLine()) != null){     
                if(line.startsWith("cpu")){  
                    line = line.trim();  
                    String[] temp = line.split("\\s+");   
                    idleCpuTime1 = Long.parseLong(temp[4]);  
                    for(String s : temp){  
                        if(!s.equals("cpu")){  
                            totalCpuTime1 += Long.parseLong(s);  
                        }  
                    }     
                    break;  
                }                         
            }     
            in1.close();  
            pro1.destroy();  
            try {  
                Thread.sleep(100);  
            } catch (InterruptedException ioe) {  
            	System.out.println(ioe.getMessage());
                freeResource(is, isr, in1);
                return 1;
            }  
            //第二次采集CPU时间  
            long endTime = System.currentTimeMillis();  
            pro2 = r.exec(command);  
            
            is2 = pro2.getInputStream();
            isr2 = new InputStreamReader(is2);
            in2 = new BufferedReader(isr2); 
            long idleCpuTime2 = 0, totalCpuTime2 = 0;   //分别为系统启动后空闲的CPU时间和总的CPU时间  
            while((line=in2.readLine()) != null){     
                if(line.startsWith("cpu")){  
                    line = line.trim();  
                    String[] temp = line.split("\\s+");   
                    idleCpuTime2 = Long.parseLong(temp[4]);  
                    for(String s : temp){  
                        if(!s.equals("cpu")){  
                            totalCpuTime2 += Long.parseLong(s);  
                        }  
                    }  
                    break;    
                }                                 
            }  
            if(idleCpuTime1 != 0 && totalCpuTime1 !=0 && idleCpuTime2 != 0 && totalCpuTime2 !=0 && totalCpuTime2 != totalCpuTime1){  
                cpuUsage = 1 - (double)(idleCpuTime2 - idleCpuTime1)/(double)(totalCpuTime2 - totalCpuTime1);  
            }   
            System.out.println("CPU Usage : " + cpuUsage + " idle :" + (double)(idleCpuTime2 - idleCpuTime1) + " total :" + (double)(totalCpuTime2 - totalCpuTime1));
            System.out.println("strattime " + startTime + " endtime :" + endTime);
            in2.close();  
            pro2.destroy();  
        } catch (IOException ioe) {  
        	System.out.println(ioe.getMessage());
            freeResource(is2, isr2, in2);
            return 1; 
        }     
        return cpuUsage*100;  
    }

    private static void freeResource(InputStream is, InputStreamReader isr,
            BufferedReader br) {
        try {
            if (is != null)
                is.close();
            if (isr != null)
                isr.close();
            if (br != null)
                br.close();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }

    
    private double getCpuRatioForWindows() {
        try {
            String procCmd = System.getenv("windir")
                    + "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
            // 取进程信息
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
            Thread.sleep(CPUTIME);
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
            if (c0 != null && c1 != null) {
                long idletime = c1[0] - c0[0];
                long busytime = c1[1] - c0[1];
                return Double.valueOf(
                        PERCENT * (busytime) / (busytime + idletime))
                        .doubleValue();
            } else {
                return 0.0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0.0;
        }
    }

    
    private long[] readCpu(final Process proc) {
        long[] retn = new long[2];
        try {
            proc.getOutputStream().close();
            InputStreamReader ir = new InputStreamReader(proc.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line = input.readLine();
            if (line == null || line.length() < FAULTLENGTH) {
                return null;
            }
            int capidx = line.indexOf("Caption");
            int cmdidx = line.indexOf("CommandLine");
            int rocidx = line.indexOf("ReadOperationCount");
            int umtidx = line.indexOf("UserModeTime");
            int kmtidx = line.indexOf("KernelModeTime");
            int wocidx = line.indexOf("WriteOperationCount");
            long idletime = 0;
            long kneltime = 0;
            long usertime = 0;
            while ((line = input.readLine()) != null) {
                if (line.length() < wocidx) {
                    continue;
                }
                // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,
                // ThreadCount,UserModeTime,WriteOperation
                String caption = Bytes.substring(line, capidx, cmdidx - 1)
                        .trim();
                String cmd = Bytes.substring(line, cmdidx, kmtidx - 1).trim();
                if (cmd.indexOf("wmic.exe") >= 0) {
                    continue;
                }
                String s1 = Bytes.substring(line, kmtidx, rocidx - 1).trim();
                String s2 = Bytes.substring(line, umtidx, wocidx - 1).trim();
                if (caption.equals("System Idle Process")
                        || caption.equals("System")) {
                    if (s1.length() > 0)
                        idletime += Long.valueOf(s1).longValue();
                    if (s2.length() > 0)
                        idletime += Long.valueOf(s2).longValue();
                    continue;
                }
                if (s1.length() > 0)
                    kneltime += Long.valueOf(s1).longValue();
                if (s2.length() > 0)
                    usertime += Long.valueOf(s2).longValue();
            }
            retn[0] = idletime;
            retn[1] = kneltime + usertime;
            return retn;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                proc.getInputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public void setCPUrate(int rate){
    	long startTime = 0; 
        int busyTime = 100000; 
		try {
			startTime = System.currentTimeMillis();
			
			if(rate<=100 && rate > 0){
				while(System.currentTimeMillis() - startTime <= busyTime){
					long start = System.currentTimeMillis();
					while(System.currentTimeMillis() - start <= rate){}
					Thread.sleep(100-rate);
		    	}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    }
    
}