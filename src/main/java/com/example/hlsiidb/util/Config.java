package com.example.hlsiidb.util;

/**
 * 系统相关的配置类
 * @author xie
 */
public class Config {
    //默认配置文件、历史记录文件、python程序、系统日志存放路径

    //【【需要修改】】
//    public static String DEFAULT_PATH = "/hls/DataServer";
    public static String DEFAULT_PATH = "G:\\HLSIIDB-20200617\\hlsiidb\\src\\main\\resources";

    //REIDS缓存sessionID的主机地址
    public static String REDIS_PATH = "192.168.113.36";

    //默认间隔取数据的数据量最大值
    public static long DATA_NUM = 2000;
}
