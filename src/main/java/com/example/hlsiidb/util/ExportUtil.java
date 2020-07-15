package com.example.hlsiidb.util;

import com.example.hlsiidb.entity.ChannelOut;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Logger;

public class ExportUtil {

    /**
     * 写入文件头信息
     * @param file
     * @param pvName
     * @param startTime
     * @param endTime
     * @return
     * @throws IOException
     */
    public static boolean exportHeader(File file, String pvName, String startTime, String endTime) throws IOException{
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file, true)));
        try {
            out.write("\r\n\r\n\r\n");
            out.write("--------------------------------" + "\r\n");
            out.write(pvName + "\r\n");
            out.write("start time：" + startTime + "    end time：" + endTime + "\r\n");
            out.write("-----time---------------value---" + "\r\n");
            out.write("\r\n\r\n\r\n");
            out.flush();
            out.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向文件中写入历史数据
     * @param file
     * @param pvName
     * @param startTime
     * @param endTime
     * @param list
     * @return
     * @throws IOException
     */
    public static boolean exportPv(File file, String pvName, String startTime, String endTime, List<ChannelOut> list) throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file, true)));
        try {
            for (int i = 0; i < list.size(); i++) {
                ChannelOut c = list.get(i);
                String val = list.get(i).StringSmpl_time() + "   ";
                if (c.getFloat_val() != null) {
                    if ((c.getFloat_val() > 1000)
                            || (c.getFloat_val() < 0.01)) {
                        val = val + String.format("%.3e", c.getFloat_val());
                    } else {
                        val = val + String.format("%5f", c.getFloat_val());
                    }
                } else {
                    val = val + "NULL";
                }
                out.write(val + "\r\n");
            }
            out.flush();
            out.close();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
