package com.moxi.mogublog.utils.ServerInfo;
import com.moxi.mogublog.utils.Arith;
import com.moxi.mogublog.utils.DateUtils;
import lombok.Data;

import java.lang.management.ManagementFactory;

/**
 * JVM相关信息
 * 
 * @author ruoyi
 */
@Data
public class Jvm
{
    /**
     * 虚拟机名称
     */
    private String name;

    /**
     * 当前JVM占用的内存总数(M)
     */
    private double total;

    /**
     * JVM最大可用内存总数(M)
     */
    private double max;

    /**
     * JVM空闲内存(M)
     */
    private double free;

    /**
     * JVM可用内存(M)
     */
    private double used;

    /**
     * JDK版本
     */
    private String version;

    /**
     * JDK路径
     */
    private String home;

    /**
     * 使用率
     */
    private double usage;

    /**
     * JDK启动时间
     */
    private String startTime;

    /**
     * JDK运行时间
     */
    private String runTime;
}
