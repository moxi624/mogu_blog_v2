package com.moxi.mogublog.utils.ServerInfo;

import com.moxi.mogublog.utils.Arith;
import com.moxi.mogublog.utils.DateUtils;
import com.moxi.mogublog.utils.IpUtils;
import com.openhtmltopdf.util.Util;
import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.CentralProcessor.TickType;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;


/**
 * 服务器相关信息
 *
 * @author ruoyi
 */
@Slf4j
public class ServerInfo {
    private static final int OSHI_WAIT_SECOND = 1000;

    /**
     * CPU相关信息
     */
    private Cpu cpu = new Cpu();

    /**
     * 內存相关信息
     */
    private Mem mem = new Mem();

    /**
     * JVM相关信息
     */
    private Jvm jvm = new Jvm();

    /**
     * 服务器相关信息
     */
    private Sys sys = new Sys();

    /**
     * 磁盘相关信息
     */
    private List<SysFile> sysFiles = new LinkedList<>();

    public Cpu getCpu() {
        return cpu;
    }

    public void setCpu(Cpu cpu) {
        this.cpu = cpu;
    }

    public Mem getMem() {
        return mem;
    }

    public void setMem(Mem mem) {
        this.mem = mem;
    }

    public Jvm getJvm() {
        return jvm;
    }

    public void setJvm(Jvm jvm) {
        this.jvm = jvm;
    }

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    public List<SysFile> getSysFiles() {
        return sysFiles;
    }

    public void setSysFiles(List<SysFile> sysFiles) {
        this.sysFiles = sysFiles;
    }

    /**
     * 设置磁盘信息
     */
    private void setSysFiles(OperatingSystem os) {
        FileSystem fileSystem = os.getFileSystem();
        OSFileStore[] fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
            long free = fs.getUsableSpace();
            long total = fs.getTotalSpace();
            long used = total - free;
            SysFile sysFile = new SysFile();
            sysFile.setDirName(fs.getMount());
            sysFile.setSysTypeName(fs.getType());
            sysFile.setTypeName(fs.getName());
            sysFile.setTotal(convertFileSize(total));
            sysFile.setFree(convertFileSize(free));
            sysFile.setUsed(convertFileSize(used));
            sysFile.setUsage(Arith.mul(Arith.div(used, total, 4), 100));
            sysFiles.add(sysFile);
        }
    }

    public void copyTo() {
        try {
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();

            setCpuInfo(hal.getProcessor());

            setMemInfo(hal.getMemory());

            setSysInfo();

            setJvmInfo();

            setSysFiles(si.getOperatingSystem());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 设置CPU信息
     */
    private void setCpuInfo(CentralProcessor processor) {
        // CPU信息
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        Util.sleep(OSHI_WAIT_SECOND);
        long[] ticks = processor.getSystemCpuLoadTicks();
        double nice = ticks[TickType.NICE.getIndex()] - prevTicks[TickType.NICE.getIndex()];
        double irq = ticks[TickType.IRQ.getIndex()] - prevTicks[TickType.IRQ.getIndex()];
        double softirq = ticks[TickType.SOFTIRQ.getIndex()] - prevTicks[TickType.SOFTIRQ.getIndex()];
        double steal = ticks[TickType.STEAL.getIndex()] - prevTicks[TickType.STEAL.getIndex()];
        double cSys = ticks[TickType.SYSTEM.getIndex()] - prevTicks[TickType.SYSTEM.getIndex()];
        double user = ticks[TickType.USER.getIndex()] - prevTicks[TickType.USER.getIndex()];
        double iowait = ticks[TickType.IOWAIT.getIndex()] - prevTicks[TickType.IOWAIT.getIndex()];
        double idle = ticks[TickType.IDLE.getIndex()] - prevTicks[TickType.IDLE.getIndex()];
        double totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;

        cpu.setCpuNum(processor.getLogicalProcessorCount());
        cpu.setTotal(Arith.round(Arith.mul(totalCpu, 100), 2));
        cpu.setSys(Arith.round(Arith.mul(cSys / totalCpu, 100), 2));
        cpu.setUsed(Arith.round(Arith.mul( user/ totalCpu, 100), 2));
        cpu.setWait(Arith.round(Arith.mul(iowait / totalCpu, 100), 2));
        cpu.setFree(Arith.round(Arith.mul(idle / totalCpu, 100), 2));
    }

    /**
     * 设置内存信息
     */
    private void setMemInfo(GlobalMemory memory) {
        mem.setTotal(Arith.div(memory.getTotal(), (1024 * 1024 * 1024), 2));
        mem.setUsed(Arith.div(memory.getTotal() - memory.getAvailable(), (1024 * 1024 * 1024), 2));
        mem.setFree(Arith.div(memory.getAvailable(), (1024 * 1024 * 1024), 2));
        mem.setUsage(Arith.mul(Arith.div(memory.getTotal() - memory.getAvailable(), memory.getTotal(), 4), 100));
    }

    /**
     * 设置服务器信息
     */
    private void setSysInfo() {
        Properties props = System.getProperties();
        sys.setComputerName(IpUtils.getHostName());
        sys.setComputerIp(IpUtils.getHostIp());
        sys.setOsName(props.getProperty("os.name"));
        sys.setOsArch(props.getProperty("os.arch"));
        sys.setUserDir(props.getProperty("user.dir"));
    }

    /**
     * 设置Java虚拟机
     */
    private void setJvmInfo() throws UnknownHostException {
        Properties props = System.getProperties();
        jvm.setName(ManagementFactory.getRuntimeMXBean().getVmName());
        jvm.setTotal(Arith.div(Runtime.getRuntime().totalMemory(), (1024 * 1024), 2));
        jvm.setMax(Arith.div(Runtime.getRuntime().maxMemory(), (1024 * 1024), 2));
        jvm.setFree(Arith.div(Runtime.getRuntime().freeMemory(), (1024 * 1024), 2));
        jvm.setUsed(Arith.div(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(), (1024 * 1024), 2));
        jvm.setUsage(Arith.mul(Arith.div(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory(), Runtime.getRuntime().totalMemory(), 4), 100));
        jvm.setStartTime(DateUtils.formateDate(DateUtils.getServerStartDate(), DateUtils.YYYY_MM_DD_HH_MM_SS));
        jvm.setRunTime(DateUtils.getDatePoor(DateUtils.getNowDate(), DateUtils.getServerStartDate()));
        jvm.setVersion(props.getProperty("java.version"));
        jvm.setHome(props.getProperty("java.home"));
    }

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }
}
