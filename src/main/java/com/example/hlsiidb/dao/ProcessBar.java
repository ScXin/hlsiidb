package com.example.hlsiidb.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用来存放文件下载进度的类
 * @author xie
 */
public class ProcessBar {
    //Map中key为文件名，List存放文件下载信息，list.get(1)为下载到第几个PV,list.get(2)为当前PV下载了百分之多少
    private static Map<String,List<Integer>> processMap = new HashMap<>();

    /**
     * 添加下载进程信息
     * @param key
     * @param value
     */
    public static void addProcess(String key,List<Integer> value){
        processMap.put(key,value);
    }

    /**
     * 更新进度信息
     * @param key
     * @param value
     */
    public static void changeProcess(String key,Integer value){
        processMap.get(key).set(1,value);
    }

    /**
     * 更新下载到第几个PV
     * @param key
     * @param value
     */
    public static void changeProcessNum(String key,Integer value){
        processMap.get(key).set(0,value);
    }

    /**
     * 获取进程下载信息
     * @param key
     * @return
     */
    public static List<Integer> getProcessStatus(String key){
        return processMap.get(key);
    }

    /**
     * 删除下载进程
     * @param key
     */
    public static void deleteProcess(String key){
        processMap.remove(key);
    }
}
